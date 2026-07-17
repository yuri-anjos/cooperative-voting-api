package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.AbrirSessaoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Pauta;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import br.com.yurifranca.cooperative_voting_api.domain.mapper.SessaoMapper;
import br.com.yurifranca.cooperative_voting_api.exception.NegocioException;
import br.com.yurifranca.cooperative_voting_api.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class SessaoService {

    private final SessaoRepository repository;
    private final PautaService pautaService;

    public SessaoResponse iniciarSessao(Long pautaId, AbrirSessaoRequest request) {
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

        return SessaoMapper.toResponse(sessao);
    }
}
