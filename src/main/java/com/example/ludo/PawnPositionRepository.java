package com.example.ludo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PawnPositionRepository extends JpaRepository<Pawn, PawnId> {
}
