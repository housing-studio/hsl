package org.housingstudio.hsl.importer.platform;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exec {
    public static void async(ThrowableRunnable runnable) {
        Runnable wrapper = () -> {
            try {
                runnable.run();
            } catch (Exception e) {
                ChatLib.chat("error: " + e.getMessage());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                ChatLib.chat(sw.toString());
            }
        };

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(wrapper);
        executor.shutdown();
    }

    @FunctionalInterface
    public interface ThrowableRunnable {
        void run() throws Exception;
    }
}
