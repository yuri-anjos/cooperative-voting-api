package br.com.yurifranca.cooperative_voting_api.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sessao")
public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime abertura;

    @Column(nullable = false)
    private LocalDateTime encerramento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pauta_id", nullable = false, unique = true)
    private Pauta pauta;

}
