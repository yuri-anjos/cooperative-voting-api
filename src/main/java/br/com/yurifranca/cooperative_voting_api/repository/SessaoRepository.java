package br.com.yurifranca.cooperative_voting_api.repository;

import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    boolean existsByPautaId(@NotNull(message = "O id da pauta é obrigatório") Long pautaId);

}
