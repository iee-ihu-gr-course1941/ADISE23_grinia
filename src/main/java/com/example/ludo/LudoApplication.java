package com.example.ludo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
public class LudoApplication {

    @Autowired
    private static PawnPositionService pawnPositionService;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(LudoApplication.class, args);

//        ConfigurableApplicationContext context = SpringApplication.run(LudoApplication.class, args);

//        PawnPositionService pawnPositionService = context.getBean(PawnPositionService.class);
//
//        List<Pawn> pawnPositionList = pawnPositionService.getAll();
//
//        for(Pawn pp : pawnPositionList) {
//            System.out.println(pp);
//        }

        // SpringApplication.exit(context);


    }

}
