package smeen.global;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.SVGPath;

public class SmeenComponent {

	/**
	 * Create a remove button with the given size.
	 * @param sizeIcon the size of the button.
	 * @return
	 */
	public static Button createRemoveButton(int sizeIcon) {
		Region delIcon1 = createRegion(SmeenSVGs.DELETE_PATH1, sizeIcon);
    	delIcon1.setBackground(Background.fill(Color.RED));
    	Region delIcon2 = createRegion(SmeenSVGs.DELETE_PATH2, sizeIcon * 16.66 / 40);
    	delIcon2.setBackground(Background.fill(Color.WHITE));
    	Region delIcon3 = createRegion(SmeenSVGs.DELETE_PATH3, sizeIcon * 16.66 / 40);
    	delIcon3.setBackground(Background.fill(Color.WHITE));
    	StackPane delIcon = new StackPane();
    	delIcon.getChildren().addAll(delIcon1 , delIcon2 , delIcon3);
    	delIcon.setAlignment(Pos.CENTER);
    	delIcon.setCursor(Cursor.HAND);
    	
    	Button removeButton = new Button();
    	removeButton.setGraphic(delIcon);
    	removeButton.setPrefSize(sizeIcon, sizeIcon);
    	removeButton.setStyle(
                "-fx-background-radius: "+ sizeIcon + "px;"
        );
    	removeButton.setPadding(new Insets(0, 0, 0, 0));
        
    	return removeButton;
	}

	/**
	 * Create a region with the given icon path and size.
	 * @param SVGPath
	 * @param sizeIcon
	 * @return
	 */
	public static Region createRegion(String SVGPath,double sizeIcon) {
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

	/**
	 * Create half a circle region.
	 * @return
	 */
	public static Region createHead() {
		SVGPath path = new SVGPath();
		path.setContent(SmeenSVGs.HALF_CIRCLE);
		
		Region head = new Region();
		head.setShape(path);
		head.setRotate(0);
		head.setBackground(Background.fill(Color.BLACK));
		head.setMaxSize(40, 20);
		head.setMinSize(40, 20);
		return head;
	}
}
