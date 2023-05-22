package smeen.component;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import smeen.global.Fonts;
import smeen.global.SmeenComponent;
import smeen.global.SmeenSVGs;
import smeen.util.Savable;
import smeen.views.MainView;

import java.util.HashMap;
import java.util.Map;

import static smeen.global.SmeenConstants.DEFAULT_BORDER;
import static smeen.global.SmeenConstants.HIGHLIGHTED_BORDER;

public class SpriteOption extends StackPane implements Savable {

    private VBox content;

    private SpriteObject sprite;

    private Label name;

    private ImageView imageView;

    public SpriteOption(MainView main) {
        setCursor(Cursor.HAND);

        content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(4));
        content.setBorder(DEFAULT_BORDER);

        imageView = new ImageView();
        imageView.setFitWidth(64);
        imageView.setFitHeight(90);
        imageView.setPreserveRatio(true);

        name = new Label();
        name.setFont(Fonts.EXTRA_SMALL_REGULAR_FONT);

        content.getChildren().addAll(imageView, name);

        getChildren().addAll(content);

        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            main.getSpriteEditor().selectedSpriteProperty().set(this);
        });

        sprite = new SpriteObject(main);
        
        Button removeButton = SmeenComponent.createRemoveButton(20);
        
        getChildren().add(removeButton);
        setAlignment(removeButton, Pos.TOP_RIGHT);
        
        // maybe a bit curse
        SpriteOption currentSpriteOption = this;
        removeButton.setOnAction(e -> {
        	for (Node node : main.getSpriteEditor().getSpriteFlowPane().getChildren()) {
        		if (node instanceof SpriteOption spriteOption && spriteOption.equals(this)) {
        			
        			// remove sprite from stageArea
        			main.getStageArea().getStageContent().getChildren().remove(spriteOption.getSprite());
        			// remove spriteOption from spriteEditor
        			main.getSpriteEditor().getSpriteFlowPane().getChildren().remove(spriteOption);
        			
        			main.getSpriteEditor().setRunningOrder(main.getSpriteEditor().getRunningOrder() - 1);
        			
        			// set selected to first scene
        			main.getSpriteEditor().selectedSpriteProperty().set(null);
        			main.getSceneEditor().selectedSceneProperty().set(main.getSceneEditor().getScene(1));
        			break;
        		} 
        	}
        	
        });
        
        
        main.getSpriteEditor().getSpriteFlowPane().getChildren().addListener(new InvalidationListener() {
        	
			@Override
			public void invalidated(Observable arg0) {
				int pos = main.getSpriteEditor().getSpriteFlowPane().getChildren().indexOf(currentSpriteOption);
				name.setText("ตัวละครที่ " + (pos+1));
			}
		});
    }

    public SpriteOption(Image image, int order, MainView main) {
        this(main);

        imageView.setImage(image);
        name.setText("ตัวละครที่ " + order);

        sprite.getImageView().setImage(image);
        sprite.costumesProperty().add(new CostumeOption(image, 1));
    }

    public void showSelectedHighlight() {
        content.setBorder(HIGHLIGHTED_BORDER);
    }

    public void hideSelectedHighlight() {
        content.setBorder(DEFAULT_BORDER);
    }

    public SpriteObject getSprite() {
        return sprite;
    }
    

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", name.getText());
        result.put("image", Savable.exportImageData(imageView.getImage()));
        result.put("sprite", sprite.exportData());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        name.setText((String) data.get("name"));
        imageView.setImage(Savable.importImageData((Map<String, Object>) data.get("image")));
        sprite.importData((Map<String, Object>) data.get("sprite"));
    }
}
