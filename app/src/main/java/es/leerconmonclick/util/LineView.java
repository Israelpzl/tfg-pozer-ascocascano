package es.leerconmonclick.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class LineView  extends View {
    private float x1, y1, x2, y2;
    private Paint paint = new Paint();

    public LineView(Context context, float x1, float y1, float x2, float y2) {
        super(context);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(10);
        canvas.drawLine(x1,y1+75, x2+300,y2, paint);
    }
}
