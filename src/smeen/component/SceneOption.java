package smeen.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import smeen.component.code.CodeArea;
import smeen.global.Fonts;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

import static smeen.global.SmeenConstants.DEFAULT_BORDER;
import static smeen.global.SmeenConstants.HIGHLIGHTED_BORDER;

public class SceneOption extends StackPane implements Savable {

    private CodeArea codeArea;

    private VBox content;

    private ImageView imageView;

    private Label name;

    public SceneOption(MainView main) {
        setCursor(Cursor.HAND);

        codeArea = new CodeArea(main, null);

        content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(8));
        content.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(8), BorderWidths.DEFAULT)));

        imageView = new ImageView();
        imageView.setFitWidth(110);
        imageView.setFitHeight(78);
        imageView.setPreserveRatio(true);

        name = new Label();
        name.setFont(Fonts.EXTRA_SMALL_REGULAR_FONT);

        content.getChildren().addAll(imageView, name);

        getChildren().addAll(content);

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            main.getSceneEditor().selectedSceneProperty().set(this);
        });
    }

    public SceneOption(Image image, int order, MainView main) {
        this(main);

        imageView.setImage(image);
        name.setText("ฉากที่ " + order);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(110);
        imageView.setFitHeight(78);
        imageView.setPreserveRatio(true);
    }

    public Image getImage() {
        return imageView.getImage();
    }

    public void showSelectedHighlight() {
        content.setBorder(HIGHLIGHTED_BORDER);
    }

    public void hideSelectedHighlight() {
        content.setBorder(DEFAULT_BORDER);
    }

    public CodeArea getCodeArea() {
        return codeArea;
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("codeArea", codeArea.exportData());
        result.put("image", Savable.exportImageData(imageView.getImage()));
        result.put("name", name.getText());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        codeArea.importData((Map<String, Object>) data.get("codeArea"));
        imageView.setImage(Savable.importImageData((Map<String, Object>) data.get("image")));
        name.setText((String) data.get("name"));
    }
}
