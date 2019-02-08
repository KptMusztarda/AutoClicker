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

    private List<Profile> profiles = new ArrayList<>();

    ProfileManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getPackageName() + ".prefs", Context.MODE_PRIVATE);
        prefEditor = pref.edit();
    }

    void loadProfiles() {
        String name;
        int sum = pref.getInt(PROFILE_SUM_KEY, 0);
        for(int i=0; i<sum; i++) {

            name = pref.getString(PROFILE_KEY_PREFIX + i + PROFILE_NAME_SUFFIX, "");
            Logger.log(TAG, "Found a profile named \"" + name + "\" on id=" + i);

            Profile profile = new Profile(context, name);

            String s = pref.getString(PROFILE_KEY_PREFIX + i + PROFILE_GESTURES_SUFFIX, "");
            if(!s.isEmpty()) {
                do {
                    int ind;
                    int x = Integer.parseInt(s.substring(0, ind = s.indexOf(',')));
                    int y = Integer.parseInt(s.substring(ind + 1, ind = s.indexOf(',', ind + 1)));
                    int r = Integer.parseInt(s.substring(ind + 1, ind = s.indexOf(';')));

                    profile.addPoint(x, y, r, false);

                    s = s.substring(ind + 1);

                } while(s.length() > 0);
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

    void saveProfiles() {
        for(int i=0; i<profiles.size(); i++) {
            String profileKey = PROFILE_KEY_PREFIX + i;
            Profile profile = profiles.get(i);
            prefEditor.putString(profileKey + PROFILE_NAME_SUFFIX, profile.getName());

            StringBuilder builder = new StringBuilder();
            for (PointView point : profile.getPoints()) {
                int[] coords = point.getRawCoordinates();
                builder.append(coords[0]);
                builder.append(",");
                builder.append(coords[1]);
                builder.append(",");
                builder.append(point.getRandomRadius());
                builder.append(';');
            }

            prefEditor.putString(profileKey + PROFILE_GESTURES_SUFFIX, builder.toString());

            Logger.log(TAG, "Saved profile \"" + profile.getName() + "\" on key with id=" + i + " with gestures: " + builder.toString());
        }
        prefEditor.putInt(PROFILE_SUM_KEY, profiles.size());
        Logger.log(TAG, "Saved profiles count: " + profiles.size());
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
