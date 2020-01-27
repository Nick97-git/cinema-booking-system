package com.example.lenovo.cbs.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lenovo.cbs.Loaders.NewsLoader;
import com.example.lenovo.cbs.Model.News;
import com.example.lenovo.cbs.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmResults;

public class NewsFragment extends Fragment {

    private String mID;
    private String mTitle;
    private static final int LOADER_NEWS = 5;
    @BindView(R.id.news_viewPager)
    ViewPager vp;
    @BindView(R.id.newsProgress)
    ProgressBar progress;
    @BindView(R.id.cardView_error)
    CardView cardView;
    private Unbinder unbinder;

    public static NewsFragment newInstance(String id, String title) {
        NewsFragment newsFragment = new NewsFragment();
        Bundle arguments = new Bundle();
        arguments.putString("id", id);
        arguments.putString("title",title);
        newsFragment.setArguments(arguments);
        return newsFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(LOADER_NEWS,null,loaderNews);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle!=null){
            mID = bundle.getString("id");
            mTitle = bundle.getString("title");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_pager_news,container,false);
        unbinder = ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private LoaderManager.LoaderCallbacks<String> loaderNews = new LoaderManager.LoaderCallbacks<String>() {

        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            NewsLoader newsLoader = new NewsLoader(getActivity(),mID);
            newsLoader.forceLoad();
            return newsLoader;
        }


        @Override
        public void onLoadFinished(Loader<String> loader, String news) {
           NewsPagerAdapter adapter = new NewsPagerAdapter(getFragmentManager());
            vp.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            progress.setVisibility(View.GONE);
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {

        }
    };

    private class NewsPagerAdapter extends FragmentStatePagerAdapter{

        private NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return NewsPageFragment.newInstance(position,mTitle);
        }

        @Override
        public int getCount() {
            Realm realm  = Realm.getDefaultInstance();
            RealmResults<News> mNews = realm.where(News.class).findAll();
            if (mNews.size() == 0){
                cardView.setVisibility(View.VISIBLE);
            }
            return mNews.size();
        }
    }
}
