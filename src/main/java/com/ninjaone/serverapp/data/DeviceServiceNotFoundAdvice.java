package com.ninjaone.serverapp.data;

import com.ninjaone.serverapp.exceptions.DeviceServiceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DeviceServiceNotFoundAdvice {
    
    @ResponseBody
    @ExceptionHandler(DeviceServiceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String employeeNotFoundHandler(DeviceServiceNotFoundException ex) {
        return ex.getMessage();
    }
}
