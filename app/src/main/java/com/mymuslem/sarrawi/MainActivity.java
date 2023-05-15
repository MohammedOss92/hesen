package com.mymuslem.sarrawi;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.mymuslem.sarrawi.adapter.ListProductAdapter;
import com.mymuslem.sarrawi.database.DatabaseHelper;
import com.mymuslem.sarrawi.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    DrawerLayout drawer;
    private RecyclerView recyclerView;
    private ListProductAdapter adapter;
    private List<Product> mProductList;
    private DatabaseHelper mDBHelper;

    ////////////////////////
    private SharedPreferences preferences;
    public static final String TAG = "Armstring";
    private String txtView1Size;
    private Typeface font;
    private int textSize;
    private Typeface font1,font2,font3,font4,font5,font6,font7;
    ////////////////////////
    AdView mAdView;
    AdRequest AdRequest;

    private InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbaraa);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mDBHelper = new DatabaseHelper(this);

        //Check exists database
        File database = getApplicationContext().getDatabasePath(DatabaseHelper.DBNAME);
        if(false == database.exists()) {
            mDBHelper.getReadableDatabase();
            //Copy db
            if(copyDatabase(this)) {
                Toast.makeText(this, "Copy database succes", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Copy data error", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        final Product m = new Product();
        //Get product list in db when db exists
        mProductList = mDBHelper.getAllPrayer();
//        mProductList = mDBHelper.getListProduct();
        adapter = new ListProductAdapter(mProductList, MainActivity.this,font, textSize);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        font1=Typeface.createFromAsset(getAssets(),"fonts/a.otf");
        font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
        font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
        font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
        font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
        font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
        font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);
        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_azkar) {
//                    Intent i =new Intent(MainActivity.this,MainActivity.class);
//                    startActivity(i);

                }
                else if (id == R.id.nav_fav) {
                    Intent i =new Intent(MainActivity.this,FavActivity.class);
                    startActivity(i);

                }
                else if (id == R.id.nav_seit) {
                    Intent i =new Intent(MainActivity.this,SettingsActivity.class);
                    startActivity(i);

                }


                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });
        AdsView();
        prepareAd();
        showAds();
    }

    private boolean copyDatabase(Context context) {
        try {

            InputStream inputStream = context.getAssets().open(DatabaseHelper.DBNAME);
            String outFileName = DatabaseHelper.DBLOCATION + DatabaseHelper.DBNAME;
            OutputStream outputStream = new FileOutputStream(outFileName);
            byte[]buff = new byte[1024];
            int length = 0;
            while ((length = inputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.w("MainActivity","DB copied");
            return true;
        }catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) MainActivity.this.getSystemService(Context.SEARCH_SERVICE);


        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

            final EditText searchEditText = (EditText) searchView.findViewById(androidx.appcompat.R.id.search_src_text);
            searchEditText.setTextColor(getResources().getColor(R.color.colorBlack));
            searchEditText.setBackgroundColor(getResources().getColor(R.color.colorWhite));

            int searchImgId = androidx.appcompat.R.id.search_button;
            ImageView v = (ImageView) searchView.findViewById(searchImgId);
            v.setImageResource(R.mipmap.search);

            int closeImgId = androidx.appcompat.R.id.search_close_btn;
            ImageView close_button = (ImageView) searchView.findViewById(closeImgId);
            close_button.setColorFilter(Color.WHITE);

            close_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.onActionViewCollapsed();
                    //Collapse the search widget
                    MenuItemCompat.collapseActionView(searchItem);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);


                    mProductList.clear();
                    mProductList.addAll(DatabaseHelper.getInstance(MainActivity.this).getAllPrayer());
                    adapter.notifyDataSetChanged();


                }
            });

        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(MainActivity.this.getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                  /*  mProductList.addAll(DataBase.getInstance(MainActivity.this).getAllPrayer());
                    adapter.notifyDataSetChanged();*/
                    mProductList.clear();
                    mProductList.addAll(DatabaseHelper.getInstance(MainActivity.this).getAllPrayer(newText));
                    adapter.notifyDataSetChanged();
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", "newText" + query);
                    mProductList.clear();
                    mProductList.addAll(DatabaseHelper.getInstance(MainActivity.this).getAllPrayer(query));
                    adapter.notifyDataSetChanged();

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void AdsView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });
        mAdView = (AdView) findViewById(R.id.adViewcon);
        mAdView.loadAd(new AdRequest.Builder().build());
    }
    public void  prepareAd(){

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this,"ca-app-pub-1895204889916566/9786749620", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });



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
    private void showAds() {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            public void run() {
                Log.i("hello", "world");
                prepareAd();
                runOnUiThread(new Runnable() {
                    public void run() {
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(MainActivity.this);
                        } else {
                            Log.d("TAG"," Interstitial not loaded");
                        }
                        prepareAd();
                    }
                });

            }
        }, 4, 4, TimeUnit.MINUTES);
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
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font1, textSize);
            recyclerView.setAdapter(adapter);
        }
        else if(fontName.matches("ab.otf")){
            Log.d(TAG, "specifyFont: 2");
            font = font2;

            font2=Typeface.createFromAsset(getAssets(),"fonts/ab.otf");
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font2, textSize);
            recyclerView.setAdapter(adapter);
        }
        else if(fontName.matches("ac.otf")) {
            Log.d(TAG, "specifyFont: 3");
            font = font3;

            font3=Typeface.createFromAsset(getAssets(),"fonts/ac.otf");
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font3, textSize);
            recyclerView.setAdapter(adapter);
        }
        else if(fontName.matches("ad.otf")) {
            Log.d(TAG, "specifyFont: 4");
            font = font4;
            font4=Typeface.createFromAsset(getAssets(),"fonts/ad.otf");
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font4, textSize);
            recyclerView.setAdapter(adapter);
        }
        else if(fontName.matches("ae.otf")) {
            Log.d(TAG, "specifyFont: 5");
            font = font5;

            font5=Typeface.createFromAsset(getAssets(),"fonts/ae.otf");
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font5, textSize);
            recyclerView.setAdapter(adapter);
        }
        else if(fontName.matches("af.otf")) {
            Log.d(TAG, "specifyFont: 6");
            font = font6;

            font6=Typeface.createFromAsset(getAssets(),"fonts/af.otf");
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font6, textSize);
            recyclerView.setAdapter(adapter);
        }
        else if(fontName.matches("ag.otf")) {
            Log.d(TAG, "specifyFont: 7");
            font = font7;


            font7=Typeface.createFromAsset(getAssets(),"fonts/ag.otf");
            adapter = new ListProductAdapter(mProductList, MainActivity.this, font7, textSize);
            recyclerView.setAdapter(adapter);
        }
    }
    private void specifyFontSize(){
        txtView1Size = preferences.getString("TEXT_SIZE","14");
        textSize = Integer.parseInt(txtView1Size);
        adapter = new ListProductAdapter(mProductList, MainActivity.this, font, textSize);
        recyclerView.setAdapter(adapter);
    }
}
