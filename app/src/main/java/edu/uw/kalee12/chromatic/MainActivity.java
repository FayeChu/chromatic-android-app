package edu.uw.kalee12.chromatic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button importPhoto = (Button) findViewById(R.id.action_import);
        importPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreatePaletteActivity.class);
                startActivity(intent);
            }
        });
    }
}

/*
    Karen :
        App Logo
        [Take photo]
        [Import photo]
        [View Gallery]
 */