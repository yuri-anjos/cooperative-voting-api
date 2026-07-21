package br.com.yurifranca.cooperative_voting_api.integration.cpf;

import br.com.yurifranca.cooperative_voting_api.exception.IntegracaoException;
import br.com.yurifranca.cooperative_voting_api.exception.NegocioException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Log4j2
@Component
public class CpfValidationClient {

    @Value("${cpf.api.url}")
    private String url;

    private final RestClient restClient;

    public CpfValidationClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public CpfValidationResponse validate(String cpf) {

        try {
            return restClient.get()
                    .uri(url + "/{cpf}", cpf)
                    .retrieve()
                    .body(CpfValidationResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new NegocioException("CPF inválido.");
        } catch (RestClientException e) {
            throw new IntegracaoException("Erro ao consultar serviço de validação de CPF.", e);
        }
    }
}