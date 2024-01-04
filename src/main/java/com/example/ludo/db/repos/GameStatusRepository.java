package com.example.ludo.db.repos;

import com.example.ludo.model.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameStatusRepository extends JpaRepository<GameStatus, Long> {
}
