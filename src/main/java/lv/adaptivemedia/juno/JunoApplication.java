package lv.adaptivemedia.juno;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JunoApplication {

	public static void main(String[] args) {
		SpringApplication.run(JunoApplication.class, args);
	}

}
