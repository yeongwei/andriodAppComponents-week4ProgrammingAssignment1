package vandy.mooc.assignments.assignment.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import vandy.mooc.assignments.R;
import vandy.mooc.assignments.assignment.downloader.AsyncTaskDownloader;
import vandy.mooc.assignments.framework.application.activities.GalleryActivityBase;
import vandy.mooc.assignments.framework.downloader.DownloadManager;
import vandy.mooc.assignments.framework.utils.UriUtils;
import vandy.mooc.assignments.framework.utils.ViewUtils;

/**
 * This activity class contains helper methods to support different was you can
 * use Intents to communicate between activities (in the assignments case
 * between the MainActivity and this GalleryActivity).
 * <p/>
 * The MainActivity constructs a starting intent containing the
 * displayed list of remote image URLs and then uses this intent to start this
 * GalleryActivity.
 */
public class GalleryActivity
        extends GalleryActivityBase {
    /**
     * Intent "extra" string key used to identify the list of image URLs
     */
    public static final String INTENT_EXTRA_URLS = "extra_urls";
    /**
     * Debug logging tag.
     */
    private static final String TAG = "GalleryActivity";

    /*
     * Activity Lifecycle methods.
     */

    /**
     * Factory method that creates and returns an explicit intent that can be
     * used to start this activity.
     *
     * @param context   The context of the calling activity
     * @param inputUrls A list list of input URLs to include as intent extras.
     * @return An intent that can be used to start this activity
     */
    public static Intent makeStartIntent(
            Context context,
            ArrayList<Uri> inputUrls) {
        // Create a new intent for starting this activity
        // using the passed context along with the class identifier
        // for this class.
        Intent intent = new Intent();
        intent.setClass(context, GalleryActivity.class);

        // Put the received list of input URLs as an intent
        // extra using the predefined INTENT_EXTRA_URLS extra name.
        intent.putParcelableArrayListExtra(INTENT_EXTRA_URLS, inputUrls);

        // Return the intent.
        return intent;
    }

    /*
     * Factory methods.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class method first. Normally you would follow this
        // call with a call to inflate the activity's layout from XML, but this
        // is not necessary here because the assignment super class method will
        // do that for you.
        super.onCreate(savedInstanceState);

        // When savedInstanceState is null, the activity is being started for
        // the first time, and when not null, the activity is being recreated
        // after a configuration change.
        //noinspection StatementWithEmptyBody
        if (savedInstanceState == null) {
            // The activity is being started for the first time.

            // Call local help method to extract the URLs from the activity's
            // starting intent and pass these URLs into the super class using
            // the setItems() helper method.
            Intent intentThatStartedActivity = getIntent();
            ArrayList<Uri> inputUrls = intentThatStartedActivity.getParcelableArrayListExtra(INTENT_EXTRA_URLS);
            setItems(inputUrls);

        } else {
            // The activity is being recreated after configuration change.
            // You can restore your activity's saved state from the passed
            // savedInstanceState either here or in onRestoreInstanceState().
            // This framework will automatically save and restore the
            // displayed URL list using onSaveInstanceState() and
            // onRestoreInstanceState() so you don't need to do anything here.
        }

        // Call base class helper method to register your downloader
        // implementation class.
        // TODO -- you fill in here.
        // TODO Where is the Class that extends BroadcastReceiver ???
        this.registerDownloader(AsyncTaskDownloader.class);
    }

    /**
     * Extract, validate, and returns the image urls received in the starting
     * Intent.
     *
     * @return A list of image URLs.
     */
    @SuppressWarnings("unchecked")
    private List<Uri> extractInputUrlsFromIntent(Intent intent) {
        // First extract the list of input urls from the passed
        // intent extras using the provided INTENT_EXTRA_URLS name string.
        // Next, validate the extracted list URL strings by calling the local
        // validateInput() helper method. If the entire list of received URLs
        // are valid, then return this list. Otherwise return null.
        ArrayList<Uri> inputUrls = intent.getParcelableArrayListExtra(INTENT_EXTRA_URLS);
        if (validateInput(inputUrls))
            return inputUrls;
        else
            return null;
    }

    /**
     * Ensures that the input to this activity is valid and reports errors if
     * the received URL list is null, empty, or contains any mal-formed URLs.
     *
     * @param inputUrls A List of image URLs to validate.
     * @return {@code true} if the input URL list is valid; {@code false} on
     * first error encountered.
     */
    private boolean validateInput(ArrayList<Uri> inputUrls) {
        // Validate the passed URL.
        //
        // If the list is null call ViewUtils.showToast() to display the
        // string R.string.input_url_list_is_null.
        //
        // If the list has a size of 0 then call ViewUtils.showToast()
        // to display the the string R.string.input_url_list_is_empty
        //
        // Otherwise check if each list entry is valid using the
        // UrlUtils.isValidUrl() helper and if any URL is not valid
        // return false.
        //
        // Return true if all the URLs are valid.
        if (inputUrls == null) {
            ViewUtils.showToast(getApplicationContext(), R.string.input_url_list_is_null);
            return false;
        }

        if (inputUrls.size() == 0) {
            ViewUtils.showToast(getApplicationContext(), R.string.input_url_list_is_empty);
            return false;
        }

        for (Uri uri : inputUrls)
            if (!UriUtils.isValidUrl(uri.toString()))
                return false;

        // Input passed all tests, so return true.
        return true;

    }

    /**
     * Factory method that creates and returns a results (data) intent that
     * contains the list of downloaded image URIs. This intent can then be
     * passed to this activity's setResult() method which will inform the
     * application framework to return this intent to the parent activity.
     * <p/>
     * Note: this method is declared public for integration tests.
     *
     * @param outputUrls A List of local image URLs to add as an intent extra.
     * @return An intent to return the parent activity
     */
    @SuppressWarnings("WeakerAccess")
    protected Intent makeResultIntent(ArrayList<Uri> outputUrls) {
        // create a new data intent, put the received
        // outputUrls list into the intent as an ParcelableArrayListExtra,
        // and return the intent.
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra(INTENT_EXTRA_URLS, outputUrls);
        return resultIntent;
    }

    /**
     * Creates a data results intent, sets the activity result to this intent,
     * and finishes this activity.
     *
     * @param urls The currently displayed list of image URLs.
     */
    protected void createAndReturnResultsIntent(
            @NonNull ArrayList<Uri> urls) {
        Log.d(TAG, "Setting a result intent.");

        // Call makeResultIntent to construct a return
        // intent that contains the list of currently displayed URLs
        // as an intent extra.
        Intent resultIntent = makeResultIntent(urls);

        // Now set the result intent to return.
        GalleryActivity.this.setResult(Activity.RESULT_OK, resultIntent);

        // Call an Activity method to end this activity and return
        // to parent activity.
        GalleryActivity.this.finish();

        Log.d(TAG, "Activity finished.");
    }

    /**
     * Factory method that creates and returns a results (data) intent that
     * contains the list of downloaded image URLs. This intent can then be sent
     * as a broadcast intent to a BroadcastReceiver.
     * <p/>
     * Note: this method is declared public for integration tests.
     *
     * @param urls The current list of image URLs to add as an intent extra.
     * @return An intent suitable for sending in a broadcast.
     */
    @SuppressWarnings("WeakerAccess")
    private Intent makeBroadcastIntent(@NonNull ArrayList<Uri> urls) {
        // create a new data intent with the intent action to
        // ACTION_VIEW_LOCAL as expected by the MainActivity's
        // BroadcastReceiver implementation.
        Intent broadcastIntent = new Intent(MainActivity.ACTION_VIEW_LOCAL);

        // put the received outputUrls list into the intent as an
        // ParcelableArrayListExtra.
        broadcastIntent.putParcelableArrayListExtra(INTENT_EXTRA_URLS, urls);

        // Set the intent action to ACTION_VIEW_LOCAL as is
        // expected by the MainActivity's BroadcastReceiver implementation.
        broadcastIntent.setAction(MainActivity.ACTION_VIEW_LOCAL); // This might be redundant to the one passed into the Intent constructor

        //  Return the intent.
        return broadcastIntent;
    }

    /**
     * Creates a data results intent, broadcasts this the intent intent (to the
     * MainActivity), and finishes this activity.
     *
     * @param outputUrls A list of local image URLs to broadcast.
     */
    protected void createAndBroadcastResultsIntent(
            @NonNull ArrayList<Uri> outputUrls) {
        Log.d(TAG, "Sending a broadcast Intent.");

        // Call makeBroadcastIntent to construct an intent
        // that can be used to broadcast the downloaded image list to
        // a BroadcastReceiver (the MainActivity).
        Intent broadcastIntent = makeBroadcastIntent(outputUrls);

        // Call the Activity class helper method to
        // send this intent in a local broadcast to the main activity.
        GalleryActivity.this.sendBroadcast(broadcastIntent); // Make invocation method consistent

        // Call an Activity method to end this activity and return
        // to parent activity.
        GalleryActivity.this.finish();

        Log.d(TAG, "Activity finished.");
    }
}
