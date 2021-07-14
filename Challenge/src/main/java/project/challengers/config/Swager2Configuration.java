package project.challengers.config;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Configuration
@EnableSwagger2
public class Swager2Configuration {
    Logger logger = LoggerFactory.getLogger(Swager2Configuration.class);

    public class Swagger2Config {

        private ApiKey apiKey() {
            return new ApiKey("test", "Authorization", "header");
        }

        @Bean
        public Docket api() {

            return new Docket(DocumentationType.SWAGGER_2)
                    .apiInfo(apiInfo())
                    .securitySchemes(Collections.singletonList(apiKey()))
                    .securityContexts(Collections.singletonList(securityContext()))
                    .ignoredParameterTypes(
                            WebSession.class,
                            org.springframework.http.server.ServerHttpRequest.class,
                            ServerHttpRequest.class,
                            ServerHttpResponse.class,
                            org.springframework.http.server.ServerHttpResponse.class,
                            ServerWebExchange.class,
                            Authentication.class)
                    .genericModelSubstitutes(
                            Optional.class,
                            ResponseEntity.class)
                    .select()
                    .apis(RequestHandlerSelectors.basePackage("project.challengers.controller"))
                    .paths(PathSelectors.any())
                    .build();
        }

        private SecurityContext securityContext() {
            return SecurityContext.builder().securityReferences(defaultAuth())
                    .forPaths(PathSelectors.any()).build();
        }

        private List<SecurityReference> defaultAuth() {
            AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];

            authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");

            return Arrays.asList(new SecurityReference("test", authorizationScopes));
        }

        private ApiInfo apiInfo() {
            return new ApiInfoBuilder()
                    .title("Challengers Service")
                    .version("0.1")
                    .description("<li>Health Check End Point : GET /challengers/health</li>"
                            + "<li>Token Check End Point : GET /user/check : return true </li>"
                    )
                    .build();
        }
    }
}