package demo.codingnomads.co.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping
    public String successMainMessage() {
        return "Success!";
    }

}
