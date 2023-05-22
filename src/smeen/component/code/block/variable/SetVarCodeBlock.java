package smeen.component.code.block.variable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInput;
import smeen.component.code.CodeBlockInputSlot;
import smeen.component.code.CodeSelector;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.util.Savable;
import smeen.views.MainView;

public class SetVarCodeBlock extends CodeBlock {
    private ComboBox<String> optionBox;
    private Pane slotWrap;
    private CodeSelector codeSelector;

    public SetVarCodeBlock(MainView main, CodeSelector codeSelector) {
        super(main);
        this.codeSelector = codeSelector;
        Label text1 = new Label("เซ็ตค่าตัวแปร");
        text1.setFont(Fonts.SMALL_REGULAR_FONT);
        text1.setTextFill(Color.WHITE);
        Label text2 = new Label("เป็น");
        text2.setFont(Fonts.SMALL_REGULAR_FONT);
        text2.setTextFill(Color.WHITE);
        optionBox = new ComboBox<>();
        optionBox.itemsProperty().bind(codeSelector.createdVarNameProperty());
        //optionBox.setValue(optionBox.getItems().get(0));

        // set selectBox of optionBox size
        optionBox.setPrefHeight(25);
        optionBox.setCellFactory(param -> new ListCell<String>() {
            {
                // set the height of the cell
                setPrefHeight(25);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item);
                } else {
                    setText(null);
                }
            }
        });
        optionBox.setVisibleRowCount(5);

        // When change option
        optionBox.valueProperty().addListener((obs, oldv, newv) -> {

            if (Objects.nonNull(newv)) {
                CodeBlockInputSlot<?> newSlot = new CodeBlockInputSlot<>(getMain(), codeSelector.createdVarMapProperty().get(newv));
                slotWrap.getChildren().clear();
                slotWrap.getChildren().add(newSlot);
            }

        });

        // set String as default to avoid NullException
        CodeBlockInputSlot<?> slot = new CodeBlockInputSlot<>(getMain(), Type.String);
        slotWrap = new Pane();
        slotWrap.getChildren().add(slot);

        getContent().getChildren().addAll(text1, optionBox, text2, slotWrap);
        getContent().setBackground(Background.fill(Color.PALEVIOLETRED));
    }

    private CodeBlockInputSlot<?> getCodeBlockInputSlot(){
        return (CodeBlockInputSlot<?>) slotWrap.getChildren().get(0);
    }

    @Override
    public CodeBlock copy() {
        SetVarCodeBlock copy = new SetVarCodeBlock(getMain(), getMain().getCodeSelector());
        copy.optionBox.setValue(optionBox.getValue());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        String name = optionBox.getValue();
        Object value = getCodeBlockInputSlot().getValue(context);
        context.setVariable(name, value);
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "SetVarCodeBlock");
        result.put("option", optionBox.getValue());
        result.put("value", getCodeBlockInputSlot().exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        optionBox.setValue((String) data.get("option"));
        getCodeBlockInputSlot().importData((Map<String, Object>) data.get("value"));
    }
}
