package com.test.restProject.controller;

import com.test.restProject.entity.User;
import com.test.restProject.repository.UserRepository;
import com.test.restProject.repository.UserRepositoryImpl;
import com.test.restProject.services.CustomUserDetailsServiceImplement;
import com.test.restProject.services.UserService;
import com.test.restProject.utility.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/public")
public class PublicController {
   @Autowired
   private UserService userService;
   @Autowired
   private CustomUserDetailsServiceImplement customUserDetailsServiceImplement;
   @Autowired
   private JWTUtil jwtUtil;
   @Autowired
   private AuthenticationManager authenticationManager;

   @GetMapping("/testing")
   public String testRequest() {
      return "testing done, OK System!";
   }

   @PostMapping("/signup")
   public String signup(@RequestBody User user) {
      userService.createUser(user);
      return "user saved, OK System!";
   }

//      here first user signup and username and password will create using that user and passw user will login
//      again then we pass user one token and then next time when user wants to login that time
//      they will login with token
   @PostMapping("/login")
   public ResponseEntity<String> login(@RequestBody User user) {
      try {
         authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getUserPassword()));
         UserDetails userDetails = customUserDetailsServiceImplement.loadUserByUsername(user.getUserName());
//         Think of a Claim as a "Statement of Truth" that
//         the server makes about the user and signs with a digital seal.
         Map<String, Object> claims = new HashMap<>();
         String jwt = jwtUtil.generateToken(claims, userDetails.getUsername());
         return new ResponseEntity<>(jwt, HttpStatus.OK);
      }
      catch (Exception e) {
         log.error("Exception occur at JWT public controller : ", e);
         return new ResponseEntity<>("Something went wrong with JWT ", HttpStatus.BAD_REQUEST);
      }
   }

}
