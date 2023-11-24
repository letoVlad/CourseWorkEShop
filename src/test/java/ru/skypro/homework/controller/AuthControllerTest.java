package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.service.entities.UserEntity;
import ru.skypro.homework.service.repositories.UserRepository;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private TestService testService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName(value = "Авторизация пользователя")
    void shouldReturnOkWhenAuthPassed() throws Exception {

        testService.createTestUser();

        Login login = new Login();
        login.setPassword("testPassword");
        login.setUsername("test@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .header(HttpHeaders.AUTHORIZATION,
                                "Basic " + HttpHeaders.encodeBasicAuth("testEmail@gmail.com",
                                        "testPassword", StandardCharsets.UTF_8))
                        .content(objectMapper.writeValueAsString(login))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName(value = "Регистрация пользователя")
    void shouldReturnCreatedWhenRegistryPassed() throws Exception {

        Register register = new Register();
        register.setUsername("testUserEmail@gmail.com");
        register.setPassword("testUserPassword");
        register.setFirstName("testUserFirstName");
        register.setLastName("testUserLastName");
        register.setPhone("+79444444444");
        register.setRole(Role.USER);

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .content(objectMapper.writeValueAsString(register))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isCreated());

        Assertions.assertFalse(userRepository.findByEmail(register.getUsername()).getEmail().isEmpty());
        UserEntity newUser = userRepository.findByEmail(register.getUsername());

        Assertions.assertEquals(newUser.getEmail(), register.getUsername());
        Assertions.assertTrue(passwordEncoder.matches(register.getPassword(), newUser.getPassword()));
        Assertions.assertEquals(newUser.getFirstName(), register.getFirstName());
        Assertions.assertEquals(newUser.getLastName(), register.getLastName());
        Assertions.assertEquals(newUser.getPhone(), register.getPhone());
        Assertions.assertEquals(newUser.getRole(), register.getRole());
    }

}