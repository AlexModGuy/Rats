package com.github.alexthe666.rats.server.entity;

public enum RatCommand {
    WANDER(true),
    SIT(false),
    FOLLOW(true),
    HUNT_ANIMALS(true),
    GATHER(true),
    HARVEST(true),
    TRANSPORT(true),
    HUNT_MONSTERS(true);

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
