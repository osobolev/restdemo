package ru.mirea.hello;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(produces = "application/json")
public class RestHello {

    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "world") String name) {
        return String.format("Hello, %s!", name);
    }
}
