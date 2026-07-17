package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record VotoResponse(
        Long id,
        Long pautaId,
        Long sessaoId,
        Long associadoId,
        String associadoCpf,
        OpcaoVotoEnum voto,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataCriacao
) {
}
