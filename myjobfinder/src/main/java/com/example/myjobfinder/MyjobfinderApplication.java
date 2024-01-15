package com.example.myjobfinder;

import com.example.myjobfinder.authorizer.Authorizer;
import com.example.myjobfinder.authorizer.AuthorizerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.PathVariable;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class MyjobfinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyjobfinderApplication.class, args);
		System.out.println("zalupa");
		AuthorizerConfig config = new AuthorizerConfig();
		Authorizer oath = new Authorizer(config);
		oath.getTokens();
		System.out.println(config.getAccessToken());
		System.out.println(config.getRefreshToken());
//		System.out.println(config.getClientID());
	}

}
