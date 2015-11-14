package com.anthony.myweather;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by anthonyo on 23/5/2015.
 */
public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardview_details);
        setTitle(R.string.auckland);
    }
}
