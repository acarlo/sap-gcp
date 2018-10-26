package com.application.sapgcpspring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@SpringBootApplication
@ComponentScan({"com.application.sapgcpspring","com.gdpr.apis.controller"})
@EnableAutoConfiguration
public class SapGcpSpringApplication extends SpringBootServletInitializer{

	  @Override
	    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
	        return application.sources(SapGcpSpringApplication.class);
	    }
	  
	public static void main(String[] args) {
		SpringApplication.run(SapGcpSpringApplication.class, args);
	}
}
