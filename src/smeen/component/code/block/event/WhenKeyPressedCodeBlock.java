package smeen.component.code.block.event;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.KeyPress;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class WhenKeyPressedCodeBlock extends CodeBlock {

    private ComboBox<KeyPress> optionBox;

    public WhenKeyPressedCodeBlock(MainView main) {
        super(main);
        Label first = new Label("เมื่อกดปุ่ม");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        optionBox = new ComboBox<>();
        optionBox.getItems().addAll(KeyPress.values());
        optionBox.setValue(optionBox.getItems().get(0));

        // set selectBox of optionBox size
        optionBox.setPrefHeight(30);
        optionBox.setCellFactory(param -> new ListCell<KeyPress>() {
            {
                // set the height of the cell
                setPrefHeight(30);
            }

            @Override
            protected void updateItem(KeyPress item, boolean empty) {
                super.updateItem(item, empty);
                if (!empty && item != null) {
                    setText(item.toString());
                } else {
                    setText(null);
                }

            }
        });

        optionBox.setVisibleRowCount(5);


        getContent().setBackground(Background.fill(Color.GOLD));
        getContent().getChildren().addAll(first, optionBox);
    }

    @Override
    public CodeBlock copy() {
        WhenKeyPressedCodeBlock copy = new WhenKeyPressedCodeBlock(getMain());
        copy.optionBox.setValue(optionBox.getValue());
        return copy;
    }

    public ComboBox<KeyPress> getOptionBox() {
        return optionBox;
    }

    @Override
    public boolean mustBeFirstBlock() {
        return true;
    }

    @Override
    public Result execute(SmeenContext context) {
        // no op
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WhenKeyPressedCodeBlock");
        result.put("option", optionBox.getValue());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        optionBox.setValue((KeyPress) data.get("option"));
    }
}
