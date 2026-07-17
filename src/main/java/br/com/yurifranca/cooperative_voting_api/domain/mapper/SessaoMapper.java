package br.com.yurifranca.cooperative_voting_api.domain.mapper;

import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import lombok.experimental.UtilityClass;

@UtilityClass
public class SessaoMapper {

    public static SessaoResponse toResponse(Sessao sessao) {
        return new SessaoResponse(
                sessao.getId(),
                sessao.getPauta().getId(),
                sessao.getAbertura(),
                sessao.getEncerramento()
        );
    }
}
