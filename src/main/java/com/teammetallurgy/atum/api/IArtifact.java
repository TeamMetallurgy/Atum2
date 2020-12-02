package com.teammetallurgy.atum.api;

/**
 * Implement on any Item class, that should be determined as an artifact.
 * Mainly used for the Godforge, to determine what Godshard to output
 */
public interface IArtifact {

    God getGod();
}