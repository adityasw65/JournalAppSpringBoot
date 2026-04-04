package com.test.restProject.controller;

import com.test.restProject.apiResponse.WeatherResponse;
import com.test.restProject.dto.UserDTO;
import com.test.restProject.entity.JournalEntry;
import com.test.restProject.entity.User;
import com.test.restProject.repository.UserRepository;
import com.test.restProject.services.UserService;
import com.test.restProject.services.WeatherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
@Tag(name = "User APIs", description = "read, update and delete")
public class UserControllerV2 {
   private final UserService userService;
   private final UserRepository userRepository;
   private final WeatherService weatherService;
   @Autowired
   public UserControllerV2(WeatherService weatherService, UserRepository userRepository, UserService userService) {
      this.userService = userService;
      this.userRepository = userRepository;
      this.weatherService = weatherService;
   }

   //   CRUD operation
//   @GetMapping("/all")
//   public List<User> getAllUser() {
//      return userService.getAllData();
//   }
//
   @GetMapping("/one")
   public User getById() {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String username = auth.getName();
      return userService.findByUsername(username);
//      return userService.getDataById(myId).orElse(null);
   }

//   @PostMapping
//   public String savedUser(@RequestBody User user) {
//      userService.saveEntry(user);
//      return "user saved, OK System!";
//   }

//   @DeleteMapping("/id/{myId}")
//   public String deleteUser(@PathVariable ObjectId myId) {
//      userService.deleteData(myId);
//      return "user deleted, OK System!";
//   }

   @PutMapping("/update")
   public ResponseEntity<User> updateUser(@RequestBody UserDTO newUser) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String myUsername = authentication.getName();
      User oldUser = userService.findByUsername(myUsername);
      if (oldUser != null) {
         oldUser.setUserName(newUser.getUserName());
         oldUser.setUserPassword(newUser.getUserPassword());
         userService.saveEntry(oldUser);
      }
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @DeleteMapping("/delete")
   public ResponseEntity<User> deleteUser() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      userRepository.deleteByUserName(authentication.getName());
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
   }

   @GetMapping("/greeting")
   public ResponseEntity<String> greetUser() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      String greeting = "";
      ResponseEntity<WeatherResponse> body = weatherService.getWeather("Mumbai");
      try {
         if(body.getBody() != null) {
            WeatherResponse response = body.getBody();
            greeting = "Day feels like '" + response.getCurrent().getWeatherDescriptions().get(0) + "'" + " and temperature is : " + response.getCurrent().getTemperature();
         }
      }
      catch (Exception e) {
         log.error("GREETING ERROR IN USERCONTROLLER : " + e.getMessage());
      }
//      here we have got the status code and it is already in java object so just called method getStatusCode().value
      return new ResponseEntity<>("Hi, " + authentication.getName().toUpperCase() + "\n" + greeting ,HttpStatus.valueOf(body.getStatusCode().value()));
   }
}
