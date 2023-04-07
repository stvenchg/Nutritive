package fr.stvenchg.nutritive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {

    private Paint paint;

    public OverlayView(Context context) {
        super(context);
        init();
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Définis la taille du rectangle
        int rectangleWidth = canvas.getWidth() * 2 / 3 + 200;
        int rectangleHeight = canvas.getHeight() / 4;
        int left = (canvas.getWidth() - rectangleWidth) / 2;
        int top = (canvas.getHeight() - rectangleHeight) / 2;
        int right = left + rectangleWidth;
        int bottom = top + rectangleHeight;

        // Dessine le rectangle
        canvas.drawRect(left, top, right, bottom, paint);
    }
}