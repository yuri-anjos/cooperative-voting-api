package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record PautaResponse(
        Long id,
        String titulo,
        String descricao,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime dataCriacao
) {
}