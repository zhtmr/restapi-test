package com.ex.restapipractice.controller;

import com.ex.restapipractice.bean.Post;
import com.ex.restapipractice.bean.User;
import com.ex.restapipractice.exception.UserNotFoundException;
import com.ex.restapipractice.repository.PostRepository;
import com.ex.restapipractice.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/jpa")
public class UserJpaController {
  private UserRepository userRepository;
  private PostRepository postRepository;

  public UserJpaController(UserRepository userRepository, PostRepository postRepository) {
    this.userRepository = userRepository;
    this.postRepository = postRepository;
  }

  @GetMapping("/users")
  public ResponseEntity retrieveAllUsers() {

    List<User> users = userRepository.findAll();
    int count = users.size();
    HashMap<String, Object> map = new HashMap<>();
    map.put("count", count);
    map.put("users", users);
    return ResponseEntity.ok(map);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity retrieveUsersById(@PathVariable int id) {
    Optional<User> user = userRepository.findById(id);

    if (!user.isPresent()) {
      throw new UserNotFoundException("id " + id);
    }

    EntityModel entityModel = EntityModel.of(user.get());
    WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
    WebMvcLinkBuilder selLink = linkTo(methodOn(this.getClass()).retrieveUsersById(id));
    entityModel.add(linTo.withRel("all-users"));  // http://localhost:8088/users
    entityModel.add(selLink.withSelfRel());
    return ResponseEntity.ok(entityModel);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUserById(@PathVariable int id) {
    userRepository.deleteById(id);
  }

  @PostMapping("/users")
  public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
    User saved = userRepository.save(user);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}") // 현재 uri (/users) 뒤에 id 를 붙일건데,
            .buildAndExpand(saved.getId())  // 그 id 는 saved.getId() 에서 가져온다.
            .toUri();
    return ResponseEntity.created(location).build();
  }

  @GetMapping("/users/{id}/posts")
  public List<Post> retrieveAllPostsByUser(@PathVariable int id) {
    Optional<User> user = userRepository.findById(id);
    if (!user.isPresent()) {
      throw new UserNotFoundException("id " + id);
    }

    return user.get().getPosts();
  }

  @PostMapping("/users/{id}/posts")
  public ResponseEntity createPost(@PathVariable int id, @RequestBody Post post) {
    Optional<User> findUser = userRepository.findById(id);
    if (!findUser.isPresent()) {
      throw new UserNotFoundException("id " + id);
    }

    User user = findUser.get();
    post.setUser(user);

    postRepository.save(post);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}") // 현재 uri (/users) 뒤에 id 를 붙일건데,
            .buildAndExpand(post.getId())  // 그 id 는 post.getId() 에서 가져온다.
            .toUri();

    return ResponseEntity.created(location).build();
  }
}
