package br.com.yurifranca.cooperative_voting_api.controller;

import br.com.yurifranca.cooperative_voting_api.domain.dto.request.AbrirSessaoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.request.CriarPautaRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.request.RegistrarVotoRequest;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.PautaResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.ResultadoVotacaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.SessaoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.dto.response.VotoResponse;
import br.com.yurifranca.cooperative_voting_api.domain.entity.Sessao;
import br.com.yurifranca.cooperative_voting_api.domain.enums.OpcaoVotoEnum;
import br.com.yurifranca.cooperative_voting_api.domain.enums.ResultadoVotacaoEnum;
import br.com.yurifranca.cooperative_voting_api.exception.ErrorResponse;
import br.com.yurifranca.cooperative_voting_api.repository.SessaoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ImportTestcontainers
class PautaControllerIT extends IntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessaoRepository sessaoRepository;

    @Test
    void criarPauta_deveCriarPauta() throws Exception {

        CriarPautaRequest request = new CriarPautaRequest(
                "Assembleia Geral",
                "Deliberação sobre investimento"
        );

        String response = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PautaResponse pauta = objectMapper.readValue(response, PautaResponse.class);

        assertNotNull(pauta.pautaId());
        assertEquals("Assembleia Geral", pauta.titulo());
        assertEquals("Deliberação sobre investimento", pauta.descricao());
        assertNotNull(pauta.dataCriacao());
    }

    @Test
    void criarPauta_deveRetornar400QuandoTituloNaoInformado() throws Exception {

        CriarPautaRequest request = new CriarPautaRequest(
                "",
                "Descrição válida"
        );

        String response = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(400, error.status());
        assertNotNull(error.timestamp());
        assertEquals("Validation Error", error.error());
        assertNotNull(error.message());
        assertEquals("/api/v1/pautas", error.path());
    }

    @Test
    void criarPauta_deveRetornar400QuandoDescricaoNaoInformada() throws Exception {

        CriarPautaRequest request = new CriarPautaRequest(
                "Título",
                ""
        );

        String response = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(400, error.status());
        assertNotNull(error.timestamp());
        assertEquals("Validation Error", error.error());
        assertNotNull(error.message());
        assertEquals("/api/v1/pautas", error.path());
    }

    private Long criarPauta() throws Exception {
        CriarPautaRequest request = new CriarPautaRequest(
                "Pauta Teste",
                "Descrição"
        );

        String response = mockMvc.perform(post("/api/v1/pautas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = objectMapper.readTree(response);

        return json.get("pautaId").asLong();
    }

    @Test
    void iniciarSessao_deveAbrirSessao() throws Exception {

        Long pautaId = criarPauta();

        AbrirSessaoRequest request = new AbrirSessaoRequest(5);

        String response = mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        SessaoResponse sessao = objectMapper.readValue(response, SessaoResponse.class);

        assertNotNull(sessao.sessaoId());
        assertEquals(pautaId, sessao.pautaId());
        assertNotNull(sessao.abertura());
        assertNotNull(sessao.encerramento());
    }

    @Test
    void iniciarSessao_deveRetornar404AoAbrirSessaoParaPautaInexistente() throws Exception {

        AbrirSessaoRequest request = new AbrirSessaoRequest(5);

        String response = mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(404, error.status());
        assertNotNull(error.timestamp());
        assertEquals("Not Found", error.error());
        assertEquals("Pauta não encontrada", error.message());
        assertEquals("/api/v1/pautas/999/sessoes", error.path());
    }

    @Test
    void registrarVoto_deveRegistrarVoto() throws Exception {

        Long pautaId = criarPauta();

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AbrirSessaoRequest(5)
                        )))
                .andExpect(status().isCreated());

        RegistrarVotoRequest request =
                new RegistrarVotoRequest(
                        1L,
                        "39053344705",
                        OpcaoVotoEnum.SIM
                );

        String response = mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        VotoResponse voto = objectMapper.readValue(response, VotoResponse.class);

        assertNotNull(voto.id());
        assertEquals(pautaId, voto.pautaId());
        assertNotNull(voto.sessaoId());
        assertEquals(1L, voto.associadoId());
        assertEquals("39053344705", voto.associadoCpf());
        assertEquals(OpcaoVotoEnum.SIM, voto.voto());
        assertNotNull(voto.dataCriacao());
    }

    @Test
    void registrarVoto_deveRetornar400QuandoAssociadoJaVotou() throws Exception {

        Long pautaId = criarPauta();

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AbrirSessaoRequest(5)
                        )))
                .andExpect(status().isCreated());

        RegistrarVotoRequest request =
                new RegistrarVotoRequest(
                        1L,
                        "39053344705",
                        OpcaoVotoEnum.SIM
                );

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        String response = mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);
        assertEquals(400, error.status());
        assertNotNull(error.timestamp());
        assertEquals("Business Error", error.error());
        assertEquals("O associado já votou nesta pauta.", error.message());
        assertEquals("/api/v1/pautas/" + pautaId + "/votos", error.path());
    }

    @Test
    void consultarResultado_deveLancarExcecaoQuandoSessaoAindaEstaEmAndamento() throws Exception {

        Long pautaId = criarPauta();

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AbrirSessaoRequest(5)
                        )))
                .andExpect(status().isCreated());

        RegistrarVotoRequest voto = new RegistrarVotoRequest(1L, "39053344705", OpcaoVotoEnum.SIM);
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voto)))
                .andExpect(status().isCreated());

        String response = mockMvc.perform(get("/api/v1/pautas/{pautaId}/resultado", pautaId))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ErrorResponse error = objectMapper.readValue(response, ErrorResponse.class);

        assertEquals(400, error.status());
        assertNotNull(error.timestamp());
        assertEquals("Business Error", error.error());
        assertEquals("A votação ainda está em andamento.", error.message());
        assertEquals("/api/v1/pautas/" + pautaId + "/resultado", error.path());
    }

    @Test
    void consultarResultado_deveConsultarResultado() throws Exception {

        Long pautaId = criarPauta();

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new AbrirSessaoRequest(5)
                        )))
                .andExpect(status().isCreated());

        RegistrarVotoRequest voto =
                new RegistrarVotoRequest(
                        1L,
                        "39053344705",
                        OpcaoVotoEnum.SIM
                );

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pautaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voto)))
                .andExpect(status().isCreated());

        Sessao sessao = sessaoRepository.findByPautaId(pautaId).orElseThrow();
        sessao.setEncerramento(LocalDateTime.now().minusMinutes(1));
        sessaoRepository.save(sessao);

        String response = mockMvc.perform(get("/api/v1/pautas/{pautaId}/resultado", pautaId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ResultadoVotacaoResponse resultado = objectMapper.readValue(response, ResultadoVotacaoResponse.class);

        assertEquals(pautaId, resultado.pautaId());
        assertEquals(sessao.getId(), resultado.sessaoId());
        assertEquals(1L, resultado.votosSim());
        assertEquals(0L, resultado.votosNao());
        assertEquals(1L, resultado.totalVotos());
        assertEquals(ResultadoVotacaoEnum.APROVADO, resultado.resultado());
    }
}