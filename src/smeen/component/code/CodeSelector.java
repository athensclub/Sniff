package smeen.component.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollToEvent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import smeen.component.code.block.control.BreakCodeBlock;
import smeen.component.code.block.control.FalseCodeBlockInput;
import smeen.component.code.block.control.IfCodeBlock;
import smeen.component.code.block.control.IfElseCodeBlock;
import smeen.component.code.block.control.RepeatCodeBlock;
import smeen.component.code.block.control.TrueCodeBlockInput;
import smeen.component.code.block.control.WaitCodeBlock;
import smeen.component.code.block.control.WaitUntilCodeBlock;
import smeen.component.code.block.control.WhileCodeBlock;
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
import smeen.component.code.block.movement.GetXCodeBlockInput;
import smeen.component.code.block.movement.GetYCodeBlockInput;
import smeen.component.code.block.movement.MoveBackwardCodeBlock;
import smeen.component.code.block.movement.MoveForwardCodeBlock;
import smeen.component.code.block.movement.MoveToCodeBlock;
import smeen.component.code.block.movement.RotateClockwiseCodeBlock;
import smeen.component.code.block.movement.RotateCounterClockwiseCodeBlock;
import smeen.component.code.block.operator.inputBoolean.AndCodeBlockInput;
import smeen.component.code.block.operator.inputBoolean.EqualCodeBlockInput;
import smeen.component.code.block.operator.inputBoolean.GreaterCodeBlockInput;
import smeen.component.code.block.operator.inputBoolean.LessCodeBlockInput;
import smeen.component.code.block.operator.inputBoolean.NotCodeBlockInput;
import smeen.component.code.block.operator.inputBoolean.OrCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.AddCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.DivideCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.FunctionOfCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.MinusCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.ModCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.MultiCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.RandomNumberCodeBlockInput;
import smeen.component.code.block.operator.inputDouble.RoundCodeBlockInput;
import smeen.component.code.block.operator.inputString.ContainCodeBlockInput;
import smeen.component.code.block.operator.inputString.JoinCodeBlockInput;
import smeen.component.code.block.operator.inputString.LetterOfCodeBlockInput;
import smeen.component.code.block.sensing.AnswerCodeBlockInput;
import smeen.component.code.block.sensing.AskAndWaitCodeBlock;
import smeen.component.code.block.sensing.KeyPressedCodeBlockInput;
import smeen.component.code.block.sensing.MouseDownCodeBlockInput;
import smeen.component.code.block.sensing.TouchingObjCodeBlockInput;
import smeen.component.code.block.variable.*;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.Type;
import smeen.global.SmeenSVGs;
import smeen.util.Resettable;
import smeen.util.Savable;
import smeen.views.MainView;

import static smeen.global.SmeenComponent.createRegion;

public class CodeSelector extends ScrollPane implements Resettable, Savable {

    private MainView main;

    private VBox createdVarPane;
    //private ArrayList<String> createdVarName;
    private MapProperty<String, Type> createdVarMap;

    private ListProperty<String> createdVarName;

