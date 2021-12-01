package eu.loumadev.enhancer.structures;

public class WorldMismatchException extends Exception {
    public WorldMismatchException() {
        super("Positions are not in the same world!");
    }
}
