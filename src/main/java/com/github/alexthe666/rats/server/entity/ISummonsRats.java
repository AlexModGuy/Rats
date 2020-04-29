package com.github.alexthe666.rats.server.entity;

public interface ISummonsRats {

    boolean encirclesSummoner();

    boolean readsorbRats();

    int getRatsSummoned();

    void setRatsSummoned(int i);

    default float getRadius(){ return 5; }

}
