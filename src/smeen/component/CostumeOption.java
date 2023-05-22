package smeen.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import smeen.global.Fonts;
import smeen.util.Savable;

import java.util.HashMap;
import java.util.Map;

public class CostumeOption extends StackPane implements Savable {

    private ImageView imageView;

    private Label name;

    public CostumeOption() {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(4));
        content.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(8), BorderWidths.DEFAULT)));

        imageView = new ImageView();
        imageView.setFitWidth(64);
        imageView.setFitHeight(90);
        imageView.setPreserveRatio(true);

        name = new Label();
        name.setFont(Fonts.EXTRA_SMALL_REGULAR_FONT);

        content.getChildren().addAll(imageView, name);

        getChildren().addAll(content);
    }

    public CostumeOption(Image image, int order) {
        this();

        imageView.setImage(image);
        name.setText("ชุดที่ " + order);
    }

    public Image getImage() {
        return imageView.getImage();
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name.getText());
        result.put("image", Savable.exportImageData(imageView.getImage()));
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        name.setText((String) data.get("name"));
        imageView.setImage(Savable.importImageData((Map<String, Object>) data.get("image")));
    }
}
