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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

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
    public void findById() throws BadRequestException, UserNotFound {
        UUID randomUUID = UUID.randomUUID();
        User user = new User().setId(randomUUID);
        when(repository.findById(randomUUID)).thenReturn(Optional.of(user));
        assertThat(service.findById(randomUUID)).isEqualTo(user);
        verify(repository).findById(randomUUID);
    }

    @Test(expected = UserNotFound.class)
    public void findByIdNotFound() throws UserNotFound {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        service.findById(UUID.randomUUID());
    }

    @Test
    public void findAll() {
        when(repository.findAll()).thenReturn(Collections.EMPTY_LIST);
        assertThat(service.findAll()).isEmpty();
    }

    @Test
    public void update() {
        UUID uuid = UUID.randomUUID();
        User user = new User()
                .setId(uuid)
                .setName("name").setEmail("mail@mail.com").setCpf("11111111111")
                .setBirthDate(LocalDate.of(1998, 10, 9));
        when(repository.save(user)).thenReturn(user);
        assertThat(service.update(user)).isEqualTo(user);
        verify(repository).save(user);
    }

    @Test
    public void delete() throws UserNotFound {
        UUID randomUUID = UUID.randomUUID();
        User user = new User().setId(randomUUID);
        when(repository.findById(randomUUID)).thenReturn(Optional.of(user));
        service.delete(randomUUID);
        verify(repository).delete(user);
    }

    @Test(expected = UserNotFound.class)
    public void deleteNotFound() throws UserNotFound {
        when(repository.findById(any(UUID.class))).thenReturn(Optional.empty());
        service.delete(UUID.randomUUID());
    }
}