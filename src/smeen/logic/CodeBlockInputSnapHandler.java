package smeen.logic;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.SmeenConstants.Type;
import smeen.views.MainView;

/**
 * Handle snapping of code block input
 */
public class CodeBlockInputSnapHandler implements SnapHandler{

    private MainView main;

    private CodeBlockInputSlot old;

    /**
     * Constructor for the CodeBlockInputSnapHandler
     * @param main
     */
    public CodeBlockInputSnapHandler(MainView main){
        this.main = main;
    }

    @Override
    public void update() {
        if(main.draggingProperty().get() instanceof CodeBlockInput<?> input){
            CodeBlockInputSlot snappable = null;
            for (Node node : main.codeAreaProperty().get().getChildren()) {
                snappable = findSnappable(input, node);
                if(snappable != null)
                    break;
            }

            if(old != null && snappable != old)
                old.hideSnapHint();

            if(snappable != null) {
                snappable.snapHintWidthProperty().set(input.getWidth());
                snappable.showSnapHint();
            }

            old = snappable;
        }
    }

    @Override
    public boolean performSnap() {
        boolean snapped = false;
        if(old != null){
            old.hideSnapHint();
            old.contentProperty().set(main.draggingProperty().get());
            snapped = true;
        }

        old = null;
        return snapped;
    }

    /**
     * Check the node and all its descendants to find any CodeBlockInputSlot that can be snapped
     * with the currently dragging node.
     * @param node the node to find CodeBlockInputSlot.
     * @return CodeBlockInputSlot instance that the currently dragging node can be snapped to, or null if there's none.
     */
    private CodeBlockInputSlot<?> findSnappable(CodeBlockInput<?> dragging, Node node){
        if(node instanceof CodeBlockInputSlot<?> slot && check(dragging, slot))
            return slot;

        if(node instanceof Parent parent){
            for(Node child : parent.getChildrenUnmodifiable()){
                CodeBlockInputSlot<?> result = findSnappable(dragging, child);
                if(result != null)
                    return result;
            }
        }
        return null;
    }

    private boolean check(CodeBlockInput<?> dragging, CodeBlockInputSlot<?> target){
        if(!dragging.getType().equals(target.getType()) || target.contentProperty().get() != null)
            return false;

        Bounds a = dragging.localToScene(dragging.getBoundsInLocal());
        // we must make sure that text field is always present when we call this check function (in other words, the target content is always null)
        Bounds b = target.getTextField().localToScene(target.getTextField().getBoundsInLocal());
        return a.intersects(b);
    }

}
