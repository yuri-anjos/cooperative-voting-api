package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.AbrirSessaoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import br.com.yurifranca.cooperative_voting_api.exception.NegocioException;
import br.com.yurifranca.cooperative_voting_api.repository.SessaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessaoServiceTest {

    @Mock
    private SessaoRepository repository;

    @Mock
    private PautaService pautaService;

    @InjectMocks
    private SessaoService service;

    @Test
    void deveIniciarSessaoComDuracaoInformada() {

        AbrirSessaoRequest request = new AbrirSessaoRequest(10);

        Pauta pauta = new Pauta();
        pauta.setId(1L);

        Sessao sessao = new Sessao();
        sessao.setId(1L);
        sessao.setPauta(pauta);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(repository.existsByPautaId(1L)).thenReturn(false);
        when(repository.save(any(Sessao.class))).thenReturn(sessao);

        SessaoResponse response = service.iniciarSessao(1L, request);

        assertNotNull(response);

        verify(repository).save(any(Sessao.class));
    }

    @Test
    void deveIniciarSessaoComDuracaoPadraoQuandoNaoInformada() {

        AbrirSessaoRequest request = new AbrirSessaoRequest(null);

        Pauta pauta = new Pauta();
        pauta.setId(1L);

        ArgumentCaptor<Sessao> captor = ArgumentCaptor.forClass(Sessao.class);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(repository.existsByPautaId(1L)).thenReturn(false);
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

        service.iniciarSessao(1L, request);

        verify(repository).save(captor.capture());

        Sessao sessao = captor.getValue();

        long minutos = ChronoUnit.MINUTES.between(
                sessao.getAbertura(),
                sessao.getEncerramento()
        );

        assertEquals(1, minutos);
    }

    @Test
    void deveLancarExcecaoQuandoPautaJaPossuirSessao() {

        AbrirSessaoRequest request = new AbrirSessaoRequest(5);

        Pauta pauta = new Pauta();
        pauta.setId(1L);

        when(pautaService.buscarPorId(1L)).thenReturn(pauta);
        when(repository.existsByPautaId(1L)).thenReturn(true);

        assertThrows(
                NegocioException.class,
                () -> service.iniciarSessao(1L, request)
        );

        verify(repository, never()).save(any());
    }
}