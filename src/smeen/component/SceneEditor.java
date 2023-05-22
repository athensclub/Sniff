package smeen.component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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

public class SceneEditor extends VBox implements Resettable, Savable {
	private ScrollPane scenePane;

    private VBox content;

    private ObjectProperty<SceneOption> selectedScene;

    private int runningOrder;

    private MainView main;
    public SceneEditor(MainView main){
        this.main = main;

        runningOrder = 0;

        selectedScene = new SimpleObjectProperty<>();
        selectedScene.addListener((obs, oldv, newv) -> {
            for (Node n : content.getChildren())
                if (n instanceof SceneOption o)
                    o.hideSelectedHighlight();

            if (newv != null) {
                newv.showSelectedHighlight();
                main.getSpriteEditor().selectedSpriteProperty().set(null);
                main.codeAreaProperty().set(newv.getCodeArea());
                // probably should do a better design to achieve O(1) but oh well.
                main.getStageArea().shownSceneProperty().set(content.getChildren().indexOf(newv)+1);
            }
        });

    	scenePane = new ScrollPane();
    	scenePane.setBackground(Background.fill(Color.WHITE));
    	scenePane.setPrefSize(170,200);
    	scenePane.setStyle(
    			"-fx-border-color: #FFFFFF;"
    			+ "-fx-background: #FFFFFF;");
        scenePane.setFitToWidth(true);
        scenePane.setFitToHeight(true);

        content = new VBox();

        content.setPadding(new Insets(8));
        content.setSpacing(8);
        scenePane.setContent(content);
    	
        Button addSceneButton = createAddSceneButton(main);
        
        Text sceneText = new Text("ฉากแสดง");
        sceneText.setFont(Fonts.SMALL_REGULAR_FONT);
        HBox sceneTextBox = new HBox();
        sceneTextBox.getChildren().add(sceneText);
        sceneTextBox.setPadding(new Insets(8,10,5,10));
        sceneTextBox.setBackground(Background.fill(Color.WHITE));
        sceneTextBox.setMaxWidth(60);
        
//        VBox scenePaneWrapper = new VBox();
//        scenePaneWrapper.getChildren().addAll(sceneTextBox , scenePane);
        
        StackPane scenePaneWrapper = new StackPane();
        scenePaneWrapper.getChildren().addAll(scenePane , addSceneButton);
        
    	getChildren().addAll(sceneTextBox , scenePaneWrapper);
    	StackPane.setAlignment(addSceneButton , Pos.BOTTOM_RIGHT);
    	StackPane.setMargin(addSceneButton, new Insets(5,5,5,5));
    }
    
    public ScrollPane getScenePane() {
    	return scenePane;
    }
    
    public Button createAddSceneButton(MainView main) {
    	SVGPath plusPath = new SVGPath();
        plusPath.setContent(SmeenSVGs.PLUS_PATH);
        Region plusicon = new Region();
        plusicon.setShape(plusPath);
        plusicon.setBackground(Background.fill(Color.BLACK));
        plusicon.setMaxSize(10,10);
        plusicon.setPrefSize(10, 10);
        plusicon.setTranslateX(10);
        plusicon.setTranslateY(10);
        
        SVGPath imgIconPath = new SVGPath();
        imgIconPath.setContent(SmeenSVGs.IMG_ICON_PATH);
        Region imgIcon = new Region();
        imgIcon.setShape(imgIconPath);
        imgIcon.setBackground(Background.fill(Color.BLACK));
        imgIcon.setMaxSize(18,18);
        imgIcon.setPrefSize(18, 18);
       
        
        StackPane groupIcon = new StackPane();
        groupIcon.getChildren().addAll(imgIcon , plusicon);
        groupIcon.setBackground(null);
        
        Button addSceneButton = new Button();
        addSceneButton.setGraphic(groupIcon);
        addSceneButton.setPrefSize(32, 32);
        addSceneButton.setStyle(
        		"-fx-background-radius: 32px;" + 
        		"-fx-background-color: #00ffff;"
        		);
        addSceneButton.setPadding(new Insets(0,0,0,0));
    	addSceneButton.setOnMouseEntered(e -> {
    		setCursor(Cursor.HAND);
    	});
    	addSceneButton.setOnMouseExited(e -> {
    		setCursor(Cursor.DEFAULT);
    	});
    	addSceneButton.setOnMouseClicked(e -> {
    		File selectedFile = SmeenFileChooser.chooseImage();
    		if (selectedFile != null) {
                try {
                    addScene(new Image(new FileInputStream(selectedFile)));
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
    	});
    	return addSceneButton;
    }

    public void addScene(Image image){
        SceneOption scene = new SceneOption(image, ++runningOrder, main);
        content.getChildren().add(scene);
    }

    /**
     * Get the scene with the given order number
     * @param order
     * @return
     */
    public SceneOption getScene(int order){
        return (SceneOption) content.getChildren().get(order-1);
    }

    public int getSceneCount(){
        return content.getChildren().size();
    }

    public ObjectProperty<SceneOption> selectedSceneProperty() {
        return selectedScene;
    }

    @Override
    public void clearData() {
        selectedScene.set(null);
        content.getChildren().clear();
        runningOrder = 0;
    }

    @Override
    public void reset() {
        clearData();

        addScene(SmeenConstants.DEFAULT_BACKGROUND);
    }

    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("runningOrder",runningOrder);
        result.put("scenes",content.getChildren().stream().map(node -> ((Savable) node).exportData()).toList());
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
        clearData();

        runningOrder = (int) data.get("runningOrder");

        List<?> scenes = (List<?>) data.get("scenes");
        for(Object o : scenes){
            if(o instanceof  Map m){
                SceneOption scene = new SceneOption(main);
                scene.importData(m);
                content.getChildren().add(scene);
            }
        }
    }
}
