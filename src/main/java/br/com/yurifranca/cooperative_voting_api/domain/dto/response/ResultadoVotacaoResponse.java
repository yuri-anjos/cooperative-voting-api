package br.com.yurifranca.cooperative_voting_api.domain.dto.response;

import br.com.yurifranca.cooperative_voting_api.domain.enums.ResultadoVotacaoEnum;

public record ResultadoVotacaoResponse(
        Long pautaId,
        Long sessaoId,
        Long votosSim,
        Long votosNao,
        Long totalVotos,
        ResultadoVotacaoEnum resultado
) {
}
