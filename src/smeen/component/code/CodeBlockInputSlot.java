package smeen.component.code;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class CodeBlockInputSlot<T> extends StackPane implements Savable {
    private TextField textField;

    private final ObjectProperty<CodeBlockInput<T>> content;

    private final Pane contentPane;

    private final Pane snapHintPane;

    private final DoubleProperty snapHintWidth;
    private Type prefType; // use in snapping check
    private T value;

    private MainView main;

    public CodeBlockInputSlot(MainView main, Type prefType) {
        this.main = main;

        contentPane = new Pane();
        snapHintPane = new Pane();
        snapHintPane.setDisable(true);
        getChildren().addAll(contentPane, snapHintPane);

        textField = new TextField();
        textField.setPrefWidth(30);
        textField.setPrefHeight(20);
        textField.setFont(Fonts.EXTRA_SMALL_REGULAR_FONT);
        contentPane.getChildren().add(textField);

        content = new SimpleObjectProperty<>();
        content.addListener((props, oldv, newv) -> {
            contentPane.getChildren().clear();
            if (newv == null) {
                oldv.holderProperty().set(null);
                contentPane.getChildren().add(textField);
            } else {
                // snapped block will be located at 0,0 relative to the content pane.
                newv.relocate(0, 0);
                newv.holderProperty().set(this);
                contentPane.getChildren().add(newv);
            }
        });

        snapHintWidth = new SimpleDoubleProperty();

        setType(prefType);
        setAlignment(Pos.CENTER_LEFT);
    }

    /**
     * The current content of this CodeBlockInput, setting this property will automatically
     * add the node to this slot. Setting this property to null will also automatically remove
     * the node from this slot and use default text field node as input instead.
     *
     * @return
     */
    public ObjectProperty<CodeBlockInput<T>> contentProperty() {
        return content;
    }

    public void showSnapHint() {
        if (!snapHintPane.getChildren().isEmpty())
            return;
        Rectangle hint = new Rectangle(0, 40, Color.GREY);

        hint.widthProperty().bind(Bindings.max(textField.widthProperty(), snapHintWidth));
        snapHintPane.getChildren().add(hint);
    }

    public void hideSnapHint() {
        snapHintPane.getChildren().clear();
    }

    public void setType(Type type) {
        prefType = type;
        Color setColor = Color.BLACK;

        if (type.equals(Type.String) && content.get() == null) {
            textField.setPrefWidth(60);
            setColor = Color.RED;
        } else if (type.equals(Type.Double) && content.get() == null) {
            textField.setTextFormatter(new TextFormatter<>(new DoubleStringConverter()));
            setColor = Color.GREEN;
        } else if (type.equals(Type.Boolean) && content.get() == null) {
            textField.setEditable(false);
            setColor = Color.BLUE;
        }

        getTextField().setBorder(new Border(
                new BorderStroke(setColor, BorderStrokeStyle.SOLID, new CornerRadii(3), new BorderWidths(1.5))));
    }

    public T getValue(SmeenContext context) {
        if (content.get() == null) {
            if (prefType.equals(Type.Double))
                value = (!textField.getText().isEmpty()) ? (T) Double.valueOf(textField.getText()) : (T) Double.valueOf(0);
            else if (prefType.equals(Type.String))
                value = (T) textField.getText();
            else if (prefType.equals(Type.Boolean))
                value = (T) Boolean.FALSE;
            return value;

        } else {
            return content.get().getValue(context);
        }
    }

    public Type getType() {
        return prefType;
    }

    public TextField getTextField() {
        return textField;
    }

    public DoubleProperty snapHintWidthProperty() {
        return snapHintWidth;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        if (content.get() == null) {
            result.put("isTextfield", true);
            result.put("text", textField.getText());
        } else {
            result.put("isTextfield", false);
            result.put("content", content.get().exportData());
        }
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        if ((boolean) data.get("isTextfield")) {
            textField.setText((String) data.get("text"));
        } else {
            content.set((CodeBlockInput<T>) Savable.importCodeBlockInputData((Map<String, Object>)data.get("content"), main));
        }
    }
}