package br.com.yurifranca.cooperative_voting_api.repository.projection;

import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;

public interface ContagemVotosProjection {
    OpcaoVotoEnum getVoto();
    Long getQuantidade();
}
