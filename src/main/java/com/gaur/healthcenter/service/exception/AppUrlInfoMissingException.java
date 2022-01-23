package com.gaur.healthcenter.service.exception;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
public class AppUrlInfoMissingException extends RuntimeException {

    private static final long serialVersionUID = 6796893346418116376L;

    public AppUrlInfoMissingException(String s) {
        super(s);
    }
}
