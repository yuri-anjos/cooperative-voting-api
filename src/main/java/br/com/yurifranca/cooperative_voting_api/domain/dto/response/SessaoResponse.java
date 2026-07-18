package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record SessaoResponse(

        @Schema(
                description = "Identificador da sessão de votação",
                example = "10"
        )
        Long sessaoId,

        @Schema(
                description = "Identificador da pauta vinculada à sessão",
                example = "1"
        )
        Long pautaId,

        @Schema(
                description = "Data e hora de abertura da sessão de votação",
                example = "17/07/2026 15:30:00"
        )
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime abertura,

        @Schema(
                description = "Data e hora de encerramento da sessão de votação",
                example = "17/07/2026 15:35:00"
        )
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime encerramento

) {
}
