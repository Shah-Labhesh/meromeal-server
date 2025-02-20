package com.labhesh.MeroMeal;

import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.swagger.v3.oas.models.info.Info;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;
import jakarta.annotation.PostConstruct;

@SecurityScheme(
		name = "auth",
		type = SecuritySchemeType.HTTP,
		scheme = "bearer",
		bearerFormat = "JWT"
)
@SpringBootApplication
@Controller
@Tag(name = "Default")
@EnableAsync
public class MeroMealApplication {

	@Value("${swagger.api.title}")
	private String title;
	@Value("${swagger.api.description}")
	private String description;
	@Value("${swagger.api.version}")
	private String version;
	@Value("${application.timezone}")
	private String applicationTimeZone;
	
	public static void main(String[] args) {
		SpringApplication.run(MeroMealApplication.class, args);
	}

	@PostConstruct
	public void executeAfterMain() {
		TimeZone.setDefault(TimeZone.getTimeZone(applicationTimeZone));
		System.out.println("Application Timezone set to : " + applicationTimeZone);
		System.out.println("Application started at : " + java.time.LocalDateTime.now());
	}

	@GetMapping("/")
    public String redirectToSwagger() {
        return "redirect:/swagger-ui.html";
    }

	// swagger

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title(title)
						.version(version)
						.description(description)
						.license(new License().name("Apache 2.0").url("https://springdoc.org")));
	}

}
