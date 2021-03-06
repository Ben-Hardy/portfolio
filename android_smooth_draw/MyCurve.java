package ben.assignment5;

/**
 * Ben Hardy
 * 
 * 
 * CMPT 381
 * Assignment 5
 */

import android.graphics.PointF;
import java.util.ArrayList;


/**
 * Simple curve storage thingy. Keeps track of all points for a single curve
 */
public class MyCurve {

    /**
     * The points stored in the object
     */
    ArrayList<PointF> points;

    /**
     * Basic autogenerated constructor
     * @param points - the points that are to be stored
     */
    public MyCurve(ArrayList<PointF> points) {
        this.points = points;
    }

}

