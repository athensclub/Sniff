package smeen.component.code.block.variable;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeSelector;
import smeen.global.Fonts;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class HideVarCodeBlock extends CodeBlock {
    private CodeSelector codeSelector;
    private ComboBox<String> optionBox;

    public HideVarCodeBlock(MainView main, CodeSelector codeSelector) {
        super(main);
        this.codeSelector = codeSelector;
        Label text = new Label("ซ่อนตัวแปร");
        text.setFont(Fonts.SMALL_REGULAR_FONT);
        text.setTextFill(Color.WHITE);
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

        getContent().getChildren().addAll(text, optionBox);
        getContent().setBackground(Background.fill(Color.PALEVIOLETRED));
    }

    @Override
    public CodeBlock copy() {
        HideVarCodeBlock copy = new HideVarCodeBlock(getMain(), getMain().getCodeSelector());
        copy.optionBox.setValue(optionBox.getValue());
        return copy;
    }
    @Override
    public Result execute(SmeenContext context) {
        String name = optionBox.getValue();
        Platform.runLater(() -> getMain().getStageArea().shownVariableProperty().remove(name));
        return new Result(false,false);
    }
    public void updateOption(ObservableList<String> newOptionList) {
        optionBox.setItems(newOptionList);
        if (newOptionList.size() == 0) return;
        optionBox.setValue(newOptionList.get(0));
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "HideVarCodeBlock");
        result.put("option", optionBox.getValue());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        optionBox.setValue((String) data.get("option"));
    }
}
