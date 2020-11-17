package com.example.touchgesture;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private ImageView imageView;
    private float scale = 1f;
     ScaleGestureDetector detector;

    private ViewGroup RootLayout;
    private int Position_X;
    private int Position_Y;
    long startTime = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView=findViewById(R.id.imageView);
        detector = new ScaleGestureDetector(this,new ScaleListener());

        RootLayout = findViewById(R.id.rootLayout);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        return super.onTouchEvent(event);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();

        int pointerCount = event.getPointerCount();


        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                Position_X = X - layoutParams.leftMargin;
                Position_Y = Y - layoutParams.topMargin;
                break;

            case MotionEvent.ACTION_UP:
                if (startTime != 0) {
                    if (System.currentTimeMillis() - startTime < 200) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Are you sure you want to delete this?");
                        builder.setPositiveButton("Yes", (dialog, which) -> view.setVisibility(View.GONE));

                        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }

                }
                startTime = System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

            case MotionEvent.ACTION_POINTER_UP:
                break;

            case MotionEvent.ACTION_MOVE:

                if (pointerCount == 1){
                    RelativeLayout.LayoutParams Params = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    Params.leftMargin = X - Position_X;
                    Params.topMargin = Y - Position_Y;
                    Params.rightMargin = -500;
                    Params.bottomMargin = -500;
                    view.setLayoutParams(Params);
                }

                if (pointerCount == 2){

                    RelativeLayout.LayoutParams layoutParams1 =  (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams1.width = Position_X +(int)event.getX();
                    layoutParams1.height = Position_Y + (int)event.getY();
                    view.setLayoutParams(layoutParams1);
                }

                //Rotation
                if (pointerCount == 3){
                    //Rotate the ImageView
                    view.setRotation(view.getRotation() + 10.0f);
                }

                break;
        }

// Schedules a repaint for the root Layout.
        RootLayout.invalidate();
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        float onScaleBegin = 0;
        float onScaleEnd = 0;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale *= detector.getScaleFactor();
            imageView.setScaleX(scale);
            imageView.setScaleY(scale);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            onScaleBegin = scale;
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            onScaleEnd = scale;

            if (onScaleEnd > onScaleBegin){
                Toast.makeText(getApplicationContext(),"Scaled Up by a factor of  " + onScaleEnd / onScaleBegin, Toast.LENGTH_SHORT  ).show();
            }

            if (onScaleEnd < onScaleBegin){
                Toast.makeText(getApplicationContext(),"Scaled Down by a factor of  " + onScaleBegin / onScaleEnd, Toast.LENGTH_SHORT  ).show();
            }

            super.onScaleEnd(detector);
        }
    }
}
