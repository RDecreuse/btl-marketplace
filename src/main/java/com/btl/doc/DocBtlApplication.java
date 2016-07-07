package com.btl.doc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.context.web.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages = "com.btl.doc")
@EnableAutoConfiguration(exclude = RepositoryRestMvcAutoConfiguration.class)
public class DocBtlApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DocBtlApplication.class, args);
	}
}
