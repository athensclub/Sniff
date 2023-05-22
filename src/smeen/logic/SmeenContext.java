package smeen.logic;

import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import smeen.global.SmeenConstants;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A class that hold necessary data for code blocks executions.
 */
public class SmeenContext implements HasEvents {

    private Set<KeyCode> activeKeys;

    private Map<String, ObjectProperty> variables;

    private StringProperty currentAnswer;

    private boolean answerChanged;

    /**
     * Constructor for SmeenContext.
     */
    public SmeenContext() {
        activeKeys = ConcurrentHashMap.newKeySet();
        variables = new ConcurrentHashMap<>();
        currentAnswer = new SimpleStringProperty();
        currentAnswer.addListener((prop, oldv, newv) -> {
            if (!Objects.equals(oldv, newv))
                answerChanged = true;
        });
    }

    /**
     * Set the variable value.
     *
     * @param name  the name of variable to set.
     * @param value the new value.
     */
    public void setVariable(String name, Object value) {
        variables.computeIfAbsent(name, n -> new SimpleObjectProperty()).set(value);
    }

    /**
     * Get the current value of the variable.
     *
     * @param name the name of the variable to get value.
     * @param <T>  the java type of the variable value.
     * @return the variable value.
     */
    public <T> T getVariable(String name) {
        return (T) getVariableProperty(name).get();
    }

    /**
     * The variable value.
     *
     * @param name the name of the variable.
     * @return
     */
    public ReadOnlyObjectProperty getVariableProperty(String name) {
        return variables.get(name);
    }

    /**
     * @param key the key to check.
     * @return whether the given key being pressed.
     */
    public boolean isKeyActive(SmeenConstants.KeyPress key) {
        return activeKeys.stream().anyMatch(code -> key.checkKeyCode(code));
    }

    /**
     * The latest answer retrieved from sprite's ask code block.
     *
     * @return
     */
    public StringProperty currentAnswerProperty() {
        return currentAnswer;
    }

    /**
     * @return whether the current answer property has been changed. this will only
     * return true once then other call will return false (except if the answer change during
     * that period).
     */
    public boolean isAnswerChanged() {
        boolean result = answerChanged;
        if (answerChanged)
            answerChanged = false;
        return result;
    }

    @Override
    public void registerEvents(Scene scene) {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            activeKeys.add(e.getCode());
        });
        scene.addEventHandler(KeyEvent.KEY_RELEASED, e -> {
            activeKeys.remove(e.getCode());
        });
    }
}
