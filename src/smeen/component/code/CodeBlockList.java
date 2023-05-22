package smeen.component.code;

import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ListChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Transform;
import smeen.component.code.block.control.*;
import smeen.component.code.block.event.BroadcastAndWaitCodeBlock;
import smeen.component.code.block.event.BroadcastCodeBlock;
import smeen.component.code.block.event.WhenClickedCodeBlock;
import smeen.component.code.block.event.WhenKeyPressedCodeBlock;
import smeen.component.code.block.event.WhenReceivedBroadcastCodeBlock;
import smeen.component.code.block.event.WhenSceneChangeToCodeBlock;
import smeen.component.code.block.event.WhenStartCodeBlock;
import smeen.component.code.block.look.ChangeCostumeCodeBlock;
import smeen.component.code.block.look.ChangeHeightCodeBlock;
import smeen.component.code.block.look.ChangeSceneCodeBlock;
import smeen.component.code.block.look.ChangeWidthCodeBlock;
import smeen.component.code.block.look.HideCodeBlock;
import smeen.component.code.block.look.SayCodeBlock;
import smeen.component.code.block.look.ShowCodeBlock;
import smeen.component.code.block.meen.BitkubCodeBlock;
import smeen.component.code.block.meen.TransformCodeBlock;
import smeen.component.code.block.meen.WhenBitCodeBlock;
import smeen.component.code.block.movement.*;
import smeen.component.code.block.sensing.AskAndWaitCodeBlock;
import smeen.component.code.block.variable.HideVarCodeBlock;
import smeen.component.code.block.variable.SetVarCodeBlock;
import smeen.component.code.block.variable.ShowVarCodeBlock;
import smeen.global.SmeenConstants;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Assume that the CodeBlockList is either being dragged or in CodeArea only.
 * <p>
 * Do not add the CodeBlock component directly to this component getChildren(). Instead,
 * add it using getCodeList().getChildren().add/addAll.
 */
public class CodeBlockList extends VBox implements Savable, Executable {

    private MainView main;
    private VBox codeList;
    private DoubleProperty snapHintWidth;

    private Pane snapHintWhenEmpty;

    public CodeBlockList(MainView main) {
        this.main = main;

        codeList = new VBox();
        codeList.setFillWidth(false);

        // prevent adding code block directly to this component children list.
        getChildren().addListener((ListChangeListener<? super Node>) c -> {
            while (c.next()) {
                if (c.wasAdded() && c.getAddedSubList().stream().anyMatch(node -> node instanceof CodeBlock))
                    throw new IllegalStateException("Do not add the CodeBlock component directly to CodeBlockList's getChildren(). Instead, add it using getCodeList().getChildren().add/addAll.");
            }
        });

        // allow only code block to be added to code block list
        codeList.getChildren().addListener((ListChangeListener<? super Node>) c -> {
            while (c.next()) {
                if (c.wasAdded() && c.getAddedSubList().stream().anyMatch(node -> !(node instanceof CodeBlock)))
                    throw new IllegalStateException("Nodes added to CodeBlockList.getCodeList().getChildren() must be of class CodeBlock or its subclasses only.");
            }
        });

        snapHintWidth = new SimpleDoubleProperty();
        snapHintWhenEmpty = new Pane();

        getChildren().addAll(snapHintWhenEmpty, codeList);
    }


    public VBox getCodeList() {
        return codeList;
    }

    /**
     * Only used when the code block list is empty.
     */
    public void showSnapHint() {
        if (!snapHintWhenEmpty.getChildren().isEmpty())
            return;
        Rectangle hint = new Rectangle(0, 40, Color.GREY);
        hint.widthProperty().bind(snapHintWidth);
        snapHintWhenEmpty.getChildren().add(hint);
    }

    public void hideSnapHint() {
        snapHintWhenEmpty.getChildren().clear();
    }

    public DoubleProperty snapHintWidthProperty() {
        return snapHintWidth;
    }

