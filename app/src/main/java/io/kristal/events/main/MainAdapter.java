package io.kristal.events.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;

import io.kristal.events.R;
import io.kristal.events.model.Event;

/**
 * Created by sebastien on 10/01/2018.
 */

final class MainAdapter extends ArrayAdapter<Event> {

    private static DateFormat sDateFormat = DateFormat.getDateInstance();

    MainAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView titleTextView = view.findViewById(R.id.text_title);
        TextView placeTextView = view.findViewById(R.id.text_place);
        TextView dateTextView = view.findViewById(R.id.text_date);

        Event event = getItem(position);
        titleTextView.setText(event.getTitle());
        placeTextView.setText(event.getPlace());
        dateTextView.setText(sDateFormat.format(event.getDate()));

        return view;
    }
}
