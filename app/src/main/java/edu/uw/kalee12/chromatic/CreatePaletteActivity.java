package edu.uw.kalee12.chromatic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CreatePaletteActivity extends AppCompatActivity {

    private Button pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_palette);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Button colorOne = (Button) findViewById(R.id.color_one);
        colorOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    set(colorOne);
                }
                return true;
            }
        });

        final Button colorTwo = (Button) findViewById(R.id.color_two);
        colorTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    set(colorTwo);
                }
                return true;
            }
        });

        final Button colorThree = (Button) findViewById(R.id.color_three);
        colorThree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    set(colorThree);
                }
                return true;
            }
        });

        final Button colorFour = (Button) findViewById(R.id.color_four);
        colorFour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    set(colorFour);
                }
                return true;
            }
        });

        final Button colorFive = (Button) findViewById(R.id.color_five);
        colorFive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    set(colorFive);
                }
                return true;
            }
        });

        final ImageView image = (ImageView) findViewById(R.id.photo_box);
        final Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ||
                        motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    float[] points = getPointerCoords(image, motionEvent);
                    if (points[0] > 0 && points[0] < bitmap.getWidth()
                            && points[1] > 0 && points[1] < bitmap.getHeight()) {
                        int color = bitmap.getPixel((int) points[0], (int) points[1]);
                        if (pressed != null) {
                            pressed.setBackgroundColor(color);
                        }
                    }
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_create_palette, menu);
        return true;
    }

    final float[] getPointerCoords(ImageView view, MotionEvent e)
    {
        final int index = e.getActionIndex();
        final float[] coords = new float[] { e.getX(index), e.getY(index) };
        Matrix matrix = new Matrix();
        view.getImageMatrix().invert(matrix);
        matrix.postTranslate(view.getScrollX(), view.getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    public void set(Button button) {
        button.setPressed(!button.isPressed());
        if (pressed != null) {
            pressed.setPressed(false);
        }
        if (button.isPressed()) {
            pressed = button;
        } else {
            pressed = null;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.next:
                Intent intent = new Intent(CreatePaletteActivity.this,
                        SetPaletteInfoActivity.class);

                Button[] buttons = new Button[] {findViewById(R.id.color_one), findViewById(R.id.color_two),
                        findViewById(R.id.color_three), findViewById(R.id.color_four),
                        findViewById(R.id.color_five)};

                int[] colors = new int[5];
                for (int i = 0; i < buttons.length; i++) {
                    ColorDrawable buttonColor = (ColorDrawable) buttons[i].getBackground();
                    colors[i] = buttonColor.getColor();
                }
                intent.putExtra("colors", colors);

                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

/*
    Karen :
        User uses default camera or imports a photo
        Creating a palette
            (1) pick five points for user to show potential colors allowing user
            to know whether they want to take a new picture
            (2) let user edit palette by choosing palette square and picking part of picture
        I might split this into two parts - picture preview, and the actual editing
 */