package com.example.spy;

public enum SpyInitialValues {
    PLAYERS_FROM(3),
    PLAYERS_TO(50),
    SPIES_FROM(1),
    TIMER_FROM(1),
    TIMER_TO(30);

    private final int value;

    SpyInitialValues(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}