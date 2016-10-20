package kamehameha.beam.percept.AugmentingBoard;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
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
    Context context;


    public AugmentCanvas(Context c, AttributeSet attrs) {
        super(c, attrs);
        context = c;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
    }


    //override the onTouchEvent
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        addLocation(x,y);

        return true;
    }

    //adds the location pointed by the user to his set
    public void addLocation(float x,float y){

    }

    public void showLocation(ArrayList<CoordinatePoint> points){
        Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.location_icon);
        for(CoordinatePoint coordinatePoint: points){
            mCanvas.drawBitmap(icon,(float)coordinatePoint.getAugmentableX(),(float)coordinatePoint.getAugmentableY(),null);
        }
    }
}
