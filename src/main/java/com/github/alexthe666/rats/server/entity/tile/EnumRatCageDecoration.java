package com.github.alexthe666.rats.server.entity.tile;

public enum EnumRatCageDecoration {
    IGLOO(true);

    public boolean colorized;

    EnumRatCageDecoration(boolean color){
        this.colorized = color;
    }
}
