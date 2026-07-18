package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import br.com.yurifranca.cooperative_voting_api.domain.enums.ResultadoVotacaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record ResultadoVotacaoResponse(

        @Schema(
                description = "Identificador da pauta",
                example = "1"
        )
        Long pautaId,

        @Schema(
                description = "Identificador da sessão de votação",
                example = "10"
        )
        Long sessaoId,

        @Schema(
                description = "Quantidade de votos favoráveis (Sim)",
                example = "8"
        )
        Long votosSim,

        @Schema(
                description = "Quantidade de votos contrários (Não)",
                example = "3"
        )
        Long votosNao,

        @Schema(
                description = "Quantidade total de votos registrados",
                example = "11"
        )
        Long totalVotos,

        @Schema(
                description = "Resultado final da votação",
                example = "APROVADO",
                allowableValues = {"APROVADO", "REJEITADO"}
        )
        ResultadoVotacaoEnum resultado

) {
}
