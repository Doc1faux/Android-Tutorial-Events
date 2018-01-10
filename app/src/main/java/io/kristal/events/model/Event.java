package io.kristal.events.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
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

public final class Event implements Parcelable {

    private static String TAG = Event.class.getSimpleName();
    private static String SHARED_PREFERENCES_NAME = "io.kristal.events.PREFERENCES";
    private static String NEXT_IDENTIFIER_KEY = "io.kristal.events.NEXT_IDENTIFIER";

    private static String IDENTIFIER_FIELD = "id";
    private static String TITLE_FIELD = "title";
    private static String DATE_FIELD = "date";
    private static String PLACE_FIELD = "place";

    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private long mIdentifier;
    private String mTitle;
    private Date mDate;
    private String mPlace;

    public static final Parcelable.Creator<Event> CREATOR = new Parcelable.Creator<Event>() {
        public Event createFromParcel(Parcel in) {
            try {
                return new Event(in);
            }
            catch (ParseException exception) {
                return null;
            }
        }

        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel in) throws ParseException {
        mIdentifier = in.readLong();
        mTitle = in.readString();
        try {
            mDate = sDateFormat.parse(in.readString());
        }
        catch(ParseException exception) {
            Log.e(TAG, "constructor(Parcel): date parsing failed");
            exception.printStackTrace();
            throw exception;
        }
        mPlace = in.readString();
    }

    private Event(Context context, JSONObject json) throws ParseException {
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

            updateNextIdentifier(context, mIdentifier);
        }
        catch(JSONException exception) {
            String message = "missing 'id', 'title' or 'date' field";
            Log.e(TAG, "constructor(JSONObject): " + message);
            exception.printStackTrace();
            throw new ParseException(message, 0);
        }
    }

    public Event(@NonNull Context context, @NonNull String title, @NonNull Date date,
                 @NonNull String place) {
        mIdentifier = getNextIdentifier(context);
        mTitle = title;
        mDate = date;
        mPlace = place;
    }

    public static @Nullable Event fromJSON(@NonNull Context context, @NonNull JSONObject json) {
        try {
            return new Event(context, json);
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

    public void setTitle(@NonNull String title) {
        mTitle = title;
    }

    public void setDate(@NonNull Date date) {
        mDate = date;
    }

    public void setPlace(@NonNull String place) {
        mPlace = place;
    }

    @Override
    public String toString() {
        return "Event{"+IDENTIFIER_FIELD+"="+mIdentifier+","
                +TITLE_FIELD+"="+mTitle+","
                +DATE_FIELD+"="+mDate+","
                +PLACE_FIELD+"="+mPlace+"}";
    }

    private static long getNextIdentifier(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        long nextIdentifier = preferences.getLong(NEXT_IDENTIFIER_KEY, 0);
        preferences.edit().putLong(NEXT_IDENTIFIER_KEY, nextIdentifier + 1).commit();
        return nextIdentifier;
    }

    private static void updateNextIdentifier(Context context, long current) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE);
        long nextIdentifier = preferences.getLong(NEXT_IDENTIFIER_KEY, 0);
        if (current >= nextIdentifier) {
            preferences.edit().putLong(NEXT_IDENTIFIER_KEY, current + 1).commit();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(mIdentifier);
        out.writeString(mTitle);
        out.writeString(sDateFormat.format(mDate));
        out.writeString(mPlace);
    }
}
