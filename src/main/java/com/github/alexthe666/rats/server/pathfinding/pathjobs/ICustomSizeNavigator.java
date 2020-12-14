package com.github.alexthe666.rats.server.pathfinding.pathjobs;

public interface ICustomSizeNavigator {

    boolean isSmallerThanBlock();
    float getXZNavSize();
    int getYNavSize();
}
