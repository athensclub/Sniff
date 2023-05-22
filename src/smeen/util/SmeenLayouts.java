package smeen.util;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import smeen.component.code.CodeBlockList;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Utility class for handling UI.
 */
public final class SmeenLayouts {
    /**
     * Clips the children of the specified {@link Region} to its current size.
     * This requires attaching a change listener to the region’s layout bounds,
     * as JavaFX does not currently provide any built-in way to clip children.
     *
     * @param region the {@link Region} whose children to clip
     * @throws NullPointerException if {@code region} is {@code null}
     * @see <a href="https://news.kynosarges.org/2016/11/03/javafx-pane-clipping/">source</a>
     */
    public static void clipChildren(Region region) {
        final Rectangle outputClip = new Rectangle();
        region.setClip(outputClip);

        region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
            outputClip.setWidth(newValue.getWidth());
            outputClip.setHeight(newValue.getHeight());
        });
    }

}
