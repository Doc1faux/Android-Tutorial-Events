package io.kristal.events.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import io.kristal.events.R;
import io.kristal.events.main.model.Event;
import io.kristal.events.main.model.EventsList;

public final class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: init adapter with Constructor(Context, ResId, ResId)

        setContentView(R.layout.activity_main);

        // TODO: retrieve ListView & set adapter

        if (savedInstanceState == null) {
            reset();
        }
        else {
            reload();
        }
    }

    private void reset() {
        // TODO: init RequestQueue if null, then add request to "http://cobaltians.org/events.json",
        // save events with `EventsList.setAll(Context, JSONArray)` & call `reload()` method
    }

    private void reload() {
        ArrayList<Event> events = EventsList.getAll();
        // TODO: reload adapter data
    }
}
