package smeen.logic;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import smeen.views.MainView;

/**
 * A handler to manage movement of currently dragging node from mouse drag event.
 */
public class DraggingHandler {

    private double oldX, oldY;

    private boolean hasOld = false;

    private MainView main;

    /**
     * Constructor for DraggingHandler.
     * @param main
     */
    public DraggingHandler(MainView main) {
        this.main = main;
    }

    /**
     * Perform necessary calculations and move the currently dragging node based on mouse movement.
     *
     * @param e the mouse drag event.
     */
    public void onDrag(MouseEvent e) {
        Node d = main.draggingProperty().get();
        if (d == null)
            return;

        // calculate delta mouse x and delta mouse y.
        double dx = e.getX() - oldX;
        double dy = e.getY() - oldY;

        // set the oldPos to calculate in next iteration
        oldX = e.getX();
        oldY = e.getY();

        // first call of the drag event, the calculated delta will not be correct since the old values are not correct.
        if (!hasOld) {
            hasOld = true;
            return;
        }

        // move the dragging node by the delta.
        d.relocate(d.getLayoutX() + dx, d.getLayoutY() + dy);
    }

    /**
     * Perform necessary cleanups to be ready for next drag event.
     */
    public void onRelease(){
        hasOld = false;
    }

}
