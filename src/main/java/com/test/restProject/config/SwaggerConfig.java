package com.test.restProject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SwaggerConfig {
   @Bean
   public OpenAPI configSwaggerInfo() {
      return new OpenAPI()
//              basic information like title, description etc
              .info(
                      new Info().title("Journal App APIs")
                      .description("-  By Aditya.<br><b>For create, update, read, delete journals</b>")
              )
//              to add multiple server url
              .servers(
                      List.of(new Server().url("http://localhost:8080").description("Local"),
                              new Server().url("http://localhost:8081").description("Live"))
              )
//              it way to customize order, but it is not working
              .tags(Arrays.asList(
                      new Tag().name("Health Check API"),
                      new Tag().name("Public APIs").description("testing api, create user and login with user"),
                      new Tag().name("Journal APIs").description("read, save, update and delete"),
                      new Tag().name("User APIs").description("read, update and delete"),
                      new Tag().name("Admin APIs").description("read all users, save admin role user and restart app-Cache"),
                      new Tag().name("Old Journal APIs").description("read, save, update and delete")
              ))
//              this below code for bearer token permission
              .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
              .components(new Components()
                      .addSecuritySchemes("bearerAuth", new SecurityScheme()
                              .name("bearerAuth")
                              .type(SecurityScheme.Type.HTTP)
                              .scheme("bearer")
                              .bearerFormat("JWT")
                      ));
   }
}
