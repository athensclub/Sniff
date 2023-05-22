package smeen.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import smeen.global.Fonts;
import smeen.global.SmeenConstants;
import smeen.global.SmeenFileChooser;
import smeen.global.SmeenSVGs;
import smeen.util.Resettable;
import smeen.util.Savable;
import smeen.views.MainView;

public class SpriteEditor extends VBox implements Savable, Resettable {
    private MainView main;
    private ScrollPane spritePane;

    private FlowPane spritePaneContent;
    private ScrollPane costumePane;

    private FlowPane costumePaneContent;
    private StackPane selectedPane;

    private ObjectProperty<SpriteOption> selectedSprite;

    private int runningSpriteOrder;

    public SpriteEditor(MainView main) {
        this.main = main;

        runningSpriteOrder = 0;

        spritePane = new ScrollPane();
        spritePane.setPrefSize(400, 200);
        spritePane.setFitToWidth(true);
        spritePane.setFitToHeight(true);

        spritePaneContent = new FlowPane();
        spritePaneContent.setPadding(new Insets(12));
        spritePaneContent.setVgap(8);
        spritePaneContent.setHgap(8);
        spritePaneContent.setBackground(Background.fill(Color.WHITE));

        spritePane.setContent(spritePaneContent);

        costumePane = new ScrollPane();
        costumePane.setPrefSize(400, 200);
        costumePane.setFitToWidth(true);
        costumePane.setFitToHeight(true);

        costumePaneContent = new FlowPane();
        costumePaneContent.setPadding(new Insets(12));
        costumePaneContent.setVgap(8);
        costumePaneContent.setHgap(8);
        costumePaneContent.setBackground(Background.fill(Color.WHITE));

        costumePane.setContent(costumePaneContent);

        selectedSprite = new SimpleObjectProperty<>();
        selectedSprite.addListener((obs, oldv, newv) -> {
            for (Node n : spritePaneContent.getChildren())
                if (n instanceof SpriteOption o)
                    o.hideSelectedHighlight();

            if (newv != null) {
                newv.showSelectedHighlight();
                costumePaneContent.getChildren().clear();
                costumePaneContent.getChildren().addAll(newv.getSprite().costumesProperty());
                main.getSceneEditor().selectedSceneProperty().set(null);
                main.codeAreaProperty().set(newv.getSprite().getCodeArea());
            } else {
                setSelectedPane("Sprite");
            }
        });

        HBox selector = new HBox();
        Text spriteText = new Text("ตัวละคร");
        spriteText.setFont(Fonts.SMALL_REGULAR_FONT);
        Text costumeText = new Text("แต่งตัว");
        costumeText.setFont(Fonts.SMALL_REGULAR_FONT);

        VBox spriteTextBox = new VBox();
        spriteTextBox.getChildren().add(spriteText);
        spriteTextBox.setPadding(new Insets(8, 10, 5, 10));
        spriteTextBox.setBackground(Background.fill(Color.WHITE));
        VBox costumeTextBox = new VBox();
        costumeTextBox.getChildren().add(costumeText);
        costumeTextBox.setPadding(new Insets(8, 10, 5, 10));
        costumeTextBox.setBackground(Background.fill(Color.LIGHTGRAY));
        // custom function
        setOnClick(spriteTextBox, "Sprite");
        setOnClick(costumeTextBox, "Costume");

        selector.getChildren().addAll(spriteTextBox, costumeTextBox);

        Button spriteButton = createAddSpriteButton();

        selectedPane = new StackPane();
        selectedPane.getChildren().addAll(spritePane, spriteButton);

        getChildren().addAll(selector, selectedPane);
        StackPane.setAlignment(spriteButton, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(spriteButton, new Insets(5, 5, 5, 5));
    }

    public ScrollPane getSpritePane() {
        return spritePane;
    }

    public FlowPane getSpriteFlowPane() {
        return spritePaneContent;
    }

    public ScrollPane getCostumePane() {
        return costumePane;
    }

    public ScrollPane selectedPaneProperty() {
        return (ScrollPane) selectedPane.getChildren().get(0);
    }

    public int getRunningOrder() {
        return runningSpriteOrder;
    }

    public void setRunningOrder(int value) {
        if (value >= 0) runningSpriteOrder = value;
    }

    public Button createAddSpriteButton() {
        SVGPath plusPath = new SVGPath();
        plusPath.setContent(SmeenSVGs.PLUS_PATH);
        Region plusicon = new Region();
        plusicon.setShape(plusPath);
        plusicon.setBackground(Background.fill(Color.BLACK));
        plusicon.setMaxSize(10, 10);
        plusicon.setPrefSize(10, 10);
        plusicon.setTranslateX(10);
        plusicon.setTranslateY(10);

        SVGPath spriteIconPath = new SVGPath();
        spriteIconPath.setContent(SmeenSVGs.SPRITE_PATH);
        Region spriteIcon = new Region();
        spriteIcon.setShape(spriteIconPath);
        spriteIcon.setBackground(Background.fill(Color.BLACK));
        spriteIcon.setMaxSize(20, 20);
        spriteIcon.setPrefSize(20, 20);


        StackPane groupIcon = new StackPane();
        groupIcon.getChildren().addAll(spriteIcon, plusicon);
        groupIcon.setBackground(null);

        Button addSpriteButton = new Button();
        addSpriteButton.setGraphic(groupIcon);
        addSpriteButton.setPrefSize(32, 32);
        addSpriteButton.setStyle(
                "-fx-background-radius: 32px;" +
                        "-fx-background-color: #00ffff;"
        );
        addSpriteButton.setPadding(new Insets(0, 0, 0, 0));

        addSpriteButton.setCursor(Cursor.HAND);

        addSpriteButton.setOnMouseClicked(e -> {
            File selectedFile = SmeenFileChooser.chooseImage();

            if (selectedFile != null) {
                try {
                    addSprite(new Image(new FileInputStream(selectedFile)));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return addSpriteButton;
    }

    /**
     * Add the given image as a sprite option and add the sprite to the stage area.
     *
     * @param image
     */
    public void addSprite(Image image) {
        // add sprite option to the editor
        SpriteOption spriteOption = new SpriteOption(image, ++runningSpriteOrder, main);
        spritePaneContent.getChildren().add(spriteOption);
        // also add sprite to stage
        main.getStageArea().getStageContent().getChildren().add(spriteOption.getSprite());
    }

    /**
     * Run the given function for all sprite option in this editor.
     *
     * @param func
     */
    public void forEachSpriteOption(Consumer<SpriteOption> func) {
        for (Node child : spritePaneContent.getChildren())
            if (child instanceof SpriteOption so)
                func.accept(so);
    }

    public int getSpriteCount() {
        return spritePaneContent.getChildren().size();
    }

    public SpriteOption getSpriteOption(int order) {
        return (SpriteOption) spritePaneContent.getChildren().get(order - 1);
    }

    /**
     * The selected sprite. setting this property will automatically highlight the selected sprite
     * and update necessary data.
     *
     * @return
     */
    public ObjectProperty<SpriteOption> selectedSpriteProperty() {
        return selectedSprite;
    }

    public Button createAddCostumeButton() {
        SVGPath plusPath = new SVGPath();
        plusPath.setContent(SmeenSVGs.PLUS_PATH);
        Region plusicon = new Region();
        plusicon.setShape(plusPath);
        plusicon.setBackground(Background.fill(Color.BLACK));
        plusicon.setMaxSize(10, 10);
        plusicon.setPrefSize(10, 10);
        plusicon.setTranslateX(10);
        plusicon.setTranslateY(10);

        SVGPath costumeIconPath = new SVGPath();
        costumeIconPath.setContent(SmeenSVGs.COSTUME_PATH);
        Region costumeIcon = new Region();
        costumeIcon.setShape(costumeIconPath);
        costumeIcon.setBackground(Background.fill(Color.BLACK));
        costumeIcon.setMaxSize(20, 20);
        costumeIcon.setPrefSize(20, 20);

        StackPane groupIcon = new StackPane();
        groupIcon.getChildren().addAll(costumeIcon, plusicon);
        groupIcon.setBackground(null);

        Button addCostumeButton = new Button();
        addCostumeButton.setGraphic(groupIcon);
        addCostumeButton.setPrefSize(32, 32);
        addCostumeButton.setStyle(
                "-fx-background-radius: 32px;" +
                        "-fx-background-color: #00ffff;"
        );
        addCostumeButton.setPadding(new Insets(0, 0, 0, 0));

        addCostumeButton.setOnMouseEntered(e -> {
            setCursor(Cursor.HAND);
        });
        addCostumeButton.setOnMouseExited(e -> {
            setCursor(Cursor.DEFAULT);
        });
        addCostumeButton.setOnMouseClicked(e -> {
            File selectedFile = SmeenFileChooser.chooseImage();

            if (selectedFile != null) {
                try {
                    int order = selectedSprite.get().getSprite().costumesProperty().getSize();
                    CostumeOption costume = new CostumeOption(new Image(new FileInputStream(selectedFile)), order + 1);
                    costumePaneContent.getChildren().add(costume);
                    selectedSprite.get().getSprite().costumesProperty().add(costume);
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        return addCostumeButton;
    }

    private void setSelectedPane(String type) {
        if (Objects.equals(type, "Sprite")) {
            if (selectedPaneProperty().equals(getCostumePane())) {
                selectedPane.getChildren().set(0, spritePane);
                VBox spriteBox = (VBox) ((HBox) getChildren().get(0)).getChildren().get(0);
                VBox costumeBox = ((VBox) ((HBox) getChildren().get(0)).getChildren().get(1));
                spriteBox.setBackground(Background.fill(Color.WHITE));
                costumeBox.setBackground(Background.fill(Color.LIGHTGRAY));
                Button spriteButton = createAddSpriteButton();
                selectedPane.getChildren().set(1, spriteButton);
                StackPane.setAlignment(spriteButton, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(spriteButton, new Insets(5, 5, 5, 5));
            }
        } else if (Objects.equals(type, "Costume")) {
            if (selectedPaneProperty().equals(getSpritePane())) {
                selectedPane.getChildren().set(0, costumePane);
                VBox spriteBox = (VBox) ((HBox) getChildren().get(0)).getChildren().get(0);
                VBox costumeBox = ((VBox) ((HBox) getChildren().get(0)).getChildren().get(1));
                spriteBox.setBackground(Background.fill(Color.LIGHTGRAY));
                costumeBox.setBackground(Background.fill(Color.WHITE));
                Button costumeButton = createAddCostumeButton();
                selectedPane.getChildren().set(1, costumeButton);
                StackPane.setAlignment(costumeButton, Pos.BOTTOM_RIGHT);
                StackPane.setMargin(costumeButton, new Insets(5, 5, 5, 5));
            }
        }
    }

    public void setOnClick(VBox selected, String type) {
        if (Objects.equals(type, "Sprite")) {
            selected.setCursor(Cursor.HAND);
            selected.setOnMouseClicked(e -> setSelectedPane(type));
        } else if (Objects.equals(type, "Costume")) {
            selected.cursorProperty().bind(Bindings.when(selectedSprite.isNotNull()).then(Cursor.HAND).otherwise(Cursor.DEFAULT));
            selected.visibleProperty().bind(selectedSprite.isNotNull());
            // set current pane to costumePane
            selected.setOnMouseClicked(e -> {
                // can not edit costume when sprite is not selected
                if (selectedSpriteProperty().get() == null)
                    return;
                setSelectedPane(type);
            });
        }

    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("runningOrder", runningSpriteOrder);
        result.put("sprites", spritePaneContent.getChildren().stream().map(node -> ((Savable) node).exportData()).toList());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        clearData();

        runningSpriteOrder = (int) data.get("runningOrder");

        List<?> sprites = (List<?>) data.get("sprites");
        for (Object o : sprites) {
            if (o instanceof Map m) {
                SpriteOption option = new SpriteOption(main);
                option.importData(m);
                spritePaneContent.getChildren().add(option);
            }
        }
    }

    @Override
    public void clearData() {
        spritePaneContent.getChildren().clear();
        selectedSprite.set(null);
        runningSpriteOrder = 0;
    }

    @Override
    public void reset() {
        clearData();

        SpriteOption spriteOption = new SpriteOption(SmeenConstants.DOG_SPRITE_1, ++runningSpriteOrder, main);
        spriteOption.getSprite().costumesProperty().add(new CostumeOption(SmeenConstants.DOG_SPRITE_2, 2));
        // add sprite option to the editor
        spritePaneContent.getChildren().add(spriteOption);
        // also add sprite to stage
        main.getStageArea().getStageContent().getChildren().add(spriteOption.getSprite());
    }
}
