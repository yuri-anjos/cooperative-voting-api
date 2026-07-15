package br.com.yurifranca.cooperative_voting_api.controller;

import br.com.yurifranca.cooperative_voting_api.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas")
@RequiredArgsConstructor
public class PautaController {

    private final PautaService pautaService;

    @GetMapping
    public String test() {
        return "API está funcionando";
    }
}
