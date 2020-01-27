package com.example.lenovo.cbs.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.example.lenovo.cbs.Model.Actor;
import com.example.lenovo.cbs.R;

import java.util.*;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.FilmViewHolder> {
    private ArrayList<Actor> data;

    public RecyclerAdapter(ArrayList<Actor> actors) {
        data = actors;

    }

    @Override
    public FilmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_actors, parent, false);
        return new FilmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FilmViewHolder holder, int position) {
        Actor actor = data.get(position);
        holder.actorImage.setImageBitmap(actor.getPhoto());
        holder.actorRole.setText(actor.getRole());
        holder.actorName.setText(actor.getActor());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class FilmViewHolder extends RecyclerView.ViewHolder {
      private ImageView actorImage;
      private TextView actorName;
      private TextView actorRole;

        public FilmViewHolder(View itemView) {
            super(itemView);
            actorImage = itemView.findViewById(R.id.actorImage);
            actorName = itemView.findViewById(R.id.actorName);
            actorRole = itemView.findViewById(R.id.actorRole);


        }
    }

}
