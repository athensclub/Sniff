package smeen.global;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Handle file choosing. The main method should call initialize before doing anything else from this class.
 */
public final class SmeenFileChooser {

    private static Stage primaryStage;

    public static void initialize(Stage primaryStage) {
        SmeenFileChooser.primaryStage = primaryStage;
    }

    /**
     * Launch a file chooser with image filter.
     *
     * @return a selected image, or null if none is selected.
     */
    public static File chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Scene Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg")
        );
        return fileChooser.showOpenDialog(primaryStage);
    }

    /**
     * Laumch a file chooser with smeen project filter.
     *
     * @return a selected project file, or null if none is selected.
     */
    public static File openProject() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Scene Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sniff project files (*.sniff)", "*.sniff"),
                new FileChooser.ExtensionFilter("Any File (*.*)", "*.*")
        );
        return fileChooser.showOpenDialog(primaryStage);
    }

    /**
     * Launch a file chooser to save project data.
     *
     * @return a target file to save, or null if not selected.
     */
    public static File saveProject() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Sniff project files (*.sniff)", "*.sniff"),
                new FileChooser.ExtensionFilter("Any File (*.*)", "*.*"));
        return fileChooser.showSaveDialog(primaryStage);
    }

}
