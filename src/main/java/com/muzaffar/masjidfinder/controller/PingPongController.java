package com.muzaffar.masjidfinder.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingPongController {


    @GetMapping("/ping")
    public PingPong getPingPOng() {
        return new PingPong("Pong");
    }

    record PingPong(String result){}
}
