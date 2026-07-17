package br.com.yurifranca.cooperative_voting_api.domain.dto.request;

import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.br.CPF;

public record RegistrarVotoRequest(
        @NotNull(message = "ID do associado é obrigatório")
        @Positive(message = "O ID do associado deve ser maior que zero.")
        Long associadoId,

        @NotBlank(message = "O CPF do associado é obrigatório")
        @CPF(message = "CPF inválido")
        String associadoCpf,

        @NotNull(message = "O voto é obrigatório")
        @Schema(
                description = "Opção de voto.",
                allowableValues = {"Sim", "Não"},
                example = "Sim"
        )
        OpcaoVotoEnum voto
) {
}
