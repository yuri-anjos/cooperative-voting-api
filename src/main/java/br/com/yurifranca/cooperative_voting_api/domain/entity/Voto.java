package br.com.yurifranca.cooperative_voting_api.domain.entity;

import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "voto")
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "associado_id", nullable = false)
    private Long associadoId;

    @Column(name = "associado_cpf", nullable = false)
    private String associadoCpf;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OpcaoVotoEnum voto;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime dataCriacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sessao_id", nullable = false)
    private Sessao sessao;

    @PrePersist
    protected void onCreate() {
        this.dataCriacao = LocalDateTime.now();
    }
}
