package smeen.component.code.block.sensing;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInput;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MouseDownCodeBlockInput extends CodeBlockInput<Boolean> {

    public MouseDownCodeBlockInput(MainView main){
        super(main);
        Label first = new Label("ถูกเม้าส์คลิก ?");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(new Background(new BackgroundFill(Color.DARKTURQUOISE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first);
        
        // to check T type
        setColorBorder(); 
    }

    @Override
    public CodeBlockInput<?> copy() {
    	MouseDownCodeBlockInput copy = new MouseDownCodeBlockInput(getMain());
        return copy;
    }
    
    public Type getType() {
    	return Type.Boolean;
    }
    
    public Boolean getValue(SmeenContext context) {
        if (getCodeArea() == null)
            return false;

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(), sprite))
            return false;

        return getMain().getMouseEventsHandler().isSpritePressed(sprite);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "MouseDownCodeBlockInput");
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
