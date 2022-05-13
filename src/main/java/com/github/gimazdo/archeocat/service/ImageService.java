package com.github.gimazdo.archeocat.service;


import com.github.gimazdo.archeocat.entity.Image;
import com.github.gimazdo.archeocat.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;

    public Image add(Image image){
        return imageRepository.save(image);
    }

    public Image findById(Long id){
        return imageRepository.getById(id);
    }

}
