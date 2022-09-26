package br.com.coffeandit.transactionbff.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String msg) {
        super(msg);
    }

}
