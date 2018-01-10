package io.kristal.events.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;

import io.kristal.events.R;
import io.kristal.events.model.Event;
import io.kristal.events.model.EventsList;

public final class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

    private static String RESET_URL = "http://cobaltians.org/events.json";

    private MainAdapter mAdapter;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new MainAdapter(this, R.layout.row_main, R.id.text_title);

        setContentView(R.layout.activity_main);

        ListView listView = findViewById(android.R.id.list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "ListView - onItemClick: "+ position);
            }
        });

        reset();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_restore) {
            reset();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void reset() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        mRequestQueue.add(new JsonArrayRequest(RESET_URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "reset: " + response.toString());

                        EventsList.setAll(response);
                        ArrayList<Event> events = EventsList.getAll();
                        if (! mAdapter.isEmpty()) {
                            mAdapter.clear();
                        }
                        mAdapter.addAll(events);

                        Toast.makeText(MainActivity.this, R.string.reset_success, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, R.string.reset_error, Toast.LENGTH_LONG).show();

                        Log.e(TAG, "reset: " + error.getMessage());
                        error.printStackTrace();
                    }
                }));
    }
}
