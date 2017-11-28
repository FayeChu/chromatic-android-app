package edu.uw.wuyiz.chromatic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class CreationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creations);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_moodboards:
                                Intent moodBoardsIntent = new Intent(getApplicationContext(), MoodBoardActivity.class);
                                startActivity(moodBoardsIntent);
                            case R.id.action_gallery:
                                Intent galleryIntent = new Intent(getApplicationContext(), GalleryScreenActivity.class);
                                startActivity(galleryIntent);
                            case R.id.action_creations:
//                                Intent creationsIntent = new Intent(getApplicationContext(), CreationsActivity.class);
//                                startActivity(creationsIntent);
                                return true;

                        }
                        return true;
                    }
                });
    }

}
