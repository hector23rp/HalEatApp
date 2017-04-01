package com.haleat.haleat;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


public class RectView extends View{

    private int w;
    private int h;
    private Paint paint;

    public RectView(Context context){
        super(context);
        init(context);
    }

    public RectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RectView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context){
        w = getWidth();
        h = getHeight();
    }

    public void onDraw(Canvas canvas){
        paint = new Paint();
        paint.setColor(Color.parseColor("#8AB944"));

        int height = this.getBottom() - this.getTop();

        int newTop = this.getTop()+(height/3);
        int newBottom = this.getTop()+(2*height/3);
        canvas.drawRect(this.getLeft(),newTop,this.getRight(),newBottom,paint);
    }
}
