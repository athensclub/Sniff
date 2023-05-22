package smeen.component.code.block.look;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class HideCodeBlock extends CodeBlock {

    public HideCodeBlock(MainView main){
        super(main);
        Label first = new Label("ซ่อน");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(Background.fill(Color.MEDIUMORCHID));

        getContent().getChildren().addAll(first);
    }

    @Override
    public CodeBlock copy() {
    	HideCodeBlock copy = new HideCodeBlock(getMain());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        if(getCodeArea() == null)
            return new Executable.Result(false, false);

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(),sprite))
            return new Executable.Result(false, false); // no op on movement code that is not in sprite or the sprite is being dragged.

        Platform.runLater(() -> sprite.setVisible(false));
        return new Result(false,false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "HideCodeBlock");
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
    	
    }
}
