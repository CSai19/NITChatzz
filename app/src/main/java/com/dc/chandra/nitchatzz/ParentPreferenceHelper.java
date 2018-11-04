package com.dc.chandra.nitchatzz;

import android.content.Context;
import android.content.SharedPreferences;

public class ParentPreferenceHelper {
    private static ParentPreferenceHelper instance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static String SHARE_parent_INFO = "parentinfo";
    private static String SHARE_KEY_NAME = "name";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATA = "avata";
    private static String SHARE_KEY_UID = "uid";


    private ParentPreferenceHelper() {}

    public static ParentPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ParentPreferenceHelper();
            preferences = context.getSharedPreferences(SHARE_parent_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }

    public void saveparentInfo(Parent parent) {
        editor.putString(SHARE_KEY_NAME, parent.name);
        editor.putString(SHARE_KEY_EMAIL, parent.email);
        editor.putString(SHARE_KEY_AVATA, parent.avata);
        editor.putString(SHARE_KEY_UID, StaticConfig.UID);
        editor.apply();
    }

    public Parent getparentInfo(){
        String parentName = preferences.getString(SHARE_KEY_NAME, "");
        String email = preferences.getString(SHARE_KEY_EMAIL, "");
        String avatar = preferences.getString(SHARE_KEY_AVATA, "default");

        Parent parent = new Parent();
        parent.name = parentName;
        parent.email = email;
        parent.avata = avatar;

        return parent;
    }

    public String getUID(){
        return preferences.getString(SHARE_KEY_UID, "");
    }

}

