package com.joel.controller;

import com.joel.model.User;
import com.joel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    UserRepository userRepository;

    public UserController (UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @PostMapping("/api/user")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (!user.isEmpty()) {
            this.userRepository.save(user);
            return ResponseEntity.ok().body(user);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user");
    }

    @GetMapping("/api/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok().body(userRepository.findAll());
    }

    @GetMapping("/api/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/api/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id,
                                           @RequestBody User user) {
        Optional<User> opt = userRepository.findById(id);
        if(!user.isEmpty() && !opt.isEmpty()) {
            User existingUser = opt.get();

            existingUser.setFullName(user.getFullName());
            existingUser.setEmail(user.getEmail());
            existingUser.setRole(user.getRole());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            userRepository.save(existingUser);
            return ResponseEntity.ok(existingUser);

        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update");
        }
    }

    @DeleteMapping("/api/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> opt = userRepository.findById(id);
        if(opt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user not found");
        } else {
            User user = opt.get();

            userRepository.delete(user);
            return ResponseEntity.ok().body("user deleted");
        }
    }
}
