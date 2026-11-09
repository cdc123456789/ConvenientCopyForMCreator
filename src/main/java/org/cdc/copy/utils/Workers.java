package org.cdc.copy.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * &#064;Developer  user
 * &#064;CreatedIn  2026/11/9
 */
public class Workers {
    private static Workers INSTANCE;

    public static Workers getInstance() {
        if (INSTANCE == null) INSTANCE = new Workers();
        return INSTANCE;
    }

    public Workers(){}

    private final ThreadPoolExecutor copyAndPasteThread = new ThreadPoolExecutor(1,1,3, TimeUnit.MINUTES, new ArrayBlockingQueue<>(3));

    public void executeCopyOrPaste(Runnable runnable){
        copyAndPasteThread.submit(runnable);
    }
}
