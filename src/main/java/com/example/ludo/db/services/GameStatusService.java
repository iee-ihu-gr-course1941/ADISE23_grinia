package com.example.ludo.db.services;

import com.example.ludo.db.repos.GameStatusRepository;
import com.example.ludo.model.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameStatusService {

    private final GameStatusRepository gameStatusRepository;

    @Autowired
    public GameStatusService(GameStatusRepository gameStatusRepository) {
        this.gameStatusRepository = gameStatusRepository;
    }

    public void saveGameStatus(GameStatus gameStatus) {
        gameStatusRepository.save(gameStatus);
    }

    public void deleteData() {
        gameStatusRepository.deleteAll();
    }
}
