package io.kristal.events.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sebastien on 08/01/2018.
 */

public final class Event {

    private static String TAG = Event.class.getSimpleName();

    private static String IDENTIFIER_FIELD = "id";
    private static String TITLE_FIELD = "title";
    private static String DATE_FIELD = "date";
    private static String PLACE_FIELD = "place";

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private long mIdentifier;
    private String mTitle;
    private Date mDate;
    private String mPlace;

    private Event(JSONObject json) throws ParseException {
        try {
            mIdentifier = json.getLong(IDENTIFIER_FIELD);
            mTitle = json.getString(TITLE_FIELD);

            try {
                String dateString = json.getString(DATE_FIELD);
                mDate = sDateFormat.parse(dateString);
            }
            catch(ParseException exception) {
                Log.e(TAG, "constructor(JSONObject): date parsing failed");
                exception.printStackTrace();
                throw exception;
            }

            mPlace = json.optString(PLACE_FIELD);
        }
        catch(JSONException exception) {
            String message = "missing 'id', 'title' or 'date' field";
            Log.e(TAG, "constructor(JSONObject): " + message);
            exception.printStackTrace();
            throw new ParseException(message, 0);
        }
    }

    public static @Nullable Event fromJSON(@NonNull JSONObject json) {
        try {
            return new Event(json);
        }
        catch (ParseException exception) {
            return null;
        }
    }

    public long getIdentifier() {
        return mIdentifier;
    }

    public @NonNull String getTitle() {
        return mTitle;
    }

    public @NonNull Date getDate() {
        return mDate;
    }

    public @NonNull String getPlace() {
        return mPlace;
    }

    @Override
    public String toString() {
        return "Event{"+IDENTIFIER_FIELD+"="+mIdentifier+","
                +TITLE_FIELD+"="+mTitle+","
                +DATE_FIELD+"="+mDate+","
                +PLACE_FIELD+"="+mPlace+"}";
    }
}
