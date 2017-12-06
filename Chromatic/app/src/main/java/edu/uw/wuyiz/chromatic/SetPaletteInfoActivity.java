package edu.uw.wuyiz.chromatic;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;

public class SetPaletteInfoActivity extends AppCompatActivity {

    private int[] colors;
    private DatabaseReference mDatabase;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_palette_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        colors = intent.getIntArrayExtra("colors");
        TextView[] boxes = new TextView[] {findViewById(R.id.set_one), findViewById(R.id.set_two),
                findViewById(R.id.set_three), findViewById(R.id.set_four),
                findViewById(R.id.set_five)};

        for (int i = 0; i < colors.length; i++) {
            boxes[i].setBackgroundColor(colors[i]);
        }

        imageUri = Uri.parse(intent.getStringExtra("uri"));
        ImageView image = findViewById(R.id.image);
        try {
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        ImageButton location = (ImageButton) findViewById(R.id.btn_location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SetPaletteInfoActivity.this), 1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        TextView txtDate = (TextView) findViewById(R.id.txt_date);
        txtDate.setText(df.format(c.getTime()));

        ImageButton date = (ImageButton) findViewById(R.id.btn_date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                TextView location = (TextView) findViewById(R.id.txt_location);
                location.setText(place.getName() + " " + place.getAddress());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.save:
                // Palette Name
                EditText pName = findViewById(R.id.palette_name);
                if (TextUtils.isEmpty(pName.getText())) {
                    Toast message = Toast.makeText(getApplicationContext(), "Set Palette Name", Toast.LENGTH_SHORT);
                    message.show();
                } else {
                    Log.v("name", pName.getText().toString());
                    // Palette Location
                    TextView pLocation = findViewById(R.id.txt_location);
                    Log.v("location", pLocation.getText().toString());
                    // Palette Date
                    TextView pDate = findViewById(R.id.txt_date);
                    Log.v("date", pDate.getText().toString());
                    // Palette Colors // Convert to hex codes
                    Log.v("colors", Arrays.toString(colors));
                    // Palette Notes
                    EditText pNote = findViewById(R.id.palette_desc);
                    Log.v("Note", pNote.getText().toString());

                    final String PALETTE_COLLECTION_STORAGE_KEY = getString(R.string.palette_collection_storage_key);

                    // Upload palette information to Firebase
                    // Name / location / colors / notes / image
                    // Storage -> image
                    Palette palette = new Palette(pName.getText().toString(), imageUri.toString(),
                            pLocation.getText().toString(), pDate.getText().toString(),
                            colors[0], colors[1], colors[2], colors[3], colors[4]);
                    mDatabase.child(PALETTE_COLLECTION_STORAGE_KEY)
                            .child(mDatabase.push().getKey())
                            .setValue(palette);

                    Intent intent = new Intent(SetPaletteInfoActivity.this, PaletteGalleryScreenActivity.class);
                    startActivity(intent);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return dialog;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            TextView date = (TextView) getActivity().findViewById(R.id.txt_date);
            date.setText(month + "/" + day + "/" + year);
        }
    }

}

/*
    Karen :
        Have picture & palette
        Let user set
            (1) item name
            (2) location / time
            (3) description / notes
 */

