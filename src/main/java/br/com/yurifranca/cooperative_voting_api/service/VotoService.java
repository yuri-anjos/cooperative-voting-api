package br.com.yurifranca.cooperative_voting_api.service;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.RegistrarVotoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.ResultadoVotacaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.VotoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Voto;
import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import br.com.yurifranca.cooperative_voting_api.domain.enums.ResultadoVotacaoEnum;
import br.com.yurifranca.cooperative_voting_api.domain.mapper.VotoMapper;
import br.com.yurifranca.cooperative_voting_api.exception.NegocioException;
import br.com.yurifranca.cooperative_voting_api.integration.cpf.CpfStatusEnum;
import br.com.yurifranca.cooperative_voting_api.integration.cpf.CpfValidationClient;
import br.com.yurifranca.cooperative_voting_api.repository.VotoRepository;
import br.com.yurifranca.cooperative_voting_api.repository.projection.ContagemVotosProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VotoService {

    private final VotoRepository repository;
    private final SessaoService sessaoService;
    private final CpfValidationClient cpfValidationClient;

    public VotoResponse registrarVoto(Long pautaId, RegistrarVotoRequest request) {
        log.info("Registrando voto para pauta {} pelo associado {}", pautaId, request.associadoId());

        Sessao sessao = sessaoService.findByPautaId(pautaId);

        if (!sessao.estaAberta()) {
            throw new NegocioException("A sessão de votação está encerrada.");
        }
        if (repository.existsBySessaoIdAndAssociadoId(sessao.getId(), request.associadoId())) {
            throw new NegocioException("O associado já votou nesta pauta.");
        }

        var cpfValidationResponse = cpfValidationClient.validate(request.cpfSemMascara());
        if(CpfStatusEnum.UNABLE_TO_VOTE.equals(cpfValidationResponse.status())) {
            throw new NegocioException("O associado não pode realizar o voto.");
        }

        Voto voto = new Voto();
        voto.setSessao(sessao);
        voto.setAssociadoId(request.associadoId());
        voto.setAssociadoCpf(request.cpfSemMascara());
        voto.setVoto(request.voto());

        voto = repository.save(voto);

        log.info("Voto registrado com sucesso. Id: {}, Pauta: {}, Sessão: {}, Associado: {}", voto.getId(), pautaId, sessao.getId(), request.associadoId());
        return VotoMapper.toResponse(voto);
    }

    public ResultadoVotacaoResponse consultarResultado(Long pautaId) {
        log.info("Consultando resultado da votação da pauta {}", pautaId);

        Sessao sessao = sessaoService.findByPautaId(pautaId);
        if (LocalDateTime.now().isBefore(sessao.getEncerramento())) {
            throw new NegocioException("A votação ainda está em andamento.");
        }

        List<ContagemVotosProjection> contagem = repository.contarVotosPorSessao(sessao.getId());
        long votosSim = 0;
        long votosNao = 0;
        for (ContagemVotosProjection item : contagem) {
            if (OpcaoVotoEnum.SIM.equals(item.getVoto())) {
                votosSim = item.getQuantidade();
            }
            if (OpcaoVotoEnum.NAO.equals(item.getVoto())) {
                votosNao = item.getQuantidade();
            }
        }
        ResultadoVotacaoEnum resultado = votosSim > votosNao ? ResultadoVotacaoEnum.APROVADO : ResultadoVotacaoEnum.REJEITADO;

        log.info("Resultado calculado. Pauta: {}, SIM: {}, NÃO: {}, Resultado: {}", pautaId, votosSim, votosNao, resultado);
        return new ResultadoVotacaoResponse(
                pautaId,
                sessao.getId(),
                votosSim,
                votosNao,
                votosSim + votosNao,
                resultado
        );
    }
}
