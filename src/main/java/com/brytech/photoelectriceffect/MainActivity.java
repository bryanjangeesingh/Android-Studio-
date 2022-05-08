package com.brytech.photoelectriceffect;
//Intellectual Property of Brytech
//Bryan Jangeesingh - 2020
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.rtugeek.android.colorseekbar.ColorSeekBar;

public class MainActivity extends AppCompatActivity {

    double e = 2.718281828459045235360287471352662497757247093699959574966967627724076630353;
    int min = -250;
    int max = 250;
    //int initialState = 0, initialState1=0, initialState2 = 0;
    int initialState, initialState1, initialState2;
    int min1 = 250, max1 = 700;
    public int color;
    int intenAdjuster=99;
    int initialColour;

    public static double currentParameterAdjuster;
    public static double wavelengthParameterAdjuster;
    public static double decimalValueOfVoltage;
    RelativeLayout mainScreen;
    String hexColor;
    String hexColor2;

    public String instantaneousCurrentString;
    public String instantaneousCurrentValueString;
    double instantaneousCurrentValue;

    InterstitialAd interstitialAd;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        interstitialAd = new InterstitialAd(MainActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.interstitialAdId));
        AdRequest adRequest2 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest2);
        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }

        });


        mAdView = (AdView) findViewById(R.id.adViewBanner);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest1);

        //This is the debug code for when ads aren't showing
        /*mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                Toast.makeText(getApplicationContext(),"reason"+i,Toast.LENGTH_SHORT).show();

            }
        });*/


        final ImageView imgAnimation = (ImageView) findViewById(R.id.imageViewElectrons);

        imgAnimation.setImageResource(R.drawable.movingelectrons);
        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
        movingElectrons.start();

        mainScreen = (RelativeLayout) findViewById(R.id.screen);
        initialColour = Color.parseColor("#95000085");
        mainScreen.setBackgroundColor(initialColour);


        wavelengthParameterAdjuster = 9.8;currentParameterAdjuster = 133.333;decimalValueOfVoltage = 0;

        final TextView textViewVoltage = (TextView) findViewById(R.id.textViewVoltage);
        final TextView textViewIntensity = (TextView) findViewById(R.id.textViewIntensity);
        final TextView textViewWavelength = (TextView) findViewById(R.id.textViewWavelength);
        final SeekBar seekBarVoltage = (SeekBar) findViewById(R.id.seekBarVoltage);
        final SeekBar seekBarIntensity = (SeekBar) findViewById(R.id.seekBarIntensity);
        final ColorSeekBar seekBarWavelength = (ColorSeekBar) findViewById(R.id.seekBarWavelength);
        final TextView textViewCurrent = (TextView) findViewById(R.id.textViewCurrent);

        //Code for drop down spinner menu with metals
        Spinner spinnerSelectMetal = (Spinner) findViewById(R.id.spinnerSelectMetal);

        ArrayAdapter<String> metalsAdaptor = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.listofmetals));

        metalsAdaptor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectMetal.setAdapter(metalsAdaptor);

        spinnerSelectMetal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Rubidium")) {

                    //wavelengthParameterAdjuster = 9.8;
                    //currentParameterAdjuster =133.33;
                    //decimalValueOfVoltage = 0;


                    seekBarVoltage.setMax(max - min);
                    seekBarVoltage.setProgress(initialState - min);

                    seekBarWavelength.setMaxPosition(max1-min1);
                    seekBarWavelength.setColorBarPosition(0-min1);

                    seekBarIntensity.setMax(100-0);
                    seekBarIntensity.setProgress(100);

                    initialState1=400;

                    //Wavelength
                    seekBarWavelength.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                        @Override
                        public void onColorChangeListener(int progressWavelength, int alphaBar, int color) {

                            TextView textHoldColour = (TextView) findViewById(R.id.textViewHoldColour);
                            textHoldColour.setTextColor(color);
                            final int textColor = textHoldColour.getCurrentTextColor();
                            hexColor2 = String.format("%06X", (0xFFFFFF & textColor));

                            if(intenAdjuster >10 && initialState1>=400) {
                                mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor2));
                            }
                            else{
                                mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor2));
                            }

                            initialState1 = progressWavelength + min1;
                            textViewWavelength.setText((initialState1) + "nm");
                            if(initialState1<543.478) {
                                //wavelengthParameterAdjuster = -1 / (0.0016 * (0.7955612 * progressWavelength - 382));
                                wavelengthParameterAdjuster = 0.0066*Math.pow(e, 0.009*initialState1 +3.7);
                                double wavelengthFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 2.2)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 2.2) + 8.13);
                                textViewCurrent.setText(String.format("%.2f", wavelengthFormulaRubidium - 0.005) + "nA");
                            }
                            else {
                                textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                            }

                            instantaneousCurrentString = textViewCurrent.getText().toString();
                            instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                            instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                            if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                imgAnimation.setImageResource(R.drawable.movingelectrons);
                                AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                movingElectrons.start();
                            }
                            else{
                                if(instantaneousCurrentValue>70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue==0){
                                        imgAnimation.setImageResource(R.drawable.electrons_blank);
                                    }
                                }
                            }


                        }
                    });


                    //Voltage
                    seekBarVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            initialState = progress + min;
                            decimalValueOfVoltage = ((double) initialState / 100);
                            textViewVoltage.setText("" + decimalValueOfVoltage + "V");
                            if (initialState1 < 543.478) {
                                double voltageFormulaForRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 2.2)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 2.2) + 8.13);
                                textViewCurrent.setText(String.format("%.2f", voltageFormulaForRubidium - 0) + "nA ");
                            }

                            else{
                                textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                            }

                            instantaneousCurrentString = textViewCurrent.getText().toString();
                            instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                            instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                            if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                imgAnimation.setImageResource(R.drawable.movingelectrons);
                                AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                movingElectrons.start();
                            }
                            else{
                                if(instantaneousCurrentValue>70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue==0){
                                        imgAnimation.setImageResource(R.drawable.electrons_blank);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {
                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }

                    });

                    //Intensity
                    seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progressIntensity, boolean fromUser) {

                            ColorDrawable viewColor = (ColorDrawable) mainScreen.getBackground();
                            int colorId = viewColor.getColor();
                            hexColor = String.format("%06X", (0xFFFFFF & colorId));

                            initialState2 = progressIntensity +0;
                            intenAdjuster =initialState2-5;
                            currentParameterAdjuster = 1.33333333*initialState2;
                            if (initialState1 < 543.478) {
                                double intensityFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 2.2)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 2.2) + 8.13);
                                textViewIntensity.setText((initialState2) + "%");
                                textViewCurrent.setText(String.format("%.2f", intensityFormulaRubidium - 0) + "nA ");

                            }
                            else{
                                textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                textViewIntensity.setText((initialState2) + "%");
                            }

                            if(intenAdjuster>10  && initialState1>=400) {
                                mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor));
                            }
                            else{
                                mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor));
                            }

                            instantaneousCurrentString = textViewCurrent.getText().toString();
                            instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                            instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                            if(instantaneousCurrentValue>0.00 && instantaneousCurrentValue<70.00) {
                                imgAnimation.setImageResource(R.drawable.movingelectrons);
                                AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                movingElectrons.start();
                            }

                            else{
                                if(instantaneousCurrentValue>70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue==0){
                                        imgAnimation.setImageResource(R.drawable.electrons_blank);
                                    }
                                }
                            }

                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {
                        }
                    });

                }

                else {

                    String item = parent.getItemAtPosition(position).toString();
                    if (parent.getItemAtPosition(position).equals("Calcium")){
                        AdRequest adRequest2 = new AdRequest.Builder().build();
                        interstitialAd.loadAd(adRequest2);
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdLoaded() {
                                interstitialAd.show();
                            }
                        });

                        //This is the debug code for when interstitial ads don't show
                        /**interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                                Toast.makeText(getApplicationContext(),"reason for ad failure =" +i,Toast.LENGTH_SHORT).show();

                            }
                        });*/

                        //wavelengthParameterAdjuster=40;
                        //currentParameterAdjuster =133.33;
                        //decimalValueOfVoltage = 0;


                        seekBarVoltage.setMax(max - min);
                        seekBarVoltage.setProgress(initialState - min);

                        seekBarWavelength.setMaxPosition(max1 -min1);
                        seekBarWavelength.setColorBarPosition(0-min1);

                        seekBarIntensity.setMax(100-0);
                        seekBarIntensity.setProgress(100);
                        mainScreen.setBackgroundColor(Color.parseColor("#00000000"));

                        //Wavelength
                        seekBarWavelength.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int progressWavelength, int alphaBar, int color) {

                                TextView textHoldColour = (TextView) findViewById(R.id.textViewHoldColour);
                                textHoldColour.setTextColor(color);
                                final int textColor = textHoldColour.getCurrentTextColor();
                                hexColor2 = String.format("%06X", (0xFFFFFF & textColor));

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor2));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor2));
                                }

                                initialState1 = progressWavelength + min1;
                                textViewWavelength.setText((initialState1) + "nm");
                                if(initialState1<597.609) {
                                    //wavelengthParameterAdjuster = -1 / (0.0016 * (0.7955612 * progressWavelength - 382));
                                    wavelengthParameterAdjuster = 50*Math.pow(e, 0.0144*initialState1 -6);
                                    double wavelengthFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.791)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.791) + 2.1);
                                    textViewCurrent.setText(String.format("%.2f", wavelengthFormulaRubidium - 0.005) + "nA");
                                }
                                else {
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }



                            }
                        });


                        //Voltage
                        seekBarVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                initialState = progress + min;
                                decimalValueOfVoltage = ((double) initialState / 100);
                                textViewVoltage.setText("" + decimalValueOfVoltage + "V");
                                if (initialState1 < 597.609) {
                                    double voltageFormulaForRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.791)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.791) + 2.1);
                                    textViewCurrent.setText(String.format("%.2f", voltageFormulaForRubidium - 0) + "nA ");
                                }

                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }

                        });

                        //Intensity
                        seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progressIntensity, boolean fromUser) {

                                ColorDrawable viewColor = (ColorDrawable) mainScreen.getBackground();
                                int colorId = viewColor.getColor();
                                hexColor = String.format("%06X", (0xFFFFFF & colorId));

                                initialState2 = progressIntensity +0;
                                intenAdjuster =initialState2-5;
                                currentParameterAdjuster = 1.33333333*initialState2;
                                if (initialState1 < 597.609) {
                                    double intensityFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.791)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.791) + 2.1);
                                    textViewIntensity.setText((initialState2) + "%");
                                    textViewCurrent.setText(String.format("%.2f", intensityFormulaRubidium - 0) + "nA ");

                                }
                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                    textViewIntensity.setText((initialState2) + "%");
                                }

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor));
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0.00 && instantaneousCurrentValue<70.00) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }

                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });

                    }
                    else if(parent.getItemAtPosition(position).equals("Thorium")) {
                        AdRequest adRequest2 = new AdRequest.Builder().build();
                                interstitialAd.loadAd(adRequest2);
                                interstitialAd.setAdListener(new AdListener(){
                                    @Override
                                    public void onAdLoaded() {
                                        interstitialAd.show();
                                    }
                                });

                                //wavelengthParameterAdjuster=40;
                                //currentParameterAdjuster =133.33;
                                //decimalValueOfVoltage = 0;


                                seekBarVoltage.setMax(max - min);
                                seekBarVoltage.setProgress(initialState - min);

                                seekBarWavelength.setMaxPosition(max1 -min1);
                                seekBarWavelength.setColorBarPosition(0-min1);

                                seekBarIntensity.setMax(100-0);
                                seekBarIntensity.setProgress(100);
                                mainScreen.setBackgroundColor(Color.parseColor("#00000000"));

                                //Wavelength
                                seekBarWavelength.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                                    @Override
                                    public void onColorChangeListener(int progressWavelength, int alphaBar, int color) {

                                        TextView textHoldColour = (TextView) findViewById(R.id.textViewHoldColour);
                                        textHoldColour.setTextColor(color);
                                        final int textColor = textHoldColour.getCurrentTextColor();
                                        hexColor2 = String.format("%06X", (0xFFFFFF & textColor));

                                        if(intenAdjuster>10  && initialState1>=400) {
                                            mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor2));
                                        }
                                        else{
                                            mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor2));
                                        }

                                        initialState1 = progressWavelength + min1;
                                        textViewWavelength.setText((initialState1) + "nm");
                                        if(initialState1<364.963) {
                                            //wavelengthParameterAdjuster = -1 / (0.0016 * (0.7955612 * progressWavelength - 382));
                                            wavelengthParameterAdjuster = 50*Math.pow(e, 0.0282*initialState1 -10);
                                            double wavelengthFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.1)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.1) + 1);
                                            textViewCurrent.setText(String.format("%.2f", wavelengthFormulaRubidium - 0.005) + "nA");
                                        }
                                        else {
                                            textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                        }

                                        instantaneousCurrentString = textViewCurrent.getText().toString();
                                        instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                        instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                        if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                            imgAnimation.setImageResource(R.drawable.movingelectrons);
                                            AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                            movingElectrons.start();
                                        }
                                        else{
                                            if(instantaneousCurrentValue>70) {
                                                imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                                AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                                movingElectrons.start();
                                            }
                                            else{
                                                if(instantaneousCurrentValue==0){
                                                    imgAnimation.setImageResource(R.drawable.electrons_blank);
                                                }
                                            }
                                        }



                                    }
                                });


                                //Voltage
                                seekBarVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                        initialState = progress + min;
                                        decimalValueOfVoltage = ((double) initialState / 100);
                                        textViewVoltage.setText("" + decimalValueOfVoltage + "V");
                                        if (initialState1 < 364.963) {
                                            double voltageFormulaForRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.1)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.1) + 1);
                                            textViewCurrent.setText(String.format("%.2f", voltageFormulaForRubidium - 0) + "nA ");
                                        }

                                        else{
                                            textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                        }

                                        instantaneousCurrentString = textViewCurrent.getText().toString();
                                        instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                        instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                        if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                            imgAnimation.setImageResource(R.drawable.movingelectrons);
                                            AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                            movingElectrons.start();
                                        }
                                        else{
                                            if(instantaneousCurrentValue>70) {
                                                imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                                AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                                movingElectrons.start();
                                            }
                                            else{
                                                if(instantaneousCurrentValue==0){
                                                    imgAnimation.setImageResource(R.drawable.electrons_blank);
                                                }
                                            }
                                        }

                                    }

                                    @Override
                                    public void onStartTrackingTouch(SeekBar seekBar) {
                                    }

                                    @Override
                                    public void onStopTrackingTouch(SeekBar seekBar) {

                                    }

                                });

                                //Intensity
                                seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                                    @Override
                                    public void onProgressChanged(SeekBar seekBar, int progressIntensity, boolean fromUser) {

                                        ColorDrawable viewColor = (ColorDrawable) mainScreen.getBackground();
                                        int colorId = viewColor.getColor();
                                        hexColor = String.format("%06X", (0xFFFFFF & colorId));

                                        initialState2 = progressIntensity +0;
                                        intenAdjuster =initialState2-5;
                                        currentParameterAdjuster = 1.33333333*initialState2;
                                        if (initialState1 < 364.963) {
                                            double intensityFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.1)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.1) + 1);
                                            textViewIntensity.setText((initialState2) + "%");
                                            textViewCurrent.setText(String.format("%.2f", intensityFormulaRubidium - 0) + "nA ");

                                        }
                                        else{
                                            textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                            textViewIntensity.setText((initialState2) + "%");
                                        }

                                        if(intenAdjuster>10  && initialState1>=400) {
                                            mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor));
                                        }
                                        else{
                                            mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor));
                                        }

                                        instantaneousCurrentString = textViewCurrent.getText().toString();
                                        instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                        instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                        if(instantaneousCurrentValue>0.00 && instantaneousCurrentValue<70.00) {
                                            imgAnimation.setImageResource(R.drawable.movingelectrons);
                                            AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                            movingElectrons.start();
                                        }

                                        else{
                                            if(instantaneousCurrentValue>70) {
                                                imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                                AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                                movingElectrons.start();
                                            }
                                            else{
                                                if(instantaneousCurrentValue==0){
                                                    imgAnimation.setImageResource(R.drawable.electrons_blank);
                                                }
                                            }
                                        }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });

                    }

                    else if(parent.getItemAtPosition(position).equals("Magnesium")){

                        AdRequest adRequest2 = new AdRequest.Builder().build();
                        interstitialAd.loadAd(adRequest2);
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdLoaded() {
                                interstitialAd.show();
                            }
                        });

                        //wavelengthParameterAdjuster=40;
                        //currentParameterAdjuster =133.33;
                        //decimalValueOfVoltage = 0;


                        seekBarVoltage.setMax(max - min);
                        seekBarVoltage.setProgress(initialState - min);

                        seekBarWavelength.setMaxPosition(max1 -min1);
                        seekBarWavelength.setColorBarPosition(0-min1);

                        seekBarIntensity.setMax(100-0);
                        seekBarIntensity.setProgress(100);
                        mainScreen.setBackgroundColor(Color.parseColor("#00000000"));

                        //Wavelength
                        seekBarWavelength.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int progressWavelength, int alphaBar, int color) {

                                TextView textHoldColour = (TextView) findViewById(R.id.textViewHoldColour);
                                textHoldColour.setTextColor(color);
                                final int textColor = textHoldColour.getCurrentTextColor();
                                hexColor2 = String.format("%06X", (0xFFFFFF & textColor));

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor2));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor2));
                                }

                                initialState1 = progressWavelength + min1;
                                textViewWavelength.setText((initialState1) + "nm");
                                if(initialState1<339.366) {
                                    //wavelengthParameterAdjuster = -1 / (0.0016 * (0.7955612 * progressWavelength - 382));
                                    wavelengthParameterAdjuster = 78*Math.pow(e, 0.027*initialState1 -9.6);
                                    double wavelengthFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.6)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.6) + 4.71);
                                    textViewCurrent.setText(String.format("%.2f", wavelengthFormulaRubidium - 0.005) + "nA");
                                }
                                else {
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }



                            }
                        });


                        //Voltage
                        seekBarVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                initialState = progress + min;
                                decimalValueOfVoltage = ((double) initialState / 100);
                                textViewVoltage.setText("" + decimalValueOfVoltage + "V");
                                if (initialState1 < 339.366) {
                                    double voltageFormulaForRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.6)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.6) + 4.71);
                                    textViewCurrent.setText(String.format("%.2f", voltageFormulaForRubidium - 0) + "nA ");
                                }

                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }

                        });

                        //Intensity
                        seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progressIntensity, boolean fromUser) {

                                ColorDrawable viewColor = (ColorDrawable) mainScreen.getBackground();
                                int colorId = viewColor.getColor();
                                hexColor = String.format("%06X", (0xFFFFFF & colorId));

                                initialState2 = progressIntensity +0;
                                intenAdjuster =initialState2-5;
                                currentParameterAdjuster = 1.33333333*initialState2;
                                if (initialState1 < 339.366) {
                                    double intensityFormulaRubidium =(currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.6)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.6) + 4.71);
                                    textViewIntensity.setText((initialState2) + "%");
                                    textViewCurrent.setText(String.format("%.2f", intensityFormulaRubidium - 0) + "nA ");

                                }
                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                    textViewIntensity.setText((initialState2) + "%");
                                }

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor));
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0.00 && instantaneousCurrentValue<70.00) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }

                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });


                    }
                    else if(parent.getItemAtPosition(position).equals("Silver")){

                        AdRequest adRequest2 = new AdRequest.Builder().build();
                        interstitialAd.loadAd(adRequest2);
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdLoaded() {
                                interstitialAd.show();
                            }
                        });

                        //wavelengthParameterAdjuster=40;
                        //currentParameterAdjuster =133.33;
                        //decimalValueOfVoltage = 0;


                        seekBarVoltage.setMax(max - min);
                        seekBarVoltage.setProgress(initialState - min);

                        seekBarWavelength.setMaxPosition(max1 -min1);
                        seekBarWavelength.setColorBarPosition(0-min1);

                        seekBarIntensity.setMax(100-0);
                        seekBarIntensity.setProgress(100);
                        mainScreen.setBackgroundColor(Color.parseColor("#00000000"));

                        //Wavelength
                        seekBarWavelength.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int progressWavelength, int alphaBar, int color) {

                                TextView textHoldColour = (TextView) findViewById(R.id.textViewHoldColour);
                                textHoldColour.setTextColor(color);
                                final int textColor = textHoldColour.getCurrentTextColor();
                                hexColor2 = String.format("%06X", (0xFFFFFF & textColor));

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor2));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor2));
                                }

                                initialState1 = progressWavelength + min1;
                                textViewWavelength.setText((initialState1) + "nm");
                                if(initialState1<288.46) {
                                    //wavelengthParameterAdjuster = -1 / (0.0016 * (0.7955612 * progressWavelength - 382));
                                    wavelengthParameterAdjuster = 78*Math.pow(e, 0.027*initialState1 -9.6);
                                    double wavelengthFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.3)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.3) + 1.284);
                                    textViewCurrent.setText(String.format("%.2f", wavelengthFormulaRubidium - 0.005) + "nA");
                                }
                                else {
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }



                            }
                        });


                        //Voltage
                        seekBarVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                initialState = progress + min;
                                decimalValueOfVoltage = ((double) initialState / 100);
                                textViewVoltage.setText("" + decimalValueOfVoltage + "V");
                                if (initialState1 < 288.46) {
                                    double voltageFormulaForRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.3)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.3) + 1.284);
                                    textViewCurrent.setText(String.format("%.2f", voltageFormulaForRubidium - 0) + "nA ");
                                }

                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }

                        });

                        //Intensity
                        seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progressIntensity, boolean fromUser) {

                                ColorDrawable viewColor = (ColorDrawable) mainScreen.getBackground();
                                int colorId = viewColor.getColor();
                                hexColor = String.format("%06X", (0xFFFFFF & colorId));

                                initialState2 = progressIntensity +0;
                                intenAdjuster =initialState2-5;
                                currentParameterAdjuster = 1.33333333*initialState2;
                                if (initialState1 < 288.46) {
                                    double intensityFormulaRubidium =(currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.3)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 0.3) + 1.284);
                                    textViewIntensity.setText((initialState2) + "%");
                                    textViewCurrent.setText(String.format("%.2f", intensityFormulaRubidium - 0) + "nA ");

                                }
                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                    textViewIntensity.setText((initialState2) + "%");
                                }

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor));
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0.00 && instantaneousCurrentValue<70.00) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }

                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });

                    }
                    else if(parent.getItemAtPosition(position).equals("Iron")){
                        AdRequest adRequest2 = new AdRequest.Builder().build();
                        interstitialAd.loadAd(adRequest2);
                        interstitialAd.setAdListener(new AdListener(){
                            @Override
                            public void onAdLoaded() {
                                interstitialAd.show();
                            }
                        });

                        //wavelengthParameterAdjuster=40;
                        //currentParameterAdjuster =133.33;
                        //decimalValueOfVoltage = 0;


                        seekBarVoltage.setMax(max - min);
                        seekBarVoltage.setProgress(initialState - min);

                        seekBarWavelength.setMaxPosition(max1 -min1);
                        seekBarWavelength.setColorBarPosition(0-min1);

                        seekBarIntensity.setMax(100-0);
                        seekBarIntensity.setProgress(100);
                        mainScreen.setBackgroundColor(Color.parseColor("#00000000"));

                        //Wavelength
                        seekBarWavelength.setOnColorChangeListener(new ColorSeekBar.OnColorChangeListener() {
                            @Override
                            public void onColorChangeListener(int progressWavelength, int alphaBar, int color) {

                                TextView textHoldColour = (TextView) findViewById(R.id.textViewHoldColour);
                                textHoldColour.setTextColor(color);
                                final int textColor = textHoldColour.getCurrentTextColor();
                                hexColor2 = String.format("%06X", (0xFFFFFF & textColor));

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor2));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor2));
                                }

                                initialState1 = progressWavelength + min1;
                                textViewWavelength.setText((initialState1) + "nm");
                                if(initialState1<275.229) {
                                    //wavelengthParameterAdjuster = -1 / (0.0016 * (0.7955612 * progressWavelength - 382));
                                    wavelengthParameterAdjuster = 78*Math.pow(e, 0.027*initialState1 -9.6);
                                    double wavelengthFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.5)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.5) + 4);
                                    textViewCurrent.setText(String.format("%.2f", wavelengthFormulaRubidium - 0.005) + "nA");
                                }
                                else {
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }



                            }
                        });


                        //Voltage
                        seekBarVoltage.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                initialState = progress + min;
                                decimalValueOfVoltage = ((double) initialState / 100);
                                textViewVoltage.setText("" + decimalValueOfVoltage + "V");
                                if (initialState1 < 275.229) {
                                    double voltageFormulaForRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.5)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.5) + 4);
                                    textViewCurrent.setText(String.format("%.2f", voltageFormulaForRubidium - 0) + "nA ");
                                }

                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0 && instantaneousCurrentValue<70) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }
                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }

                        });

                        //Intensity
                        seekBarIntensity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progressIntensity, boolean fromUser) {

                                ColorDrawable viewColor = (ColorDrawable) mainScreen.getBackground();
                                int colorId = viewColor.getColor();
                                hexColor = String.format("%06X", (0xFFFFFF & colorId));

                                initialState2 = progressIntensity +0;
                                intenAdjuster =initialState2-5;
                                currentParameterAdjuster = 1.33333333*initialState2;
                                if (initialState1 < 275.229) {
                                    double intensityFormulaRubidium = (currentParameterAdjuster * Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.5)) / (Math.pow(e, wavelengthParameterAdjuster * decimalValueOfVoltage + 1.5) + 4);
                                    textViewIntensity.setText((initialState2) + "%");
                                    textViewCurrent.setText(String.format("%.2f", intensityFormulaRubidium - 0) + "nA ");

                                }
                                else{
                                    textViewCurrent.setText(String.format("%.2f", 0.00) + "nA");
                                    textViewIntensity.setText((initialState2) + "%");
                                }

                                if(intenAdjuster>10  && initialState1>=400) {
                                    mainScreen.setBackgroundColor(Color.parseColor("#"+intenAdjuster+hexColor));
                                }
                                else{
                                    mainScreen.setBackgroundColor(Color.parseColor("#00"+hexColor));
                                }

                                instantaneousCurrentString = textViewCurrent.getText().toString();
                                instantaneousCurrentValueString = instantaneousCurrentString.replaceAll("[a-z,A-Z,]","");
                                instantaneousCurrentValue = new Double(instantaneousCurrentValueString).doubleValue();



                                if(instantaneousCurrentValue>0.00 && instantaneousCurrentValue<70.00) {
                                    imgAnimation.setImageResource(R.drawable.movingelectrons);
                                    AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                    movingElectrons.start();
                                }

                                else{
                                    if(instantaneousCurrentValue>70) {
                                        imgAnimation.setImageResource(R.drawable.movingelectronslarger);
                                        AnimationDrawable movingElectrons = (AnimationDrawable) imgAnimation.getDrawable();
                                        movingElectrons.start();
                                    }
                                    else{
                                        if(instantaneousCurrentValue==0){
                                            imgAnimation.setImageResource(R.drawable.electrons_blank);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                            }
                        });


                    }
                    else if(parent.getItemAtPosition(position).equals("Gold")){

                    }
                    else if(parent.getItemAtPosition(position).equals("Platinum")){

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

}
