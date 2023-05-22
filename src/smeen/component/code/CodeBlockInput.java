package smeen.component.code;

import javafx.beans.property.*;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.util.Copyable;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.Map;

public abstract class CodeBlockInput<T> extends VBox implements Copyable<CodeBlockInput<?>>, Savable {

    private MainView main;

    private HBox content;

    private ObjectProperty<CodeBlockInputSlot<T>> holder;

    public CodeBlockInput(MainView main) {
        this.main = main;

        holder = new SimpleObjectProperty<>();

        setFillWidth(false);

        content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(20), null)));
        content.setSpacing(10);
        content.setPadding(new Insets(10, 10, 5, 8));

        // set color of border to check type of CodeBlockInput

        getChildren().addAll(content);
        // don't allow children modification after this, other classes must use
        // getContent() to modify the CodeBlock's content.
        getChildren().addListener((ListChangeListener<? super Node>) c -> {
            throw new IllegalStateException(
                    "Do not modify the CodeBlockInput children directly (even for subclasses). Instead, use getContent().getChildren().add/addAll.");
        });
    }

    public HBox getContent() {
        return content;
    }

    public MainView getMain() {
        return main;
    }

    public abstract T getValue(SmeenContext context);

    public abstract Type getType();

    // Check input type by set Color to border
    // Red -> String
    // Green -> Double
    // Blue -> Boolean
    public void setColorBorder() {
        Color setcolor = Color.BLACK;
        if (getType() == Type.String) {
            setcolor = Color.RED;
        } else if (getType() == Type.Double) {
            setcolor = Color.GREEN;
        } else if (getType() == Type.Boolean) {
            setcolor = Color.BLUE;
        }
        getContent().setBorder(new Border(
                new BorderStroke(setcolor, BorderStrokeStyle.SOLID, new CornerRadii(20), new BorderWidths(1.5))));
    }

    /**
     * @return the CodeArea that this code block input is in, or null if this code block input is not in CodeeArea.
     * @implNote There might be a better way, but considering that code blocks would probably
     * not be very deep, I think the current implementation is efficient enough.
     */
    public CodeArea getCodeArea() {
        Node temp = getParent();
        while (temp != null) {
            if (temp instanceof CodeArea ca)
                return ca;
            temp = temp.getParent();
        }
        return null;
    }

    /**
     * @return the CodeBlockInputSlot that this CodeBlockInput is currently in, or null if this
     * CodeBlockInput is not currently in CodeBlockInputSlot.
     */
    public ObjectProperty<CodeBlockInputSlot<T>> holderProperty() {
        return holder;
    }

    @Override
    public Map<String, Object> exportData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void importData(Map<String, Object> data) {
        throw new UnsupportedOperationException();
    }
}
