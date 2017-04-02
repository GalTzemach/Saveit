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
import android.widget.TextView;

import com.example.galtzemach.saveit.UI.SalaryFragment;
import com.example.galtzemach.saveit.UI.dummy.DummyContent;

import java.util.ArrayList;

import static com.example.galtzemach.saveit.UI.SalaryFragment.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener {
    private final int CONTAINER_ID = R.id.NestedScrollView_main;

    private TextView textView;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView nestedScrollView;
    private ArrayList<String> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arr = new ArrayList<String>();
        arr.add(0,"0");
        arr.add(1,"1");
        arr.add(2,"2");

        nestedScrollView = (NestedScrollView) findViewById(R.id.NestedScrollView_main);
        textView = new TextView(this);
        //textView = (TextView) findViewById(R.id.text_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                nestedScrollView.removeAllViews();
            }
        });


        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_warranty:
                       // textView.setText(R.string.title_warranty);
//                        textView.setText("Warranty");

                        nestedScrollView.removeAllViews();
//                        nestedScrollView.addView(textView);
                        return true;
                    case R.id.navigation_monthly_bills:
                        //textView.setText(R.string.title_monthly_bills);
//                        MyListFragment listFragment = new MyListFragment();
                        nestedScrollView.removeAllViews();
//                        getSupportFragmentManager().beginTransaction().add(CONTAINER_ID,listFragment).addToBackStack(null).commit();
                        return true;
                    case R.id.navigation_salary:
                        //textView.setText(R.string.title_salary);
                        nestedScrollView.removeAllViews();
                        SalaryFragment salaryFragment = SalaryFragment.newInstance(1);
                        getSupportFragmentManager().beginTransaction().add(nestedScrollView.getId(), salaryFragment).commit();

                        return true;
                }
                return false;
            }
        });

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
