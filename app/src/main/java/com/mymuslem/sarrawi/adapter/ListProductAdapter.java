package com.mymuslem.sarrawi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mymuslem.sarrawi.PagerDoaa;
import com.mymuslem.sarrawi.R;
import com.mymuslem.sarrawi.database.DatabaseHelper;
import com.mymuslem.sarrawi.model.Product;

import java.util.List;

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.MyViewHolder>{
    private List<Product> mProductList;
    Context con;
    private int fontSize;
    private Typeface font;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        ImageView fav;
        RelativeLayout card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_doaa);
            fav = (ImageView) view.findViewById(R.id.img_fav);
            card_view = (RelativeLayout) view.findViewById(R.id.card_view);

        }
    }

    public ListProductAdapter(List<Product> mProductList, Context context,Typeface fontFamily, int fontSize) {
        this.con = context;
        this.mProductList = mProductList;
        this.font = fontFamily;
        this.fontSize = fontSize;
    }


    @NonNull
    @Override
    public ListProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row2, parent, false);


        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
        final Product m = mProductList.get(position);
        holder.title.setText(m.getName());
        holder.title.setTextSize(fontSize);
        holder.title.setTypeface(font);

        if (m.getFav() == 0) {
//            holder.fav.setBackgroundColor(Color.rgb(76, 255, 0));
            holder.fav.setImageResource(R.mipmap.nf);
        } else {
//            holder.fav.setBackgroundColor(Color.rgb(255, 0, 0));
            holder.fav.setImageResource(R.mipmap.f);
        }
        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = MediaPlayer.create(con, R.raw.sound_like_incoming);
                mediaPlayer.start();

                if (m.getFav() == 1) {
//                    holder.fav.setBackgroundColor(Color.rgb(76, 255, 0));
                    holder.fav.setImageResource(R.mipmap.nf);
                    DatabaseHelper.getInstance(con).fav(m.getId(),m.getName(),m.getName_filter(),0);
                    mProductList.get(position).setFav(0);
                    m.setFav(0);
                    Toast.makeText(con, "تم الإزالة من المفضلة", Toast.LENGTH_LONG).show();
                } else {
//                    holder.fav.setBackgroundColor(Color.rgb(255, 0, 0));
                    holder.fav.setImageResource(R.mipmap.f);
                    DatabaseHelper.getInstance(con).fav(m.getId(),m.getName(),m.getName_filter(),1);
                    mProductList.get(position).setFav(1);
                    m.setFav(1);
                    Toast.makeText(con, "تم الإضافة إلى المفضلة", Toast.LENGTH_LONG).show();
                }




            }
        });

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Product m = mProductList.get(position);
                        Intent i = new Intent(con, PagerDoaa.class);
                        i.putExtra("titleID", m.getId());
                        con.startActivity(i);
                    }
        });

    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }
}
