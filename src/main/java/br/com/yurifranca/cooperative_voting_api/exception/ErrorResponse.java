package br.com.yurifranca.cooperative_voting_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Resposta padrão para erros da API")
public record ErrorResponse(

        @Schema(
                description = "Data e hora em que o erro ocorreu",
                example = "17/07/2026 15:30:00"
        )
        LocalDateTime timestamp,
        
        @Schema(
                description = "Código HTTP do erro",
                example = "400"
        )
        int status,

        @Schema(
                description = "Descrição do tipo de erro HTTP",
                example = "Bad Request"
        )
        String error,

        @Schema(
                description = "Mensagem detalhando o erro ocorrido",
                example = "A duração da sessão deve ser de no mínimo 1 minuto."
        )
        String message,

        @Schema(
                description = "Endpoint onde ocorreu o erro",
                example = "/api/v1/pautas/1/sessoes"
        )
        String path

) {
}