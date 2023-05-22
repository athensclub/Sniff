package smeen.logic;

/**
 * Represent a class that handle snapping between blocks.
 */
public interface SnapHandler {

    /**
     * Perform necessary calculation for the currently dragging node to check
     * if there's a node that can be snapped and show the snap hint.
     */
    void update();

    /**
     * If there's a node that can be snapped with currently dragging node,
     * make it snap, otherwise this method does nothing. Finally, this method will also
     * perform necessary resets to be ready for next drag events.
     *
     * @return whether the snap happened or not.
     */
    boolean performSnap();
}
