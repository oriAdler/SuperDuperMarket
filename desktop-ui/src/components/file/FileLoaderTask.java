package components.file;

import engine.Engine;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.util.Random;
import java.util.function.Consumer;

public class FileLoaderTask extends Task<Boolean> {

    private String fileName;
    private Consumer<String> finalMessageDelegator;
    private Consumer<Boolean> isFileLoadedDelegator;
    private Engine engine;

    // Progress variables:
    private final int SLEEP_TIME = 5;
    private final int MAX_PROGRESS = 100;
    private final int HALF_PROGRESS = 50;
    private final int QUARTER_PROGRESS = 25;
    private int dummyProgress;

    public FileLoaderTask(String fileName, Consumer<String> finalMessage,
                          Consumer<Boolean> isFileLoadedDelegator, Engine engine) {
        this.fileName = fileName;
        this.finalMessageDelegator = finalMessage;
        this.isFileLoadedDelegator = isFileLoadedDelegator;
        this.engine = engine;
        dummyProgress = 0;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            updateMessage("Fetching file...");
            updateProgressWithFlow(HALF_PROGRESS);

            // Actual work:
            engine.loadDataFromFile(fileName);
            isFileLoadedDelegator.accept(true);

            updateMessage("Checking validation...");
            updateProgressWithFlow(HALF_PROGRESS);

            updateMessage("Done...");
            Platform.runLater(()->finalMessageDelegator.accept("File was loaded successfully"));
        }
        catch (Exception exception){
            updateMessage("Checking validation...");
            Random rand = new Random();
            updateProgressWithFlow(rand.nextInt(QUARTER_PROGRESS));
            Platform.runLater(()->finalMessageDelegator.accept(exception.getMessage()));
        }

        return true;
    }

    private void updateProgressWithFlow(int advance){
        for(int i=0; i<advance; i++){
            dummyProgress++;
            updateProgress(dummyProgress, MAX_PROGRESS);
            FileLoaderUtils.sleepForAWhile(SLEEP_TIME);
        }
    }
}
