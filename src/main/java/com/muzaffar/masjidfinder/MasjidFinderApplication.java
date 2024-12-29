package com.muzaffar.masjidfinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MasjidFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasjidFinderApplication.class, args);
	}

}
