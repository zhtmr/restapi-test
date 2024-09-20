package com.ex.restapipractice.controller;

import com.ex.restapipractice.bean.User;
import com.ex.restapipractice.dao.UserDaoService;
import com.ex.restapipractice.exception.UserNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "user-controller", description = "일반 사용자 서비스를 위한 컨트롤러입니다.")
public class UserController {
  private UserDaoService userDaoService;

  public UserController(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  @GetMapping("/users")
  public List<User> retrieveAllUsers() {
    return userDaoService.findAll();
  }

  @Operation(summary = "사용자 정보 조회 API", description = "사용자 ID 를 이용해서 사용자 상세 정보 조회를 합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "OK !!"),
      @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
      @ApiResponse(responseCode = "404", description = "USER NOT FOUND !!"),
      @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!"),
  })
  @GetMapping("/users/{id}")
  public EntityModel<User> retrieveUser(
      @Parameter(description = "사용자 ID", required = true, example = "1") @PathVariable int id) {
    User user = userDaoService.findOne(id);
    if (user == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    }
    EntityModel entityModel = EntityModel.of(user);
    WebMvcLinkBuilder linTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
    WebMvcLinkBuilder selLink = linkTo(methodOn(this.getClass()).retrieveUser(id));
    entityModel.add(linTo.withRel("all-users"));  // http://localhost:8088/users
    entityModel.add(selLink.withSelfRel());    // http://localhost:8088/users/2
    return entityModel;
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
