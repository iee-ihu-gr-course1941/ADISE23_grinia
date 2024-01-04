package com.example.ludo;

import com.example.ludo.db.services.PawnPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class LudoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LudoApplication.class, args);
    }

}
