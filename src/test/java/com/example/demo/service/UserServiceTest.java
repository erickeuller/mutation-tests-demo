package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.exception.*;
import com.example.demo.repository.UserRepository;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test(expected = RequiredFieldException.class)
    public void createEmptyName() throws BadRequestException {
        User user = new User().setName("");
        service.create(user);
    }

    @Test(expected = FieldMaxSizeException.class)
    public void createOverMaxName() throws BadRequestException {
        User user = new User().setName(RandomString.make(300));
        service.create(user);
    }

    @Test(expected = RequiredFieldException.class)
    public void createEmptyEmail() throws BadRequestException {
        User user = new User().setName("name").setEmail("");
        service.create(user);
    }

    @Test(expected = FieldMaxSizeException.class)
    public void createOverMaxEmail() throws BadRequestException {
        User user = new User().setName("name").setEmail(RandomString.make(300));
        service.create(user);
    }

    @Test(expected = InvalidFieldFormatException.class)
    public void createInvalidMail() throws BadRequestException {
        User user = new User().setName("name").setEmail("erick123");
        service.create(user);
    }

    @Test(expected = RequiredFieldException.class)
    public void createEmptyCpf() throws BadRequestException {
        User user = new User().setName("name").setEmail("mail@mail.com");
        service.create(user);
    }

    @Test(expected = InvalidFieldFormatException.class)
    public void createInvalidCPF() throws BadRequestException {
        User user = new User().setName("name").setEmail("mail@mail.com").setCpf("123");
        service.create(user);
    }

    @Test(expected = RequiredFieldException.class)
    public void createEmptyBirthDate() throws BadRequestException {
        User user = new User().setName("name").setEmail("mail@mail.com").setCpf("11111111111");
        service.create(user);
    }

    @Test(expected = InvalidAgeException.class)
    public void createInvalidAge() throws BadRequestException {
        User user = new User().setName("name").setEmail("mail@mail.com").setCpf("11111111111")
                .setBirthDate(LocalDate.of(2001, 10, 9));
        service.create(user);
    }

    @Test
    public void create() throws BadRequestException {
        User user = new User().setName("name").setEmail("mail@mail.com").setCpf("11111111111")
                .setBirthDate(LocalDate.of(1998, 10, 9));
        when(repository.save(user)).thenReturn(user);
        assertThat(service.create(user)).isEqualTo(user);
    }


    @Test
    public void findById() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}