package kr.ac.cnu.computer.advtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.mlkit.vision.common.PointF3D;
import com.google.mlkit.vision.facemesh.FaceMesh;
import com.google.mlkit.vision.facemesh.FaceMeshPoint;

import java.util.ArrayList;
import java.util.List;

public final class CustomView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    List<FaceMesh> target = new ArrayList<>();

    public CustomView(Context context) {
        super(context);
        setBackgroundColor(Color.TRANSPARENT);
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.STROKE);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        synchronized (this) {
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            float scaleX = getWidth() / 720.f;
            float scaleY = getHeight() / 1280.f;
            Rect toDraw = new Rect();
            for (FaceMesh mesh : target) {
                Rect temp = mesh.getBoundingBox();
                toDraw.left = (int)((360 - (temp.left - 360)) * scaleX);
                toDraw.right = (int)((360 -(temp.right - 360)) * scaleX);


                toDraw.top = (int)(temp.top * scaleY);
                toDraw.bottom = (int)(temp.bottom * scaleY);
                canvas.drawRect(toDraw, this.paint);

                List<FaceMeshPoint> meshPoints = mesh.getAllPoints();
                for (FaceMeshPoint meshPoint : meshPoints) {
                    PointF3D tempPoint = meshPoint.getPosition();

                    canvas.drawCircle(
                            (360 - (tempPoint.getX() - 360)) * scaleX,
                            tempPoint.getY() * scaleY,
                            3.f,
                            this.paint);
                }
            }
        }
    }

    public void setTargets(List<FaceMesh> list) {
        synchronized (this) {
            target.clear();
            target.addAll(list);
            postInvalidate();
        }
    }
}
