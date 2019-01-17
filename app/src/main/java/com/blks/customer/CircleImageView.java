package com.blks.customer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blks.antrscapp.R;


/**
 * 圆形图片
 * Created by limh on 2018/11/2.
 */

public class CircleImageView extends View {

    private int pictureId;
    Bitmap bitmap;
    Shader shader;
    Paint paint;
    float mRadius;
    private float mScale;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paint = new Paint();
        paint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView);
        pictureId = typedArray.getResourceId(R.styleable.CircleImageView_imgSrc, 0);
        init();
        typedArray.recycle();
    }

    public void setPictureId (int resId) {
        pictureId = resId;
        init();
        invalidate();
    }

    public void setBitmap (Bitmap bitmap) {
        this.bitmap = bitmap;
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        invalidate();
    }

    private void init() {
        bitmap = BitmapFactory.decodeResource(getResources(), pictureId);
        shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pictureId == 0) {
            return;
        }



        //计算缩放比例
        mScale = ((mRadius * 2.0f) / Math.min(bitmap.getHeight(), bitmap.getWidth()));
        //可以保证显示的时候图片内容照应Shader大小,否者图片显示偏移
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        shader.setLocalMatrix(matrix);

        paint.setShader(shader);
        canvas.drawCircle(mRadius, mRadius, mRadius, paint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSpecModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecModel = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//取宽度或者高度的最小值
        int size = Math.min(widthSize, heightSize);
        mRadius = size / 2;

//        if (widthSpecModel != MeasureSpec.EXACTLY ||
////                heightSpecModel != MeasureSpec.EXACTLY) {
////            widthSize = Math.max(bitmap.getWidth(), bitmap.getHeight());
////            heightSize = widthSize;
////        }

        setMeasuredDimension(size, size);
    }

}
