package smeen.util;

/**
 * Represent class in which its data can be cleared and reset.
 */
public interface Resettable {
    /**
     * Clear all the necessary data
     */
    void clearData();

    /**
     * Reset the necessary data back to its default state.
     */
    void reset();

}
