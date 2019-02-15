package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import me.kptmusztarda.handylib.Logger;

class ProfileManager {

    private static final String TAG = "ProfileManager";
    private static final String PROFILE_SUM_KEY = "profile_sum";
    private static final String PROFILE_KEY_PREFIX = "profile_";
    private static final String PROFILE_NAME_SUFFIX = "_name";
    private static final String PROFILE_GESTURES_SUFFIX = "_gestures";
    /*  profile_sum
        profile_x_name
        profile_x_gestures   */

    private SharedPreferences pref;
    private SharedPreferences.Editor prefEditor;
    private Context context;

    private List<Profile> profiles;

    ProfileManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getPackageName() + ".prefs", Context.MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    void loadProfiles() {
        String name;
        int sum = pref.getInt(PROFILE_SUM_KEY, 0);
        profiles = new ArrayList<>(sum);
        for(int i=0; i<sum; i++) {

            name = pref.getString(PROFILE_KEY_PREFIX + i + PROFILE_NAME_SUFFIX, "");
            Logger.log(TAG, "Found a profile named \"" + name + "\" on id=" + i);

            Profile profile = new Profile(context, name);

            String s = pref.getString(PROFILE_KEY_PREFIX + i + PROFILE_GESTURES_SUFFIX, "");
            if(!s.isEmpty()) {
                profile.createGesturesFromString(s);
            }
            profiles.add(profile);
        }
    }

    void addProfile(String s) {
        Logger.log(TAG, "Adding new profile named \"" + s + "\"");
        profiles.add(0, new Profile(context, s));
        saveProfiles();
    }

    void deleteProfile(int i) {
        if(profiles.size() > 0) {
            Logger.log(TAG, "Deleting profile " + i + " named \"" + profiles.get(i) + "\"");
            profiles.remove(i);
            saveProfiles();
        } else {
            Toast.makeText(context, "No profile selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfiles() {
        for(int i=0; i<profiles.size(); i++) {
            saveProfile(i);
        }
        prefEditor.putInt(PROFILE_SUM_KEY, profiles.size());
        Logger.log(TAG, "Saved profiles count: " + profiles.size());
        prefEditor.apply();
    }

    void saveProfile(int i) {
        String profileKey = PROFILE_KEY_PREFIX + i;
        Profile profile = profiles.get(i);
        prefEditor.putString(profileKey + PROFILE_NAME_SUFFIX, profile.getName());

        prefEditor.putString(profileKey + PROFILE_GESTURES_SUFFIX, profile.toString());

        Logger.log(TAG, "Saved profile \"" + profile.getName() + "\" on key with id=" + i + " with gestures: " + profile.toString());
        prefEditor.apply();
    }

    ArrayList<String> getProfilesNames() {
        ArrayList<String> profilesNames = new ArrayList<>();
        for(Profile profile : profiles) {

            profilesNames.add(profile.getName());
        }
        return  profilesNames;
    }

    Profile getProfile(int i) {
        return profiles.get(i);
    }
}
