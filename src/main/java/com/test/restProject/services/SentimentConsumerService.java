package com.test.restProject.services;

import com.test.restProject.entity.SentimentsData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class SentimentConsumerService {
   @Autowired
   private EmailService emailService;

   @KafkaListener(topics = "sentiment_data", groupId = "sentiment_data_analysis")
   public void consume(SentimentsData sentimentsData) {
      sending(sentimentsData);
   }
   public void sending(SentimentsData sentimentsData) {
      emailService.sendEmail(sentimentsData.getEmail(), "Sentiments of previous week", sentimentsData.getSentiment());
   }
}

/*

command for print consumer data

PS C:\kafka_2.13-4.2.0\kafka> bin\windows\kafka-console-consumer.bat --topic sentiment_data --from-beginning --bootstrap-server localhost:9092 --formatter-property print.key=true --formatter-property key.separator=" : "

2026-03-28T05:12:21.532863800Z main ERROR Reconfiguration failed: No configuration found for '266474c2' at 'null' in 'null'
waghmareaditya3248@gmail.com : {"email":"waghmareaditya3248@gmail.com","sentiment":"Sentiments for last days 'JOY'"}
waghmareadityas65@gmail.com : {"email":"waghmareadityas65@gmail.com","sentiment":"Sentiments for last days 'HAPPY'"}
waghmareaditya3248@gmail.com : {"email":"waghmareaditya3248@gmail.com","sentiment":"Sentiments for last days 'JOY'"}
waghmareadityas65@gmail.com : {"email":"waghmareadityas65@gmail.com","sentiment":"Sentiments for last days 'HAPPY'"}

*/