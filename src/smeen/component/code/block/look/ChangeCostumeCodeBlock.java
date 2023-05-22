package smeen.component.code.block.look;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import smeen.component.SpriteObject;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockInputSlot;
import smeen.global.Fonts;
import smeen.global.SmeenConstants.Type;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class ChangeCostumeCodeBlock extends CodeBlock {

    private CodeBlockInputSlot<Double> posCostume;

    public ChangeCostumeCodeBlock(MainView main) {
        super(main);
        Label first = new Label("เปลี่ยชุดเป็นรูปแบบที่");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);

        posCostume = new CodeBlockInputSlot<>(main, Type.Double);

        getContent().setBackground(Background.fill(Color.MEDIUMORCHID));

        getContent().getChildren().addAll(first, posCostume);
    }

    @Override
    public CodeBlock copy() {
        ChangeCostumeCodeBlock copy = new ChangeCostumeCodeBlock(getMain());
        copy.posCostume.getTextField().setText(posCostume.getTextField().getText());
        return copy;
    }

    @Override
    public Result execute(SmeenContext context) {
        if (getCodeArea() == null)
            return new Executable.Result(false, false);

        SpriteObject sprite = getCodeArea().getSprite();
        if (sprite == null || Objects.equals(getMain().draggingProperty().get(), sprite))
            return new Executable.Result(false, false); // no op on movement code that is not in sprite or the sprite is being dragged.

        int order = posCostume.getValue(context).intValue();
        if (order >= 1 && order <= sprite.costumesProperty().size()) {
            Platform.runLater(() -> sprite.getImageView().setImage(sprite.costumesProperty().get(order - 1).getImage()));
        }
        return new Result(false, false);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "ChangeCostumeCodeBlock");
        result.put("posCostume", posCostume.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        posCostume.importData((Map<String, Object>) data.get("posCostume"));
    }
}
