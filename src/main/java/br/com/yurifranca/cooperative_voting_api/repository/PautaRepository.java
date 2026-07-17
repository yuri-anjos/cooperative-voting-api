package br.com.yurifranca.cooperative_voting_api.repository;

import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long> {
}
