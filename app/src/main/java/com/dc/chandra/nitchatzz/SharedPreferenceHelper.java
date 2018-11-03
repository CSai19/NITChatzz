package com.dc.chandra.nitchatzz;

import android.content.Context;
import android.content.SharedPreferences;

import com.dc.chandra.nitchatzz.Faculty;



public class SharedPreferenceHelper {
    private static SharedPreferenceHelper instance = null;
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static String SHARE_faculty_INFO = "facultyinfo";
    private static String SHARE_KEY_NAME = "name";
    private static String SHARE_KEY_EMAIL = "email";
    private static String SHARE_KEY_AVATA = "avata";
    private static String SHARE_KEY_UID = "uid";


    private SharedPreferenceHelper() {}

    public static SharedPreferenceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferenceHelper();
            preferences = context.getSharedPreferences(SHARE_faculty_INFO, Context.MODE_PRIVATE);
            editor = preferences.edit();
        }
        return instance;
    }

    public void savefacultyInfo(Faculty faculty) {
        editor.putString(SHARE_KEY_NAME, faculty.name);
        editor.putString(SHARE_KEY_EMAIL, faculty.email);
        editor.putString(SHARE_KEY_AVATA, faculty.avata);
        editor.putString(SHARE_KEY_UID, StaticConfig.UID);
        editor.apply();
    }

    public Faculty getfacultyInfo(){
        String facultyName = preferences.getString(SHARE_KEY_NAME, "");
        String email = preferences.getString(SHARE_KEY_EMAIL, "");
        String avatar = preferences.getString(SHARE_KEY_AVATA, "default");

        Faculty faculty = new Faculty();
        faculty.name = facultyName;
        faculty.email = email;
        faculty.avata = avatar;

        return faculty;
    }

    public String getUID(){
        return preferences.getString(SHARE_KEY_UID, "");
    }

}
