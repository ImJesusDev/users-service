
package com.jdiaz.users.service.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

	private Contact contact = new Contact("Jesús Díaz", "https://github.com/ImJesusDev", "jesus740@gmail.com");
	private ApiInfo apiInfo = new ApiInfoBuilder()
			.title("Users Micro Service Documentation")
			.version("1.0.0")
			.contact(contact)
			.description("API that manages the Users of a micro services infrastrucure")
			.build();

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.tags(new Tag("Users API", "Interface to manage users"))
				.apiInfo(apiInfo)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.jdiaz.users.service.controllers"))
				.paths(PathSelectors.any())
				.build();
	}
}