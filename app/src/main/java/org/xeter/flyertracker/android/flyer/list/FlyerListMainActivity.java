package org.xeter.flyertracker.android.flyer.list;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.xeter.flyertracker.android.R;
import org.xeter.flyertracker.android.db.FlyerReaderDbHelper;
import org.xeter.flyertracker.android.flyer.create.NewFlyerActivity;
import org.xeter.flyertracker.android.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FlyerListMainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerAdapter recyclerAdapter;
    private FlyerReaderDbHelper flyerReaderDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flyer_list_main);

        // Toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Search view
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        flyerReaderDbHelper = new FlyerReaderDbHelper(this);
        recyclerAdapter = new RecyclerAdapter(new ArrayList<Flyer>());
        recyclerView.setAdapter(recyclerAdapter);

        // Create new flyer button
        final FloatingActionButton createFlyerFab = findViewById(R.id.create_flyer_fab);
        createFlyerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FlyerListMainActivity.this, NewFlyerActivity.class));
            }
        });
    }

    @NonNull
    private List<Flyer> flyersFromDB() {
        return buildDataFromIterator(flyerReaderDbHelper.getAllFlyers());
    }

    @NonNull
    private List<Flyer> buildDataFromIterator(Iterator<Flyer> flyerIterator) {
        final List<Flyer> flyersFromDB = new ArrayList<>();
        while (flyerIterator.hasNext()) {
            flyersFromDB.add(flyerIterator.next());
        }
        return flyersFromDB;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_flyer_list_main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(FlyerListMainActivity.this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.recyclerAdapter.setFilter(flyersFromDB());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        final List<Flyer> flyers;
        if (newText.equals("")) {
            flyers = flyersFromDB();
        } else {
            flyers = buildDataFromIterator(flyerReaderDbHelper.getFlyersMatching(newText));
        }
        recyclerAdapter.setFilter(flyers);
        return true;
    }
}
