package br.com.yurifranca.cooperative_voting_api.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CriarPautaRequest(
        @Schema(
                description = "Título da pauta de votação",
                example = "Aprovação do novo plano de benefícios",
                maxLength = 255
        )
        @NotBlank(message = "O título é obrigatório")
        @Size(max = 255, message = "O título deve ter no máximo 255 caracteres")
        String titulo,

        @Schema(
                description = "Descrição detalhada da pauta de votação",
                example = "Pauta para votação sobre a aprovação do novo plano de benefícios",
                maxLength = 10000
        )
        @NotBlank(message = "A descrição é obrigatória")
        @Size(max = 10000, message = "A descrição deve ter no máximo 10000 caracteres")
        String descricao
) {
}
