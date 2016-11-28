package ben.assignment5;

/**
 * Ben Hardy
 * 
 * 
 * CMPT 381
 * Assignment 5
 */


/**
 * Main start up class. Not much happens here
 */
import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {

    /**
     * A single instance of our custom view
     */
    DrawView drawview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawview = new DrawView(this);
        setContentView(drawview);

    }
}

