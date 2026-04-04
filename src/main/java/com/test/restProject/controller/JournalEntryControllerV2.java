package com.test.restProject.controller;

import com.test.restProject.entity.JournalEntry;
import com.test.restProject.entity.User;
import com.test.restProject.services.JournalEntryService;
import com.test.restProject.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal") // this is parent endpoint
@Slf4j
@Tag(name = "Journal APIs", description = "read, save, update and delete")
public class JournalEntryControllerV2 {

   private final JournalEntryService journalEntryService;
   private final UserService userService;
   @Autowired
   public JournalEntryControllerV2(UserService userService, JournalEntryService journalEntryService) {
      this.userService = userService;
      this.journalEntryService = journalEntryService;
   }

   @GetMapping
   @Operation(summary = "Get all journal entries of a user")
   public ResponseEntity<List<JournalEntry>> getAllJournals() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication == null || !authentication.isAuthenticated()) {
         return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
      }
      String myUsername = authentication.getName();
      User user = userService.findByUsername(myUsername);
      List<JournalEntry> allData = user.getJournalEntryList();
      if (allData != null) {
         // you can give directly status code also in ResponseEntity
         return new ResponseEntity<>(allData, HttpStatus.valueOf(200));
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   @PostMapping
   @Operation(summary = "save journal entry of a user")
   public ResponseEntity<?> saveJournal(@RequestBody JournalEntry je) {
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
         String myUsername = authentication.getName();
         journalEntryService.saveEntry(je, myUsername); // this calls service in that saveEntry method
         return new ResponseEntity<>(HttpStatus.CREATED);
      } catch (Exception e) {
         log.error("error for user saving : {}", e.getMessage());
         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
   }

   //   you can use /id or direct id
   @GetMapping("/id/{myId}")
   @Operation(summary = "Get single journal entry of a user by passing journal id")
   public ResponseEntity<JournalEntry> getJournalEntry(@PathVariable long myId) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String username = auth.getName();
      User user = userService.findByUsername(username);
      List<JournalEntry> collect = user.getJournalEntryList()
              .stream()
              .filter(x -> x.getJournalId() == myId)
              .collect(Collectors.toList());
      if (!collect.isEmpty()) {
         JournalEntry journalEntry = journalEntryService.getDataById(myId).orElse(null);
         if (journalEntry != null) {
            return new ResponseEntity<>(journalEntry, HttpStatus.OK);
         }
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

   // we can do with GetMapping also but some standard and this will create conflict
   @DeleteMapping("/id/{myId}")
   @Operation(summary = "Delete journal entry by passing journal id")
   public ResponseEntity<?> deleteJournalEntry(@PathVariable long myId) {
      try {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String username = auth.getName();
         boolean removed = journalEntryService.deleteData(myId, username);
         if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
         }
         else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
         }
      } catch (Exception e) {
         log.error("Error while Deleting Journal entries : {}", e.getMessage() );
         return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
   }

   //   we didn't write separate code for put or update method, we just have update here
   // one this that if I am updating my journalEntries while saving it with users then why again put
   // cause there we update both user and journal entries but here we are just updating journal entries
   // and it is reflecting to user data also ---> called cascading
   @PutMapping("/id/{myId}")
   @Operation(summary = "Update journal entry")
   public ResponseEntity<?> updateJournalEntry(@PathVariable long myId,
                                               @RequestBody JournalEntry newEntry) {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      String username = auth.getName();
      User user = userService.findByUsername(username);
      List<JournalEntry> collectData = user.getJournalEntryList()
              .stream()
              .filter(x -> x.getJournalId() == myId)
              .collect(Collectors.toList());
      if(!collectData.isEmpty()) {
         Optional<JournalEntry> entry = journalEntryService.getDataById(myId);
         if (entry.isPresent()) {
            JournalEntry old = entry.get();
            old.setJournalTitle(newEntry.getJournalTitle() != null && !newEntry.getJournalTitle().equals("") ? newEntry.getJournalTitle() : old.getJournalTitle());
            old.setJournalContent(newEntry.getJournalContent() != null && !newEntry.getJournalContent().equals("") ? newEntry.getJournalContent() : old.getJournalContent());
            // why I have saved old one cause, we have changed in old one that why
            journalEntryService.saveEntry(old); // calling overridden method
            return new ResponseEntity<>(old, HttpStatus.OK);
         }
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
   }

}

/*
Note :
   According to project standards we have to call service in controller and service contains actual
   business logic.

   controller -------> service ---------> repository
*/