package io.kristal.events.main;

import android.content.Intent;
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
import io.kristal.events.detail.CreateActivity;
import io.kristal.events.detail.DetailActivity;
import io.kristal.events.detail.EditActivity;
import io.kristal.events.model.Event;
import io.kristal.events.model.EventsList;

public final class MainActivity extends AppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();

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
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(DetailActivity.EXTRA_EVENT, mAdapter.getItem(position));
                startActivityForResult(intent, 0);
            }
        });

        if (savedInstanceState == null) {
            reset();
        }
        else {
            reload();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                startActivityForResult(new Intent(this, CreateActivity.class),
                        0);
                return true;
            case R.id.action_restore:
                reset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        boolean handled = false;

        if (requestCode == 0) {
            Event event;
            switch(resultCode) {
                case CreateActivity.RESULT_CREATED:
                    event = data.getParcelableExtra(DetailActivity.EXTRA_EVENT);
                    EventsList.addEvent(event);
                    reload();
                    handled = true;
                    break;
                case EditActivity.RESULT_EDITED:
                    event = data.getParcelableExtra(DetailActivity.EXTRA_EVENT);
                    EventsList.editEvent(event);
                    reload();
                    handled = true;
                    break;
            }
        }

        if (! handled) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void reset() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        mRequestQueue.add(new JsonArrayRequest("http://cobaltians.org/events.json",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        EventsList.setAll(MainActivity.this, response);
                        reload();

                        Toast.makeText(MainActivity.this, R.string.reset_success,
                                Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, R.string.reset_error,
                                Toast.LENGTH_LONG).show();

                        Log.e(TAG, "reset: " + error.getMessage());
                        error.printStackTrace();
                    }
                }));
    }

    private void reload() {
        ArrayList<Event> events = EventsList.getAll();
        if (! mAdapter.isEmpty()) {
            mAdapter.clear();
        }
        mAdapter.addAll(events);
    }
}
