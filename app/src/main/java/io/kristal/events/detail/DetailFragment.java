package io.kristal.events.detail;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.kristal.events.R;
import io.kristal.events.model.Event;

/**
 * Created by sebastien on 10/01/2018.
 */

public final class DetailFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DetailFragment.class.getSimpleName();

    private static DateFormat sDateFormat = DateFormat.getDateInstance(DateFormat.SHORT);

    private Event mEvent;
    private PlaceTextListener mListener;

    private EditText mEditTitle;
    private EditText mEditDate;
    private EditText mEditPlace;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Bundle args = getArguments();
            if (args != null) {
                mEvent = args.getParcelable(DetailActivity.EXTRA_EVENT);
            }
        }
        else {
            mEvent = savedInstanceState.getParcelable(DetailActivity.EXTRA_EVENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        mEditTitle = view.findViewById(R.id.form_title);
        mEditDate = view.findViewById(R.id.form_date);
        mEditDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePicker();
                }
            }
        });
        mEditDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        mEditPlace = view.findViewById(R.id.form_place);
        mEditPlace.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                    && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (mListener != null) {
                        mListener.onPlaceTextChanged(mEditPlace.getText().toString());
                    }
                    return true;
                }

                return false;
            }
        });

        if (mEvent != null) {
            mEditTitle.setText(mEvent.getTitle());
            mEditPlace.setText(mEvent.getPlace());
            mEditDate.setText(sDateFormat.format(mEvent.getDate()));
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String place = mEditPlace.getText().toString();
        if (place.length() > 0
            && mListener != null) {
            mListener.onPlaceTextChanged(place);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Event event = getEvent();
        if (event != null) {
            outState.putParcelable(DetailActivity.EXTRA_EVENT, mEvent);
        }
    }

    void setPlaceTextListener(PlaceTextListener listener) {
        mListener = listener;
    }

    @Nullable Event getEvent() {
        Date date;
        try {
            date = sDateFormat.parse(mEditDate.getText().toString());
        }
        catch(ParseException exception) {
            Log.e(TAG, "getEvent: date parsing failed");
            exception.printStackTrace();
            return null;
        }

        String title = mEditTitle.getText().toString();
        String place = mEditPlace.getText().toString();

        if (mEvent != null) {
            mEvent.setDate(date);
            mEvent.setTitle(title);
            mEvent.setPlace(place);
        }
        else {
            mEvent = new Event(getContext(), title, date, place);
        }

        return mEvent;
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        if (mEvent != null) {
            calendar.setTime(mEvent.getDate());
        }

        new DatePickerDialog(getContext(), DetailFragment.this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    void onPlaceChanged(String place) {
        mEditPlace.setText(place);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, dayOfMonth);
        mEditDate.setText(sDateFormat.format(calendar.getTime()));
    }
}
