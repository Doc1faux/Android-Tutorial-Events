package io.kristal.events.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import io.kristal.events.R;
import io.kristal.events.model.Event;

/**
 * Created by sebastien on 10/01/2018.
 */

public final class EditActivity extends DetailActivity {

    public static final int RESULT_EDITED = -2;

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);

        if (fragment instanceof DetailFragment) {
            Bundle bundle = new Bundle(1);
            bundle.putParcelable(EXTRA_EVENT, getIntent().getParcelableExtra(EXTRA_EVENT));
            fragment.setArguments(bundle);
        }
    }

    @Override
    protected void onCheckOptionsItemSelected() {
        Event event = mDetail.getEvent();
        if (event != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_EVENT, event);
            setResult(RESULT_EDITED, intent);
            finish();
        }
        else {
            Toast.makeText(this, R.string.check_error, Toast.LENGTH_LONG).show();
        }
    }
}
