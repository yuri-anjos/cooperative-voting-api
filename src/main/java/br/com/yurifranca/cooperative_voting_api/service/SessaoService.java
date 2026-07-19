package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.AbrirSessaoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import br.com.yurifranca.cooperative_voting_api.domain.mapper.SessaoMapper;
import br.com.yurifranca.cooperative_voting_api.exception.NegocioException;
import br.com.yurifranca.cooperative_voting_api.exception.RecursoNaoEncontradoException;
import br.com.yurifranca.cooperative_voting_api.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SessaoService {

    private final SessaoRepository repository;
    private final PautaService pautaService;

    @Cacheable(value = "sessoes", key = "#pautaId")
    public Sessao findByPautaId(Long pautaId) {
        log.debug("Buscando sessão da pauta {}", pautaId);

        return repository.findByPautaId(pautaId).orElseThrow(() -> new RecursoNaoEncontradoException("Pauta ou Sessão não foram encontrados"));
    }

    public SessaoResponse iniciarSessao(Long pautaId, AbrirSessaoRequest request) {
        log.info("Iniciando sessão de votação para a pauta {}", pautaId);

        int duracaoDaSessaoEmMinutos = request.duracaoEmMinutos() != null ? request.duracaoEmMinutos() : 1;

        LocalDateTime abertura = LocalDateTime.now();
        LocalDateTime encerramento = abertura.plusMinutes(duracaoDaSessaoEmMinutos);

        Pauta pauta = pautaService.buscarPorId(pautaId);
        if (repository.existsByPautaId(pautaId)) {
            throw new NegocioException("A pauta já possui uma sessão aberta.");
        }

        Sessao sessao = new Sessao();
        sessao.setAbertura(abertura);
        sessao.setEncerramento(encerramento);
        sessao.setPauta(pauta);
        sessao = repository.save(sessao);

        log.info("Sessão {} criada com sucesso para a pauta {}. Encerramento: {}", sessao.getId(), pautaId, encerramento);
        return SessaoMapper.toResponse(sessao);
    }
}
