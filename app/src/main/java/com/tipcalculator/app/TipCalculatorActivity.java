package com.tipcalculator.app;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TipCalculatorActivity extends AppCompatActivity {

    @Bind(R.id.container) FrameLayout layout; //butterknife
    @Bind(R.id.my_awesome_toolbar) Toolbar toolbar; //support toolbar
    @Bind(R.id.appTitleTextView) TextView appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);
        ButterKnife.bind(this); //creates all variables in scope

        buildToolBar(); //sets up the toolbar for the current activity

        if (savedInstanceState == null) { //check if fragment has not been initialized
            getSupportFragmentManager().beginTransaction()
                    .replace(layout.getId(), new TipFragment()).commit();
        }

    }

    //build custom elements onto support toolbar
    //toolbar is the successor to action bar for newer API's past kitkat
    private void buildToolBar() {

        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#60BCD3"));
        new Fonts().setFontLollipopLightRoboto(appTitle);
    }


}
