CREATE TABLE GAME_STATUS (
                             ID INT PRIMARY KEY,
                             PLAYER1 VARCHAR(255),
                             PLAYER2 VARCHAR(255),
                             PLAYERS_TURN VARCHAR(255),
                             SHOULD_ROLL TINYINT(1),
                             SHOULD_MOVE TINYINT(1),
                             GAME_OVER TINYINT(1),
                             WINNER VARCHAR(255)
);

CREATE TABLE PAWN_POSITION (
                               USERNAME VARCHAR(255) NOT NULL,
                               PAWN_ID INT NOT NULL,
                               X INT NOT NULL,
                               Y INT NOT NULL,
                               PRIMARY KEY (USERNAME, PAWN_ID)
);
