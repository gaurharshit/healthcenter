package com.gaur.healthcenter.service.exception;

/**
 * @author Harshit Gaur <harshit@gaurs.in>
 */
public class ApplicationHealthEndpointUnreachableException extends RuntimeException {

    private static final long serialVersionUID = -3812585928251110010L;

    public ApplicationHealthEndpointUnreachableException(String s) {
        super(s);
    }

}
