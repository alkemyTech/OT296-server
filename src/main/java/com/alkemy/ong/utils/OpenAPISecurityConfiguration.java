package com.alkemy.ong.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;

@Configuration
@OpenAPIDefinition(
		info =@Info(
				title = "ONG Somos mas - API",
				version = "${api.version}",
				contact = @Contact(
						name = "ONG somos mas", email = "senderongsomomas@gmail.com", url = "https://www.ongsomosmas.com"
						),
				termsOfService = "${tos.uri}",
				description = "${api.description}"
				),
		servers = @Server(
				url = "/",
				description = "Development"
				)
		)
public class OpenAPISecurityConfiguration {

	@Bean
	public OpenAPI customizeOpenAPI() {
		final String securitySchemeName = "bearerAuth";
		return new OpenAPI()
				.addSecurityItem(new SecurityRequirement()
						.addList(securitySchemeName))
				.components(new Components()
						.addSecuritySchemes(securitySchemeName, new SecurityScheme()
								.name(securitySchemeName)
								.type(SecurityScheme.Type.HTTP)
								.scheme("bearer")
								.description(
										"Provide the JWT")
								.bearerFormat("JWT")));
	}
}