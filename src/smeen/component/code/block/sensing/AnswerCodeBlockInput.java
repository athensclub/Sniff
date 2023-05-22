package smeen.component.code.block.sensing;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInput;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

public class AnswerCodeBlockInput extends CodeBlockInput<String> {
    public AnswerCodeBlockInput(MainView main){
        super(main);
        Label first = new Label("คำตอบ");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(new Background(new BackgroundFill(Color.DARKTURQUOISE, new CornerRadii(20), null)));
        getContent().getChildren().addAll(first);
        
        // to check T type
        setColorBorder(); 
    }

    @Override
    public CodeBlockInput<?> copy() {
    	AnswerCodeBlockInput copy = new AnswerCodeBlockInput(getMain());
        return copy;
    }
    
    public Type getType() {
    	return Type.String;
    }
    
    public String getValue(SmeenContext context) {
        return  context.currentAnswerProperty().get();
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "AnswerCodeBlockInput");
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
    }
}
