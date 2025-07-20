package at.magic.olga;

import at.magic.olga.config.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CorsProperties.class)

public class OlgaApplication {

	public static void main(String[] args) {
		SpringApplication.run(OlgaApplication.class, args);
	}

}
