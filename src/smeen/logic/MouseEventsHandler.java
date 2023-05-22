package smeen.logic;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import smeen.component.SpriteObject;
import smeen.views.MainView;

/**
 * A class responsible for handling mouse event. Also responsible for broadcasting mouse event
 * and checking for when sprite is mouse pressed.
 */
public class MouseEventsHandler implements HasEvents {

    private BooleanProperty mouseDown;

    private ObjectProperty<Point2D> mouseScenePos;

    private MainView main;

    /**
     * Constructor for MouseEventsHandler.
     * @param main
     */
    public MouseEventsHandler(MainView main) {
        this.main = main;

        mouseDown = new SimpleBooleanProperty();
        mouseScenePos = new SimpleObjectProperty<>();
    }

    /**
     * @return Whether the cursor is being pressed and is hovering over the given sprite.
     */
    public boolean isSpritePressed(SpriteObject sprite) {
        return mouseDown.get() && sprite.getImageView().localToScene(sprite.getImageView().getBoundsInLocal()).contains(mouseScenePos.get());
    }

    @Override
    public void registerEvents(Scene scene) {
        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            mouseDown.set(true);
            mouseScenePos.set(new Point2D(e.getSceneX(), e.getSceneY()));

            main.getStageArea().forEachSprite(sprite -> {
                if(isSpritePressed(sprite))
                    main.getCodeExecutionHandler().onSpritePressed(sprite);
            });

            Pane stage = main.getStageArea().getStageContent();
            if(stage.localToScene(stage.getBoundsInLocal()).contains(mouseScenePos.get()))
                main.getCodeExecutionHandler().onStagePressed();
        });
        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            mouseScenePos.set(new Point2D(e.getSceneX(), e.getSceneY()));
        });
        scene.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            mouseDown.set(false);
        });
    }
}
