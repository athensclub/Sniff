package smeen.component.code.block.look;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class SayCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<String> word;
    public SayCodeBlock(MainView main){
        super(main);
        Label first = new Label("พูด");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        word = new CodeBlockInputSlot<>(main, Type.String);

        getContent().setBackground(Background.fill(Color.MEDIUMORCHID));

        getContent().getChildren().addAll(first, word);
    }

    @Override
    public CodeBlock copy() {
    	SayCodeBlock copy = new SayCodeBlock(getMain());
        copy.word.getTextField().setText(word.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        if (getCodeArea() == null)
            return new Executable.Result(false, false);

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(), sprite))
            return new Executable.Result(false, false); // no op on movement code that is not in sprite or the sprite is being dragged.

        String text = word.getValue(context);
        Platform.runLater(() -> sprite.say(text));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            return new Result(false, true);
        }
        Platform.runLater(() -> sprite.stopSaying());
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "SayCodeBlock");
        result.put("word", word.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        word.importData((Map<String, Object>) data.get("word"));
    }
}
