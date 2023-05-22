package smeen.component;

import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import smeen.global.SmeenFileChooser;
import smeen.views.MainView;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class TopMenuBar extends MenuBar {

    private File workingFile;

    private MainView main;

    public TopMenuBar(MainView main) {
        this.main = main;

        Menu fileMenu = new Menu("File");

        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.addEventHandler(ActionEvent.ACTION, e -> {
            main.reset();
        });

        MenuItem openMenuItem = new MenuItem("Open...");
        openMenuItem.addEventHandler(ActionEvent.ACTION, e -> {
            File file = SmeenFileChooser.openProject();
            if (file != null) {
                // stop executions before loading
                main.getCodeExecutionHandler().onStopButtonClicked();
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    main.importData((Map<String, Object>) in.readObject());
                    workingFile = file;
                } catch (IOException | ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        MenuItem saveMenuItem = new MenuItem("Save");
        saveMenuItem.addEventHandler(ActionEvent.ACTION, e -> {
            if (workingFile == null) {
                saveAs();
            } else {
                try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(workingFile));) {
                    out.writeObject(main.exportData());
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        });

        MenuItem saveAsMenuItem = new MenuItem("Save as...");
        saveAsMenuItem.addEventHandler(ActionEvent.ACTION, e -> saveAs());

        fileMenu.getItems().addAll(newMenuItem, openMenuItem, saveMenuItem, saveAsMenuItem);

        getMenus().addAll(fileMenu);
    }

    /**
     * Perform "Save As" action.
     */
    private void saveAs() {
        File file = SmeenFileChooser.saveProject();
        if (file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(main.exportData());
                workingFile = file;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