    public CodeSelector(MainView main) {
        this.main = main;
        VBox root = new VBox();
        root.setFillWidth(false);

        // Movement
        Label moveLabel = new Label("เคลื่อนไหว");
        moveLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlock moveForward = new MoveForwardCodeBlock(main);
        addMouseDragEventHandler(moveForward);

        CodeBlock moveBackward = new MoveBackwardCodeBlock(main);
        addMouseDragEventHandler(moveBackward);

        CodeBlock rotateClock = new RotateClockwiseCodeBlock(main);
        addMouseDragEventHandler(rotateClock);

        CodeBlock rotateCounterClock = new RotateCounterClockwiseCodeBlock(main);
        addMouseDragEventHandler(rotateCounterClock);

        CodeBlock moveTo = new MoveToCodeBlock(main);
        addMouseDragEventHandler(moveTo);

        CodeBlockInput<?> getX = new GetXCodeBlockInput(main);
        addMouseDragEventHandler(getX);

        CodeBlockInput<?> getY = new GetYCodeBlockInput(main);
        addMouseDragEventHandler(getY);

        root.getChildren().addAll(moveLabel, moveForward, moveBackward, rotateClock, rotateCounterClock, moveTo, getX, getY);

        // Look
        Label lookLabel = new Label("การแสดง");
        lookLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlock say = new SayCodeBlock(main);
        addMouseDragEventHandler(say);

        CodeBlock changeWidth = new ChangeWidthCodeBlock(main);
        addMouseDragEventHandler(changeWidth);

        CodeBlock changeHeight = new ChangeHeightCodeBlock(main);
        addMouseDragEventHandler(changeHeight);

        CodeBlock changeCostume = new ChangeCostumeCodeBlock(main);
        addMouseDragEventHandler(changeCostume);

        CodeBlock changeScene = new ChangeSceneCodeBlock(main);
        addMouseDragEventHandler(changeScene);

        CodeBlock show = new ShowCodeBlock(main);
        addMouseDragEventHandler(show);

        CodeBlock hide = new HideCodeBlock(main);
        addMouseDragEventHandler(hide);

        root.getChildren().addAll(lookLabel, say, changeWidth, changeHeight, changeCostume, changeScene, show, hide);

        // Event
        Label eventLabel = new Label("เหตุการณ์");
        eventLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlock whenStart = new WhenStartCodeBlock(main);
        addMouseDragEventHandler(whenStart);

        CodeBlock whenKeyPressed = new WhenKeyPressedCodeBlock(main);
        addMouseDragEventHandler(whenKeyPressed);

        CodeBlock whenClicked = new WhenClickedCodeBlock(main);
        addMouseDragEventHandler(whenClicked);

        CodeBlock whenSceneChangeTo = new WhenSceneChangeToCodeBlock(main);
        addMouseDragEventHandler(whenSceneChangeTo);

        CodeBlock whenReceived = new WhenReceivedBroadcastCodeBlock(main);
        addMouseDragEventHandler(whenReceived);

        CodeBlock broadcast = new BroadcastCodeBlock(main);
        addMouseDragEventHandler(broadcast);

        CodeBlock broadcastAndWait = new BroadcastAndWaitCodeBlock(main);
        addMouseDragEventHandler(broadcastAndWait);

        root.getChildren().addAll(eventLabel, whenStart, whenKeyPressed, whenClicked, whenSceneChangeTo, whenReceived,
                createSubBlock(broadcast), createSubBlock(broadcastAndWait));

        // Control
        Label controlLabel = new Label("การควบคุม");
        controlLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlock wait = new WaitCodeBlock(main);
        addMouseDragEventHandler(wait);

        CodeBlock ifCb = new IfCodeBlock(main);
        addMouseDragEventHandler(ifCb);

        CodeBlock ifElse = new IfElseCodeBlock(main);
        addMouseDragEventHandler(ifElse);

        CodeBlock repeat = new RepeatCodeBlock(main);
        addMouseDragEventHandler(repeat);

        CodeBlock whileCB = new WhileCodeBlock(main);
        addMouseDragEventHandler(whileCB);

        CodeBlock waitUntil = new WaitUntilCodeBlock(main);
        addMouseDragEventHandler(waitUntil);

        CodeBlock breakCB = new BreakCodeBlock(main);
        addMouseDragEventHandler(breakCB);

        CodeBlockInput<?> trueCB = new TrueCodeBlockInput(main);
        addMouseDragEventHandler(trueCB);

        CodeBlockInput<?> falseCB = new FalseCodeBlockInput(main);
        addMouseDragEventHandler(falseCB);

        root.getChildren().addAll(controlLabel, ifCb, ifElse, repeat, whileCB, wait, waitUntil, breakCB, trueCB, falseCB);

        // Sensing
        Label sensingLabel = new Label("การสัมผัส");
        sensingLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlock askAndWait = new AskAndWaitCodeBlock(main);
        addMouseDragEventHandler(askAndWait);

        CodeBlockInput<?> answer = new AnswerCodeBlockInput(main);
        addMouseDragEventHandler(answer);
        HBox answerSub = createSubBlock(answer);

        CodeBlockInput<?> touchingObj = new TouchingObjCodeBlockInput(main);
        addMouseDragEventHandler(touchingObj);

        CodeBlockInput<?> keyPressed = new KeyPressedCodeBlockInput(main);
        addMouseDragEventHandler(keyPressed);

        CodeBlockInput<?> mouseDown = new MouseDownCodeBlockInput(main);
        addMouseDragEventHandler(mouseDown);

        root.getChildren().addAll(sensingLabel, askAndWait, answerSub, touchingObj, keyPressed, mouseDown);

        // Operator
        // input Double
        Label operNumLabel = new Label("การคำนวณ (ตัวเลข)");
        operNumLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlockInput<?> add = new AddCodeBlockInput(main);
        addMouseDragEventHandler(add);

        CodeBlockInput<?> minus = new MinusCodeBlockInput(main);
        addMouseDragEventHandler(minus);

        CodeBlockInput<?> multi = new MultiCodeBlockInput(main);
        addMouseDragEventHandler(multi);

        CodeBlockInput<?> divide = new DivideCodeBlockInput(main);
        addMouseDragEventHandler(divide);

        CodeBlockInput<?> mod = new ModCodeBlockInput(main);
        addMouseDragEventHandler(mod);

        CodeBlockInput<?> round = new RoundCodeBlockInput(main);
        addMouseDragEventHandler(round);

        CodeBlockInput<?> ranNum = new RandomNumberCodeBlockInput(main);
        addMouseDragEventHandler(ranNum);

        CodeBlockInput<?> function = new FunctionOfCodeBlockInput(main);
        addMouseDragEventHandler(function);

        root.getChildren().addAll(operNumLabel, add, minus, multi, divide, mod, round, ranNum, function);

        // input Boolean
        Label operBoolLabel = new Label("การคำนวณ (เปรียบเทียบ)");
        operBoolLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlockInput<?> greater = new GreaterCodeBlockInput(main);
        addMouseDragEventHandler(greater);

        CodeBlockInput<?> less = new LessCodeBlockInput(main);
        addMouseDragEventHandler(less);

        CodeBlockInput<?> equal = new EqualCodeBlockInput(main);
        addMouseDragEventHandler(equal);

        CodeBlockInput<?> and = new AndCodeBlockInput(main);
        addMouseDragEventHandler(and);

        CodeBlockInput<?> or = new OrCodeBlockInput(main);
        addMouseDragEventHandler(or);

        CodeBlockInput<?> not = new NotCodeBlockInput(main);
        addMouseDragEventHandler(not);

        root.getChildren().addAll(operBoolLabel, greater, less, equal, and, or, not);

        // input String
        Label operStrLabel = new Label("การคำนวณ (ตัวอักษร)");
        operStrLabel.setFont(Fonts.BASIC_BOLD_FONT);

        CodeBlockInput<?> join = new JoinCodeBlockInput(main);
        addMouseDragEventHandler(join);

        CodeBlockInput<?> letterOf = new LetterOfCodeBlockInput(main);
        addMouseDragEventHandler(letterOf);

        CodeBlockInput<?> contain = new ContainCodeBlockInput(main);
        addMouseDragEventHandler(contain);

        root.getChildren().addAll(operStrLabel, join, letterOf, contain);

        // Meen

//        Label meen = new Label("การมีน");
//        meen.setFont(Fonts.BASIC_BOLD_FONT);
//
//        CodeBlock bit = new BitkubCodeBlock(main);
//        addMouseDragEventHandler(bit);
//
//        CodeBlock whenbit = new WhenBitCodeBlock(main);
//        addMouseDragEventHandler(whenbit);
//
//        CodeBlock transform = new TransformCodeBlock(main);
//        addMouseDragEventHandler(transform);
//
//        root.getChildren().addAll(meen, bit, createSubBlock(whenbit), transform);

        // Variable

        Label vari = new Label("การสร้างตัวแปร");
        vari.setFont(Fonts.BASIC_BOLD_FONT);

        Button addVar = new CreateVarButton(main, this);
        createdVarPane = new VBox();
        createdVarMap = new SimpleMapProperty<>(FXCollections.observableMap(new ConcurrentHashMap<>()));
        createdVarPane.setSpacing(5);

        createdVarName = new SimpleListProperty<>(FXCollections.observableArrayList());
        createdVarMap.addListener((MapChangeListener<? super String, ? super Type>) c -> {
            if (c.wasAdded()) {
                if (!createdVarName.contains(c.getKey()))
                    createdVarName.add(c.getKey());
            } else {
                createdVarName.remove(c.getKey());
            }
        });
        // ((CreateVarButton)addVar).addVar("variable" , Type.Double);

        CodeBlock setVar = new SetVarCodeBlock(main, this);
        addMouseDragEventHandler(setVar);
        setVar.setVisible(false);

        CodeBlock showVar = new ShowVarCodeBlock(main, this);
        addMouseDragEventHandler(showVar);
        showVar.setVisible(false);

        CodeBlock hideVar = new HideVarCodeBlock(main, this);
        addMouseDragEventHandler(hideVar);
        hideVar.setVisible(false);

        createdVarPane.getChildren().addListener(new InvalidationListener() {

            private int size = createdVarPane.getChildren().size();

            @Override
            public void invalidated(Observable o) {
                int newSize = createdVarPane.getChildren().size();
                if (size != newSize) { // prevent triggering if the size did not change
                    if (newSize == 0) {
                        setVar.setVisible(false);
                        showVar.setVisible(false);
                        hideVar.setVisible(false);
                    } else if (newSize > 0) {
                        setVar.setVisible(true);
                        showVar.setVisible(true);
                        hideVar.setVisible(true);
                    }
                }
                size = newSize;
            }
        });

        root.getChildren().addAll(vari, addVar, createdVarPane);
        root.getChildren().addAll(setVar, showVar, hideVar);
        root.setSpacing(5);
        root.setPadding(new Insets(0, 10, 0, 10));

        setHbarPolicy(ScrollBarPolicy.NEVER);
        setMinWidth(280);

        setContent(root);

        // if focus at Scene CodeArea
        main.codeAreaProperty().addListener((obs, oldv, newv) -> {
            boolean isVisible = true;
            if (newv == null || newv.getSprite() == null) {
                isVisible = false;
            }
            // setVisible
            moveLabel.setVisible(isVisible);
            moveForward.setVisible(isVisible);
            moveBackward.setVisible(isVisible);
            rotateClock.setVisible(isVisible);
            rotateCounterClock.setVisible(isVisible);
            moveTo.setVisible(isVisible);
            getX.setVisible(isVisible);
            getY.setVisible(isVisible);

            //lookLabel.setVisible(isVisible);
            changeWidth.setVisible(isVisible);
            changeHeight.setVisible(isVisible);
            changeCostume.setVisible(isVisible);
            say.setVisible(isVisible);
            hide.setVisible(isVisible);
            show.setVisible(isVisible);

            askAndWait.setVisible(isVisible);
            answerSub.setVisible(isVisible);

//            transform.setVisible(isVisible);

            // setManaged
            moveLabel.setManaged(isVisible);
            moveLabel.setManaged(isVisible);
            moveForward.setManaged(isVisible);
            moveBackward.setManaged(isVisible);
            rotateClock.setManaged(isVisible);
            rotateCounterClock.setManaged(isVisible);
            moveTo.setManaged(isVisible);
            getX.setManaged(isVisible);
            getY.setManaged(isVisible);

            //lookLabel.setManaged(isVisible);
            changeWidth.setManaged(isVisible);
            changeHeight.setManaged(isVisible);
            changeCostume.setManaged(isVisible);
            say.setManaged(isVisible);
            hide.setManaged(isVisible);
            show.setManaged(isVisible);

            askAndWait.setManaged(isVisible);
            answerSub.setManaged(isVisible);

//            transform.setManaged(isVisible);
        });

    }

