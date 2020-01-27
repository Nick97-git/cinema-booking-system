package com.example.lenovo.cbs.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.cbs.Model.Theater;
import com.example.lenovo.cbs.R;

import io.realm.Realm;
import io.realm.RealmResults;



public class TheatersAdapter extends RecyclerView.Adapter<TheatersAdapter.TheatersHolder> {

    private RealmResults<Theater> theaters = findTheaters();
    private OnTheaterClickListener mListener;

    public TheatersAdapter(OnTheaterClickListener onTheaterClickListener){
        mListener = onTheaterClickListener;
    }

    @Override
    public TheatersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_list_theaters, parent, false);
        return new TheatersAdapter.TheatersHolder(view);
    }

    @Override
    public void onBindViewHolder(TheatersHolder holder, int position) {
        final Theater theater = theaters.get(position);
        holder.theaterTitle.setText(theater.getName());
        holder.linearLayout.setOnClickListener(view -> {
            if (mListener!=null){
                mListener.onTheaterClick(theater.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return theaters.size();
    }

    public interface OnTheaterClickListener {
        void onTheaterClick(String id);
    }

    private RealmResults<Theater> findTheaters(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Theater.class).sort("name").findAll();

    }

    public static class TheatersHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView theaterTitle;
        private LinearLayout linearLayout;

        public TheatersHolder(View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgTheater);
            linearLayout = itemView.findViewById(R.id.theaters_layout);
            theaterTitle = itemView.findViewById(R.id.theaterTitle);


        }
    }
}
