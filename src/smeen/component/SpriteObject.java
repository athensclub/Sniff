package smeen.component;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import smeen.component.code.CodeArea;
import smeen.global.Fonts;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpriteObject extends Pane implements Savable {

    private Point2D oldPos;

    private ListProperty<CostumeOption> costumes;

    private CodeArea codeArea;

    private ImageView imageView;

    private StackPane speech;

    private Text speechText;

    public SpriteObject(MainView main) {
        codeArea = new CodeArea(main, this);

        costumes = new SimpleListProperty<>(FXCollections.observableArrayList());

        imageView = new ImageView();
        imageView.imageProperty().addListener((prop, oldv, newv) -> {
            if (newv != null) {
                imageView.setFitWidth(newv.getWidth());
                imageView.setFitHeight(newv.getHeight());
            }
        });
        imageView.setSmooth(true);

        speech = new StackPane();
        speech.setAlignment(Pos.CENTER);
        speech.layoutXProperty().bind(imageView.fitWidthProperty().add(20));
        speech.setLayoutY(-12);
        speech.setPadding(new Insets(4, 16, 4, 16));
        speech.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, new CornerRadii(12), BorderWidths.DEFAULT)));
        speech.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(12), Insets.EMPTY)));

        speechText = new Text();
        speechText.setFill(Color.BLACK);
        speechText.setFont(Fonts.SMALL_REGULAR_FONT);
        speech.getChildren().add(speechText);

        getChildren().add(imageView);

        addEventHandler(MouseEvent.DRAG_DETECTED, e -> {
            if (!isVisible() || main.getCodeExecutionHandler().runningProperty().get())
                return; // do not allow drag when hidden or state is running.

            startFullDrag();

            Point2D pos = localToScene(0, 0);

            oldPos = pos;
            relocate(pos.getX(), pos.getY());

            main.getStageArea().getStageContent().getChildren().remove(this);
            main.draggingProperty().set(this);
        });
    }

    /**
     * @return The old position when this sprite is first being dragged, relative to scene.
     */
    public Point2D getOldPos() {
        return oldPos;
    }

    public ListProperty<CostumeOption> costumesProperty() {
        return costumes;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    /**
     * Make the sprite say the given text.
     *
     * @param text
     */
    public void say(String text) {
        speechText.setText(text);
        getChildren().add(speech);
    }

    /**
     * Make the sprite hide its speech (if it was saying, otherwise no op).
     */
    public void stopSaying() {
        getChildren().remove(speech);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("x", getLayoutX());
        result.put("y", getLayoutY());
        result.put("width", imageView.getFitWidth());
        result.put("height", imageView.getFitHeight());
        result.put("rotation", imageView.getRotate());
        result.put("currentImage", Savable.exportImageData(imageView.getImage()));
        result.put("codeArea", codeArea.exportData());
        result.put("costumes", costumes.stream().map(node -> ((Savable) node).exportData()).toList());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        relocate((double) data.get("x"), (double) data.get("y"));
        // must read image before size so that the size binding does not matter
        imageView.setImage(Savable.importImageData((Map<String, Object>) data.get("currentImage")));
        imageView.setFitWidth((double) data.get("width"));
        imageView.setFitHeight((double) data.get("height"));
        imageView.setRotate((double) data.get("rotation"));
        codeArea.importData((Map<String, Object>) data.get("codeArea"));

        List<?> c = (List<?>) data.get("costumes");
        for (Object o : c) {
            if (o instanceof Map m) {
                CostumeOption costume = new CostumeOption();
                costume.importData(m);
                costumes.add(costume);
            }
        }
    }
}
