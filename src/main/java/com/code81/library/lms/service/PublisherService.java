package com.code81.library.lms.service;

import com.code81.library.lms.entity.Publisher;
import com.code81.library.lms.repository.PublisherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class PublisherService {
    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherService(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }


    public List<Publisher> findAllPublishers() {
        return publisherRepository.findAll();
    }


    public Optional<Publisher> findPublisherById(Long id) {
        return publisherRepository.findById(id);
    }

    public Publisher savePublisher(Publisher publisher) {
        return publisherRepository.save(publisher);
    }

    public Publisher updatePublisher(Publisher publisherDetails) {
        return publisherRepository.findById(publisherDetails.getId())
                .map(existingPublisher -> {
                    existingPublisher.setName(publisherDetails.getName());
                    existingPublisher.setAddress(publisherDetails.getAddress());
                    existingPublisher.setContactInfo(publisherDetails.getContactInfo());
                    return publisherRepository.save(existingPublisher);
                })
                .orElse(null);
    }

    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }
}