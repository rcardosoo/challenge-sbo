package com.triade.simple.exception;

import static com.triade.simple.core.Constants.NOT_FOUND_MESSAGE;

public class BridgeNotFoundException extends RuntimeException {
    public BridgeNotFoundException(Long id) {
        super(NOT_FOUND_MESSAGE + id);
    }
}
