package se.deved.lektion7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Detta är huvudklassen för vår Spring Boot-applikation
// @SpringBootApplication säger till Spring att detta är startpunkten för programmet
@SpringBootApplication
public class Lektion7Application {

	// main-metoden är startpunkten för alla Java-program
	// Den startar hela Spring Boot-applikationen
	public static void main(String[] args) {
		// SpringApplication.run() startar webbservern och alla Spring-komponenter
		SpringApplication.run(Lektion7Application.class, args);
	}

}
