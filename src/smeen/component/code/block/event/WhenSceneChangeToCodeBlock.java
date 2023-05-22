package smeen.component.code.block.event;

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

public class WhenSceneChangeToCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Double> posScene;
    public WhenSceneChangeToCodeBlock(MainView main){
        super(main);
        Label first = new Label("เมื่อฉากเปลี่ยนเป็นรูปแบบที่");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        posScene = new CodeBlockInputSlot<>(main, Type.Double);
        
        getContent().setBackground(Background.fill(Color.GOLD));
        
        getContent().getChildren().addAll(first, posScene);
    }

    public int getPosScene(SmeenContext context){
        return posScene.getValue(context).intValue();
    }
    @Override
    public boolean mustBeFirstBlock() {
        return true;
    }

    @Override
    public CodeBlock copy() {
    	WhenSceneChangeToCodeBlock copy = new WhenSceneChangeToCodeBlock(getMain());
        copy.posScene.getTextField().setText(posScene.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        // no op
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WhenSceneChangeToCodeBlock");
        result.put("posScene", posScene.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        posScene.importData((Map<String, Object>) data.get("posScene"));
    }
}
