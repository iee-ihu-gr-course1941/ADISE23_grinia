package com.example.ludo;

public enum Emojis {

    DISAPPROVAL("ಠ_ಠ"),
    EXCITED("٩(◕‿◕｡)۶"),
    SUNGLASSES("(⌐■_■)"),
    MUSCLES("ᕙ(⇀‸↼‶)ᕗ"),
    WIDE_EYES("◉_◉"),
    SMIRK("(¬‿¬)");

    private final String face;

    Emojis(String face) {
        this.face = face;
    }

    public String getFace() {
        return face;
    }


}
