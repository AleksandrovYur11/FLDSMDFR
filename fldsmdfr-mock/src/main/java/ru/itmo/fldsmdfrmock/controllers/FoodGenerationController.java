package ru.itmo.fldsmdfrmock.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class FoodGenerationController {
    @PostMapping("/generatefood")
    public ResponseEntity<String> generateFood() {
        //TODO validate body content and respond with 4xx if bad
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.OK);
        return responseEntity;
    }
}
