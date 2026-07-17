package br.com.yurifranca.cooperative_voting_api.repository;

import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    boolean existsByPautaId(Long pautaId);

    Optional<Sessao> findByPautaId(Long pautaId);
}
