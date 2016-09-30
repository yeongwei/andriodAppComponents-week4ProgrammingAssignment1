package vandy.mooc.assignments.assignment.downloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * A AsyncTask downloader implementation that uses an AsyncTask to download a
 * single image in a background thread.
 * <p/>
 * The base ImageDownloader class provides helper methods to perform the
 * download operation as well as to return the resulting image bitmap to the
 * framework where it will be displayed in a layout ImageView.
 */
public class AsyncTaskDownloader extends ImageDownloader {
    /**
     * Logging tag.
     */
    private static final String TAG = "AsyncTaskDownloader";

    /**
     * A reference to the background task to support the cancel hook.
     */
    // TODO A3: Create a private AsyncTask called 'mTask'.
   

    /**
     * Starts the asynchronous download request.
     */
    @Override
    public void execute() {
        // TODO A3: Initialize mTask.
        // TODO A3: In the background: Call abstract class helper method to perform the download request and decode the resource.
        // TODO A3: After downloading is complete: Call the super class setResource helper method to set the resource. // The helper will also display and error bitmap if the passed bitmap is null (signalling a failed download).
        // TODO A3: run mTask.
       
    }

    /**
     * Cancels the current download operation.
     */
    @Override
    public void cancel() {
        // If the download thread is alive and running, cancel it by
        // invoking an interrupt.
        // TODO A3: Return 'true' if mTask is currently running.
       
    }

    /**
     * Reports if the task is currently running.
     *
     * @return {@code true} if the task is running; {@code false} if not.
     */
    @Override
    public boolean isRunning() {
        // TODO A3: Return 'true' if mTask is currently running.
       
    }

    /**
     * Reports if the task has been cancelled.
     *
     * @return {@code true} if the task has cancelled ; {@code false} if not.
     */
    @Override
    public boolean isCancelled() {
        // TODO A3: Return 'true' if mTask has been cancelled.
       
    }

    /**
     * Reports if the task has completed.
     *
     * @return {@code true} if the task has successfully completed; {@code
     * false} if not.
     */
    @Override
    public boolean hasCompleted() {
        // TODO A3: Return 'true' if mTask has finished running.
        
    }
}
