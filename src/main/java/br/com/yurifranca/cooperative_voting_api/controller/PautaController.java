package br.com.yurifranca.cooperative_voting_api.controller;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.AbrirSessaoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.request.RegistrarVotoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.ResultadoVotacaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.VotoResponse;
import br.com.yurifranca.cooperative_voting_api.service.PautaService;
import br.com.yurifranca.cooperative_voting_api.service.SessaoService;
import br.com.yurifranca.cooperative_voting_api.service.VotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {

    private final PautaService pautaService;
    private final SessaoService sessaoService;
    private final VotoService votoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PautaResponse criarPauta(@RequestBody @Valid CriarPautaRequest request) {
        return pautaService.criarPauta(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{pautaId}/sessoes")
    public SessaoResponse iniciarSessao(@PathVariable Long pautaId,
                                        @RequestBody @Valid AbrirSessaoRequest request) {
        return sessaoService.iniciarSessao(pautaId, request);
    }

    @PostMapping("/{pautaId}/votos")
    @ResponseStatus(HttpStatus.CREATED)
    public VotoResponse registrarVoto(
            @PathVariable Long pautaId,
            @Valid @RequestBody RegistrarVotoRequest request
    ) {
        return votoService.registrarVoto(pautaId, request);
    }

    @GetMapping("/{pautaId}/resultado")
    public ResultadoVotacaoResponse consultarResultado(
            @PathVariable Long pautaId
    ) {
        return votoService.consultarResultado(pautaId);
    }
}
