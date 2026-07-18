package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record VotoResponse(

        @Schema(
                description = "Identificador do voto registrado",
                example = "100"
        )
        Long id,

        @Schema(
                description = "Identificador da pauta relacionada ao voto",
                example = "1"
        )
        Long pautaId,

        @Schema(
                description = "Identificador da sessão de votação",
                example = "10"
        )
        Long sessaoId,

        @Schema(
                description = "Identificador do associado que realizou o voto",
                example = "123"
        )
        Long associadoId,

        @Schema(
                description = "CPF do associado que realizou o voto",
                example = "12345678909"
        )
        String associadoCpf,

        @Schema(
                description = "Opção de voto registrada",
                example = "SIM",
                allowableValues = {"SIM", "NAO"}
        )
        OpcaoVotoEnum voto,

        @Schema(
                description = "Data e hora em que o voto foi registrado",
                example = "17/07/2026 15:45:00"
        )
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataCriacao

) {
}