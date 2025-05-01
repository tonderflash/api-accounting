package com.pluralsight.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class UltraAccountingApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(UltraAccountingApiApplication.class, args);
    }
    
    @GetMapping("/")
    public String home() {
        return "¡Ultra Accounting API está funcionando!";
    }
}
