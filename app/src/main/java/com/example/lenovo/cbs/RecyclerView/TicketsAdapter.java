package com.example.lenovo.cbs.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.cbs.Model.Theater;
import com.example.lenovo.cbs.Model.Tickets;
import com.example.lenovo.cbs.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class TicketsAdapter extends RecyclerView.Adapter<TicketsAdapter.TicketsHolder> {

    private RealmResults<Tickets> tickets = findTheaters();
    private OnTicketClickListener mListener;

    public TicketsAdapter(Context context,OnTicketClickListener listener){
        Context mContext = context;
        mListener = listener;
    }

    @Override
    public TicketsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_tickets_back, parent, false);
        return new TicketsAdapter.TicketsHolder(view);
    }

    @Override
    public void onBindViewHolder(TicketsHolder holder, int position) {
        Tickets ticket = tickets.get(position);
        ArrayList<String> mIDs = new ArrayList<>();
        String id = ticket.getId();
        holder.title.setText(ticket.getTitle());
        holder.seat.setText(ticket.getSeat());
        holder.row.setText(ticket.getRow());
        holder.checkBox.setOnClickListener(view -> {
            if (holder.checkBox.isChecked()){
                mIDs.add(id);
            } else if (!holder.checkBox.isChecked()){
                mIDs.remove(id);
            }
            mListener.onTicketClick(mIDs);
        });
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public interface OnTicketClickListener {
        void onTicketClick(ArrayList<String> ids);
    }

    private RealmResults<Tickets> findTheaters(){
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Tickets.class).findAll();

    }

    public static class TicketsHolder extends RecyclerView.ViewHolder {
        private TextView row;
        private TextView seat;
        private TextView title;
        private CheckBox checkBox;

        public TicketsHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_ticket);
            row = itemView.findViewById(R.id.row_num);
            seat = itemView.findViewById(R.id.seat_num);
            checkBox = itemView.findViewById(R.id.ticket_checkbox);


        }
    }
}
