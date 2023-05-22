package smeen.component.code.block.look;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class ChangeSceneCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Double> posScene;

    public ChangeSceneCodeBlock(MainView main) {
        super(main);
        Label first = new Label("เปลี่ยนฉากเป็นรูปแบบที่");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        posScene = new CodeBlockInputSlot<>(main, Type.Double);

        getContent().setBackground(Background.fill(Color.MEDIUMORCHID));

        getContent().getChildren().addAll(first, posScene);
    }

    @Override
    public CodeBlock copy() {
        ChangeSceneCodeBlock copy = new ChangeSceneCodeBlock(getMain());
        copy.posScene.getTextField().setText(posScene.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        int order = posScene.getValue(context).intValue();
        if(order >= 1 && order <= getMain().getSceneEditor().getSceneCount())
            Platform.runLater(() -> getMain().getStageArea().shownSceneProperty().set(order));
        return new Result(false,false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "ChangeSceneCodeBlock");
        result.put("posScene", posScene.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        posScene.importData((Map<String, Object>) data.get("posScene"));
    }
}
