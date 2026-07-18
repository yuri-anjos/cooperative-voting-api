package br.com.yurifranca.cooperative_voting_api.repository;

import br.com.yurifranca.cooperative_voting_api.domain.entity.Voto;
import br.com.yurifranca.cooperative_voting_api.repository.projection.ContagemVotosProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    boolean existsBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

    @Query("""
                SELECT
                    v.voto AS voto,
                    COUNT(v) AS quantidade
                FROM Voto v
                WHERE v.sessao.id = :sessaoId
                GROUP BY v.voto
            """)
    List<ContagemVotosProjection> contarVotosPorSessao(Long sessaoId);
}
