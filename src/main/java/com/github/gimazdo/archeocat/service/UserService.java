package com.github.gimazdo.archeocat.service;

import com.github.gimazdo.archeocat.entity.User;
import com.github.gimazdo.archeocat.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public User findByChatId(Long chatId) {
        return userRepository.findByChatId(chatId);
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }


}