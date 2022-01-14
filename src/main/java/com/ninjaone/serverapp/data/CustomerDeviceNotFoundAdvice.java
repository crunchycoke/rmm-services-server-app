package com.ninjaone.serverapp.data;

import com.ninjaone.serverapp.exceptions.CustomerDeviceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CustomerDeviceNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(CustomerDeviceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(CustomerDeviceNotFoundException ex) {
        return ex.getMessage();
    }
}

