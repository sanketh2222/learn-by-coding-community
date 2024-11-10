package org.lbcc.bms.bms_monolith;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BmsMonolithApplication {

	public static void main(String[] args) {
		SpringApplication.run(BmsMonolithApplication.class, args);
	}

}
