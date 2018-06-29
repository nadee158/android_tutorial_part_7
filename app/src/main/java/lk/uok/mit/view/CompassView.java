package lk.uok.mit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CompassView extends View {

    //a class variable to refer to android.graphics.Paint object
    private Paint paint;
    //a class variable to store the position of the paint
    private float position = 0;

    //a constructor to call the super class's constructor
    public CompassView(Context context) {
        super(context);
        this.initializePaint();
    }

    //override the onDraw method to define the information to be displayed on the view
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //get the pont along X axis as half from the raw measured width of this view
        int xPoint = getMeasuredWidth() / 2;
        //get the pont along Y axis as half from the raw measured height of this view
        int yPoint = getMeasuredHeight() / 2;

        //get the max value of wither X or Y points we got above
        float maxLengthOfCanvas = Math.max(xPoint, yPoint);
        //calculate radius based on maxLengthOfCanvas, make it 0.6 from the max length
        float radius = (float) (maxLengthOfCanvas * 0.6);

        //draw a circle on canvas based on the radius we calulated
        // by taking X and Y points(the canter of canvas) as the center of circle
        //The circle will be filled or framed based on the Style in the paint
        canvas.drawCircle(xPoint, yPoint, radius, paint);

        //The rectangle will be filled or framed based on the Style in the paint
        //draw a rectangle within 5 points the view
        canvas.drawRect(0, 0, (getMeasuredWidth() - 5), (getMeasuredHeight() - 5), paint);

        //To display the indicator of the compass,
        // we need to create a line inside circle from center to its radius
        //the starting point of the radius will be the center of the circle
        //the end point on the perimeter of the circle has to be calculated

        // 3.143 is a good approximation for the circle
        float PI_CONSTANT = 3.143f;

        //calculate the end point along X axis based on the rotation degree
        float xPointEnd = (float) (xPoint + radius * Math.sin((-position) / 180 * PI_CONSTANT));

        //calculate the end point along Y axis based on the rotation degree
        float yPointEnd = (float) (yPoint - radius * Math.cos((-position) / 180 * PI_CONSTANT));

        //The drawLine method draws a line segment with the specified start and stop x,y coordinates,
        // using the specified paint.
        // @param xPoint The x-coordinate of the start point of the line
        // @param yPoint The y-coordinate of the start point of the line
        // @param xPointEnd The x-coordinate of the end point of the line
        // @param yPointEnd The y-coordinate of the end point of the line
        // @param paint The paint used to draw the line
        canvas.drawLine(xPoint, yPoint, xPointEnd, yPointEnd, paint);

        //finally, at the center of the circle, draw the txt to disply the passed rotation value
        canvas.drawText(String.valueOf(position), xPoint, yPoint, paint);
    }

    private void initializePaint() {
        //initialize a paint object
        this.paint = new Paint();
        //set the attributes of the paint object
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(2);
        this.paint.setTextSize(25);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setColor(Color.BLACK);
    }

    public void updatePaintPosition(float position) {
        //set the passed position to the position of paint
        this.position = position;
        //call invalidate to force to redraw the view based on new position
        //this allows the wiew to get update based on new position
        invalidate();
    }


}
