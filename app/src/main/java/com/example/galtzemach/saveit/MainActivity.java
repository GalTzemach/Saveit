package com.example.galtzemach.saveit;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.galtzemach.saveit.UI.SalaryFragment;
import com.example.galtzemach.saveit.UI.YearArrAdapter;
import com.example.galtzemach.saveit.UI.dummy.DummyContent;

import java.util.ArrayList;

import static com.example.galtzemach.saveit.UI.SalaryFragment.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {

    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView nestedScrollView;
    private ListView listView;
    private ArrayList<String> yearArrayList;
    private String[] yearArr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fillArrays();

        nestedScrollView = (NestedScrollView) findViewById(R.id.NestedScrollView_main);
        nestedScrollView.setNestedScrollingEnabled(true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                openFragmentListTest();
            }
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_warranty:
                        nestedScrollView.removeAllViews();
                        return true;
                    case R.id.navigation_monthly_bills:
                        nestedScrollView.removeAllViews();
                        return true;
                    case R.id.navigation_salary:
                        //nestedScrollView.removeAllViews();
                        openSalaryList();
                        return true;
                }
                return false;
            }
        });

    }

    private void openSalaryList() {
        openYearList();
    }



    private void fillArrays() {
        yearArrayList = new ArrayList<>();
        yearArrayList.add("2017");
        yearArrayList.add("2016");
        yearArrayList.add("2015");
        yearArrayList.add("2014");
        yearArrayList.add("2013");
        yearArrayList.add("2012");
        yearArrayList.add("2011");

        yearArr = new String[]{"2017", "2016", "2015", "2014"};
    }

    private void openYearList() {
//        nestedScrollView.removeAllViews();
//        nestedScrollView.addView(listView);
        listView = (ListView) findViewById(R.id.main_list_view);
//        YearArrayListAdapter yearArrayListAdapter = new YearArrayListAdapter(this, yearArrayList);
        YearArrAdapter yearArrAdapter = new YearArrAdapter(this, yearArr);
        listView.setAdapter(yearArrAdapter);
    }

    private void openFragmentListTest() {
        nestedScrollView.removeAllViews();
        SalaryFragment salaryFragment = SalaryFragment.newInstance(1);
        getSupportFragmentManager().beginTransaction().add(nestedScrollView.getId(), salaryFragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

}
