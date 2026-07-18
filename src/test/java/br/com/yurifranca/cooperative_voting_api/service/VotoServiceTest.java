package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.RegistrarVotoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.ResultadoVotacaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.VotoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Voto;
import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import br.com.yurifranca.cooperative_voting_api.domain.enums.ResultadoVotacaoEnum;
import br.com.yurifranca.cooperative_voting_api.exception.NegocioException;
import br.com.yurifranca.cooperative_voting_api.repository.VotoRepository;
import br.com.yurifranca.cooperative_voting_api.repository.projection.ContagemVotosProjection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VotoServiceTest {
    @Mock
    private VotoRepository repository;

    @Mock
    private SessaoService sessaoService;

    @InjectMocks
    private VotoService service;

    @Test
    void registrarVoto_deveRegistrarVotoComSucesso() {
        Long pautaId = 1L;

        RegistrarVotoRequest request = new RegistrarVotoRequest(
                10L,
                "12345678909",
                OpcaoVotoEnum.SIM
        );

        Sessao sessao = new Sessao();
        sessao.setId(100L);
        sessao.setEncerramento(LocalDateTime.now().plusMinutes(1));
        sessao.setPauta(Pauta.builder().id(1L).build());
        Voto voto = new Voto();
        voto.setId(1L);
        voto.setSessao(sessao);
        voto.setAssociadoId(request.associadoId());
        voto.setAssociadoCpf(request.associadoCpf());
        voto.setVoto(request.voto());

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);
        when(repository.existsBySessaoIdAndAssociadoId(100L, 10L)).thenReturn(false);
        when(repository.save(any(Voto.class))).thenReturn(voto);

        VotoResponse response = service.registrarVoto(pautaId, request);

        assertNotNull(response);

        verify(repository).save(any(Voto.class));
    }

    @Test
    void registrarVoto_deveLancarExcecaoQuandoSessaoEstiverEncerrada() {
        Long pautaId = 1L;

        RegistrarVotoRequest request = new RegistrarVotoRequest(
                10L,
                "12345678909",
                OpcaoVotoEnum.SIM
        );

        Sessao sessao = new Sessao();
        sessao.setEncerramento(LocalDateTime.now().minusMinutes(1));

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);

        assertThrows(
                NegocioException.class,
                () -> service.registrarVoto(pautaId, request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void registrarVoto_deveLancarExcecaoQuandoAssociadoJaVotou() {
        Long pautaId = 1L;

        RegistrarVotoRequest request = new RegistrarVotoRequest(
                10L,
                "12345678909",
                OpcaoVotoEnum.SIM
        );

        Sessao sessao = new Sessao();
        sessao.setId(100L);
        sessao.setEncerramento(LocalDateTime.now().plusMinutes(1));

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);
        when(repository.existsBySessaoIdAndAssociadoId(100L, 10L)).thenReturn(true);

        assertThrows(
                NegocioException.class,
                () -> service.registrarVoto(pautaId, request)
        );

        verify(repository, never()).save(any());
    }

    @Test
    void consultarResultado_deveConsultarResultadoAprovado() {
        Long pautaId = 1L;

        Sessao sessao = new Sessao();
        sessao.setId(100L);
        sessao.setEncerramento(LocalDateTime.now().minusMinutes(1));

        ContagemVotosProjection votosSim = mock(ContagemVotosProjection.class);
        when(votosSim.getVoto()).thenReturn(OpcaoVotoEnum.SIM);
        when(votosSim.getQuantidade()).thenReturn(8L);

        ContagemVotosProjection votosNao = mock(ContagemVotosProjection.class);
        when(votosNao.getVoto()).thenReturn(OpcaoVotoEnum.NAO);
        when(votosNao.getQuantidade()).thenReturn(3L);

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);
        when(repository.contarVotosPorSessao(100L))
                .thenReturn(List.of(votosSim, votosNao));

        ResultadoVotacaoResponse response = service.consultarResultado(pautaId);

        assertEquals(8L, response.votosSim());
        assertEquals(3L, response.votosNao());
        assertEquals(11L, response.totalVotos());
        assertEquals(ResultadoVotacaoEnum.APROVADO, response.resultado());
    }


    @Test
    void consultarResultado_deveRetornarRejeitadoQuandoEmpatar() {
        Long pautaId = 1L;

        Sessao sessao = new Sessao();
        sessao.setId(100L);
        sessao.setEncerramento(LocalDateTime.now().minusMinutes(1));

        ContagemVotosProjection votosSim = mock(ContagemVotosProjection.class);
        when(votosSim.getVoto()).thenReturn(OpcaoVotoEnum.SIM);
        when(votosSim.getQuantidade()).thenReturn(5L);

        ContagemVotosProjection votosNao = mock(ContagemVotosProjection.class);
        when(votosNao.getVoto()).thenReturn(OpcaoVotoEnum.NAO);
        when(votosNao.getQuantidade()).thenReturn(5L);

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);
        when(repository.contarVotosPorSessao(100L))
                .thenReturn(List.of(votosSim, votosNao));

        ResultadoVotacaoResponse response = service.consultarResultado(pautaId);

        assertEquals(ResultadoVotacaoEnum.REJEITADO, response.resultado());
    }


    @Test
    void consultarResultado_deveConsultarResultadoRejeitado() {
        Long pautaId = 1L;

        Sessao sessao = new Sessao();
        sessao.setId(100L);
        sessao.setEncerramento(LocalDateTime.now().minusMinutes(1));

        ContagemVotosProjection votosSim = mock(ContagemVotosProjection.class);
        when(votosSim.getVoto()).thenReturn(OpcaoVotoEnum.SIM);
        when(votosSim.getQuantidade()).thenReturn(4L);

        ContagemVotosProjection votosNao = mock(ContagemVotosProjection.class);
        when(votosNao.getVoto()).thenReturn(OpcaoVotoEnum.NAO);
        when(votosNao.getQuantidade()).thenReturn(7L);

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);
        when(repository.contarVotosPorSessao(100L)).thenReturn(List.of(votosSim, votosNao));

        ResultadoVotacaoResponse response = service.consultarResultado(pautaId);

        assertEquals(4L, response.votosSim());
        assertEquals(7L, response.votosNao());
        assertEquals(11L, response.totalVotos());
        assertEquals(ResultadoVotacaoEnum.REJEITADO, response.resultado());
    }


    @Test
    void consultarResultado_deveLancarExcecaoQuandoConsultarResultadoComSessaoAberta() {
        Long pautaId = 1L;

        Sessao sessao = new Sessao();
        sessao.setId(100L);
        sessao.setEncerramento(LocalDateTime.now().plusMinutes(5));

        when(sessaoService.findByPautaId(pautaId)).thenReturn(sessao);

        assertThrows(
                NegocioException.class,
                () -> service.consultarResultado(pautaId)
        );

        verify(repository, never()).contarVotosPorSessao(anyLong());
    }
}