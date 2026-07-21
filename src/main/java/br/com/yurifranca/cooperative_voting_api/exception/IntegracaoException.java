package br.com.yurifranca.cooperative_voting_api.exception;

public class IntegracaoException extends RuntimeException {

    public IntegracaoException(String message) {
        super(message);
    }

    public IntegracaoException(String message, Throwable cause) {
        super(message, cause);
    }

}