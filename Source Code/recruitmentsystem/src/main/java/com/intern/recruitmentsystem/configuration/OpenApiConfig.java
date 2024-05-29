package com.fpt.recruitmentsystem.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact =@Contact(
                        name = "Team 2"
                ),
                description = "Api documentation for Recruiter system",
                title = "Recruiter System",
                version = "∞",
                license = @License(
                        name="Team 2",
                        url = "https://swagger.io/license/"//Để tạm hoặc bỏ cũng được
                ), termsOfService = "Term of Service"
        ),servers = {
                @Server (
                        description = "PROD ENV",
                        url = "https://recruiment-deploy.onrender.com"
                ),
        @Server (
                description = "LOCAL ENV",
                url = "http://localhost:8080"
        )
}
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
