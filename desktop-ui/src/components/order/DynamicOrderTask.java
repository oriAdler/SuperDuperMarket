package components.order;

import components.file.FileLoaderUtils;
import javafx.concurrent.Task;

import java.util.function.Consumer;

public class DynamicOrderTask extends Task<Boolean> {

    private Consumer<Boolean> isOrderReadyDelegator;

    // Progress variables:
    private final int SLEEP_TIME = 5;
    private final int MAX_PROGRESS = 100;
    private final int QUARTER_PROGRESS = 25;
    private int dummyProgress;

    public DynamicOrderTask(Consumer<Boolean> isOrderReadyDelegator) {
        this.isOrderReadyDelegator = isOrderReadyDelegator;
        dummyProgress = 0;
    }

    @Override
    protected Boolean call() throws Exception {
        updateMessage("Finding Best Prices.");
        updateProgressWithFlow(QUARTER_PROGRESS);

        updateMessage("Finding Best Prices..");
        updateProgressWithFlow(QUARTER_PROGRESS);

        updateMessage("Finding Best Prices...");
        updateProgressWithFlow(QUARTER_PROGRESS);

        updateMessage("Finding Best Prices.");
        updateProgressWithFlow(QUARTER_PROGRESS);

        isOrderReadyDelegator.accept(true);

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
