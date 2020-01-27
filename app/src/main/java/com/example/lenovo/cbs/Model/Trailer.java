package com.example.lenovo.cbs.Model;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by LenoVo on 01.05.2018.
 */

public class Trailer extends RealmObject {
    private String title;
    private String language;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return title + "-" + language;
    }
}
