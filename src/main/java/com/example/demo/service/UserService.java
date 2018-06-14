package com.example.demo.service;

import com.example.demo.domain.User;
import com.example.demo.exception.*;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(User user) throws BadRequestException {
        validateRequiredField(user.getName(), "name");
        validateMaxSiseField(user.getName(), 255, "name");

        validateRequiredField(user.getEmail(), "email");
        validateMaxSiseField(user.getEmail(), 255, "email");
        validateEmailFormat(user.getEmail());

        validateRequiredField(user.getCpf(), "cpf");
        if (user.getCpf().length() != 11) {
            throw new InvalidFieldFormatException("Invalid cpf");
        }

        validateRequiredField(user.getBirthDate(), "birthDate");
        if (Period.between(user.getBirthDate(), LocalDate.now()).getYears() < 18) {
            throw new InvalidAgeException("Age must be greater than 18 years old");
        }

        return repository.save(user);
    }

    private void validateRequiredField(String field, String fieldName) throws RequiredFieldException {
        if (isBlank(field)) {
            throw new RequiredFieldException(String.format("%s is required", fieldName));
        }
    }

    private void validateRequiredField(Object field, String fieldName) throws RequiredFieldException {
        if (Objects.isNull(field)) {
            throw new RequiredFieldException(String.format("%s is required", fieldName));
        }
    }

    private void validateMaxSiseField(String field, int fieldSize, String fieldName) throws FieldMaxSizeException {
        if (field.length() > fieldSize) {
            throw new FieldMaxSizeException(String.format("%s must be less than %d characters", fieldName, fieldSize));
        }
    }

    public User findById(UUID id) throws UserNotFound {
        return repository.findById(id).orElseThrow(UserNotFound::new);
    }

    public Collection<User> findAll() {
        Collection<User> collection = new ArrayList<>();
        repository.findAll().forEach(collection::add);
        return collection;
    }

    public User update(User user) {
        return repository.save(user);
    }

    public void delete(UUID id) throws UserNotFound {
        repository.delete(findById(id));
    }

    private boolean isBlank(String field) {
        return Objects.isNull(field) || field.trim().isEmpty();
    }

    private void validateEmailFormat(String email) throws InvalidFieldFormatException {
        Pattern emailRegex = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z" +
                "0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?" +
                "[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\" +
                "x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
        if (!emailRegex.matcher(email).matches()) {
            throw new InvalidFieldFormatException("Invalid email format");
        }
    }
}
