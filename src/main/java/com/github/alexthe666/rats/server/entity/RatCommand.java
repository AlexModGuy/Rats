package com.github.alexthe666.rats.server.entity;

public enum RatCommand {
    WANDER(true),
    SIT(false),
    FOLLOW(true),
    HUNT(true),
    GATHER(true),
    HARVEST(true),
    TRANSPORT(true);

    public boolean freeMove;

    RatCommand(boolean freeMove) {
        this.freeMove = freeMove;
    }

    public String getTranslateName() {
        return "entity.rat.command." + this.name().toLowerCase();
    }

    public String getTranslateDescription() {
        return "entity.rat.command." + this.name().toLowerCase() + ".desc";
    }

}
