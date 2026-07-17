package br.com.yurifranca.cooperative_voting_api.domain.dto.request;

import jakarta.validation.constraints.Min;

public record AbrirSessaoRequest(
        @Min(value = 1, message = "A duração da sessão deve ser de no mínimo 1 minuto.")
        Integer duracaoEmMinutos
) {
}
