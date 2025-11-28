package com.ecommerce.auth_server;

import com.ecommerce.auth_server.config.RsaProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaProperties.class)
public class OAuthAuthServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(OAuthAuthServerApplication.class, args);
	}

}
