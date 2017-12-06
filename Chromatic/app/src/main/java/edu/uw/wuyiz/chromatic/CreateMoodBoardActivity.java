package edu.uw.wuyiz.chromatic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CreateMoodBoardActivity extends AppCompatActivity {

    // drawingView for creating mood board
    public DrawingView drawingView;
    // store created mood board for this activity
    public static Bitmap createdMoodBoardBitmap;

//    private List<String> checkedList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_mood_board);

//        checkedList = new ArrayList<>();

        Intent intent = getIntent();
        List<String> checkedList = intent.getStringArrayListExtra("checkedList");

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // initialize the drawingView and createdMoodBoardBitmap
        drawingView = findViewById(R.id.drawing_view);
        createdMoodBoardBitmap = null;

        findViewById(R.id.create_mood_board_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // when clicking Create, all the MoodBoardComponentBitmap objects
                // in the drawingView are turned into a Bitmap and returned
                createdMoodBoardBitmap = getBitmap(drawingView);
                // show created mood board
                startActivity(new Intent(CreateMoodBoardActivity.this,
                        ShowCreatedMoodBoardActivity.class));
            }
        });

        findViewById(R.id.add_text_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // show the keyboard with 200ms delay after clicking the Add Text button
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager inputMethodManager =
                                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }, 200);

                // null parent ViewGroup for AlertDialog
                final ViewGroup nullParent = null;
                // get add_text_prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(CreateMoodBoardActivity.this);
                View promptsView = layoutInflater.inflate(R.layout.add_text_prompts, nullParent);

                // initialize promptsView components
                final EditText editTextUserInput = promptsView
                        .findViewById(R.id.user_input_edit_text);
                // request focus on initialization
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
                                        String userInputText = editTextUserInput.getText().toString();

                                        // if the user input string is valid
                                        if (userInputText.length() > 0) {
                                            try {
                                                // draw the string on a new bitmap's canvas
                                                // and add the bitmap to the drawingView
                                                Paint paint = new Paint();
                                                paint.setStyle(Paint.Style.FILL);
                                                paint.setAntiAlias(true);
                                                paint.setTextSize(60.0F);
                                                paint.setColor(Color.BLACK);
                                                float baseline = -paint.ascent();
                                                int width = (int) (paint.measureText(userInputText)
                                                        + 0.5f);
                                                int height = (int) (baseline + paint.descent() + 0.5f);

                                                Bitmap textBitmap = Bitmap.createBitmap(
                                                        width, height, Bitmap.Config.ARGB_8888);
                                                Canvas canvas = new Canvas(textBitmap);
                                                canvas.drawText(userInputText, 0, baseline, paint);

//                                                byte[] decodedString = Base64.decode(userInput, Base64.DEFAULT);
//                                                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,
//                                                        0, decodedString.length);

                                                // create a new MoodBoardComponentBitmap object
                                                MoodBoardComponentBitmap tempTextMoodBoardComponentBitmap =
                                                        new MoodBoardComponentBitmap(textBitmap);
                                                tempTextMoodBoardComponentBitmap.setId(
                                                        drawingView.bitmapList.size() + 1);
                                                tempTextMoodBoardComponentBitmap.widthAfterScale =
                                                        textBitmap.getWidth();
                                                tempTextMoodBoardComponentBitmap.heightAfterScale =
                                                        textBitmap.getHeight();

                                                // add to drawingView
                                                drawingView.addBitmap(tempTextMoodBoardComponentBitmap);

//                                                Toast.makeText(CreateMoodBoardActivity.this,
//                                                        String.valueOf(drawingView.bitmapList.size()) + "a",
//                                                        Toast.LENGTH_SHORT).show();

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

        // set offset when clicking the direction button
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

        // rotate selected image by 45 or -45 degrees when clicking the rotate button
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

        // zoom selected image in or out when clicking the zoom button
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

        // initialize the drawingView
        init(checkedList);

        Toast.makeText(this, String.valueOf(checkedList.size()) + "a", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void init(List<String> checkedList) {
//        for (int i = 0; i < 3; i++) {
//            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//            MoodBoardComponentBitmap moodBoardComponentBitmap = new MoodBoardComponentBitmap(bitmap);
//            moodBoardComponentBitmap.setId(i);
//            moodBoardComponentBitmap.widthAfterScale = bitmap.getWidth();
//            moodBoardComponentBitmap.heightAfterScale = bitmap.getHeight();
//            drawingView.addBitmap(moodBoardComponentBitmap);
//        }
        for (int i = 0; i < checkedList.size(); i++) {

//            Toast.makeText(CreateMoodBoardActivity.this,
//                    checkedList.size(),
//                    Toast.LENGTH_SHORT).show();

//            Uri imageUri = Uri.parse(checkedList.get(i));
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                MoodBoardComponentBitmap moodBoardComponentBitmap = new MoodBoardComponentBitmap(bitmap);
//                moodBoardComponentBitmap.setId(i);
//                moodBoardComponentBitmap.widthAfterScale = bitmap.getWidth();
//                moodBoardComponentBitmap.heightAfterScale = bitmap.getHeight();
//                drawingView.addBitmap(moodBoardComponentBitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            Bitmap bitmap = imageUriStringToBitmap(checkedList.get(i));
            MoodBoardComponentBitmap moodBoardComponentBitmap = new MoodBoardComponentBitmap(bitmap);
            moodBoardComponentBitmap.setId(i);
            moodBoardComponentBitmap.widthAfterScale = bitmap.getWidth();
            moodBoardComponentBitmap.heightAfterScale = bitmap.getHeight();
            drawingView.addBitmap(moodBoardComponentBitmap);
        }
    }

    private Bitmap imageUriStringToBitmap(String imageUriStr) {
        try {
            Uri imageUri = Uri.parse(imageUriStr);
            InputStream imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    private Bitmap stringToBitmap(String encodedString) {
        try {
            byte[] encodedByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodedByte, 0, encodedByte.length);

            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    // given a view, turn it into a Bitmap object
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