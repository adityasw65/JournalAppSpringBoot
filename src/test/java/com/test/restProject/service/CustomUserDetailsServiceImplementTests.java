package com.test.restProject.service;


import com.test.restProject.entity.User;
import com.test.restProject.repository.UserRepository;
import com.test.restProject.services.CustomUserDetailsServiceImplement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Arrays;

import static org.mockito.Mockito.*;

//@SpringBootTest
public class CustomUserDetailsServiceImplementTests {

//   @Autowired
   @InjectMocks
   private CustomUserDetailsServiceImplement customUserDetailsServiceImplement;

//   @MockitoBean
   @Mock
   private UserRepository userRepository;

   @BeforeEach
   void setupMocks() {
//      MockitoAnnotations.openMocks(CustomUserDetailsServiceImplementTests.class);
//      or
      MockitoAnnotations.openMocks(this); // this current class
   }


//   @Disabled
   @Test
   void loadUsernameByUsernameTest() {
      User mockUser = new User();
      mockUser.setUserName("admin");
      mockUser.setUserPassword("asfasjnu94nimasi");
      mockUser.setRoles(Arrays.asList("ADMIN","USER"));

      when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(mockUser);
      UserDetails admin = customUserDetailsServiceImplement.loadUserByUsername("admin");
      Assertions.assertNotNull(admin);
   }
}

/*

   Explanation :
      Here I have written @SpringBootTest hence all beans have been creating and automatically it
      assigned. for example customUserDetailsServiceImplement

      why we @MockitoBean for UserRepository, cause this customUserDetailsServiceImplement class depends
      upon the UserRepository. inside loadUserByUsername method we are calling findByUserName (story
      of CustomUserDetailsServiceImplement class) so we need to call that function findByUserName through
      the bean of userRepository that's why we are passing mock bean

      in function @Test - loadUsernameByUsernameTest
         we are setting mock user data.
         here we are using when method that contains this -
         when(userRepository.findByUserName(ArgumentMatchers.anyString())).thenReturn(mockUser);
         so when statement executes and passed any string as username in
         CustomUserDetailsServiceImplement class then pass mockUser
       (you can debug it). we have set random password

       we are storing UserDetails and checking

  -----------------------------------------------------------------------------------------------------

       because we are using @SpringBootTest it creates all beans not only for particular class that
       we are using,
         @InjectMocks for customUserDetailsServiceImplement
         and @Mock for userRepository

         why @Mock, why not @MockitoBean for userRepository cause,
         @InjectMocks is for Inject mock beans only inside class whichever it is.
         @Mock is for create fake mock bean, it is not working with
         spring container(@SpringBootTest) that's why we have to write separate function for setup mock
         beans using @BeforeEach - before each test create mock beans.


*/



