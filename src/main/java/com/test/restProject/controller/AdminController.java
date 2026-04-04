package com.test.restProject.controller;

import com.test.restProject.appCache.AppCache;
import com.test.restProject.entity.User;
import com.test.restProject.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin APIs", description = "read all users, save admin role user and restart app-Cache")
public class AdminController {
   private final UserService userService;
   private final AppCache appCache;
   @Autowired
   public AdminController(AppCache appCache, UserService userService) {
      this.userService = userService;
      this.appCache = appCache;
   }

   @GetMapping("/all-users")
   public ResponseEntity<?> getAllUser() {
      List<User> allData = userService.getAllData();
      if(allData != null && !allData.isEmpty()) {
         return new ResponseEntity<>(allData,HttpStatus.FOUND);
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   @PostMapping("/create-new-admin")
   public ResponseEntity<?> createAdmin(@RequestBody User user) {
      userService.saveAdmin(user);
      return new ResponseEntity<>("New Admin user created, OK System!", HttpStatus.CREATED);
   }
//   I have already added one admin by default cause while creating one new admin we have to pass one such
//   user that has admin role and basic authentication such as username and password


   @GetMapping("/restart-api")
   public void reconnectToAPI() {
      appCache.init();
   }
}
