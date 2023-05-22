package smeen.component.code.block.control;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class WaitCodeBlock extends CodeBlock {
	
	private CodeBlockInputSlot<Double> amount;
    public WaitCodeBlock(MainView main){
        super(main);
        Label first = new Label("รอ");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        amount = new CodeBlockInputSlot<>(main, Type.Double);
        
        Label second = new Label("วินาที");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);
        
        getContent().setBackground(Background.fill(Color.ORANGE));
        
        getContent().getChildren().addAll(first, amount, second);
    }

    @Override
    public CodeBlock copy() {
    	WaitCodeBlock copy = new WaitCodeBlock(getMain());
    	copy.amount.getTextField().setText(amount.getTextField().getText());
        return copy;
    }
    
    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WaitCodeBlock");
        result.put("amount", amount.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        amount.importData((Map<String, Object>) data.get("amount"));
    }
    
    @Override
    public Result execute(SmeenContext context) {
        long millis = (long) (amount.getValue(context) * 1000);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            return new Result(false,true);
        }
        return new Result(false, false);
    }
}
