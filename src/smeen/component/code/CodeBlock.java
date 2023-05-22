package smeen.component.code;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import smeen.logic.SmeenContext;
import smeen.util.Copyable;
import smeen.global.SmeenComponent;
import smeen.logic.Executable;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.Map;


public abstract class CodeBlock extends VBox implements Copyable<CodeBlock>, Savable, Executable {

    private MainView main;

    private HBox content;

    private Pane bottomSnapHintContainer;

    private Pane topSnapHintContainer;
    private DoubleProperty snapHintWidth;
    
    private Region head;

    public CodeBlock(MainView main) {
        this.main = main;

        setFillWidth(false);

        content = new HBox();
        content.setAlignment(Pos.CENTER);
        content.setBackground(Background.fill(Color.BLACK));
        content.setSpacing(10);
        content.setPadding(new Insets(10, 10, 5, 5));

        topSnapHintContainer = new Pane();
        bottomSnapHintContainer = new Pane();
        snapHintWidth = new SimpleDoubleProperty();
        
        getChildren().addAll(topSnapHintContainer, content, bottomSnapHintContainer);
        
        if (mustBeFirstBlock()) {
        	head = SmeenComponent.createHead();
        	head.backgroundProperty().bind(content.backgroundProperty());
        	getChildren().add(1, head);
        }
        // don't allow children modification after this, other classes must use getContent() to modify the CodeBlock's content.
        getChildren().addListener((ListChangeListener<? super Node>) c -> {
            throw new IllegalStateException("Do not modify the CodeBlock children directly (even for subclasses). Instead, use getContent().getChildren().add/addAll.");
        });
    }

    public HBox getContent() {
        return content;
    }

    public MainView getMain() {
        return main;
    }

    /**
     * Whether this code block can only the first block in a code block list.
     * If it is true, it can only be snapped to the top of a code block list and cannot
     * be snap to any other block in a code block list. Returns false by default, subclasses
     * should override to return true when needed.
     *
     * @return
     */
    public boolean mustBeFirstBlock() {
        return false;
    }

    /**
     * @return the CodeArea that this code block is in, or null if this code block is not in CodeeArea.
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
     * @return the code block list that this CodeBlock is in, or null if this code block is not in any codde block list.
     */
    public CodeBlockList getCodeBlockList() {
        Node temp = getParent();
        while (temp != null) {
            if (temp instanceof CodeBlockList list)
                return list;
            temp = temp.getParent();
        }
        return null;
    }

    /**
     * Show the top snap hint for this CodeBlock.
     * This will do nothing if the top snap hint is already showing.
     */
    public void showTopSnapHint() {
        if (!topSnapHintContainer.getChildren().isEmpty())
            return;
        Rectangle hint = new Rectangle(0, 40, Color.GREY);
        hint.widthProperty().bind(snapHintWidth);
        topSnapHintContainer.getChildren().add(hint);
    }

    /**
     * Show the bottom snap hint for this CodeBlock.
     * This will do nothing if the bottom snap hint is already showing.
     */
    public void showBottomSnapHint() {
        if (!bottomSnapHintContainer.getChildren().isEmpty())
            return;
        Rectangle hint = new Rectangle(0, 40, Color.GREY);
        hint.widthProperty().bind(snapHintWidth);
        bottomSnapHintContainer.getChildren().add(hint);
    }

    /**
     * Hide both the top snap hint and bottom snap hint for this CodeBlock.
     */
    public void hideSnapHints() {
        topSnapHintContainer.getChildren().clear();
        bottomSnapHintContainer.getChildren().clear();
    }

    public DoubleProperty snapHintWidthProperty() {
        return snapHintWidth;
    }

    @Override
    public Map<String, Object> exportData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void importData(Map<String, Object> data) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        throw new UnsupportedOperationException();
    }
}
