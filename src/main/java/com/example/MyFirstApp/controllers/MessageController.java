package com.example.MyFirstApp.controllers;

import com.example.MyFirstApp.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/hello")
    public Message sayHeloo(){
        return new Message( "test");
    }

}
