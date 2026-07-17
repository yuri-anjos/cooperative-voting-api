package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record SessaoResponse(
        Long sessaoId,

        Long pautaId,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime abertura,

        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime encerramento
) {
}
