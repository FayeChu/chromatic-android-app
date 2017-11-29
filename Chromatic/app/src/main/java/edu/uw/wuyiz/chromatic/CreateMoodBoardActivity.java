package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


public class CreateMoodBoardActivity extends AppCompatActivity {

    public DrawingView drawingView;

    public static Bitmap createdMoodBoardBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood_board);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawingView = (DrawingView) findViewById(R.id.drawing_view);

        findViewById(R.id.create_mood_board).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createdMoodBoardBitmap = getBitmap(drawingView);
                startActivity(new Intent(CreateMoodBoardActivity.this,
                        ShowCreatedMoodBoardActivity.class));
            }
        });

        findViewById(R.id.up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setOffset(DrawingView.DIRECTION.UP);
            }
        });

        findViewById(R.id.left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setOffset(DrawingView.DIRECTION.LEFT);
            }
        });

        findViewById(R.id.right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setOffset(DrawingView.DIRECTION.RIGHT);
            }
        });

        findViewById(R.id.down_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.setOffset(DrawingView.DIRECTION.DOWN);
            }
        });

        init();
    }

    private void init() {
        for (int i = 0; i < 3; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            CustomBitmap customBitmap = new CustomBitmap(bitmap);
            customBitmap.setId(i);
            customBitmap.widthAfterScale = bitmap.getWidth();
            customBitmap.heightAfterScale = bitmap.getHeight();
            drawingView.addBitmap(customBitmap);
        }
    }

    private static Bitmap getBitmap(View view) {
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCacheDrawing = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            view.destroyDrawingCache();
        }

        view.buildDrawingCache();

        Bitmap cacheBitmap = view.getDrawingCache();

        if (cacheBitmap == null) {
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
        // restore the view
        view.destroyDrawingCache();
        view.setWillNotCacheDrawing(willNotCacheDrawing);
        view.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }
}
/*
    Karen :
        Creating a mood board options
            (1) A particular view as a collection of palettes / pictures
            (2) having an exportable image (which moodboards are typically used as)
 */
