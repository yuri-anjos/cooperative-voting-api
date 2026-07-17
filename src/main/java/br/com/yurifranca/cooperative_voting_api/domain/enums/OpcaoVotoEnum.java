package br.com.yurifranca.cooperative_voting_api.domain.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OpcaoVotoEnum {
    SIM("Sim"),
    NAO("Não");

    private final String descricao;

    OpcaoVotoEnum(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }
}
