package io.kristal.events.detail;

import android.content.Intent;
import android.widget.Toast;

import io.kristal.events.R;
import io.kristal.events.model.Event;

/**
 * Created by sebastien on 10/01/2018.
 */

public final class CreateActivity extends DetailActivity {

    public static final int RESULT_CREATED = -1;

    @Override
    protected void onCheckOptionsItemSelected() {
        Event event = mDetail.getEvent();
        if (event != null) {
            // TODO: set result with created event & finish activity
            Intent intent = new Intent();
            intent.putExtra(EXTRA_EVENT, event);
            setResult(RESULT_CREATED, intent);
            finish();
        }
        else {
            Toast.makeText(this, R.string.check_error, Toast.LENGTH_LONG).show();
        }
    }
}
