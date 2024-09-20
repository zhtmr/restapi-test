package com.ex.restapipractice.controller;

import com.ex.restapipractice.bean.AdminUser;
import com.ex.restapipractice.bean.AdminUserV2;
import com.ex.restapipractice.bean.User;
import com.ex.restapipractice.dao.UserDaoService;
import com.ex.restapipractice.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import javax.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminUserController {
  private UserDaoService userDaoService;

  public AdminUserController(UserDaoService userDaoService) {
    this.userDaoService = userDaoService;
  }

  //  @GetMapping("/v1/users/{id}")
  //  @GetMapping(value = "/users/{id}", params = "v=1")
  //  @GetMapping(value = "/users/{id}", headers = "X-API-VERSION=1")
  @GetMapping(value = "/users/{id}", produces = "application/ex.restapi.test1+json")
  public MappingJacksonValue retrieveAdminUser(@PathVariable int id) {
    User user = userDaoService.findOne(id);
    AdminUser adminUser = new AdminUser();
    if (user == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    } else {
      BeanUtils.copyProperties(user, adminUser);
    }

    SimpleBeanPropertyFilter filter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
    FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

    MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
    mapping.setFilters(filters);
    return mapping;
  }

  @GetMapping("/users")
  public MappingJacksonValue retrieveAllUsersForAdmin() {
    List<User> users = userDaoService.findAll();
    List<AdminUser> adminUsers = new ArrayList<>();
    AdminUser adminuser = null;
    for (User user : users) {
      adminuser = new AdminUser();
      BeanUtils.copyProperties(user, adminuser);
      adminUsers.add(adminuser);
    }

    SimpleBeanPropertyFilter filter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "ssn");
    FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter);

    MappingJacksonValue mapping = new MappingJacksonValue(adminUsers);
    mapping.setFilters(filters);
    return mapping;
  }

  //  @GetMapping("/v2/users/{id}")
  //  @GetMapping(value = "/users/{id}", params = "v=2")
  @GetMapping(value = "/users/{id}", produces = "application/ex.restapi.test2+json")
  public MappingJacksonValue retrieveAdminUser2(@PathVariable int id) {
    User user = userDaoService.findOne(id);
    AdminUserV2 adminUser = new AdminUserV2();
    if (user == null) {
      throw new UserNotFoundException(String.format("ID[%s] not found", id));
    } else {
      BeanUtils.copyProperties(user, adminUser);
      adminUser.setGrade("VIP");
    }

    SimpleBeanPropertyFilter filter =
        SimpleBeanPropertyFilter.filterOutAllExcept("id", "name", "joinDate", "grade");
    FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter);

    MappingJacksonValue mapping = new MappingJacksonValue(adminUser);
    mapping.setFilters(filters);
    return mapping;
  }
}
