package com.dexmohq.dexpenses.categorize;

/**
 * @author Henrik Drefs
 */
public class InvalidCategorizerConfigurationException extends Exception {

    public InvalidCategorizerConfigurationException() {
    }

    public InvalidCategorizerConfigurationException(String message) {
        super(message);
    }

    public InvalidCategorizerConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidCategorizerConfigurationException(Throwable cause) {
        super(cause);
    }

}
