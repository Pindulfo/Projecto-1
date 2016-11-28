package com.davidmiguel.gobees.addedithive;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.davidmiguel.gobees.Injection;
import com.davidmiguel.gobees.R;
import com.davidmiguel.gobees.data.source.cache.GoBeesRepository;
import com.davidmiguel.gobees.utils.ActivityUtils;

/**
 * Add / edit hive activity.
 */
public class AddEditHiveActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_HIVE = 1;
    public static final int NEW_HIVE = -1;

    private GoBeesRepository goBeesRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addedithive_act);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        // Get hive id (if edit)
        long hiveId = getIntent()
                .getLongExtra(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID, NEW_HIVE);

        // Add fragment to the activity and set title
        AddEditHiveFragment addEditHiveFragment =
                (AddEditHiveFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.contentFrame);
        if (addEditHiveFragment == null) {
            addEditHiveFragment = AddEditHiveFragment.newInstance();
            if (getIntent().hasExtra(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID)) {
                // If edit -> set edit title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.edit_hive);
                }
                Bundle bundle = new Bundle();
                bundle.putString(AddEditHiveFragment.ARGUMENT_EDIT_HIVE_ID, hiveId + "");
                addEditHiveFragment.setArguments(bundle);
            } else {
                // If new -> set add title
                if (actionBar != null) {
                    actionBar.setTitle(R.string.add_hive);
                }
            }
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditHiveFragment, R.id.contentFrame);
        }

        // Init db
        goBeesRepository = Injection.provideApiariesRepository();
        goBeesRepository.openDb();

        // Create the presenter
        new AddEditHivePresenter(goBeesRepository, addEditHiveFragment, hiveId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database
        goBeesRepository.closeDb();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}