package com.fyl.demo.AppConstant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj){
        Map<String,Object> body=new HashMap<>();
        body.put("data", responseObj);
        body.put("status", status.value());
        body.put("message", message);
        return ResponseEntity.ok(body);
    }


}
