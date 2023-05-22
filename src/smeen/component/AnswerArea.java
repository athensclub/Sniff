package smeen.component;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import smeen.global.Fonts;
import smeen.views.MainView;

public class AnswerArea extends HBox {
	private final TextField answerField;
	private final Button confirmButton;
	public AnswerArea(MainView main) {
		confirmButton = new Button("ตอบ");
		confirmButton.setFont(Fonts.SMALL_REGULAR_FONT);
		confirmButton.setMinWidth(50);
		confirmButton.addEventHandler(ActionEvent.ACTION, e -> {
			main.getCodeExecutionHandler().getContext().currentAnswerProperty().set(getAnswerField().getText());
		});

		answerField = new TextField();
		answerField.prefWidthProperty().bind(widthProperty().subtract(confirmButton.widthProperty()));
		answerField.setFont(Fonts.EXTRA_SMALL_REGULAR_FONT);
		
		getChildren().addAll(answerField , confirmButton);
		setAlignment(Pos.CENTER);
		setSpacing(5);
		setPadding(new Insets(0,0,0,5));
		maxWidthProperty().bind(main.getStageArea().getStageContent().widthProperty());
		setMaxHeight(30);
		setBackground(Background.fill(Color.LIGHTGRAY));

		disableProperty().bind(visibleProperty().not());
		hide();
	}
	
	public void show() {
		setVisible(true);
		answerField.setText("");
	}
	
	public void hide() {
		setVisible(false);
	}
	
	public TextField getAnswerField() {
		return answerField;
	}
	
	public Button getConfirmButton() {
		return confirmButton;
	}
}
