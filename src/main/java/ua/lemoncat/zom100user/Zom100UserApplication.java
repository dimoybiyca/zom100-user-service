package ua.lemoncat.zom100user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Zom100UserApplication {

	public static void main(String[] args) {
		System.getProperties().forEach((key, value) -> System.out.println(key + ": " + value));
		SpringApplication.run(Zom100UserApplication.class, args);
	}
}
