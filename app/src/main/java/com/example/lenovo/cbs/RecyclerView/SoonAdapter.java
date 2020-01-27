package com.example.lenovo.cbs.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.cbs.Model.FilmSoon;
import com.example.lenovo.cbs.R;

import java.text.SimpleDateFormat;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class SoonAdapter extends RecyclerView.Adapter<SoonAdapter.SoonViewHolder> {

    private RealmResults<FilmSoon> filmSoons = findFilms();
    private OnInfoItemClickListener mListener;

    public SoonAdapter(OnInfoItemClickListener listener){
        mListener = listener;
    }

    @Override
    public SoonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_soon, parent, false);
        return new SoonAdapter.SoonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SoonViewHolder holder, final int position) {
        FilmSoon filmSoon = filmSoons.get(position);
        if (filmSoon!=null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            holder.date.setText(dateFormat.format(filmSoon.getDate()));
            holder.more.setOnClickListener(view -> mListener.onInfoItemClick(position,"",1));
            byte[] byteArray = filmSoon.getImage();
            if (byteArray == null) {
                holder.filmImg.setImageResource(R.drawable.poster_none);
            } else {
                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                holder.filmImg.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    public int getItemCount() {
        return filmSoons.size();
    }

    private RealmResults<FilmSoon> findFilms(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(FilmSoon.class).sort("date").findAll();

    }

    public interface OnInfoItemClickListener{
        void onInfoItemClick(int position, String id,int code);
    }

    public static class SoonViewHolder extends RecyclerView.ViewHolder {
        private ImageView filmImg;
        private TextView date;
        private Button more;


        public SoonViewHolder(View itemView) {
            super(itemView);
            filmImg = itemView.findViewById(R.id.poster_soon);
            date = itemView.findViewById(R.id.date_soon);
            more = itemView.findViewById(R.id.moreInfo);


        }
    }
}
