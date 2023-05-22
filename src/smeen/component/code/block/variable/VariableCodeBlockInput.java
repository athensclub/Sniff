package smeen.component.code.block.variable;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlockInput;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

/**
 * CodeBlock which can store a variable of the selected type
 *
 * @param <T> type of containing value
 */
public class VariableCodeBlockInput<T> extends CodeBlockInput<T> {
    private StringProperty name;
    private Label first;
    private Type type;

    public VariableCodeBlockInput(MainView main) {
        super(main);
        name = new SimpleStringProperty();

        first = new Label();
        first.textProperty().bind(name);
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(new Background(new BackgroundFill(Color.PALEVIOLETRED, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first);
    }

    public VariableCodeBlockInput(MainView main, Type type, String name) {
        this(main);
        this.type = type;
        this.name.set(name);
        // to check T type
        setColorBorder();
    }

    /**
     * A function to copy this CodeBlock
     */
    @Override
    public CodeBlockInput<T> copy() {
        VariableCodeBlockInput<T> copy = new VariableCodeBlockInput<>(getMain(), getType(), name.get());
        return copy;
    }
    
    /**
     * A function to return the containing variable value
     */
    @Override
    public T getValue(SmeenContext context) {
        return context.getVariable(name.get());
    }
    
    
    /**
     * A function to get Type of containing variable, return in enum Type
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * A function to get name of containing variable
     * @return
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * A function to export data of this CodeBlock
     */
    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "VariableCodeBlockInput");
        result.put("name", name.get());
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    /**
     * A function to import data to this CodeBlock
     */
    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
        name.set((String) data.get("name"));
    }
}
