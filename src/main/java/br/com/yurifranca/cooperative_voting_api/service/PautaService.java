package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.mapper.PautaMapper;
import br.com.yurifranca.cooperative_voting_api.exception.RecursoNaoEncontradoException;
import br.com.yurifranca.cooperative_voting_api.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PautaService {

    private final PautaRepository repository;

    public Pauta buscarPorId(Long id) {
        log.debug("Buscando pauta pelo id: {}", id);

        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));
    }

    public PautaResponse criarPauta(CriarPautaRequest request) {
        log.info("Criando nova pauta com título: {}", request.titulo());

        Pauta pauta = PautaMapper.toEntity(request);
        pauta = repository.save(pauta);

        log.info("Pauta criada com sucesso. Id: {}", pauta.getId());
        return PautaMapper.toResponse(pauta);
    }
}
