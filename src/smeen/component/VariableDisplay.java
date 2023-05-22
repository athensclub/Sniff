package smeen.component;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import smeen.global.Fonts;
import smeen.views.MainView;

/**
 * A node to display variable value.
 */
public class VariableDisplay extends HBox {

    public VariableDisplay(MainView main, String name) {
        setPadding(new Insets(4, 12, 4, 12));
        setBackground(Background.fill(Color.LIGHTGRAY));
        setSpacing(8);

        Label nameLabel = new Label(name + ": ");
        nameLabel.setFont(Fonts.SMALL_REGULAR_FONT);

        Text value = new Text();
        value.setFill(Color.BLACK);
        value.setFont(Fonts.SMALL_REGULAR_FONT);
        value.textProperty().bind(main.getCodeExecutionHandler().getContext().getVariableProperty(name).asString());

        getChildren().addAll(nameLabel, value);
    }

}
