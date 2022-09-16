package com.alkemy.ong;

import com.alkemy.ong.entity.NewsEntity;
import com.alkemy.ong.repository.NewsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class OngApplication {

	public static void main(String[] args) {
		SpringApplication.run(OngApplication.class, args);
	}


}
