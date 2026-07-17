package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.mapper.PautaMapper;
import br.com.yurifranca.cooperative_voting_api.exception.RecursoNaoEncontradoException;
import br.com.yurifranca.cooperative_voting_api.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(
        MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository repository;

    @InjectMocks
    private PautaService service;

    @Test
    void criarPauta_deveCriarPautaComSucesso() {
        CriarPautaRequest request = new CriarPautaRequest(
                "Pauta 1",
                "Descrição"
        );

        Pauta pauta = PautaMapper.toEntity(request);
        pauta.setId(1L);

        when(repository.save(any(Pauta.class))).thenReturn(pauta);

        PautaResponse response = service.criarPauta(request);

        assertNotNull(response);
        assertEquals(1L, response.id());

        verify(repository).save(any(Pauta.class));
    }

    @Test
    void buscarPorId_deveBuscarPautaPorId() {
        Pauta pauta = new Pauta();
        pauta.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(pauta));

        Pauta resultado = service.buscarPorId(1L);

        assertEquals(1L, resultado.getId());

        verify(repository).findById(1L);
    }

    @Test
    void buscarPorId_deveLancarExcecaoQuandoPautaNaoExistir() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.buscarPorId(1L)
        );

        verify(repository).findById(1L);
    }
}