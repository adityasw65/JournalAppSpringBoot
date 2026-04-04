package com.test.restProject.controller;

import com.test.restProject.entity.JournalEntry;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/_journal") // this is parent endpoint
@Tag(name = "Old Journal APIs", description = "read, save, update and delete")
public class JournalEntryController {

   private Map<Long, JournalEntry> m1 = new HashMap<>();

//   all methods in controller should be public so they can easily access by spring framework
//   here how spring get know that request is get or post, we haven't given endpoint for get or post
//   then it will see Mapping type get or post
   @GetMapping
   public List<JournalEntry> getAllJournals() {
      return new ArrayList<>(m1.values());
   }

   @PostMapping
   public String saveJournal(@RequestBody JournalEntry je) {
      m1.put(je.getJournalId(), je);
      return "saved";
   }

//   you can use /id or direct id
   @GetMapping("/id/{myId}")
   public JournalEntry getJournalEntry(@PathVariable long myId) {
      return m1.get(myId);
   }

   // we can do with GetMapping also but some standard and this will create conflict
   @DeleteMapping("/id/{myId}")
   public JournalEntry deleteJournalEntry(@PathVariable long myId) {
      return m1.remove(myId);
   }

   @PutMapping("/id/{myId}")
   public JournalEntry updateJournalEntry(@PathVariable long myId, @RequestBody JournalEntry je) {
      return m1.put(myId, je);
   }

}
