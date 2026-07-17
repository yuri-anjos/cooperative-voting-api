package br.com.yurifranca.cooperative_voting_api.controller;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.AbrirSessaoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.service.PautaService;
import br.com.yurifranca.cooperative_voting_api.service.SessaoService;
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
}
