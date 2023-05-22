package smeen.component;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import smeen.global.Fonts;
import smeen.views.MainView;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ResizeEditor extends HBox {
    private TextField xField;
    private TextField yField;
    private TextField widthField;
    private TextField heightField;

    private MainView main;

    private ChangeListener<? super Number> xListener;

    private ChangeListener<? super Number> yListener;

    private ChangeListener<? super Number> widthListener;

    private ChangeListener<? super Number> heightListener;

    public ResizeEditor(MainView main) {
        this.main = main;

        setPrefSize(300, 30);
        disableProperty().bind(main.getSpriteEditor().selectedSpriteProperty().isNull());

        setBackground(Background.fill(Color.WHITE));
        Label x = new Label("X: ");
        Label y = new Label("Y: ");
        Label width = new Label("Width: ");
        Label height = new Label("Height: ");
        x.setFont(Fonts.SMALL_REGULAR_FONT);
        y.setFont(Fonts.SMALL_REGULAR_FONT);
        width.setFont(Fonts.SMALL_REGULAR_FONT);
        height.setFont(Fonts.SMALL_REGULAR_FONT);

        xField = new TextField();
        yField = new TextField();
        widthField = new TextField();
        heightField = new TextField();
        xField.setPrefWidth(40);
        yField.setPrefWidth(40);
        widthField.setPrefWidth(40);
        heightField.setPrefWidth(40);

        // bind the selected sprite values to the text field.
        main.getSpriteEditor().selectedSpriteProperty().addListener((prop, oldv, newv) -> {
            if (oldv != null) {
                unbindAll(oldv.getSprite());
            }

            if (newv != null) {
                bindAll(newv.getSprite());
            }else{
                // clear text fields on selected (cant run this on disable listener because
                // the values might still be bind before clearing text field).
                xField.clear();
                yField.clear();
                widthField.clear();
                heightField.clear();
            }
        });

        // when the selected sprite is being dragged, it will be positioned relative to scene
        // instead of relative to stage content, which is not useful so we will unbind while
        // the selected sprite is being dragged.
        main.draggingProperty().addListener((prop, oldv, newv) -> {
            SpriteOption option = main.getSpriteEditor().selectedSpriteProperty().get();
            if (option == null)
                return;

            SpriteObject sprite = option.getSprite();
            if (Objects.equals(sprite, oldv)) {
                // dragging changed from selected object to another object, which means
                // the selected object just finished dragging, bind back the values
                bindAll(sprite);
            }

            if (Objects.equals(sprite, newv)) {
                // the selected sprite is just being dragged, unbind the values
                unbindAll(sprite);

                // also, the sprite might bind in the first iteration to the position
                // relative to scene, so we need to fix it.
                Point2D pos = main.getStageArea().getStageContent().sceneToLocal(sprite.getOldPos());
                xField.setText(Double.toString(pos.getX()));
                yField.setText(Double.toString(pos.getY()));
            }
        });

        getChildren().addAll(x, xField, y, yField, width, widthField, height, heightField);
        setAlignment(Pos.CENTER_LEFT);
        setSpacing(10);
        setPadding(new Insets(0, 0, 0, 10));
    }

    private void unbindAll(SpriteObject sprite) {
        xField.textProperty().unbindBidirectional(sprite.layoutXProperty());
        yField.textProperty().unbindBidirectional(sprite.layoutYProperty());
        widthField.textProperty().unbindBidirectional(sprite.getImageView().fitWidthProperty());
        heightField.textProperty().unbindBidirectional(sprite.getImageView().fitHeightProperty());
    }

    private void bindAll(SpriteObject sprite) {
        xField.textProperty().bindBidirectional(sprite.layoutXProperty(), new NumberStringConverter());
        yField.textProperty().bindBidirectional(sprite.layoutYProperty(), new NumberStringConverter());
        widthField.textProperty().bindBidirectional(sprite.getImageView().fitWidthProperty(), new NumberStringConverter());
        heightField.textProperty().bindBidirectional(sprite.getImageView().fitHeightProperty(), new NumberStringConverter());
    }

    public TextField getXField() {
        return xField;
    }

    public TextField getYField() {
        return yField;
    }

    public TextField getWidthField() {
        return widthField;
    }

    public TextField getHeightField() {
        return heightField;
    }
}