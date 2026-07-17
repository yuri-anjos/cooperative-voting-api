package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.mapper.PautaMapper;
import br.com.yurifranca.cooperative_voting_api.exception.RecursoNaoEncontradoException;
import br.com.yurifranca.cooperative_voting_api.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PautaService {

    private final PautaRepository repository;

    public Pauta buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RecursoNaoEncontradoException("Pauta não encontrada"));
    }

    public PautaResponse criarPauta(CriarPautaRequest request) {
        Pauta pauta = PautaMapper.toEntity(request);
        pauta = repository.save(pauta);
        return PautaMapper.toResponse(pauta);
    }
}
