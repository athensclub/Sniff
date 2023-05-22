package smeen.logic;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.SetChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import smeen.component.SpriteObject;
import smeen.component.code.CodeArea;
import smeen.component.code.CodeBlock;
import smeen.component.code.CodeBlockList;
import smeen.component.code.block.event.*;
import smeen.global.SmeenConstants;
import smeen.views.MainView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A class that handle code blocks execution.
 */
public class CodeExecutionHandler implements HasEvents {

    private MainView main;

    private SmeenContext context;

    private BooleanProperty running;
    private SetProperty<Future<?>> runningActions;

    /**
     * Constructor for CodeExecutionHandler.
     * @param main
     */
    public CodeExecutionHandler(MainView main) {
        this.main = main;

        context = new SmeenContext();

        running = new SimpleBooleanProperty();
        runningActions = new SimpleSetProperty<>(FXCollections.observableSet(ConcurrentHashMap.newKeySet()));

        runningActions.addListener((SetChangeListener<? super Future<?>>) (c) -> {
            if (c.wasAdded() && !running.get()) {
                c.getElementAdded().cancel(true);
            }
        });
    }

    /**
     * Whether the current state is running.
     * @return
     */
    public BooleanProperty runningProperty() {
        return running;
    }

    /**
     *
     * @return the context to be used by code blocks.
     */
    public SmeenContext getContext() {
        return context;
    }

    /**
     * Run all "When Green FLag Clicked" Code Block Lists
     */
    public void onGreenFlagClicked() {
        // if already running, do not repeat running flag codes.
        if (running.get()) return;

        runningActions.clear();
        running.set(true);
        runCodesIfTop(top -> top instanceof WhenStartCodeBlock);
    }

    /**
     * Stop all currently running code block actions
     */
    public void onStopButtonClicked() {
        // if already stopped, there's nothing to do
        if (!running.get()) return;

        for (Future<?> future : runningActions)
            future.cancel(true);
        runningActions.clear();

        main.getStageArea().forEachSprite(sprite -> {
            // set all sprites to visible
            sprite.setVisible(true);
            // also hide all speech
            sprite.stopSaying();
        });
        main.getAnswerArea().hide();
        main.getStageArea().shownVariableProperty().clear();

        running.set(false);
    }

    /**
     * Run all handler for when scene changed
     */
    public void onSceneChanged(int order) {
        runCodesIfTop(top -> top instanceof WhenSceneChangeToCodeBlock block && block.getPosScene(context) == order);
    }

    /**
     * Run all the necessary "On Pressed" code block list in the given sprite.
     *
     * @param sprite
     */
    public void onSpritePressed(SpriteObject sprite) {
        if (!running.get())
            return;

        forEachCodeBlockList(sprite.getCodeArea(), list -> {
            if (list.getCodeList().getChildren().get(0) instanceof WhenClickedCodeBlock)
                registerFuture(() -> list.execute(context));
        });
    }

    /**
     * Run all the necessary "On Pressed" code block list in the currently shown scene.
     */
    public void onStagePressed() {
        if (!running.get())
            return;

        forEachCodeBlockList(main.getSceneEditor().getScene(main.getStageArea().shownSceneProperty().get()).getCodeArea(), list -> {
            if (list.getCodeList().getChildren().get(0) instanceof WhenClickedCodeBlock)
                registerFuture(() -> list.execute(context));
        });
    }

    /**
     * @return
     * @see <a href="https://stackoverflow.com/a/19348417">https://stackoverflow.com/a/19348417</a>
     */
    public Future<?> broadcast(String name) {
        if (!running.get())
            return null;

        return registerFuture(() -> {
            List<Future<?>> toWait = new ArrayList<>();
            runIf(list -> list.getCodeList().getChildren().get(0) instanceof WhenReceivedBroadcastCodeBlock cb && cb.getBroadcastName(context).equals(name), list -> toWait.add(registerFuture(() -> list.execute(context))));
            for (Future<?> w : toWait) {
                try {
                    w.get();
                } catch (InterruptedException e) {
                    return;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Run the given consumer on all interested code area (which is all sprite's code area and
     * the currently selected stage's code area)
     *
     * @param func
     */
    private void forEachCodeArea(Consumer<CodeArea> func) {
        main.getSpriteEditor().forEachSpriteOption(option -> func.accept(option.getSprite().getCodeArea()));
        func.accept(main.getSceneEditor().getScene(main.getStageArea().shownSceneProperty().get()).getCodeArea());
    }

    /**
     * Run all the code block lists that has their top block pass the predicate and is in the
     * interested code area (defined by {@link #forEachCodeArea}
     *
     * @param pred
     */
    private void runCodesIfTop(Predicate<CodeBlock> pred) {
        runCodesIf(list -> pred.test((CodeBlock) list.getCodeList().getChildren().get(0)));
    }

    /**
     * If the current state is running, run all the code block lists that pass the predicate
     * that is in the interested code area (defined by {@link #forEachCodeArea}
     *
     * @param pred
     */
    private void runCodesIf(Predicate<CodeBlockList> pred) {
        if (!running.get())
            return;

        runIf(pred, list -> registerFuture(() -> list.execute(context)));
    }

    /**
     * Run the given consumer on all the interested code block list that pass the given
     * predicate (interested code block list is defined by {@link #forEachCodeArea}.
     *
     * @param pred
     * @param toRun
     */
    private void runIf(Predicate<CodeBlockList> pred, Consumer<CodeBlockList> toRun) {
        forEachCodeArea(codeArea -> forEachCodeBlockList(codeArea, list -> {
            if (pred.test(list))
                toRun.accept(list);
        }));
    }

    /**
     * Run the given func on all code block list inside the given code area.
     *
     * @param area
     * @param func
     */
    private void forEachCodeBlockList(CodeArea area, Consumer<CodeBlockList> func) {
        for (Node child : area.getChildren()) {
            if (child instanceof CodeBlockList list) {
                func.accept(list);
            }
        }
    }

    /**
     * @param r
     * @return a new runnable that will run the old runnable and print exception stack trace if there's an exception.
     */
    private Runnable withExceptionLogging(Runnable r) {
        return () -> {
            try {
                r.run();
            } catch (Exception e) {
                if (e != null && !(e instanceof CancellationException) && !(e instanceof CompletionException ex && ex.getCause() instanceof CancellationException)) {
                    e.printStackTrace();
                }
            }
        };
    }

    /**
     * Register the given future to be cancelled when the red button is clicked. Also will
     * print the stack trace of any error from the given error to the console.
     *
     * @param r
     */
    public Future<?> registerFuture(Runnable r) {
        Future<?>[] future = {null};
        future[0] = SmeenConstants.THREAD_POOL.submit(() -> {
            withExceptionLogging(r).run();
            if (future[0] != null)
                runningActions.remove(future[0]);
        });
        runningActions.add(future[0]);
        return future[0];
    }

    @Override
    public void registerEvents(Scene scene) {
        context.registerEvents(scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e ->
                runCodesIfTop(top -> top instanceof WhenKeyPressedCodeBlock cb && cb.getOptionBox().getValue().checkKeyCode(e.getCode())));
    }
}
