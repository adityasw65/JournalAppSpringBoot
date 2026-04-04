package com.test.restProject.entity;

// to connect with mongodb we have extends MongoRepository which we have done it, now we have to map
// class with collection(table)

import com.test.restProject.enums.Sentiments;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Journal_Entries")
@Data // this will add all annotations like @Getter, @Setter, toString etc...
public class JournalEntry {
   @Id
   private long journalId;
   @NonNull
   @NotEmpty
   @NotBlank
   private String journalTitle;
   @NotNull
   @NotEmpty
   @NotBlank
   private String journalContent;
   private Sentiments sentiments;

//   public long getJournalId() {
//      return journalId;
//   }
//
//   public void setJournalId(long journalId) {
//      this.journalId = journalId;
//   }
//
//   public String getJournalTitle() {
//      return journalTitle;
//   }
//
//   public void setJournalTitle(String journalTitle) {
//      this.journalTitle = journalTitle;
//   }
//
//   public String getJournalContent() {
//      return journalContent;
//   }
//
//   public void setJournalContent(String journalContent) {
//      this.journalContent = journalContent;
//   }
}

/*

controller -------> service ---------> repository

this is just pojo class (plain old java object class). this is for exchanging data through this

*/

