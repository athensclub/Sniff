package smeen.component.code.block.event;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import smeen.component.code.CodeBlock;
import smeen.global.Fonts;
import smeen.global.SmeenSVGs;
import smeen.logic.Executable;
import smeen.logic.SmeenContext;
import smeen.views.MainView;

public class WhenStartCodeBlock extends CodeBlock {

    public WhenStartCodeBlock(MainView main){
        super(main);
        Label first = new Label("เมื่อกด");
        first.setTextFill(Color.WHITE);
        first.setFont(Fonts.SMALL_REGULAR_FONT);
        
        SVGPath flagPath = new SVGPath();
        flagPath.setContent(SmeenSVGs.FLAG_PATH);
        Region flagRegion = new Region();
        flagRegion.setShape(flagPath);
        flagRegion.setBackground(Background.fill(Color.web("#00ff33")));
        flagRegion.setMaxSize(16,20);
        flagRegion.setPrefSize(16, 20);

        getContent().setBackground(Background.fill(Color.GOLD));

        getContent().getChildren().addAll(first, flagRegion);
    }

    @Override
    public boolean mustBeFirstBlock() {
        return true;
    }

    @Override
    public CodeBlock copy() {
    	WhenStartCodeBlock copy = new WhenStartCodeBlock(getMain());
        return copy;
    }

    @Override
    public Executable.Result execute(SmeenContext context) {
        // no op
        return new Executable.Result(false, false);
    }
    
    @Override
    public Map<String, Object> exportData() {
        Map<String, Object> result = new HashMap<>();
        result.put("type", "WhenStartCodeBlock");
        return result;
    }

    @Override
    public void importData(Map<String, Object> data) {
    	// no op
    }
}
