package kamehameha.beam.percept.AugmentingBoard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import kamehameha.beam.percept.R;
import kamehameha.beam.percept.models.CoordinatePoint;

/**
 * Created by sbjr on 10/20/16.
 *
 * canvas class which takes input and draws location drawable on that position
 */

public class AugmentCanvas extends View {

    private Canvas mCanvas;
    private Context context;
    private Paint paint;

    private ArrayList<CoordinatePoint> points;

    public AugmentCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
        paint = new Paint(Paint.DITHER_FLAG);
        points = new ArrayList<>();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mCanvas = canvas;

        for(CoordinatePoint point : points){
            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.location_icon);
            mCanvas.drawBitmap(icon,(float)point.getAugmentableX(),(float)point.getAugmentableY(),paint);
        }

    }


    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        invalidate();

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            addLocation(x, y);
        }

        return true;
    }

    //adds the location pointed by the user to his set
    public void addLocation(float x,float y){
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.location_icon);
        mCanvas.drawBitmap(icon,x,y,paint);
        CoordinatePoint point = new CoordinatePoint();
        point.setAugmentableX(x);
        point.setAugmentableY(y);
        points.add(point);
        //Log.d("touch","("+x+","+y+")");
    }

    public void showLocation(ArrayList<CoordinatePoint> points){
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.location_icon);
        for(CoordinatePoint coordinatePoint: points){
            mCanvas.drawBitmap(icon,(float)coordinatePoint.getAugmentableX(),(float)coordinatePoint.getAugmentableY(),null);
        }
    }
}
