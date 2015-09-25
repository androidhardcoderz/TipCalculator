package com.tipcalculator.app;

/*
 * Helper class that contains all external fonts
 * location: /assets/fonts/
 * each method takes in a textview assigns the typeface created and 
 * returns that trextview
 */

import android.graphics.Typeface;
import android.widget.TextView;

public class Fonts {

	public Fonts() {
		// TODO Auto-generated constructor stub
	}

	// sets custom font from assets folder in project hierarchy
	public void setFontLollipopLightRoboto(TextView textView) {
		Typeface tf = Typeface.createFromAsset(textView.getContext()
				.getAssets(), "fonts/Roboto-LightItalic.ttf");
		textView.setTypeface(tf);

	}
	
	

}
