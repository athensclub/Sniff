package smeen.component;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import smeen.global.SmeenConstants;
import smeen.global.SmeenSVGs;
import smeen.util.Resettable;
import smeen.util.Savable;
import smeen.util.SmeenLayouts;
import smeen.views.MainView;

import javax.swing.*;
import javax.swing.text.html.ImageView;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.function.Consumer;

public class StageArea extends VBox implements Resettable, Savable {

    private Pane stageContent;

    private IntegerProperty shownScene;

    private SetProperty<String> shownVariable;

    private Map<String, VariableDisplay> nameToVariableDisplay;

    private VariableDisplayList variableList;

    public StageArea(MainView main) {
        setSpacing(10);

        Button flagButton = new Button();
        SVGPath flagPath = new SVGPath();
        flagPath.setContent(SmeenSVGs.FLAG_PATH);
        Region flagRegion = new Region();
        flagRegion.setShape(flagPath);
        flagRegion.setBackground(Background.fill(Color.web("#00ff33")));
        flagRegion.setMaxSize(16, 20);
        flagRegion.setPrefSize(16, 20);
        flagButton.setGraphic(flagRegion);
        flagButton.setPrefSize(32, 32);
        flagButton.setStyle("-fx-background-radius: 32px;");
        flagButton.setCursor(Cursor.HAND);
        flagButton.addEventHandler(ActionEvent.ACTION, e -> main.getCodeExecutionHandler().onGreenFlagClicked());

        Button stopButton = new Button();
        Circle redCircle = new Circle(9);
        redCircle.setFill(Color.RED);
        stopButton.setGraphic(redCircle);
        stopButton.setPrefSize(32, 32);
        stopButton.setStyle("-fx-background-radius: 32px;");
        stopButton.setCursor(Cursor.HAND);
        stopButton.addEventHandler(ActionEvent.ACTION, e -> {
            main.getCodeExecutionHandler().onStopButtonClicked();
        });

        Button expandButton = new Button();
        SVGPath expandPath = new SVGPath();
        expandPath.setContent(SmeenSVGs.EXPAND_PATH);
        Region expandRegion = new Region();
        expandRegion.setShape(expandPath);
        expandRegion.setBackground(Background.fill(Color.BLACK));
        expandRegion.setMaxSize(20, 20);
        expandRegion.setPrefSize(20, 20);
        expandButton.setGraphic(expandRegion);
        expandButton.setPrefSize(32, 32);
        expandButton.setStyle("-fx-background-radius: 32px;");
        expandButton.setPadding(new Insets(0, 0, 0, 0));
        expandButton.setOnMouseEntered(e -> {
            setCursor(Cursor.HAND);
        });
        expandButton.setOnMouseExited(e -> {
            setCursor(Cursor.DEFAULT);
        });

        HBox buttonGroupLeft = new HBox();
        buttonGroupLeft.getChildren().addAll(flagButton, stopButton);
        buttonGroupLeft.setSpacing(10);
        HBox buttonGroupRight = new HBox();
//        buttonGroupRight.getChildren().addAll(expandButton);

        BorderPane buttonGroup = new BorderPane();
        buttonGroup.setLeft(buttonGroupLeft);
        buttonGroup.setRight(buttonGroupRight);
        //BorderPane.setMargin(buttonGroupRight, new Insets(0, 10, 0, 0));

        stageContent = new Pane();
        stageContent.setMinSize(SmeenConstants.STAGE_WIDTH, SmeenConstants.STAGE_HEIGHT);
        stageContent.setPrefSize(SmeenConstants.STAGE_WIDTH, SmeenConstants.STAGE_HEIGHT);
        stageContent.setMaxSize(SmeenConstants.STAGE_WIDTH, SmeenConstants.STAGE_HEIGHT);
        stageContent.addEventHandler(MouseEvent.MOUSE_CLICKED,e -> requestFocus());
        SmeenLayouts.clipChildren(stageContent);

        buttonGroup.maxWidthProperty().bind(stageContent.widthProperty());

        variableList = new VariableDisplayList(main);
        variableList.getChildren().addListener((ListChangeListener<? super Node>) c -> {
            if(variableList.getChildren().isEmpty()){
                stageContent.getChildren().remove(variableList);
            }else{
                stageContent.getChildren().add(variableList);
            }
        });

        nameToVariableDisplay = new HashMap<>();
        shownVariable = new SimpleSetProperty<>(FXCollections.observableSet(new HashSet<>()));
        shownVariable.addListener((SetChangeListener<? super String>) c -> {
            if(c.wasAdded()){
                VariableDisplay display = new VariableDisplay(main, c.getElementAdded());
                nameToVariableDisplay.put(c.getElementAdded(), display);
                variableList.getChildren().add(display);
            }else if(c.wasRemoved()){
                variableList.getChildren().remove(nameToVariableDisplay.get(c.getElementRemoved()));
                nameToVariableDisplay.remove(c.getElementRemoved());
            }
        });

        shownScene = new SimpleIntegerProperty();
        shownScene.addListener((prop, oldv, newv) -> {
            if (newv != null) {
                stageContent.setBackground(new Background(new BackgroundImage(main.getSceneEditor().getScene((int) newv).getImage(), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, false, true))));
                main.getCodeExecutionHandler().onSceneChanged((int) newv);
            }
        });

