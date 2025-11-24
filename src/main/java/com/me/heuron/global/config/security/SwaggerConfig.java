package com.me.heuron.global.config.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * @Configuration : Spring 설정 클래스임(빈으로 등록)
 */
@Configuration
public class SwaggerConfig {

	/**
	 * @Bean : 메서드에 선언시 스프링 컨테이너가 해당 메서드를 실행해
	 *       : 객체(Bean)으로 등록
	 */
	@Bean
	public OpenAPI swaggerOpenAPI() {
		// Spring Data의 Pageable은 Swagger에서 제대로 문서화되지 않기에 추가 처리
		SpringDocUtils.getConfig().replaceWithClass(
				org.springframework.data.domain.Pageable.class,
				org.springdoc.core.converters.models.Pageable.class
		);

		// Info 객체 설정
		Info info = new Info().title("HEURON-API")
				.version("v1");

		// JWT 인증 등록
		SecurityScheme securityScheme = new SecurityScheme()
				.type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
				.in(SecurityScheme.In.HEADER).name("Authorization");

		// 모든 API에 인증 필요하도록 지정
		SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

		// OpenAPI 구성 리턴
		return new OpenAPI()
				.info(info)
				.components(new Components().addSecuritySchemes("bearerAuth",securityScheme))
				.security(Collections.singletonList(securityRequirement));
	}
}

