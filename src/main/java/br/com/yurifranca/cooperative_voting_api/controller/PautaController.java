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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Criar uma pauta",
            description = "Cria uma nova pauta de votação"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Pauta criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PautaResponse criarPauta(@RequestBody @Valid CriarPautaRequest request) {
        return pautaService.criarPauta(request);
    }

    @Operation(
            summary = "Abrir sessão de votação",
            description = "Inicia uma sessão de votação para uma pauta existente"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Sessão iniciada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta não encontrada"
            )
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{pautaId}/sessoes")
    public SessaoResponse iniciarSessao(
            @Parameter(
                    description = "ID da pauta",
                    example = "1"
            )
            @PathVariable Long pautaId,

            @RequestBody @Valid AbrirSessaoRequest request) {
        return sessaoService.iniciarSessao(pautaId, request);
    }

    @Operation(
            summary = "Registrar voto",
            description = "Registra um voto SIM ou NÃO para uma pauta"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Voto registrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos ou voto já registrado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta ou sessão não encontrada"
            )
    })
    @PostMapping("/{pautaId}/votos")
    @ResponseStatus(HttpStatus.CREATED)
    public VotoResponse registrarVoto(
            @Parameter(
                    description = "ID da pauta",
                    example = "1"
            )
            @PathVariable Long pautaId,

            @Valid @RequestBody RegistrarVotoRequest request
    ) {
        return votoService.registrarVoto(pautaId, request);
    }

    @Operation(
            summary = "Consultar resultado da votação",
            description = "Retorna o resultado final da votação de uma pauta após o encerramento da sessão"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado calculado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "A votação ainda está em andamento"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pauta ou sessão não encontrada"
            )
    })
    @GetMapping("/{pautaId}/resultado")
    public ResultadoVotacaoResponse consultarResultado(
            @Parameter(
                    description = "ID da pauta",
                    example = "1"
            )
            @PathVariable Long pautaId
    ) {
        return votoService.consultarResultado(pautaId);
    }
}
