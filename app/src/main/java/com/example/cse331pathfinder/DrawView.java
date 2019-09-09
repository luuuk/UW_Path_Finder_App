package com.example.cse331pathfinder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Toast;

import java.util.List;

import hw5.Node;
import hw7.Path;
import hw8.Coordinate;

/**
 * Modified ImageView for purpose of drawing over map in this app
 */
public class DrawView extends AppCompatImageView {

    private boolean drawPath = false;
    private Path<Coordinate, Double> path;
    private static final float COORDINATE_SCALE = .25f;

    //I have no idea how to write JavaDoc for the constructors. Here goes nothing...
    /**
     * Constructs a new DrawView
     * @param context context in which this is constructed
     */
    public DrawView(Context context) {
        super(context);
    }
    /**
     * Constructs a new DrawView
     * @param context context in which this is constructed
     * @param attrs attributes of this
     */
    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /**
     * Constructs a new DrawView
     * @param context context in which this is constructed
     * @param attrs attributes of this
     * @param defStyle Style of this as specified previously, maybe?
     */
    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * When invalidated and in even state, app will draw path over map. When in odd state,
     * app will clear drawing on canvas
     * @param canvas canvas this draws on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(30f);
        if (drawPath) {
            List<Node<Coordinate, Double>> pathList = path.getPath();
            canvas.drawCircle(COORDINATE_SCALE * (float) pathList.get(0).getLabel().getX(),
                    COORDINATE_SCALE * (float) pathList.get(0).getLabel().getY(),
                    10f, paint);
            canvas.drawCircle(COORDINATE_SCALE * (float) pathList.get(pathList.size() - 1)
                    .getLabel().getX(), COORDINATE_SCALE * (float)
                    pathList.get(pathList.size() - 1).getLabel().getY(), 10f, paint);
            paint.setStrokeWidth(5f);
            paint.setColor(Color.RED);
            for (int i = 0; i < pathList.size() - 1; i++) {
                Node<Coordinate, Double> orig = pathList.get(i);
                Node<Coordinate, Double> dest = pathList.get(i + 1);
                canvas.drawLine(COORDINATE_SCALE * (float) orig.getLabel().getX(),
                        COORDINATE_SCALE * (float) orig.getLabel().getY(),
                        COORDINATE_SCALE * (float) dest.getLabel().getX(),
                        COORDINATE_SCALE * (float) dest.getLabel().getY(), paint);
            }
        }
    }

    /**
     * Calls method to display path between 2 buildings on map
     * @param path path to be drawn on the map
     */
    public void displayPath(Path<Coordinate, Double> path) {
        toggleDrawPath();
        this.path = path;
        this.invalidate();
    }

    /**
     * Toggles path drawing variable
     */
    public void toggleDrawPath() {
        drawPath = !drawPath;
    }
}
