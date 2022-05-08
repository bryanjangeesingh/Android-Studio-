package com.brytech.seamathrevisiontnt;
//Bryan Jangeesingh 2020

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;


import java.util.Random;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    AdView bannerAd;
    InterstitialAd interstitialAd;


    SharedPreferences sharedPreferences;
    Boolean firstTime;
    TextView quote;

    Button easterEgg1;
    Button easterEgg2;
    int count =0;
    int count2;
    int firstCheck=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestNewInterstitial();

        easterEgg1 = findViewById(R.id.buttonEasterEgg1);
        easterEgg2 = findViewById(R.id.buttonEasterEgg2);

        easterEgg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstCheck = firstCheck +1;
            }
            });

            easterEgg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    count2 = 5 - count;
                    count = count +1;

                    if(firstCheck==0){
                        Toast.makeText(getApplicationContext(),"Nothing to see here",Toast.LENGTH_SHORT).show();
                        count2 =5;
                    }
                    else if(firstCheck >0 && count2 >0){
                        Toast.makeText(getApplicationContext(),"You are " + count2 + " steps away from unlocking about menu",Toast.LENGTH_SHORT).show();

                    }
                    else if(firstCheck >0 && count2 ==0){
                        showAboutDialog();
                    }
                    else  if(firstCheck >0 && count2 <0){
                        Toast.makeText(getApplicationContext(),"Lost time is never found again!",Toast.LENGTH_SHORT).show();
                    }

                    /*if(count2 ==0 && firstCheck>0){
                        showAboutDialog();
                    }
                    else {
                        if(firstCheck==0){
                            Toast.makeText(getApplicationContext(),"Nothing to see here",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            if(count2 != 0 && firstCheck>1){
                                Toast.makeText(getApplicationContext(),"You are " + count2 + " steps away from unlocking about menu",Toast.LENGTH_SHORT).show();
                            }

                        }


                    }*/

                }
            });


        printRandomQuote();

        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(2)
                .setRemindInterval(2)
                .setShowLaterButton(true)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);
        //AppRate.with(this).showRateDialog(this); //use this to show Rate Dialog when debugging

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_open);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        bannerAd = (AdView) findViewById(R.id.adViewBannerWholeNos);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        firstTime = sharedPreferences.getBoolean("firstTime", true);

        if (firstTime) {
            showDialog(); //causes force close on S7 Edge
            quote.setText("Welcome to SEA Math Revision. Navigate this app using side menu");
        } else {

        }
    }

    public void requestNewInterstitial() {
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getString(R.string.interstitialAdId));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
    }

    public void showDialog() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        firstTime = false;
        editor.putBoolean("firstTime", firstTime);
        editor.apply();

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.intro_dialog, null);

        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();

        //alertDialog.show();
        Button accept = view.findViewById(R.id.accept);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    public void showAboutDialog(){
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.about, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setView(view).create();
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        }, 2000);

        alertDialog.show();
    }


    public void printRandomQuote(){

        Random rn = new Random();
        int range = 20 - 0 + 1;
        final int randomNum =  rn.nextInt(range) + 0;

        String  QUOTES[] = {
                "Pure mathematics is, in its way, the poetry of logical ideas.\n" +
                        "— Albert Einstein",
                "Without mathematics, there’s nothing you can do. Everything around you is mathematics. Everything around you is numbers.\n" +
                        "— Shakuntala Devi",
                "Mathematics is the most beautiful and most powerful creation of the human spirit.\n" +
                        "— Stefan Banach",
                "Mathematics is the music of reason.\n" +
                        "— James Joseph Sylvester",
                "Why do children dread mathematics? Because of the wrong approach. Because it is looked at as a subject.\n" +
                        "— Shakuntala Devi",
                "The study of mathematics, like the Nile, begins in minuteness but ends in magnificence.\n" +
                        "— Charles Caleb Colton",
                "Life is a math equation. In order to gain the most, you have to know how to convert negatives into positives.\n" +
                        "— Anonymous",
                "Just because you can’t find a solution, it doesn’t mean there isn’t one.\n" +
                        "— Andrew Wiles",
                "The only way to learn mathematics is to do mathematics.\n" +
                        "— Paul R. Halmos",
                "Sometimes the questions are complicated and the answers are simple.\n" +
                        "— Dr. Seuss",
                "The essence of math is not to make simple things complicated, but to make complicated things simple.\n" +
                        "— Stan Gudder", "Mathematics is not about numbers, equations, computations, or algorithms: it is about understanding.\n" + "— William Paul Thurston",
                "Mathematics is, in its way, the poetry of logical ideas.\n" +
                        "— Albert Einstein",
                "Mathematics may not teach us to add love or subtract hate, but it gives us hope that every problem has a solution.\n" +
                        "— Anonymous",
                "Education is the passport to the future, for tomorrow belongs to those who prepare for it today.\n" +
                        "— Malcolm X",
                "The beautiful thing about learning is that no one can take it away from you\n" +
                        "— B.B. King",
                "Learning is never done without errors and defeat.\n" +
                        "— Vladimir Lenin",
                "You don’t have to be great to start, but you have to start to be great.\n" +
                        "— Zig Ziglar",
                "The way to get started is to quit talking and begin doing.\n" +
                        "— Walt Disney",
                "There are no shortcuts to any place worth going.\n" +
                        "— Beverly Stills",
                "Success is the sum of small efforts, repeated.\n" +
                        "— R Collier",
        };

        quote = findViewById(R.id.textViewQuote);
        quote.setText(QUOTES[randomNum]);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
            }
        });

        switch (menuItem.getItemId()) {
            case R.id.nav_wholenumbers:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new WholeNumbers()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_fractions:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Fractions()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_decimals:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Decimals()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_percent:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Percent()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_linearmeasure:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LinearMeasure()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_area:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Area()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_volumeandcapacity:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new VolAndCapacity()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_mass:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Mass()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_time:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Time()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_solidsandplaneshapes:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SolidsPlaneShapes()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_symmetry:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Symmetry()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_angles:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Angles()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_statistics:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Statistics()).commit();
                //Interstitial Ad Code
                interstitialAd.show();
                //End of Interstitial Ad Code For Switching Categories
                break;

            case R.id.nav_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String shareBody = "Check out SEA Math Revision on Google Play Store! - https://play.google.com/store/apps/details?id=com.brytech.seamathrevisiontnt";
                String shareSub= "";
                share.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                share.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(share, "Share using"));
                //End of Interstitial Ad Code For Switching Categories
                break;

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }

        requestNewInterstitial();
    }

}
