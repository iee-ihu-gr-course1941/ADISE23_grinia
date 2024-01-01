package com.example.ludo;

public enum Pawns {

    R1("R1"),
    R2("R2"),
    R3("R3"),
    R4("R4"),
    B1("B1"),
    B2("B2"),
    B3("B3"),
    B4("B4"),
    NONE("0");

    private final String stringVal;

    Pawns(String stringVal) {
        this.stringVal = stringVal;
    }

    public String getStringVal() {
        return stringVal;
    }

    public static Pawns getColorI(Color color, int i) {
        if (color.equals(Color.RED) || color.equals(Color.BLUE)) {
            String pawnId = color.equals(Color.RED) ? "R" : "B";
            switch (i) {
                case 0:
                    return Pawns.valueOf(pawnId + "1");
                case 1:
                    return Pawns.valueOf(pawnId + "2");
                case 2:
                    return Pawns.valueOf(pawnId + "3");
                case 3:
                    return Pawns.valueOf(pawnId + "4");
                default:
                    return null;
            }
        } else {
            return null;
        }
    }

    public static boolean stringValExists(String pawnVal) {
        for(Pawns ps : Pawns.values()) {
            if(ps.getStringVal().equals(pawnVal)) {
                return true;
            }
        }

        return false;
    }



}
