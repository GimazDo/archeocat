package com.github.gimazdo.archeocat.repository;

import com.github.gimazdo.archeocat.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}