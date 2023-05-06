package com.ufcg.booker;

import com.ufcg.booker.security.LoggedUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BookerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookerApplication.class, args);
	}

	@GetMapping("/")
	public String hello(@AuthenticationPrincipal LoggedUser loggedUser) {
		return String.format("Hello %s!", loggedUser.get().getEmail());
	}

	@GetMapping("/hello")
	public String helloWorld(@RequestParam(value = "name", defaultValue = "World") String name) {
		return String.format("Hello %s!", name);
	}
}
