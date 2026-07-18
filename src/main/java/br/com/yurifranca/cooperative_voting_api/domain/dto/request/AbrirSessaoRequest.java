package br.com.yurifranca.cooperative_voting_api.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record AbrirSessaoRequest(
        @Schema(
                description = "Duração da sessão de votação em minutos. Caso não informado, será utilizado o valor padrão: 1.",
                example = "5",
                minimum = "1"
        )
        @Min(value = 1, message = "A duração da sessão deve ser de no mínimo 1 minuto.")
        Integer duracaoEmMinutos
) {
}
