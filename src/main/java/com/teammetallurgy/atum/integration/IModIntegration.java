package com.teammetallurgy.atum.integration;

public interface IModIntegration {
    default void preInit() {
    }

    default void init() {
    }

    default void postInit() {
    }

    default void clientSide() {
    }
}