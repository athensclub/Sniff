package smeen.component;

import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import smeen.views.MainView;

/**
 * A list of multiple variable display that can be draggable.
 */
public class VariableDisplayList extends VBox {
    private Point2D oldPos;

    public VariableDisplayList(MainView main){
        addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
            startFullDrag();

            Point2D pos = localToScene(0, 0);

            oldPos = pos;
            relocate(pos.getX(), pos.getY());

            main.getStageArea().getStageContent().getChildren().remove(this);
            main.draggingProperty().set(this);
        });
    }

    /**
     *
     * @return the old position relative to the stage area content before this variable display list is dragged.
     */
    public Point2D getOldPos() {
        return oldPos;
    }
}
