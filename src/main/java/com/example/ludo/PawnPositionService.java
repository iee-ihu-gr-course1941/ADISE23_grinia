package com.example.ludo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PawnPositionService {

    private final PawnPositionRepository pawnPositionRepository;

    @Autowired
    public PawnPositionService(PawnPositionRepository pawnPositionRepository) {
        this.pawnPositionRepository = pawnPositionRepository;
    }

    public List<Pawn> getAll() {
        return pawnPositionRepository.findAll();
    }

    public void savePawn(Pawn pawn) {
        pawnPositionRepository.save(pawn);
    }

}
