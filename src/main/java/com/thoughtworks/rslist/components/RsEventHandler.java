package com.thoughtworks.rslist.components;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventHandler {

    private static final String logExceptionFormat = "[System] Capture Exception - Detail: %s";
    private static Logger log = LoggerFactory.getLogger(RsEventHandler.class);

    @ExceptionHandler({RsEventNotValidException.class, MethodArgumentNotValidException.class})
    public ResponseEntity rsExceptionHandler(Exception e) {
        String errorMessage;
        if (e instanceof MethodArgumentNotValidException) {
            if (((MethodArgumentNotValidException) e).getBindingResult().getTarget() instanceof User) {
                errorMessage = "invalid user";
            } else {
                errorMessage = "invalid param";
            }
        } else {
            errorMessage = e.getMessage();
        }
        Error error = new Error();
        error.setError(errorMessage);

        log.error(String.format(logExceptionFormat, errorMessage));

        return ResponseEntity.badRequest().body(error);
    }
}
