package com.example.lenovo.cbs.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lenovo.cbs.Activities.MainActivity;
import com.example.lenovo.cbs.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import butterknife.Unbinder;


public class FeaturesFragment extends Fragment implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private Unbinder unbinder;
    @BindView(R.id.tickets)
    LinearLayout myTickets;
    @BindView(R.id.back)
    LinearLayout backTickets;
    @BindView(R.id.reg)
    LinearLayout registration;
    @BindView(R.id.log_out)
    LinearLayout logOut;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_features,container,false);
        unbinder = ButterKnife.bind(this,v);
        Bundle bundle = getArguments();
        String id = bundle.getString("id");
        if (id.equals("0")){
            registration.setVisibility(View.GONE);
        } else if (id.equals("1")){
            myTickets.setVisibility(View.GONE);
            backTickets.setVisibility(View.GONE);
            logOut.setVisibility(View.GONE);

        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Optional
    @OnClick({R.id.tickets,R.id.back,R.id.theaters,R.id.language,R.id.reg,R.id.log_out})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tickets:
                callFragment(new MyTicketsFragment(),"myTickets");
                break;
            case R.id.back:
                callFragment(new TicketsBackFragment(),"backTickets");
                break;
            case R.id.theaters:
                callFragment(new MapTheatersFragment(),"mapTheaters");
                break;
            case R.id.language:
                callFragment(new LanguageFragment(),"language");
                break;
            case R.id.reg:
                intentToMain();
                break;
            case R.id.log_out:
                mAuth.signOut();
                intentToMain();
                break;
        }

    }

    private void intentToMain(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private void callFragment(Fragment fragment,String back){
    FragmentManager fm = getFragmentManager();
            fm.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                    .addToBackStack(back)
                .commit();
    }
}
