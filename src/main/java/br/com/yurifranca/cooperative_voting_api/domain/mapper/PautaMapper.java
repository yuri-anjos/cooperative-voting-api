package br.com.yurifranca.cooperative_voting_api.domain.mapper;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PautaMapper {

    public static Pauta toEntity(CriarPautaRequest request) {
        Pauta pauta = new Pauta();
        pauta.setTitulo(request.titulo());
        pauta.setDescricao(request.descricao());
        return pauta;
    }

    public static PautaResponse toResponse(Pauta pauta) {
        return new PautaResponse(
                pauta.getId(),
                pauta.getTitulo(),
                pauta.getDescricao(),
                pauta.getDataCriacao()
        );
    }

}
