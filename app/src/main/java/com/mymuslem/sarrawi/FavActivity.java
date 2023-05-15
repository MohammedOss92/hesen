package com.mymuslem.sarrawi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mymuslem.sarrawi.adapter.AdapterFav;
import com.mymuslem.sarrawi.database.DatabaseHelper;
import com.mymuslem.sarrawi.model.Product;

import java.util.List;

public class FavActivity extends AppCompatActivity {
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DrawerLayout drawer;
    private RecyclerView recyclerView;
    private DatabaseHelper mDBHelper;
    private List<Product> mProductList;
    private AdapterFav madapter;
    ///////////////////////////
    private SharedPreferences preferences;
    public static final String TAG = "Armstring";
    private String txtView1Size;
    private Typeface font;
    private int textSize;
    private Typeface font1,font2,font3,font4,font5,font6,font7;
    ///////////////////////////////////////////////
    AdView mAdView;
    AdRequest AdRequest;
//    private static final String App_ID = "ca-app-pub-1895204889916566~4050285607";
    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraa);
        setSupportActionBar(toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(FavActivity.this);
        font1= Typeface.createFromAsset(getAssets(),"fonts/a.otf");
        font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
        font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
        font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
        font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
        font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
        font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mDBHelper = new DatabaseHelper(this);
        //Get product list in db when db exists
        mProductList = mDBHelper.DataFaVourit();
//        mProductList = mDBHelper.getListProduct();
        //Init adapter
        madapter= new AdapterFav(mProductList, FavActivity.this,font, textSize);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(madapter);
        recyclerView.setNestedScrollingEnabled(false);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_azkar) {
                    Intent i =new Intent(FavActivity.this,MainActivity.class);
                    startActivity(i);

                }
                else if (id == R.id.nav_fav) {
//                    Intent i =new Intent(MainActivity.this,FavActivity.class);
//                    startActivity(i);

                }
                else if (id == R.id.nav_seit) {
                    Intent i =new Intent(FavActivity.this,SettingsActivity.class);
                    startActivity(i);

                }
//                else if (id == R.id.nav_about) {
//                    Toast.makeText(MainActivity.this, "Right Drawer - About", Toast.LENGTH_SHORT).show();
//                }

                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });
        AdsView();
        prepareAd();
    }

    @Override
    protected void onStart() {
        super.onStart();
        specifyFont();
        specifyFontSize();
        if(mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        specifyFont();
        specifyFontSize();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        specifyFont();
        specifyFontSize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        specifyFont();
        specifyFontSize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        specifyFont();
        specifyFontSize();
//        showAds();
//        prepareAd();
//        AdsView();
        if(mAdView != null) {
            mAdView.loadAd(new AdRequest.Builder().build());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        specifyFont();
        specifyFontSize();
    }

    private void specifyFont(){
        String fontName = preferences.getString("LIST_OF_FONTS", "Chunkfive.otf");
        Log.d(TAG, "specifyFont: " + fontName);
        if(fontName.matches("a.otf")){
            Log.d(TAG, "specifyFont: 1");
            font = font1;
            font1=Typeface.createFromAsset(getAssets(),"fonts/a.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font1, textSize);
            recyclerView.setAdapter(madapter);
        }
        else if(fontName.matches("ab.otf")){
            Log.d(TAG, "specifyFont: 2");
            font = font2;

            font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font2, textSize);
            recyclerView.setAdapter(madapter);
        }
        else if(fontName.matches("ac.otf")) {
            Log.d(TAG, "specifyFont: 3");
            font = font3;

            font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font3, textSize);
            recyclerView.setAdapter(madapter);
        }
        else if(fontName.matches("ad.otf")) {
            Log.d(TAG, "specifyFont: 4");
            font = font4;
            font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font4, textSize);
            recyclerView.setAdapter(madapter);
        }
        else if(fontName.matches("ae.otf")) {
            Log.d(TAG, "specifyFont: 5");
            font = font5;

            font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font5, textSize);
            recyclerView.setAdapter(madapter);
        }
        else if(fontName.matches("af.otf")) {
            Log.d(TAG, "specifyFont: 6");
            font = font6;

            font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font6, textSize);
            recyclerView.setAdapter(madapter);
        }
        else if(fontName.matches("ag.otf")) {
            Log.d(TAG, "specifyFont: 7");
            font = font7;


            font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
            madapter = new AdapterFav(mProductList, FavActivity.this, font7, textSize);
            recyclerView.setAdapter(madapter);
        }
    }
    private void specifyFontSize(){
        txtView1Size = preferences.getString("TEXT_SIZE","14");
        textSize = Integer.parseInt(txtView1Size);
        madapter = new AdapterFav(mProductList, FavActivity.this, font, textSize);
        recyclerView.setAdapter(madapter);
    }
    private void AdsView() {
//        MobileAds.initialize(this, App_ID);
        mAdView = (AdView) findViewById(R.id.adViewcon);
        mAdView.loadAd(new AdRequest.Builder().build());
    }
    public void  prepareAd(){

//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId("ca-app-pub-1895204889916566/7230921630");
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the interstitial ad is closed.
////                Toast.makeText(FavActivity.this, "Ad is closed", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fav, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
