package com.example.contactapi.service;

import com.example.contactapi.domain.Contact;
import com.example.contactapi.repo.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ContactService {

    private final ContactRepository contactRepository;

    public ContactService(final ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Page<Contact> getAllContacts(int page, int size){

        return contactRepository.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    public Contact getContactById(UUID id){
        return contactRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Contact not found"));
    }

    public Contact createContact(Contact contact){

        List<Contact> contacts = contactRepository.findAll();

        if (contacts.stream()
                .anyMatch(c -> c.getPhone().equals(contact.getPhone()) ||
                                  c.getEmail().equals(contact.getEmail()) )) {
            throw new RuntimeException("Contact already exists");
        }

        return contactRepository.save(contact);
    }

    public void deleteContactById(Contact contact){

        if (contact == null) {
            throw new RuntimeException("Contact not found");
        }

        contactRepository.delete(contact);
    }

    public String uploadImage(UUID id, MultipartFile file){
        Contact contact = getContactById(id);

        String photoUrl = String.valueOf(photoUploader.apply(id.toString(), file));

        contact.setPhotoUrl(photoUrl);
        contactRepository.save(contact);
        return photoUrl;
    }

    private final Function<String, String> fileExtension = filename ->
            Optional.of(filename).filter(name -> name.contains("."))
                    .map(name -> name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile,String> photoUploader = (uuid,image) ->{
        try {
            Path path = Paths.get("PHOTO_DIRECTORY").toAbsolutePath().normalize();

            String fileName = fileExtension.apply(image.getOriginalFilename());
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            Files.copy(image.getInputStream(), path.resolve(uuid + fileName), REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/contacts/image" + fileName ).toUriString();

        }catch (Exception e){
            throw new RuntimeException("Unable to upload image");
        }
    };


}
