package com.test.restProject.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "User_Entries")
@Data
@Builder
@AllArgsConstructor // used this annotation cause builder require this
@NoArgsConstructor // this for when I mapped with user then it was calling RequiredArgsConstructor cause data annotation contains it
public class User {
   @Id
   private ObjectId userId; // you can do anything, I have taken Object cause MongoDB test, ID
   @Indexed(unique = true) // it will not create automatic so you have to add property in properties file
   @NonNull
   @NotEmpty
   @NotBlank
   private String userName;
   private String email;
   private boolean sentimentsAnalysis;
   @NonNull
   @NotEmpty
   @NotBlank
   private String userPassword;
   @DBRef // this annotation make sure that just Ids of Journal Entry map with user
   // if you want entire document map with it just remove @DBRef
   // @DBRef stored ID and name collection rather than storing entire object to maintain communication between
   // two different collections
   private List<JournalEntry> journalEntryList;

//   for spring security - we need authorization so roles matters
   private List<String> roles;

}

/*
   Note : with @Builder you have to write @AllArgsConstructor and @NoArgsConstructor together
   Why @Builder ?
      -  suppose you have 10 field, you are passing or initializing data through constructor but the problem
      is you don't know the actual order of fields, suppose you have passed wrong order and datatype is
      string of all fields then order may stored wrong, so to avoid these type of big mistakes
      we use @Builder annotation
      how to user
      @Builder
      class ClassName{}

      -  and while passing data use this - class is User
      User user = User.builder().userId(1).userName("adityawaghmare").userPassword("aditya3248").build();
*/

