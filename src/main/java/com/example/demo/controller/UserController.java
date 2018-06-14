package com.example.demo.controller;

import com.example.demo.domain.User;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.UserNotFound;
import com.example.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

@RestController
public class UserController {

    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> get(@PathVariable("id") UUID id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (UserNotFound userNotFound) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/")
    public ResponseEntity<Collection<User>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping(path = "/")
    public ResponseEntity<User> create(@RequestBody User user) throws BadRequestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(user));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity delete(@PathVariable("id") UUID id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (UserNotFound userNotFound) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(path = "/")
    public ResponseEntity<User> update(@RequestBody User user) {
        return ResponseEntity.ok(service.update(user));
    }

}
