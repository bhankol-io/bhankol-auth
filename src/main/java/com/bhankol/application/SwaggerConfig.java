package com.bhankol.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket distributorApi() {

		return new Docket(DocumentationType.SWAGGER_2)
				.securitySchemes(apiKey())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.bhankol.application.controller"))
				.paths(regex("/api/.*"))
				.build()
				.apiInfo(metaData());
	}
	
    @SuppressWarnings("deprecation")
	private ApiInfo metaData() {

        ApiInfo apiInfo = new ApiInfo("", "", "", "", "", "", "");
        
        return apiInfo;

    }

	private List<ApiKey> apiKey() {
		List<ApiKey> keys = new ArrayList<>();
		keys.add(new ApiKey("Authorization", "api_key", "header"));
		return keys;
	}
}
