package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.entities.UserEntity;
import ru.skypro.homework.service.repositories.AdRepository;
import ru.skypro.homework.service.repositories.CommentRepository;
import ru.skypro.homework.service.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class TestService {

    private final AdRepository adRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public UserEntity createTestUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@gmail.com");
        userEntity.setPassword("$2a$10$ne3GAFgnd5wvTg3kmRr9seGbj.oid.BJ0JSQbWZWS8T1ZXFxyy5wW");
        userEntity.setFirstName("testFirstName");
        userEntity.setLastName("testLastName");
        userEntity.setPhone("+77777777777");
        userEntity.setImage("/users/image/" + userEntity.getId());
        userEntity.setRole(Role.USER);
        userRepository.save(userEntity);
        return userEntity;
    }
}
