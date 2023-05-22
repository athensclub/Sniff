package smeen.component.code;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import smeen.component.SpriteObject;
import smeen.component.code.block.control.IfCodeBlock;
import smeen.global.SmeenConstants;
import smeen.util.Resettable;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class CodeArea extends Pane implements Savable, Resettable {

    private MainView main;

    private final DoubleProperty targetWidth;

    private final DoubleProperty targetHeight;

    private SpriteObject sprite;

    public CodeArea(MainView main, SpriteObject sprite) {
        this.main = main;
        this.sprite = sprite;

        setBackground(Background.fill(Color.WHITE));

        targetWidth = new SimpleDoubleProperty();
        targetHeight = new SimpleDoubleProperty();

        // make the content at least has some space from the furthest node in every direction.
        ChangeListener<? super Bounds> boundsListener = (prop, oldv, newv) -> {
            targetWidth.set(Math.max(targetWidth.doubleValue(), newv.getMaxX() + SmeenConstants.CODE_AREA_EXTRA_SPACE));
            targetHeight.set(Math.max(targetHeight.doubleValue(), newv.getMaxY() + SmeenConstants.CODE_AREA_EXTRA_SPACE));
        };
        getChildren().addListener((ListChangeListener<? super Node>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Node n : c.getAddedSubList()) {
                        targetWidth.set(Math.max(targetWidth.doubleValue(), n.getBoundsInParent().getMaxX() + SmeenConstants.CODE_AREA_EXTRA_SPACE));
                        targetHeight.set(Math.max(targetHeight.doubleValue(), n.getBoundsInParent().getMaxY() + SmeenConstants.CODE_AREA_EXTRA_SPACE));
                        n.boundsInParentProperty().addListener(boundsListener);
                    }
                } else {
                    for (Node n : c.getRemoved()) {
                        n.boundsInParentProperty().removeListener(boundsListener);
                    }
                }
            }
        });

        // the conde area will never resize to have less size and will always at least fill the parent.
        parentProperty().addListener((obs, oldv, newv) -> {
            minWidthProperty().unbind();
            minHeightProperty().unbind();

            if (newv instanceof Pane p) {
                minWidthProperty().bind(Bindings.max(targetWidth, Bindings.max(widthProperty(), p.widthProperty().add(SmeenConstants.CODE_AREA_EXTRA_SPACE))));
                minHeightProperty().bind(Bindings.max(targetHeight, Bindings.max(heightProperty(), p.heightProperty().add(SmeenConstants.CODE_AREA_EXTRA_SPACE))));
            }
        });

        addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
            startFullDrag();

            // always check for dragging input first (because the input might be inside code block),
            // then check for dragging code block list.
            CodeBlockInput<?> inputToDrag = findInputToDrag(e.getSceneX(), e.getSceneY(), this);
            if (inputToDrag != null) {
                // remove the deepest descendant from its slot to drag, or if there's no descendant, drag this node instead.
                Point2D pos = inputToDrag.localToScene(0, 0);
                inputToDrag.relocate(pos.getX(), pos.getY());

                CodeBlockInputSlot<?> holder = inputToDrag.holderProperty().get();
                if (holder != null)
                    holder.contentProperty().set(null);

                main.draggingProperty().set(inputToDrag);
                return;
            }

            FindCodeBlockListToDragResult result = IntStream.range(0, getChildren().size())
                    .map(i -> getChildren().size() - i - 1) // find from last children first so that the node that appear on top of other node will be dragged.
                    .mapToObj(i -> findCodeBlockListToDrag(e.getSceneX(), e.getSceneY(), getChildren().get(i)))
                    .filter(res -> res != null)
                    .findFirst()
                    .orElse(null);
            if (result != null) {
                CodeBlockList codeBlockListToDrag = result.list;
                int blockIndex = result.index;

                // if not directly in code area, then it must be in some code block (eg. if, loop, etc.)
                // in that case, we must always make a copy of code block list.
                if (blockIndex == 0 && codeBlockListToDrag.getParent() instanceof CodeArea) {
                    // drag the first block of this list: use this CodeBlockList instance as dragging node.
                    // set the position of the node from relative to the parent to relative to the scene.
                    Point2D pos = codeBlockListToDrag.localToScene(0, 0);
                    codeBlockListToDrag.relocate(pos.getX(), pos.getY());

                    main.codeAreaProperty().get().getChildren().remove(codeBlockListToDrag);
                    main.draggingProperty().set(codeBlockListToDrag);
                } else {
                    // drag block below the first block: create new CodeBlockList instance with that block and blocks below it to be dragged
                    List<Node> sublist = codeBlockListToDrag.getCodeList().getChildren().subList(blockIndex, codeBlockListToDrag.getCodeList().getChildren().size());

                    // Get the position of the first block relative to the scene.
                    Point2D pos = sublist.get(0).localToScene(Point2D.ZERO);

                    // copy the node list, then clear the sublist, which will remove the codeList children from this CodeBlockList
                    List<Node> draggedNodes = new ArrayList<>(sublist);
                    sublist.clear();

                    CodeBlockList toDragCopy = new CodeBlockList(main);
                    toDragCopy.getCodeList().getChildren().addAll(draggedNodes);
                    toDragCopy.relocate(pos.getX(), pos.getY());
                    main.draggingProperty().set(toDragCopy);
                }
            }
        });
    }

    /**
     * @return The sprite associated with this code area, or null if this code area is not
     * associated with any sprite.
     */
    public SpriteObject getSprite() {
        return sprite;
    }

    record FindCodeBlockListToDragResult(CodeBlockList list, int index) {
    }

    /**
     * @param mouseSceneX the mouse x position in the scene.
     * @param mouseSceneY the mouse y position in the scene.
     * @param node        the node to find.
     * @return the result containing deepest CodeBlockList descendant of this CodeArea
     * that the mouse position is in bounds to be used for dragging and has a code
     * block that contains the mouse, or null if there's none.
     */
    private FindCodeBlockListToDragResult findCodeBlockListToDrag(double mouseSceneX, double mouseSceneY, Node node) {
        if (node instanceof Parent p) {
            for (Node child : p.getChildrenUnmodifiable()) {
                FindCodeBlockListToDragResult res = findCodeBlockListToDrag(mouseSceneX, mouseSceneY, child);
                if (res != null)
                    return res;
            }
        }

        if (node instanceof CodeBlockList list
                && list.localToScene(list.getBoundsInLocal()).contains(mouseSceneX, mouseSceneY) &&
                list.getCodeList().getChildren().size() > 0) {
            // the code block list must also have at least one code block that contains
            // the mouse to be draggable, this would disallow the inner list to take
            // precedence when the mouse is intending to drag outer list's border
            int blockIndex = IntStream.range(0, list.getCodeList().getChildren().size())
                    .filter(idx -> {
                        Node n = list.getCodeList().getChildren().get(idx);
                        return n.localToScene(n.getBoundsInLocal()).contains(mouseSceneX, mouseSceneY);
                    }).findAny().orElse(-1);
            if (blockIndex != -1)
                return new FindCodeBlockListToDragResult(list, blockIndex);
            else
                return null;
        } else {
            return null;
        }
    }

    /**
     * @param mouseSceneX the mouse x position in the scene.
     * @param mouseSceneY the mouse y position in the scene.
     * @param node        the node to find.
     * @return the deepest CodeBlockInput descendant of this CodeArea that the mouse position is in bounds
     * to be used for dragging, or null if there's none.
     */
    private CodeBlockInput<?> findInputToDrag(double mouseSceneX, double mouseSceneY, Node node) {
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                CodeBlockInput<?> result = findInputToDrag(mouseSceneX, mouseSceneY, child);
                if (result != null)
                    return result;
            }
        }

        if (node instanceof CodeBlockInput<?> input && input.localToScene(input.getBoundsInLocal()).contains(mouseSceneX, mouseSceneY))
            return input;
        else return null;
    }

    /**
     * Add the given node to this CodeArea and perform layout positioning if the given point
     * is inside this CodeArea (assuming the given point is relative to scene).
     *
     * @param point the point to check (its coordinates are relative to scene).
     * @param node  the node to check.
     */
    public void addIfContains(Point2D point, Node node) {
        if (localToScene(getBoundsInLocal()).contains(point)) {
            // set node position from relative to scene to relative to CodeArea.
            Point2D nodePos = new Point2D(node.getLayoutX(), node.getLayoutY());
            nodePos = sceneToLocal(nodePos);
            node.relocate(nodePos.getX(), nodePos.getY());
            getChildren().add(node);
        }
    }

    @Override
    public void clearData() {
        getChildren().clear();
    }

    @Override
    public void reset() {
        clearData();
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("targetWidth", targetWidth.doubleValue());
        result.put("targetHeight", targetHeight.doubleValue());
        result.put("content", getChildren().stream().filter(node -> node instanceof Savable).map(node -> ((Savable) node).exportData()).toList());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        clearData();

        List<?> content = (List<?>) data.get("content");
        for (Object c : content) {
            if (c instanceof Map m) {
                String type = (String) m.get("type");
                if (type.equals("CodeBlockList")) {
                    CodeBlockList cb = new CodeBlockList(main);
                    cb.importData(m);
                    getChildren().add(cb);
                } else if (type.endsWith("CodeBlockInput")) {
                    CodeBlockInput cb = Savable.importCodeBlockInputData(m, main);
                    getChildren().add(cb);
                } else {
                    throw new IllegalStateException("Unsupported CodeArea content type: " + m.get("type"));
                }
            }
        }

        targetWidth.set((double) data.get("targetWidth"));
        targetHeight.set((double) data.get("targetHeight"));
    }
}
