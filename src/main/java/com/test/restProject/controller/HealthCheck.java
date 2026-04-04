package com.test.restProject.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check API")
public class HealthCheck {

   @GetMapping("/healthCheck")
   public String healthCheck() {
      return "200 OK health";
   }
}