    private void addMouseDragEventHandler(CodeBlock block) {
        block.addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
            startFullDrag();

            CodeBlockList toDrag = new CodeBlockList(main);
            CodeBlock copy = block.copy();
            toDrag.getCodeList().getChildren().add(copy);

            // set the position of the node from relative to the parent to relative to the
            // scene.
            Point2D pos = block.localToScene(0, 0);
            toDrag.relocate(pos.getX(), pos.getY());

            main.draggingProperty().set(toDrag);
        });
    }

    public void addMouseDragEventHandler(CodeBlockInput<?> block) {
        block.addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
            startFullDrag();

            CodeBlockInput<?> copy = block.copy();

            // set the position of the node from relative to the parent to relative to the
            // scene.
            Point2D pos = block.localToScene(0, 0);
            copy.relocate(pos.getX(), pos.getY());

            main.draggingProperty().set(copy);
        });
    }

    public HBox createSubBlock(Node node) {
        HBox res = new HBox();

        int size = 24;
        SVGPath subTask = new SVGPath();
        subTask.setContent(SmeenSVGs.SUBTASK_PATH);
        Region subTaskIcon = new Region();
        subTaskIcon.setShape(subTask);
        subTaskIcon.setMaxSize(size, size);
        subTaskIcon.setMinSize(size, size);
        subTaskIcon.setBackground(Background.fill(Color.BLACK));
        // subTaskIcon.setRotate(90);
        res.getChildren().addAll(subTaskIcon, node);
        HBox.setMargin(subTaskIcon, new Insets(0, 10, 0, 10));
        res.setAlignment(Pos.CENTER_LEFT);

        return res;
    }

    public void addVar(String name, Type type) {
        // add name of Variable to codeSelector first
        createdVarMapProperty().put(name, type);

        Object val = null;
        if (type.equals(Type.String))
            val = "";
        else if (type.equals(Type.Double))
            val = 0.0;
        else if (type.equals(Type.Boolean))
            val = false;
        main.getCodeExecutionHandler().getContext().setVariable(name, val);

        int sizeIcon = 30;
        HBox varWrap = new HBox();
        Region delIcon1 = createRegion(SmeenSVGs.DELETE_PATH1, 20);
        delIcon1.setBackground(Background.fill(Color.RED));
        Region delIcon2 = createRegion(SmeenSVGs.DELETE_PATH2, 12.5);
        delIcon2.setBackground(Background.fill(Color.WHITE));
        Region delIcon3 = createRegion(SmeenSVGs.DELETE_PATH3, 12.5);
        delIcon3.setBackground(Background.fill(Color.WHITE));
        StackPane delIcon = new StackPane();
        delIcon.getChildren().addAll(delIcon1, delIcon2, delIcon3);
        delIcon.setAlignment(Pos.CENTER);
        delIcon.setCursor(Cursor.HAND);
        delIcon.setMaxSize(sizeIcon, sizeIcon);
        delIcon.setMinSize(sizeIcon, sizeIcon);
        delIcon.setOnMouseClicked(e -> {
            for (Node search : main.getCodeSelector().getCreatedVar().getChildren()) {
                if (search instanceof HBox currentVarWrap && currentVarWrap.getChildren().get(0).equals(delIcon)) {
                    createdVarMapProperty().remove(((VariableCodeBlockInput<?>) (currentVarWrap.getChildren().get(1))).nameProperty().get());
                    getCreatedVar().getChildren().remove(currentVarWrap);

                    break;
                }
            }
        });

        // Create Var CB here
        VariableCodeBlockInput<?> varBlock = new VariableCodeBlockInput<>(main, type, name);
        addMouseDragEventHandler(varBlock);

        // Comment this for now
        //varWrap.getChildren().addAll(delIcon , varBlock);

        varWrap.getChildren().addAll(createSubBlock(varBlock));
        varWrap.setAlignment(Pos.CENTER_LEFT);
        varWrap.setSpacing(10);
        getCreatedVar().getChildren().add(varWrap);
    }

    public VBox getCreatedVar() {
        return createdVarPane;
    }

    public MapProperty<String, Type> createdVarMapProperty() {
        return createdVarMap;
    }

    public ReadOnlyListProperty<String> createdVarNameProperty() {
        return createdVarName;
    }


    @Override
    public void clearData() {
        createdVarMap.clear();
        createdVarPane.getChildren().clear();
    }

    @Override
    public void reset() {
        clearData();
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("createdVar", new HashMap<>(createdVarMap));
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        Map<String, Type> cv = (Map<String, Type>) data.get("createdVar");
        for (var e : cv.entrySet())
            addVar(e.getKey(), e.getValue());
    }
}
