package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record PautaResponse(

        @Schema(
                description = "Identificador da pauta criada",
                example = "1"
        )
        Long pautaId,

        @Schema(
                description = "Título da pauta",
                example = "Aprovação do novo benefício"
        )
        String titulo,

        @Schema(
                description = "Descrição detalhada da pauta",
                example = "Votação para aprovação do novo benefício aos cooperados"
        )
        String descricao,

        @Schema(
                description = "Data e hora de criação da pauta",
                example = "17/07/2026 15:30:00"
        )
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataCriacao

) {
}