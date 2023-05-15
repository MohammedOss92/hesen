package com.mymuslem.sarrawi;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mymuslem.sarrawi.Utils.Utils;
import com.mymuslem.sarrawi.database.DatabaseHelper;
import com.mymuslem.sarrawi.model.Adapter_zekr;

import java.util.List;
import java.util.Locale;

public class PagerDoaa extends AppCompatActivity {
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    int titleID;
    List<Adapter_zekr> myArrayList;
    String filter="";
    int msgId=0;
    int origPOS=0;
    int newMsg=0;
    boolean sourceISFav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_doaa);
        Intent i=getIntent();
        titleID=i.getExtras().getInt("titleID");


        DatabaseHelper ss=new DatabaseHelper(getApplicationContext());

//            myArrayList = ss.getMessagesNotOrdered(titleID);
        myArrayList = ss.getAllPrayerzekr(titleID);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        //because order of fav start from index difference while normal messages start from ok index
        mViewPager.setCurrentItem(myArrayList.size());






//////////////////////////////////


    }




    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            Adapter_zekr m=(Adapter_zekr) myArrayList.get(position);


            return PlaceholderFragment.newInstance(position + 1,m.getDescription(),m.getCounter(),m.getHint(),m);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.

            return myArrayList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
            }
            return null;
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_MSG = "msg";
        int globalTextSize=12;
        Button b1;
        String message;
        TextView tv;
        TextView tvDesc,tvcounter,tvhint,tvTitle;
        RelativeLayout rl;
        Typeface tf;
        SharedPreferences preferences;
        String txtView1Size;
        int counterr = 0;
        AdView mAdView;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */

        public static PlaceholderFragment newInstance(int sectionNumber, String hint, String counter, String description, Adapter_zekr m) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString("Resaleh", description);
            args.putString("counter", counter);
            args.putString("hint", hint);
            args.putParcelable("MsgObject",m);
//            args.putInt("newMsg", newMsg);
            args.putParcelable("MsgObject", (Parcelable) m);
//            args.putParcelable("MsgObject",  m);

            Bundle bundle = new Bundle();
//            bundle.putSerializable("msgClass", (Serializable) m);
//            args.putBundle("msgClass",(Serializable) m);
//            bundle.putSerializable("msgClass", (Serializable) m);
//            args.putBundle("msgClass",(Serializable) m);
            fragment.setArguments(args);



            return fragment;
        }

        public PlaceholderFragment()
        {

        }


        @Override
        public void onAttach(Activity activity) {
            // Toast.makeText(getActivity(),"AAA",Toast.LENGTH_SHORT).show();
            super.onAttach(activity);
        }


        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.setHasOptionsMenu(true);
            preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_pager_doaa, container, false);
            //Toast.makeText(getActivity(),"BBB",Toast.LENGTH_SHORT).show();

            tvDesc=(TextView) rootView.findViewById(R.id.textView1);
            tvcounter=(TextView) rootView.findViewById(R.id.textView3);
            tvhint=(TextView) rootView.findViewById(R.id.textView2);
            tvTitle=(TextView)rootView.findViewById(R.id.titleaaa);
            b1=(Button) rootView.findViewById(R.id.button1);
             mAdView = (AdView) rootView.findViewById(R.id.adViewfra);
            mAdView.loadAd(new AdRequest.Builder().build());
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v == b1) {


                        counterr++;
                        //textTitle.setText(Integer.toString(counter));
                        b1.setText(Integer.toString(counterr));
                        //scoreText.setBackgroundColor(Color.CYAN);
                        b1.setTextColor(Color.BLUE);
                    }
                }
            });
//            rl=(RelativeLayout) rootView.findViewById(R.id.rl);
            SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(getActivity());




            String des=getArguments().getString("Resaleh");
            String coun=getArguments().getString("counter");
            String hint=getArguments().getString("hint");

            final Adapter_zekr m=getArguments().getParcelable("MsgObject");
            final DatabaseHelper s=new DatabaseHelper(getActivity());
            String titleDesc=s.getMsgTitleByTitleID(m.getName_ID());
            tvTitle.setText(titleDesc);
            tvDesc.setText(hint);
            tvcounter.setText(coun);
            tvhint.setText(des);




            return rootView;
        }

        private void specifyFontFamilyFromList() {
            String font = preferences.getString("LIST_OF_FONTS", "Chunkfive.otf");
            Typeface typeface;
            switch (font) {
                case "a.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/a.otf");
                    tvDesc.setTypeface(typeface);
                    break;
                case "ab.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ab.otf");
                    tvDesc.setTypeface(typeface);
                    break;
                case "ac.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ac.otf");
                    tvDesc.setTypeface(typeface);
                    break;
                case "ad.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ad.otf");
                    tvDesc.setTypeface(typeface);
                    break;
                case "ae.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ae.otf");
                    tvDesc.setTypeface(typeface);
                    break;
                case "af.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/af.otf");
                    tvDesc.setTypeface(typeface);
                    break;
                case "ag.otf":
                    typeface = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ag.otf");
                    tvDesc.setTypeface(typeface);
                    break;
            }
        }

        private void specifyFontSize() {
            txtView1Size = preferences.getString("TEXT_SIZE", "20");
            int txtSizeIntegerValue = Integer.parseInt(txtView1Size);
            tvDesc.setTextSize(txtSizeIntegerValue);
        }

        @Override
        public void onStart() {
            super.onStart();
            specifyFontFamilyFromList();
            specifyFontSize();
            if(mAdView != null) {
                mAdView.loadAd(new AdRequest.Builder().build());
            }
        }

        @Override
        public void onStop() {

            super.onStop();
            specifyFontFamilyFromList();
            specifyFontSize();
        }

        @Override
        public void onDestroy() {
//            if (this.mAdView != null) {
//                this.mAdView.destroy();
//            }
            super.onDestroy();
            specifyFontFamilyFromList();
            specifyFontSize();
        }


        ///////////////////////////
        @Override
        public void onResume() {
//            if (this.mAdView != null) {
//                this.mAdView.resume();
//            }
            super.onResume();
            specifyFontFamilyFromList();
            specifyFontSize();
            if(mAdView != null) {
                mAdView.loadAd(new AdRequest.Builder().build());
            }
        }

        @Override
        public void onPause() {
//            if (this.mAdView != null) {
//                this.mAdView.pause();
//            }
            super.onPause();
            specifyFontFamilyFromList();
            specifyFontSize();

        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_pager_doaa, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_share) {
                Utils.IntenteShare(getActivity(), "حصن المسلم", "حصن المسلم", tvDesc.getText().toString());
                return true;
            } else if (id == R.id.action_copy) {
                String stringYouExtracted = tvDesc.getText().toString();
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                    clipboard.setText(stringYouExtracted);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(getActivity().CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData
                            .newPlainText(stringYouExtracted, stringYouExtracted);
                    clipboard.setPrimaryClip(clip);
                }
                Toast.makeText(getActivity(), "تم نسخ النص", Toast.LENGTH_SHORT).show();


            }
            return super.onOptionsItemSelected(item);
        }

        ////////////////////////////////////
    }

}
