package com.test.restProject.scheduler;

import com.test.restProject.entity.SentimentsData;
import com.test.restProject.entity.User;
import com.test.restProject.enums.Sentiments;
import com.test.restProject.repository.UserRepositoryImpl;
import com.test.restProject.services.EmailService;
import com.test.restProject.services.SentimentsAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler1 {
   @Autowired
   private UserRepositoryImpl userRepository;
   @Autowired
   private KafkaTemplate<String, SentimentsData> kafkaTemplate;

   @Scheduled(cron = "0 30 10 28 * ?")
   public void fetchUserAndEmailStoreInKafka() {
      List<User> allUserInCustomizeWay = userRepository.getAllUserInCustomizeWay();
      for (User user: allUserInCustomizeWay) {
         List<Sentiments> collectSentiments = user.getJournalEntryList().stream().map(x -> x.getSentiments()).collect(Collectors.toList());
         Map<Sentiments, Integer> countSentiments = new HashMap<>();
         for (Sentiments sentiment : collectSentiments) {
            if(sentiment != null) {
               countSentiments.put(sentiment, countSentiments.getOrDefault(sentiment, 0) + 1);
            }
         }
         Sentiments mostFrequentSentiments = null;
         int maxCount = 0;
         for(Map.Entry<Sentiments, Integer> entry: countSentiments.entrySet()) {
            if(entry.getValue() > maxCount) {
               maxCount = entry.getValue();
               mostFrequentSentiments = entry.getKey();
            }
         }

         if(mostFrequentSentiments != null) {
            SentimentsData sentimentsData = SentimentsData.builder().email(user.getEmail()).sentiment("Sentiments for last days '" + mostFrequentSentiments + "'").build();
//            this below line acts as producer in the code
            kafkaTemplate.send("sentiment_data", sentimentsData.getEmail(), sentimentsData);
         }
      }
   }
}

/*

here the problem with kafka cloud was, it was saying to make payment

*/
