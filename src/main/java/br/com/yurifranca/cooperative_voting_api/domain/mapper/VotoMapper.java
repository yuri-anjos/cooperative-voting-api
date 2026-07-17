package br.com.yurifranca.cooperative_voting_api.domain.mapper;

import br.com.yurifranca.cooperative_voting_api.domain.dto.response.VotoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Voto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class VotoMapper {
    public static VotoResponse toResponse(Voto voto) {
        return new VotoResponse(
                voto.getId(),
                voto.getSessao().getPauta().getId(),
                voto.getSessao().getId(),
                voto.getAssociadoId(),
                voto.getAssociadoCpf(),
                voto.getVoto(),
                voto.getDataCriacao()
        );
    }
}
