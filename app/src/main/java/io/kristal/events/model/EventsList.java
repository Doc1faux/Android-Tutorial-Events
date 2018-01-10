package io.kristal.events.model;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sebastien on 08/01/2018.
 */

public final class EventsList {

    private static String TAG = EventsList.class.getSimpleName();

    private static ArrayList<Event> sList = new ArrayList<>(0);

    public static @NonNull ArrayList<Event> getAll() {
        return sList;
    }

    public static void setAll(@NonNull JSONArray events) {
        int eventsLength = events.length();
        sList = new ArrayList<>(eventsLength);

        for (int i = 0; i < eventsLength ; i++) {
            try {
                Event event = Event.fromJSON(events.getJSONObject(i));
                if (event != null) {
                    sList.add(event);
                }
            }
            catch (JSONException exception) {
                Log.w(TAG, "setEvents: event at index " + i + " is not JSON formatted");
                exception.printStackTrace();
            }
        }
    }

    public static void addEvent(Event event) {

    }

    public static void removeEvent(Event event) {

    }

    public static void editEvent(Event event) {

    }
}
