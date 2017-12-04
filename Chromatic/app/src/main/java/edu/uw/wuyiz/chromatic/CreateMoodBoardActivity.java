package edu.uw.wuyiz.chromatic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateMoodBoardActivity extends AppCompatActivity {

    public static DrawingView drawingView;

    public static Bitmap createdMoodBoardBitmap;

    public static List<CustomBitmap> textCustomBitmapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood_board);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawingView = (DrawingView) findViewById(R.id.drawing_view);
        createdMoodBoardBitmap = null;
        textCustomBitmapList = new ArrayList<>();

        findViewById(R.id.create_mood_board_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createdMoodBoardBitmap = getBitmap(drawingView);
                startActivity(new Intent(CreateMoodBoardActivity.this,
                        ShowCreatedMoodBoardActivity.class));
            }
        });

        findViewById(R.id.add_text_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 250);

                // get add_text_prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(CreateMoodBoardActivity.this);
                View promptsView = layoutInflater.inflate(R.layout.add_text_prompts, null);

                final EditText editTextUserInput = promptsView
                        .findViewById(R.id.user_input_edit_text);
                editTextUserInput.setFocusable(true);
                editTextUserInput.setFocusableInTouchMode(true);
                editTextUserInput.requestFocus();

//                final InputMethodManager inputMethodManager =
//                        (InputMethodManager) editTextUserInput
//                                .getContext()
//                                .getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.showSoftInput(editTextUserInput, 0);

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(CreateMoodBoardActivity.this);

                // set add_text_prompts.xml to alertDialogBuilder
                alertDialogBuilder.setView(promptsView);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // get user input and transform to String
                                        String userInput = editTextUserInput.getText().toString();

                                        // if the user input String is valid
                                        if (userInput != null && userInput.length() > 0) {
                                            try {
                                                // turn into a bit map
                                                byte[] decodedString = Base64.decode(userInput, Base64.DEFAULT);
                                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,
                                                        0, decodedString.length);
                                                CustomBitmap tempTextCustomBitmap =
                                                        new CustomBitmap(bitmap);

                                                if (tempTextCustomBitmap != null) {
                                                    drawingView.addBitmap(tempTextCustomBitmap);
                                                    textCustomBitmapList.add(tempTextCustomBitmap);
                                                }

                                                Toast.makeText(CreateMoodBoardActivity.this,
                                                        String.valueOf(drawingView.getViews().size()) + "a",
                                                        Toast.LENGTH_SHORT).show();

                                            } catch (Exception e) {
                                                e.getMessage();
                                            }
                                        }
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();

//                Toast.makeText(CreateMoodBoardActivity.this,
//                        String.valueOf(textCustomBitmap == null) + "b",
//                        Toast.LENGTH_SHORT).show();
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

        findViewById(R.id.rotate_left_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.rotateSelected(-45);
            }
        });

        findViewById(R.id.rotate_right_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.rotateSelected(45);
            }
        });

        findViewById(R.id.zoom_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.scaleSelected((float) (1.15));
            }
        });

        findViewById(R.id.zoom_out_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.scaleSelected((float) (0.85));
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