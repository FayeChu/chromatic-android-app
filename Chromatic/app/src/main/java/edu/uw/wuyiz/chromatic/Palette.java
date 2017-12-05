package edu.uw.wuyiz.chromatic;

import android.net.Uri;

/**
 * Created by leeka on 12/4/2017.
 */

public class Palette {
    public String name;
    public String imageUri;
    public String location;
    public String date;
    public int colorOne;
    public int colorTwo;
    public int colorThree;
    public int colorFour;
    public int colorFive;

    public Palette(){

    }

    public Palette(String name, String imageUri, String location, String date, int colorOne, int colorTwo, int colorThree,
                   int colorFour, int colorFive) {
        this.name = name;
        this.imageUri = imageUri;
        this.location = location;
        this.date = date;
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
        this.colorThree = colorThree;
        this.colorFour = colorFour;
        this.colorFive = colorFive;
    }
}


