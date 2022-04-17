package com.github.gimazdo.archeocat.repository;

import com.github.gimazdo.archeocat.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByChatId(Long chatId);
}