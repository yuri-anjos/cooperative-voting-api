package br.com.yurifranca.cooperative_voting_api.exception;

public class NegocioException extends RuntimeException {
    public NegocioException(String message) {
        super(message);
    }
}
