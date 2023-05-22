package smeen.component.code.block.sensing;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class AskAndWaitCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<String> word;

    public AskAndWaitCodeBlock(MainView main) {
        super(main);
        Label first = new Label("ถามว่า");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        word = new CodeBlockInputSlot<>(main, Type.String);

        Label second = new Label("แล้วรอ");
        second.setTextFill(Color.WHITE);
        second.setFont(Fonts.SMALL_REGULAR_FONT);

        getContent().setBackground(Background.fill(Color.DARKTURQUOISE));

        getContent().getChildren().addAll(first, word, second);
    }

    @Override
    public CodeBlock copy() {
        AskAndWaitCodeBlock copy = new AskAndWaitCodeBlock(getMain());
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
        synchronized (SmeenConstants.ANSWER_LOCK) {
            Platform.runLater(() -> {
                sprite.say(text);
                getMain().getAnswerArea().show();
            });
            while (!context.isAnswerChanged()) {
                try {
                    Thread.sleep(SmeenConstants.EXECUTION_DELAY);
                } catch (InterruptedException e) {
                    return new Result(false, true);
                }
            }
            Platform.runLater(() -> {
                sprite.stopSaying();
                getMain().getAnswerArea().hide();
            });
            return new Result(false, false);
        }
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "AskAndWaitCodeBlock");
        result.put("word", word.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        word.importData((Map<String, Object>) data.get("word"));
    }
}
