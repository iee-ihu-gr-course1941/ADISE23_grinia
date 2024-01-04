package com.example.ludo.db.repos;

import com.example.ludo.model.Pawn;
import com.example.ludo.model.PawnId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PawnPositionRepository extends JpaRepository<Pawn, PawnId> {
}
