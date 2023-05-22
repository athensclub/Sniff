package smeen.logic;

import javafx.scene.Scene;

/**
 * Represent a class that want to register events to JavaFX scene.
 */
public interface HasEvents {

    /**
     * Register necessary events to the given scene.
     *
     * @param scene
     */
    void registerEvents(Scene scene);

}
