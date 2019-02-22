package me.kptmusztarda.autoclicker;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private List<String> names;
    private List<String> gestures;
    private Profile loadedProfile;

    ProfileManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getPackageName() + ".prefs", Context.MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    void loadProfiles() {
        int sum = pref.getInt(PROFILE_SUM_KEY, 0);

        names = new ArrayList<>(sum);
        gestures = new ArrayList<>(sum);

        for(int i=0; i<sum; i++) {
            names.add(pref.getString(PROFILE_KEY_PREFIX + i + PROFILE_NAME_SUFFIX, ""));
            gestures.add(pref.getString(PROFILE_KEY_PREFIX + i + PROFILE_GESTURES_SUFFIX, ""));

            Logger.log(TAG, "Found a profile named \"" + names.get(i) + "\" on id=" + i + " with gestures: \"" + gestures.get(i) + "\"");
        }
    }

    void addProfile(String s) {
        Logger.log(TAG, "Adding new profile named \"" + s + "\"");
        names.add(s);
        gestures.add("");
        saveProfiles();
    }

    void deleteProfile(int i) {
        if(names.size() > 0) {
            Logger.log(TAG, "Deleting profile " + i + " named \"" + names.get(i) + "\"");
            names.remove(i);
            gestures.remove(i);
            saveProfiles();
        } else {
            Toast.makeText(context, "No profile selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveProfiles() {
        for(int i=0; i<names.size(); i++) {
            String profileKey = PROFILE_KEY_PREFIX + i;
            prefEditor.putString(profileKey + PROFILE_NAME_SUFFIX, names.get(i));
            prefEditor.putString(profileKey + PROFILE_GESTURES_SUFFIX, gestures.get(i));
        }
        prefEditor.putInt(PROFILE_SUM_KEY, names.size());
        Logger.log(TAG, "Saved profiles count: " + names.size());
        prefEditor.apply();
    }

    void saveProfile(int i) {
        String profileKey = PROFILE_KEY_PREFIX + i;

        prefEditor.putString(profileKey + PROFILE_NAME_SUFFIX, loadedProfile.getName());
        prefEditor.putString(profileKey + PROFILE_GESTURES_SUFFIX, loadedProfile.toString());

        Logger.log(TAG, "Saved profile \"" + loadedProfile.getName() + "\" on key with id=" + i + " with gestures: " + loadedProfile.toString());
        prefEditor.apply();
    }

    List<String> getProfilesNames() {
        return names;
    }

    Profile getProfile(int i) {
        Profile profile = new Profile(context, names.get(i));
        profile.createGesturesFromString(gestures.get(i));
        loadedProfile = profile;
        return loadedProfile;
    }
}