        setMinWidth(SmeenConstants.STAGE_WIDTH + 40);
        getChildren().addAll(buttonGroup, stageContent);
        setPadding(new Insets(10, 0, 0, 0));
        setAlignment(Pos.CENTER);
    }

    /**
     * Add the given sprite to stage content and perform necessary calculations based on the
     * given mouse position relative to scene.
     *
     * @param pos    the mouse position relative to scene.
     * @param sprite the sprite to add.
     */
    public void addAndRelocate(Point2D pos, SpriteObject sprite) {
        // set to the new position if the dragging sprite is in stage,
        // otherwise return it to original position before the dragging.
        Point2D spritePos;
        if (stageContent.localToScene(stageContent.getBoundsInLocal()).contains(pos))
            spritePos = new Point2D(sprite.getLayoutX(), sprite.getLayoutY());
        else spritePos = sprite.getOldPos();
        spritePos = stageContent.sceneToLocal(spritePos);
        sprite.relocate(spritePos.getX(), spritePos.getY());

        stageContent.getChildren().add(sprite);
    }/**
     * Add the given variable display to stage content and perform necessary calculations based on the
     * given mouse position relative to scene.
     *
     * @param pos    the mouse position relative to scene.
     * @param display the variable display to add.
     */
    public void addAndRelocate(Point2D pos, VariableDisplayList display) {
        // set to the new position if the dragging sprite is in stage,
        // otherwise return it to original position before the dragging.
        Point2D pos2;
        if (stageContent.localToScene(stageContent.getBoundsInLocal()).contains(pos))
            pos2 = new Point2D(display.getLayoutX(), display.getLayoutY());
        else pos2 = display.getOldPos();
        pos2 = stageContent.sceneToLocal(pos2);
        display.relocate(pos2.getX(), pos2.getY());

        stageContent.getChildren().add(display);
    }

    /**
     * Run the given consumer on all sprite object inside this stage area.
     * @param func
     */
    public void forEachSprite(Consumer<SpriteObject> func){
        for(Node node : stageContent.getChildren())
            if(node instanceof SpriteObject sprite)
                func.accept(sprite);
    }

    /**
     * The order number of currently showing scene.
     * @return
     */
    public IntegerProperty shownSceneProperty() {
        return shownScene;
    }

    /**
     * A set of all variable that is being shown, updating this property will automatically
     * add/remove necessary VariableDisplay node to this stage area content.
     * @return
     */
    public SetProperty<String> shownVariableProperty() {
        return shownVariable;
    }

    public Pane getStageContent() {
        return stageContent;
    }

    @Override
    public void clearData() {
        stageContent.getChildren().clear();
        shownVariable.clear();
    }

    @Override
    public void reset() {
        clearData();
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("shownScene", shownScene.intValue());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        shownScene.set((int) data.get("shownScene"));
    }
}
