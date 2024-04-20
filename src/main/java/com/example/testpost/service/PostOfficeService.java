package com.example.testpost.service;

import com.example.testpost.entity.PostOffice;
import com.example.testpost.repository.PostOfficeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostOfficeService {

    @Autowired
    private PostOfficeRepository postOfficeRepository;

    public PostOffice findById(Long id){
        return postOfficeRepository.findById(Math.toIntExact(id)).get();
    }
}
