package com.ex.restapipractice.controller;

import com.ex.restapipractice.bean.User;
import com.ex.restapipractice.dao.UserDaoService;
import com.ex.restapipractice.exception.UserNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class UserController {
  private UserDaoService userDaoService;

  public UserController(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  @GetMapping("/users")
  public List<User> retrieveAllUsers() {
    return userDaoService.findAll();
  }

  @GetMapping("/users/{id}")
  public User retrieveUser(@PathVariable int id) {
    User user = userDaoService.findOne(id);
    if (user == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    }
    return user;
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    User saved = userDaoService.save(user);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}") // 현재 uri (/users) 뒤에 id 를 붙일건데,
            .buildAndExpand(saved.getId())  // 그 id 는 saved.getId() 에서 가져온다.
            .toUri();
    return ResponseEntity.created(location).build();
  }

  @DeleteMapping("/users/{id}")
  public ResponseEntity deleteUser(@PathVariable int id) {
    User deleted = userDaoService.deleteById(id);
    if (deleted == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    }
    return ResponseEntity.noContent().build();
  }
}
