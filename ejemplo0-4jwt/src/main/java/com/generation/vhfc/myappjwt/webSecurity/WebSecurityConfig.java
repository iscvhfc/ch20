package com.generation.vhfc.myappjwt.webSecurity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.oauth2.login.AuthorizationEndpointDsl;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class WebSecurityConfig {
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authManager) throws Exception {
		
		return http
    			.csrf().disable()
    			.httpBasic()//seguridad de http mecanismo de autenticacion basica
    			.and()
    			.authorizeRequests()
    			.requestMatchers( "/publico/**").permitAll()//url que inicien con publico
    			.requestMatchers( "/admin/**").hasRole("ADMIN")//url que empiesen con admin son del rol admin
    			.anyRequest()
    			.authenticated()    			
    			.and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .build();
    	}
	UserDetailsService userDetailsServiceUser() {	
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("juanHer")
				.password(passwordEncoder().encode("2357"))
				.roles("USER")
				.build());
		manager.createUser(User.withUsername("pedroVar")
				.password(passwordEncoder().encode("14689"))
				.roles("ADMIN")
				.build());
		
		return manager;
	}
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();	    		    		
	}
	@Bean
	AuthenticationManager authenticationManagerUser(HttpSecurity http) throws Exception {
		return http.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsServiceUser())
				.passwordEncoder(passwordEncoder())
				.and()
				.build();
	}
}
