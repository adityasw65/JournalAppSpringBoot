package com.test.restProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
   @NotNull
   @Schema(name = "User's username")
   private String userName;
   @NotNull
   @Schema(name = "User's password")
   private String userPassword;

   /*
      Notice :
         here are three things missing, (userId, JournalEntryList, roles)
         why?
            1. UserId - cause it automatically created by database
            2. JournalEntryList - this is also created/mapped by database
            3. roles - often assign internally
    */
}
