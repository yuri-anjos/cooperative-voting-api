package br.com.yurifranca.cooperative_voting_api.repository;

import br.com.yurifranca.cooperative_voting_api.domain.entity.Voto;
import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

    Long countBySessaoIdAndVoto(Long id, OpcaoVotoEnum opcaoVotoEnum);
}
