package com.test.restProject.config;

import com.test.restProject.filter.JwtFilter;
import com.test.restProject.services.CustomUserDetailsServiceImplement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringAppSecurity {

   @Autowired
   public CustomUserDetailsServiceImplement customUserDetailsServiceImplement;
   @Autowired
   private JwtFilter jwtFilter;

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http.authorizeHttpRequests(auth -> {
//       order of method chaining is matter here cause it may lead to with or without credential access
                 auth.requestMatchers("/journal/**", "/user/**").authenticated()
                         .requestMatchers("/admin/**").hasRole("ADMIN")
                         .anyRequest()
                         .permitAll();
              })
//              .httpBasic(httpBasic -> {})   // enable basic auth
              .csrf(csrf -> csrf.disable()) // optional for APIs
               // 2. Set Session Management to STATELESS
              .sessionManagement(session -> session
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

              // 3. Add your JWT Filter BEFORE the UsernamePasswordAuthenticationFilter
              .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

      return http.build();
   }

   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//      this is built-in method
      auth.userDetailsService(customUserDetailsServiceImplement).passwordEncoder(passwordEncoder());
   }

   @Bean // here this function will make password in special character thing like #
   public PasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
   }
}

/*

   note: for extending WebSecurityConfigurerAdapter is no longer part of spring 3+ version
   so that's why I have to copy and paste the code for security from chatgpt
*/


/*



@Configuration
@EnableWebSecurity
public class SpringSecurity {


    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        return http.authorizeHttpRequests(request -> request
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/journal/**", "/user/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


*/
