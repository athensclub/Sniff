package smeen.views;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import smeen.component.*;
import smeen.component.code.CodeBlockList;
import smeen.component.code.CodeSelector;
import smeen.component.code.CodeArea;
import smeen.component.code.CodeBlockInput;
import smeen.logic.*;
import smeen.util.Resettable;
import smeen.util.Savable;

import java.util.HashMap;
import java.util.Map;

/**
 * The root node for all components in Sniff.
 */
public class MainView extends Pane implements Resettable, Savable, HasEvents {
    private final TopMenuBar menuBar;
    private final CodeSelector codeSelector;

    private final StageArea stageArea;

    private final ObjectProperty<CodeArea> codeArea;

    private final ScrollPane codeAreaContainer;
    private final SpriteEditor spriteEditor;
    private final SceneEditor sceneEditor;
    private final ResizeEditor resizeEditor;
    private final AnswerArea answerArea;

    private final SnapHandler codeBlockListSnapHandler;

    private final SnapHandler codeBlockInputSnapHandler;

    private final MouseEventsHandler mouseEventsHandler;

    private final CodeExecutionHandler codeExecutionHandler;

    private final DraggingHandler draggingHandler;

    private final ObjectProperty<Node> draggingNode;
    private final Stage stage;

    /**
     * The main view constructor.
     * @param stage the primary stage.
     */
    public MainView(Stage stage) {
        draggingNode = new SimpleObjectProperty<>();
        draggingNode.addListener((obs, oldv, newv) -> {
            getChildren().remove(oldv);
            if (newv != null) {
                getChildren().add(newv);
            }
        });

        setMinSize(1024, 640);

        VBox root = new VBox();
        root.prefWidthProperty().bind(widthProperty());
        root.prefHeightProperty().bind(heightProperty());

        HBox content = new HBox();
        //content.setSpacing(10);

        codeAreaContainer = new ScrollPane();
        HBox.setHgrow(codeAreaContainer, Priority.SOMETIMES);
        codeAreaContainer.prefHeightProperty().bind(heightProperty());

        codeArea = new SimpleObjectProperty<>();
        codeArea.addListener((obs, oldv, newv) -> {
            codeAreaContainer.setContent(newv);
        });


        codeSelector = new CodeSelector(this);
        codeSelector.prefHeightProperty().bind(heightProperty());


        // componentEditor
        stageArea = new StageArea(this);
        answerArea = new AnswerArea(this);
        StackPane stageAreaWrap = new StackPane();
        stageAreaWrap.getChildren().addAll(stageArea, answerArea);
        StackPane.setAlignment(answerArea, Pos.BOTTOM_CENTER);

        HBox groupEditor = new HBox();
        spriteEditor = new SpriteEditor(this);
        sceneEditor = new SceneEditor(this);
        resizeEditor = new ResizeEditor(this);

        groupEditor.getChildren().addAll(spriteEditor, sceneEditor);
        groupEditor.setSpacing(5);

        VBox componentEditor = new VBox();
        componentEditor.getChildren().addAll(stageAreaWrap, groupEditor, resizeEditor);
        componentEditor.setSpacing(5);
        componentEditor.setPadding(new Insets(0, 10, 0, 0));


        content.getChildren().addAll(codeSelector, codeAreaContainer, componentEditor);
        HBox.setMargin(codeAreaContainer, new Insets(0, 10, 0, 0));

        menuBar = new TopMenuBar(this);

        root.getChildren().addAll(menuBar, content);

        getChildren().addAll(root);

        codeExecutionHandler = new CodeExecutionHandler(this);
        codeBlockListSnapHandler = new CodeBlockListSnapHandler(this);
        codeBlockInputSnapHandler = new CodeBlockInputSnapHandler(this);
        draggingHandler = new DraggingHandler(this);
        mouseEventsHandler = new MouseEventsHandler(this);

        // handle dragging code block from selector
        addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            draggingHandler.onDrag(e);
            if (draggingNode.get() instanceof CodeBlockList) codeBlockListSnapHandler.update();
            else if (draggingNode.get() instanceof CodeBlockInput) codeBlockInputSnapHandler.update();
        });

        addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            Node d = draggingNode.get();

            boolean snapped = false;
            if (draggingNode.get() instanceof CodeBlockList) snapped = codeBlockListSnapHandler.performSnap();
            else if (draggingNode.get() instanceof CodeBlockInput) snapped = codeBlockInputSnapHandler.performSnap();

            // destroy dragging block when released.
            draggingHandler.onRelease();
            draggingNode.set(null);

            // if already snapped, nothing more to be done.
            if (snapped) return;

            Point2D mousePos = new Point2D(e.getSceneX(), e.getSceneY());
            if (d instanceof CodeBlockList || d instanceof CodeBlockInput<?>) {
                codeArea.get().addIfContains(mousePos, d);
            } else if (d instanceof SpriteObject sprite) {
                stageArea.addAndRelocate(mousePos, sprite);
            }else if (d instanceof VariableDisplayList vd){
                stageArea.addAndRelocate(mousePos, vd);
            }

        });

        this.stage = stage;

        // reset is also initialization
        reset();
    }

    /**
     * The property of the node that is currently being dragged by user.
     * Setting this property will automatically add the node to this MainView's children.
     *
     * @return node that is currently being dragged
     */
    public ObjectProperty<Node> draggingProperty() {
        return draggingNode;
    }

    /**
     *
     * @return the CodeExecutionHandler of the MainView
     */
    public CodeExecutionHandler getCodeExecutionHandler() {
        return codeExecutionHandler;
    }

    /**
     * Get CodeSelector of the MainView
     *
     * @return CodeSelector of the MainView
     */
    public CodeSelector getCodeSelector() {
        return codeSelector;
    }

    /**
     * Get StageArea of the MainView
     *
     * @return StageArea of the MainView
     */
    public StageArea getStageArea() {
        return stageArea;
    }

    /**
     * Get SpriteEditor of the MainView
     *
     * @return SpriteEditor of the MainView
     */
    public SpriteEditor getSpriteEditor() {
        return spriteEditor;
    }

    /**
     * Get SceneEditor of the MainView
     *
     * @return SceneEditor of the MainView
     */
    public SceneEditor getSceneEditor() {
        return sceneEditor;
    }

    /**
     * Get ResizeEditor of the MainView
     *
     * @return ResizeEditor of the MainView
     */
    public ResizeEditor getResizeEditor() {
        return resizeEditor;
    }

    /**
     * Get AnswerArea of the MainView
     *
     * @return AnswerArea of the MainView
     */
    public AnswerArea getAnswerArea() {
        return answerArea;
    }

    /**
     * Get CodeArea of the currently selected Sprite/Scene
     *
     * @return CodeArea of the currently selected Sprite/Scene
     */
    public ObjectProperty<CodeArea> codeAreaProperty() {
        return codeArea;
    }

    /**
     *
     * @return The MouseEventsHandler of this MainView.
     */
    public MouseEventsHandler getMouseEventsHandler() {
        return mouseEventsHandler;
    }

    /**
     * Get Stage
     *
     * @return Stage
     */
    public Stage getStage() {
        return stage;
    }


    @Override
    public void registerEvents(Scene scene) {
        codeExecutionHandler.registerEvents(scene);
        mouseEventsHandler.registerEvents(scene);
    }

    @Override
    public void clearData() {
        stageArea.clearData();
        codeSelector.clearData();
        sceneEditor.clearData();
        spriteEditor.clearData();
    }

    @Override
    public void reset() {
        stageArea.reset();
        codeSelector.reset();
        sceneEditor.reset();
        spriteEditor.reset();

        sceneEditor.selectedSceneProperty().set(sceneEditor.getScene(1));
        stageArea.shownSceneProperty().set(1);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("sceneEditor", sceneEditor.exportData());
        result.put("codeSelector", codeSelector.exportData());
        result.put("spriteEditor", spriteEditor.exportData());
        result.put("stageArea", stageArea.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        clearData();

        sceneEditor.importData((Map<String, Object>) data.get("sceneEditor"));
        codeSelector.importData((Map<String, Object>) data.get("codeSelector"));
        spriteEditor.importData((Map<String, Object>) data.get("spriteEditor"));
        stageArea.importData((Map<String, Object>) data.get("stageArea"));

        // sprite is not added when imported from sprite editor, so we add them manually here.
        spriteEditor.forEachSpriteOption(option -> stageArea.getStageContent().getChildren().add(option.getSprite()));

        sceneEditor.selectedSceneProperty().set(sceneEditor.getScene(1));
        stageArea.shownSceneProperty().set(1);
    }
}
