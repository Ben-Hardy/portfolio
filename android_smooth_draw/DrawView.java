package ben.assignment5;

/**
 * Ben Hardy
 * 
 * 
 * CMPT 381
 * Assignment 5
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Ben on 3/27/2015.
 */
public class DrawView extends View {

    // our drawing variables
    private Canvas canvas;
    private Path path;
    private Paint paint;

    // our bitmap variables
    private Bitmap map;
    private Paint mp;

    // our container variables
    private ArrayList<PointF> points;
    private ArrayList<MyLine> lines;
    private ArrayList<MyCurve> curves;

    // variablees to store the beginning point of a stroke and the previous stroke point
    private float prevX, prevY;
    private float startX, startY;


    // basic constructor for the class
    public DrawView(Context context) {
        super(context);

        // set up the paint variable
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);

        // without this the lines are quite ugly and jaggy
        paint.setAntiAlias(true);

        // allocate the arraylist that will temporarily store points
        points = new ArrayList<>();

        // allocate our main path variable. Other temporary paths will also be used
        path = new Path();

        // allocate the bitmap's paint too
        mp = new Paint(Paint.DITHER_FLAG);

        // allocate the storage containers for our lines and curves
        lines = new ArrayList<>();
        curves = new ArrayList<>();
    }

    // this appears to be required as my app wouldn't load without it
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        map = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(map);
    }

    /**
     * Handler for touch events. Passes of work to other functions based on what event is happening
     * @param event - the event occuring
     * @return true in all cases
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // add the event's location to our thing of points. This will happen at the start of a touch, during the
        // touch and when the touch ends
        points.add(new PointF(event.getX(),event.getY()));

        // handle things based on which motion event happened
        if (event.getAction() == MotionEvent.ACTION_DOWN)
            startDraw(event.getX(), event.getY());
        else if (event.getAction() == MotionEvent.ACTION_MOVE)
            continueDraw(event.getX(), event.getY());
        else if (event.getAction() == MotionEvent.ACTION_UP)
            stopDraw();

        invalidate();
        return true;
    }

    // when a draw has happened, update both our regular canvas and our bitmap.
    // have to update bitmap first or intermediate drawing won't show up.
    // I learned that the hard way :(
    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(map, 0, 0, mp);
        canvas.drawPath( path,  paint);
    }

    // after a stroke is done, redraw all stored objects, which will include any objects added by the stroke
    public void redraw() {
        invalidate();
        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLACK);
        if (lines.size() > 0)
            // for each line, draw it
            for (MyLine line : lines) {
                canvas.drawLine(line.x1, line.y1, line.x2, line.y2, paint);
            }

        if (curves.size() > 0) {

            // for each curve stored in the list of curves, generate a path from the first point to the last
            // point using midpoints as a way of generating smoothness
            for (MyCurve curve : curves) {
                Path p = new Path();
                System.out.println("Drawing curve");
                for (int i = 0; i < curve.points.size() -2; i+=2) {
                    p.moveTo(curve.points.get(i).x, curve.points.get(i).y);
                    p.quadTo(curve.points.get(i+1).x, curve.points.get(i+1).y,curve.points.get(i+2).x, curve.points.get(i+2).y);
                }
                // draw the curve
                canvas.drawPath(p, paint);
            }
        }
        System.out.println("Number of curves: " + curves.size());
        System.out.println("Number of lines: " + lines.size());
    }


    private void startDraw(float x, float y) {

        // I had it so that it would clear all drawn stuff to make things easier to
        // see for drawing, but then I discovered just making the drawing line a different
        // colour does a way better job
        //canvas.drawColor(Color.WHITE);

        // set the line we will use to depict the thing currently being drawn as red to distinguish it
        // from all the other lines, which are black
        paint.setColor(Color.RED);
        // clear the path and move it to our start point
        path.reset();
        path.moveTo(x, y);
        startX = x;
        startY = y;

    }

    // continue drawing,
    private void continueDraw(float x, float y) {
        path.lineTo(x, y);
        prevX = x;
        prevY = y;
    }

    // uses Euclidian distance to calculate the total sum of the distances between all the points
    // that were done in the stroke. Used for judging if a stroke was a line or not
    public float calculateDrawLen() {

        if (points.size() == 1)
            return 0;

        float len = 0;
        for (int i = 0; i < points.size() -2; i++) {
            len += Math.sqrt(Math.pow((points.get(i).x - points.get(i+1).x),2) + Math.pow((points.get(i).y - points.get(i+1).y),2));
        }

        return len;
    }


    // On the stroke ending, analyze the stroke and see if it wss a straight line. If it wasn't, make it a curve
    private void stopDraw() {

        path.lineTo(prevX, prevY);
        canvas.drawPath(path,  paint);

        // we have to reset the path or else it will remain on the page even when we don't want it to
        path.reset();
        System.out.println("Start: " + points.get(0).toString());
        System.out.println("Approximate Midpoint: " + points.get((points.size()) / 2).toString());
        System.out.println("End: " + points.get(points.size() -1).toString());

        // calculate the Euclidian Distance from the startpoint to the endpoint. Used for judging if a stroke is a line or not
        float linelen = (float) Math.sqrt(Math.pow((startX - points.get(points.size() -1).x),2) + Math.pow((startY -points.get(points.size() -1).y),2));

        float drawinglen = calculateDrawLen();

        float endX = points.get(points.size() - 1).x;
        float endY = points.get(points.size() - 1).y;

        // I printed these out just to make sure things were working correctly
        System.out.println("Drawing length: " + drawinglen);
        System.out.println("Line length: " + linelen);

        // if the stroke length is within 5% of being the same length as a perfectly straight line from the start to the end, make it a line
        if (Math.abs(drawinglen - linelen) < 0.05 * linelen) {
            lines.add(new MyLine(startX, startY, endX, endY));
            redraw();
            System.out.println("Line was close enough to threshold so it was added");
        }
        // otherwise find the average curve for it
        else {
            ArrayList<PointF> curvePoints = new ArrayList<>();
            curvePoints.addAll(points);
            curves.add(new MyCurve(curvePoints));
            redraw();
            System.out.println("Was not interpreted as a line");
            System.out.println("Drawing was added as a curve instead");
        }

        // clear out the points container for the next stroke
        points.clear();

    }




}
