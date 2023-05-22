package smeen.logic;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import smeen.component.code.CodeArea;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockList;
import smeen.global.SmeenConstants;
import smeen.views.MainView;

/**
 * A handler for a currently dragging code block to check if the dragging code block can be snapped to other node.
 */
public class CodeBlockListSnapHandler implements SnapHandler {

    private MainView main;

    private CodeBlockList oldList;

    private CodeBlock old;

    private int indexToAdd;

    /**
     * Constructor for the CodeBlockListSnapHandler.
     * @param main
     */
    public CodeBlockListSnapHandler(MainView main) {
        this.main = main;
    }

    @Override
    public void update() {
        Node d = main.draggingProperty().get();

        if (d instanceof CodeBlockList block) {
            // find the block in code area that can be snapped
            FindSnapResult findResult = findSnap(main.codeAreaProperty().get(), block);
            CodeBlockList snappableList = findResult.snappableList;
            CodeBlock snappable = findResult.snappable;
            int snappableIndex = findResult.index;
            boolean snapTop = findResult.snapTop;

            // hide the snap hint of the old snappable
            if (old != null && old != snappable)
                old.hideSnapHints();

            if (oldList != null && oldList != snappableList)
                oldList.hideSnapHint();

            // show the snap hint for current snappable.
            if (snappable != null) {
                snappable.snapHintWidthProperty().set(block.getCodeList().getChildren().get(0).getBoundsInLocal().getWidth());
                if (snapTop)
                    snappable.showTopSnapHint();
                else
                    snappable.showBottomSnapHint();
            } else if (snappableList != null) {
                snappableList.snapHintWidthProperty().set(block.getCodeList().getChildren().get(0).getBoundsInLocal().getWidth());
                snappableList.showSnapHint();
            }

            old = snappable;
            oldList = snappableList;
            indexToAdd = snappableIndex;
        }
    }

    @Override
    public boolean performSnap() {
        Node d = main.draggingProperty().get();

        boolean snapped = false;
        if (d instanceof CodeBlockList block) {
            if (oldList != null) {
                oldList.getCodeList().getChildren().addAll(indexToAdd, block.getCodeList().getChildren());
                snapped = true;
            }
        }

        if (old != null) {
            old.hideSnapHints();
            old = null;
        }

        if (oldList != null) {
            oldList.hideSnapHint();
            oldList = null;
            indexToAdd = -1;
        }
        return snapped;
    }

    record FindSnapResult(int index, boolean snapTop, CodeBlock snappable, CodeBlockList snappableList) {
    }

    private static final FindSnapResult NO_RESULT = new FindSnapResult(-1, false, null, null);

    private FindSnapResult findSnap(Node node, CodeBlockList dragging) {
        if (node instanceof Parent p) {
            for (Node child : p.getChildrenUnmodifiable()) {
                FindSnapResult res = findSnap(child, dragging);
                if (res.index >= 0)
                    return res;
            }
        }

        CodeBlock draggingTop = (CodeBlock) dragging.getCodeList().getChildren().get(0);
        if (node instanceof CodeBlockList list) {
            if (list.getCodeList().getChildren().isEmpty()) {
                // check if we can snap dragging code block into the code block list as first element.
                if (checkEmptySnap(dragging, list) && !(draggingTop.mustBeFirstBlock() && !(list.getParent() instanceof CodeArea)))
                    return new FindSnapResult(0, false, null, list);
            } else {
                // check if we can snap at the top of the code block list.
                CodeBlock top = (CodeBlock) list.getCodeList().getChildren().get(0);
                if (checkTopSnap(dragging, top) && !top.mustBeFirstBlock() &&
                        // not allow block that must be first to be inserted in inner list
                        !(draggingTop.mustBeFirstBlock() && !(list.getParent() instanceof CodeArea)))
                    return new FindSnapResult(0, true, top, list);

                // if the dragging block must be first block, we must not check bottom snapping.
                if (!draggingTop.mustBeFirstBlock()) {

                    // iterate through each code block in the code block list.
                    for (int i = 0; i < list.getCodeList().getChildren().size(); i++) {
                        CodeBlock b = (CodeBlock) list.getCodeList().getChildren().get(i);
                        // find the one that can be snapped.
                        if (checkNormalSnap(dragging, b))
                            return new FindSnapResult(i + 1, false, b, list);
                    }
                }
            }
        }
        return NO_RESULT;
    }

    /**
     * Check if dragging blocklist can be snapped into empty code block list
     *
     * @param dragging
     * @param target
     * @return
     */
    private boolean checkEmptySnap(CodeBlockList dragging, CodeBlockList target) {
        Bounds a = dragging.localToScene(dragging.getBoundsInLocal());
        Bounds b = target.localToScene(target.getBoundsInLocal());
        return a.intersects(b);
    }

    /**
     * Check if the dragging blocklist can be snapped below the target block.
     *
     * @param dragging
     * @param target
     * @return
     */
    private boolean checkNormalSnap(CodeBlockList dragging, CodeBlock target) {
        Bounds a = target.localToScene(target.getContent().getBoundsInLocal());
        Bounds b = dragging.localToScene(dragging.getBoundsInLocal());

        // the x intersect should be at least 35% of the smaller bounds's width
        double l = Math.max(a.getMinX(), b.getMinX()), r = Math.min(a.getMaxX(), b.getMaxX());
        if (Math.max(0, r - l) < 0.35 * Math.min(a.getWidth(), b.getWidth()))
            return false;

        // the other CodeBlock should have distance not more than threshold
        double dist = b.getMinY() - a.getMaxY();
        return dist > -SmeenConstants.SNAP_THRESHOLD && dist <= SmeenConstants.SNAP_THRESHOLD;
    }

    /**
     * Check if the dragging blocklist can be snapped above the target block.
     *
     * @param dragging
     * @param target
     * @return
     */
    private boolean checkTopSnap(CodeBlockList dragging, CodeBlock target) {
        Bounds a = target.localToScene(target.getContent().getBoundsInLocal());
        Bounds b = dragging.localToScene(dragging.getBoundsInLocal());

        // the x intersect should be at least 35% of the smaller bounds's width
        double l = Math.max(a.getMinX(), b.getMinX()), r = Math.min(a.getMaxX(), b.getMaxX());
        if (Math.max(0, r - l) < 0.35 * Math.min(a.getWidth(), b.getWidth()))
            return false;

        // the other CodeBlock should have distance not more than threshold
        double dist = a.getMinY() - b.getMaxY();
        return dist > -SmeenConstants.SNAP_THRESHOLD && dist <= SmeenConstants.SNAP_THRESHOLD;
    }

}
