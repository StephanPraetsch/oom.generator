package com.mercateo.oom.generator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemoryRestController {

    @Autowired
    private MemoryConsumer memoryConsumer;

    @RequestMapping("memory")
    public String use(@RequestParam("add") int amount) {
        memoryConsumer.add(amount);
        return "added " + amount;
    }

    @RequestMapping("clear-memory")
    public String clear() {
        memoryConsumer.clear();
        return "cleared";
    }

    @RequestMapping("generate-oom")
    public String generateOom() {
        memoryConsumer.generateOom();
        return "if you can read this, something went wront :-/";
    }

}
