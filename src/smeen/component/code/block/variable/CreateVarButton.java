package smeen.component.code.block.variable;

import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.PopupWindow.AnchorLocation;
import smeen.component.code.CodeSelector;
import smeen.global.Fonts;
import smeen.global.SmeenSVGs;
import smeen.global.SmeenConstants.Type;
import smeen.views.MainView;

public class CreateVarButton extends Button {
	
	private MainView main;
	private Popup addVarPopup;
	private TextField nameTextField;
	private CodeSelector codeSelector;
	public CreateVarButton(MainView main, CodeSelector codeSelector) {
		this.main = main;
		this.codeSelector = codeSelector;
		nameTextField = new TextField();
        nameTextField.setPrefHeight(30);
        nameTextField.setFont(Fonts.SMALL_REGULAR_FONT);
        this.addVarPopup = createPopup();
		setText("เพิ่มตัวแปร");
		setFont(Fonts.SMALL_REGULAR_FONT);
        setOnAction(e -> {
        	if (!addVarPopup.isShowing()) {
        		addVarPopup.show(main.getStage());
        		nameTextField.setText("");
        	}
        });
	}
	
	public Popup createPopup() {
    	Popup addVarPopup = new Popup();
        addVarPopup.setAutoHide(true);
        VBox addVarPane = new VBox();
        addVarPane.setBackground(Background.fill(Color.WHITE));
        
        Label textHead = new Label("สร้างตัวแปรใหม่");
        textHead.setFont(Fonts.BASIC_BOLD_FONT);
        VBox textWrap = new VBox();
        textWrap.getChildren().add(textHead);
        textWrap.setBackground(Background.fill(Color.PALEVIOLETRED));
        textWrap.setPrefSize(300, 50);
        textWrap.setAlignment(Pos.CENTER);
        
        Button closeButton = new Button();
        SVGPath closePath = new SVGPath();
        closePath.setContent(SmeenSVGs.CLOSE_PATH);
        Region expandRegion = new Region();
        expandRegion.setShape(closePath);
        expandRegion.setBackground(Background.fill(Color.BLACK));
        expandRegion.setMaxSize(20, 20);
        expandRegion.setPrefSize(20, 20);
        closeButton.setGraphic(expandRegion);
        closeButton.setPrefSize(32, 32);
        closeButton.setStyle("-fx-background-radius: 32px;");
        closeButton.setBackground(Background.fill(Color.TRANSPARENT));
        closeButton.setPadding(new Insets(2));
        closeButton.setOnAction(e -> {
        	if (addVarPopup.isShowing()) addVarPopup.hide();
        });
        
        StackPane header = new StackPane();
        header.getChildren().addAll(textWrap , closeButton);
        StackPane.setAlignment(closeButton, Pos.TOP_RIGHT);
        StackPane.setMargin(closeButton, new Insets(10));
        
        VBox componentWrap = new VBox();
        
        Label textBody = new Label("ชื่อตัวแปร :");
        textBody.setFont(Fonts.BASIC_BOLD_FONT);
        VBox addName = new VBox();
        addName.getChildren().addAll(textBody , nameTextField);
        
        
        HBox chooseVarType = new HBox();
        Button varString = createButton("เพิ่มเป็น String", Type.String);
        Button varDouble = createButton("เพิ่มเป็น Double", Type.Double);
        Button varBoolean = createButton("เพิ่มเป็น Boolean", Type.Boolean);
        
        
        chooseVarType.getChildren().addAll(varString , varDouble , varBoolean);
        chooseVarType.setAlignment(Pos.CENTER);
        chooseVarType.setSpacing(5);
        
        componentWrap.getChildren().addAll(addName , chooseVarType);
        componentWrap.setSpacing(5);
        componentWrap.setPadding(new Insets(10));
        
        
        
        addVarPane.getChildren().addAll(header, componentWrap);
        addVarPane.setBackground(Background.fill(Color.SILVER));
        
        addVarPopup.getContent().addAll(addVarPane);
        
        
        return addVarPopup;
    }
	

	
	private Region createRegion(String SVGPath,double sizeIcon) {
		SVGPath path = new SVGPath();
    	path.setContent(SVGPath);
    	Region icon = new Region();
    	icon.setShape(path);
    	icon.setMaxSize(sizeIcon, sizeIcon);
    	icon.setMinSize(sizeIcon, sizeIcon);
    	icon.setPadding(new Insets(0));
    	icon.setBackground(Background.fill(Color.TRANSPARENT));
    	return icon;
	}
	
	private Button createButton(String buttonName, Type type) {
		Button button = new Button(buttonName);
        button.setFont(Fonts.EXTRA_SMALL_REGULAR_FONT);
        button.setOnAction(e -> {
        	boolean wasCreated = false;
        	String creatingName = nameTextField.getText();
        	for (String varName : main.getCodeSelector().createdVarNameProperty()) {
        		if (varName.equals(creatingName)) {
        			wasCreated = true;
        			break;
        		}
        	}
        	if (!creatingName.equals("") && !wasCreated) {
        		codeSelector.addVar(nameTextField.getText() , type);
        		addVarPopup.hide();
        	}
        });
        return button;
	}
}