    /**
     * Execute code of this code block list, ignoring the first block condition
     * (even if there's one or not).
     *
     * @param context
     */
    @Override
    public Executable.Result execute(SmeenContext context) {
        for (Node child : codeList.getChildren()) {
            if (child instanceof Executable e) {
                Executable.Result result = e.execute(context);
                if (result.shouldStop() || Thread.currentThread().isInterrupted())
                    return new Executable.Result(false, true);
                if (result.shouldBreak())
                    return new Executable.Result(true, false);
//                try {
//                    Thread.sleep(SmeenConstants.EXECUTION_DELAY);
//                } catch (InterruptedException ex) {
//                    return new Result(false, true);
//                }
            }
        }
        return new Executable.Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "CodeBlockList");
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        result.put("codes", codeList.getChildren().stream().map(node -> ((Savable) node).exportData()).toList());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));

        List<?> codes = (List<?>) data.get("codes");
        for (Object code : codes) {
            if (code instanceof Map m) {
                switch ((String) m.get("type")) {
                    case "MoveForwardCodeBlock":
                        MoveForwardCodeBlock moveForward = new MoveForwardCodeBlock(main);
                        moveForward.importData(m);
                        codeList.getChildren().add(moveForward);
                        break;
                    case "MoveBackwardCodeBlock":
                        MoveBackwardCodeBlock moveBackward = new MoveBackwardCodeBlock(main);
                        moveBackward.importData(m);
                        codeList.getChildren().add(moveBackward);
                        break;
                    case "MoveToCodeBlock":
                        MoveToCodeBlock moveTo = new MoveToCodeBlock(main);
                        moveTo.importData(m);
                        codeList.getChildren().add(moveTo);
                        break;
                    case "RotateClockwiseCodeBlock":
                        RotateClockwiseCodeBlock clockwise = new RotateClockwiseCodeBlock(main);
                        clockwise.importData(m);
                        codeList.getChildren().add(clockwise);
                        break;
                    case "RotateCounterClockwiseCodeBlock":
                        RotateCounterClockwiseCodeBlock counterClockwise = new RotateCounterClockwiseCodeBlock(main);
                        counterClockwise.importData(m);
                        codeList.getChildren().add(counterClockwise);
                        break;
                    case "SayCodeBlock":
                        SayCodeBlock say = new SayCodeBlock(main);
                        say.importData(m);
                        codeList.getChildren().add(say);
                        break;
                    case "ChangeWidthCodeBlock":
                        ChangeWidthCodeBlock changeWidth = new ChangeWidthCodeBlock(main);
                        changeWidth.importData(m);
                        codeList.getChildren().add(changeWidth);
                        break;
                    case "ChangeHeightCodeBlock":
                        ChangeHeightCodeBlock changeHeight = new ChangeHeightCodeBlock(main);
                        changeHeight.importData(m);
                        codeList.getChildren().add(changeHeight);
                        break;
                    case "ChangeCostumeCodeBlock":
                        ChangeCostumeCodeBlock changeCostume = new ChangeCostumeCodeBlock(main);
                        changeCostume.importData(m);
                        codeList.getChildren().add(changeCostume);
                        break;
                    case "ChangeSceneCodeBlock":
                        ChangeSceneCodeBlock changeScene = new ChangeSceneCodeBlock(main);
                        changeScene.importData(m);
                        codeList.getChildren().add(changeScene);
                        break;
                    case "HideCodeBlock":
                        HideCodeBlock hide = new HideCodeBlock(main);
                        hide.importData(m);
                        codeList.getChildren().add(hide);
                        break;
                    case "ShowCodeBlock":
                        ShowCodeBlock show = new ShowCodeBlock(main);
                        show.importData(m);
                        codeList.getChildren().add(show);
                        break;
                    case "WhenStartCodeBlock":
                        WhenStartCodeBlock whenStart = new WhenStartCodeBlock(main);
                        whenStart.importData(m);
                        codeList.getChildren().add(whenStart);
                        break;
                    case "WhenKeyPressedCodeBlock":
                        WhenKeyPressedCodeBlock whenKey = new WhenKeyPressedCodeBlock(main);
                        whenKey.importData(m);
                        codeList.getChildren().add(whenKey);
                        break;
                    case "WhenClickedCodeBlock":
                        WhenClickedCodeBlock whenclick = new WhenClickedCodeBlock(main);
                        whenclick.importData(m);
                        codeList.getChildren().add(whenclick);
                        break;
                    case "WhenSceneChangeToCodeBlock":
                        WhenSceneChangeToCodeBlock whenScene = new WhenSceneChangeToCodeBlock(main);
                        whenScene.importData(m);
                        codeList.getChildren().add(whenScene);
                        break;
                    case "WhenReceivedBroadcastCodeBlock":
                        WhenReceivedBroadcastCodeBlock whenReceived = new WhenReceivedBroadcastCodeBlock(main);
                        whenReceived.importData(m);
                        codeList.getChildren().add(whenReceived);
                        break;
                    case "BroadcastCodeBlock":
                        BroadcastCodeBlock broadcast = new BroadcastCodeBlock(main);
                        broadcast.importData(m);
                        codeList.getChildren().add(broadcast);
                        break;
                    case "BroadcastAndWaitCodeBlock":
                        BroadcastAndWaitCodeBlock broadcastWait = new BroadcastAndWaitCodeBlock(main);
                        broadcastWait.importData(m);
                        codeList.getChildren().add(broadcastWait);
                        break;
                    case "IfCodeBlock":
                        IfCodeBlock ifCB = new IfCodeBlock(main);
                        ifCB.importData(m);
                        codeList.getChildren().add(ifCB);
                        break;
                    case "IfElseCodeBlock":
                        IfElseCodeBlock ifElse = new IfElseCodeBlock(main);
                        ifElse.importData(m);
                        codeList.getChildren().add(ifElse);
                        break;
                    case "RepeatCodeBlock":
                        RepeatCodeBlock repeat = new RepeatCodeBlock(main);
                        repeat.importData(m);
                        codeList.getChildren().add(repeat);
                        break;
                    case "WhileCodeBlock":
                        WhileCodeBlock whileCB = new WhileCodeBlock(main);
                        whileCB.importData(m);
                        codeList.getChildren().add(whileCB);
                        break;
                    case "WaitCodeBlock":
                        WaitCodeBlock wait = new WaitCodeBlock(main);
                        wait.importData(m);
                        codeList.getChildren().add(wait);
                        break;
                    case "WaitUntilCodeBlock":
                        WaitUntilCodeBlock waitUntil = new WaitUntilCodeBlock(main);
                        waitUntil.importData(m);
                        codeList.getChildren().add(waitUntil);
                        break;
                    case "AskAndWaitCodeBlock":
                        AskAndWaitCodeBlock ask = new AskAndWaitCodeBlock(main);
                        ask.importData(m);
                        codeList.getChildren().add(ask);
                        break;
                    case "BreakCodeBlock":
                        BreakCodeBlock breakCodeBlock = new BreakCodeBlock(main);
                        breakCodeBlock.importData(m);
                        codeList.getChildren().add(breakCodeBlock);
                        break;
                    case "BitkubCodeBlock":
                        BitkubCodeBlock bit = new BitkubCodeBlock(main);
                        bit.importData(m);
                        codeList.getChildren().add(bit);
                        break;
                    case "TransformCodeBlock":
                        TransformCodeBlock transform = new TransformCodeBlock(main);
                        transform.importData(m);
                        codeList.getChildren().add(transform);
                        break;
                    case "WhenBitCodeBlock":
                        WhenBitCodeBlock whenBit = new WhenBitCodeBlock(main);
                        whenBit.importData(m);
                        codeList.getChildren().add(whenBit);
                        break;
                    case "HideVarCodeBlock":
                        HideVarCodeBlock hideVar = new HideVarCodeBlock(main, main.getCodeSelector());
                        hideVar.importData(m);
                        codeList.getChildren().add(hideVar);
                        break;
                    case "SetVarCodeBlock":
                        SetVarCodeBlock setVar = new SetVarCodeBlock(main, main.getCodeSelector());
                        setVar.importData(m);
                        codeList.getChildren().add(setVar);
                        break;
                    case "ShowVarCodeBlock":
                        ShowVarCodeBlock showVar = new ShowVarCodeBlock(main, main.getCodeSelector());
                        showVar.importData(m);
                        codeList.getChildren().add(showVar);
                        break;
                    default:
                        throw new IllegalStateException("Unsupported CodeBlockList code type: " + m.get("type"));
                }
            }
        }
    }

}
