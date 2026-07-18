package br.com.yurifranca.cooperative_voting_api.config;

import br.com.yurifranca.cooperative_voting_api.exception.ErrorResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSchemas(
                                "ErrorResponse",
                                ModelConverters.getInstance()
                                        .read(ErrorResponse.class)
                                        .get("ErrorResponse")
                        )
                )
                .info(new Info()
                        .title("Cooperative Voting API")
                        .version("v1")
                        .description("API para gerenciamento de pautas, sessões e votação cooperativa")
                );
    }

    @Bean
    public OperationCustomizer globalErrorResponses() {
        return (operation, handlerMethod) -> {
            ApiResponses responses = operation.getResponses();

            responses.addApiResponse(
                    "400",
                    createErrorResponse("Dados inválidos")
            );

            responses.addApiResponse(
                    "404",
                    createErrorResponse("Recurso não encontrado")
            );

            responses.addApiResponse(
                    "409",
                    createErrorResponse("Conflito de regra de negócio")
            );

            responses.addApiResponse(
                    "500",
                    createErrorResponse("Erro interno do servidor")
            );

            return operation;
        };
    }


    private ApiResponse createErrorResponse(String description) {
        Content content = new Content();
        content.addMediaType(
                "application/json",
                new MediaType().schema(new Schema<>().$ref("#/components/schemas/ErrorResponse"))
        );

        return new ApiResponse()
                .description(description)
                .content(content);
    }
}