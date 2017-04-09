package com.example.galtzemach.saveit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.galtzemach.saveit.BL.MonthlyBillsRowAdapter;
import com.example.galtzemach.saveit.BL.PhotosAdapter;
import com.example.galtzemach.saveit.BL.SalaryRowAdapter;
import com.example.galtzemach.saveit.BL.WarrantyRowAdapter;
import com.example.galtzemach.saveit.BL.YearsSalaryAdapter;
import com.example.galtzemach.saveit.BL.YearsWarrantyAdapter;
import com.example.galtzemach.saveit.UI.AddMonthlyBillsFragment;
import com.example.galtzemach.saveit.UI.AddSalaryFragment;
import com.example.galtzemach.saveit.UI.AddWarrantyFragment;
import com.example.galtzemach.saveit.UI.YearArrAdapter;
import com.example.galtzemach.saveit.UI.YearArrayListAdapter;
import com.example.galtzemach.saveit.UI.dummy.DummyContent;
import com.example.galtzemach.saveit.UI.dummy.SalaryFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static com.example.galtzemach.saveit.UI.dummy.SalaryFragment.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener, AddSalaryFragment.OnFragmentInteractionListener, AddWarrantyFragment.OnFragmentInteractionListener, AddMonthlyBillsFragment.OnFragmentInteractionListener {
    //
    private final String TAG = this.getClass().toString();

    // create FireBase auth feature
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    // create FireBaseDatabase feature + specific userRef
    private FirebaseDatabase mDataBase;
    private DatabaseReference mUserRef;

    private enum Category {salary, warranty, monthlyBills};
    private Category currentCategory;
    private enum Mode {pull, pushh};
    private Mode currentMode;


    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private NestedScrollView nestedScrollView;
    private ListView listView;
    private AddSalaryFragment addSalaryFragment;
    private AddWarrantyFragment addWarrantyFragment;
    private AddMonthlyBillsFragment addMonthlyBillsFragment;

    private ArrayList<String> yearArrayList;
    private ArrayList<String> photosArrayList;
    private String[] yearArr;
    private String[] emptyArr = new String[0];

    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mDataBase = FirebaseDatabase.getInstance();

        // check if user sign in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    user_id = mAuth.getCurrentUser().getUid();
                    mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                    Intent LogInIntent = new Intent(MainActivity.this, LogInActivity.class);
                    LogInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LogInIntent);

                }
            }
        };
        fillArrays();

        listView = (ListView) findViewById(R.id.main_list_view);

        nestedScrollView = (NestedScrollView) findViewById(R.id.NestedScrollView_main);
//        nestedScrollView.setNestedScrollingEnabled(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_main);
//        bottomNavigationView.getMenu().getItem(0).setChecked(true);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (currentCategory){
                    case salary:
                        if (currentMode == Mode.pushh) {
                            currentMode = Mode.pull;
                            openSalaryList();
                        }
                        else {
                            currentMode = Mode.pushh;
                            openAddSalaryFragment();
                        }
                        break;


                    case warranty:
                        if (currentMode == Mode.pushh) {
                            currentMode = Mode.pull;
                            openWarrantyList();
                        }
                        else {
                            currentMode = Mode.pushh;
                            openAddWarrantyFragment();
                        }
                        break;


                    case monthlyBills:
                        if (currentMode == Mode.pushh) {
                            currentMode = Mode.pull;
                            openMonthlyBillsList();
                        }
                        else {
                            currentMode = Mode.pushh;
                            openAddMonthlyBillsFragment();
                        }
                        break;
                }
                }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_salary:
                        currentCategory = Category.salary;

                        if (currentMode == Mode.pushh)
                            openAddSalaryFragment();
                        else
                            openSalaryList();
                        return true;


                    case R.id.navigation_warranty:
                        currentCategory = Category.warranty;

                        if (currentMode == Mode.pushh)
                            openAddWarrantyFragment();
                        else
                            openWarrantyList();
                        return true;


                    case R.id.navigation_monthly_bills:
                        currentCategory = Category.monthlyBills;

                        if (currentMode == Mode.pushh)
                            openAddMonthlyBillsFragment();
                        else
                            openMonthlyBillsList();
                        return true;
                }
                return false;
            }
        });

        initialDefault();
        openSalaryList();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    private void initialDefault() {
        currentMode = Mode.pull;
        currentCategory = Category.salary;
    }

    private void openAddSalaryFragment() {
        nestedScrollView.removeAllViews();
        addSalaryFragment = AddSalaryFragment.newInstance("g", "t");
        getSupportFragmentManager().beginTransaction().add(nestedScrollView.getId(), addSalaryFragment).commit();
    }

    private void openAddWarrantyFragment() {
        nestedScrollView.removeAllViews();
        addWarrantyFragment = AddWarrantyFragment.newInstance("g", "t");
        getSupportFragmentManager().beginTransaction().add(nestedScrollView.getId(), addWarrantyFragment).commit();
    }

    private void openAddMonthlyBillsFragment() {
        nestedScrollView.removeAllViews();
        addMonthlyBillsFragment = AddMonthlyBillsFragment.newInstance("g", "t");
        getSupportFragmentManager().beginTransaction().add(nestedScrollView.getId(), addMonthlyBillsFragment).commit();
    }

    private void openSalaryList() {
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearsSalaryAdapter yearAdapter = new YearsSalaryAdapter(this, yearArrayList);
        listView.setAdapter(yearAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                SalaryRowAdapter salaryRowAdapter = new SalaryRowAdapter(getApplicationContext(), yearArrayList);///
                listView.setAdapter(salaryRowAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        nestedScrollView.removeAllViews();
                        View salaryView = getLayoutInflater().inflate(R.layout.salary, nestedScrollView, true);///

                        TextView employerTextView = (TextView) salaryView.findViewById(R.id.salary_employer);
//                        employerTextView.setText();

                        TextView mYDateTextView = (TextView) salaryView.findViewById(R.id.salary_employer);
//                        mYDateTextView.setText();

                        TextView grossNetTextView = (TextView) salaryView.findViewById(R.id.salary_employer);
//                        grossNetTextView.setText();

                        TextView notesTextView = (TextView) salaryView.findViewById(R.id.salary_employer);
//                        notesTextView.setText();

                        ListView photoSalaryListView = (ListView) salaryView.findViewById(R.id.salary_photos_list_view);
                        PhotosAdapter photosAdapter = new PhotosAdapter(MainActivity.this, photosArrayList);
                        photoSalaryListView.setAdapter(photosAdapter);
                    }
                });
            }
        });
    }

    private void openEmloyersList() {
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);

        YearArrAdapter yearArrAdapter = new YearArrAdapter(this, yearArr);
        listView.setAdapter(yearArrAdapter);
    }


    private void openWarrantyList() {
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearsWarrantyAdapter yearsWarrantyAdapter = new YearsWarrantyAdapter(this, yearArrayList);
        listView.setAdapter(yearsWarrantyAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WarrantyRowAdapter warrantyRowAdapter = new WarrantyRowAdapter(getApplicationContext(), yearArrayList);
                listView.setAdapter(warrantyRowAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        nestedScrollView.removeAllViews();
                        View warrantyView = getLayoutInflater().inflate(R.layout.warranty, nestedScrollView, true);///

                        TextView nameTextView = (TextView) warrantyView.findViewById(R.id.warranty_name);
//                        nameTextView.setText();

                        TextView periodInMonthsTextView = (TextView) warrantyView.findViewById(R.id.warranty_in_months);
//                        periodInMonthsTextView.setText();

                        TextView purchaseDateTextView = (TextView) warrantyView.findViewById(R.id.warranty_purchas_date);
//                        purchaseDateTextView.setText();

                        TextView expireDateTextView = (TextView) warrantyView.findViewById(R.id.warranty_exp_date);
//                        expireDateTextView.setText();

                        TextView costTextView = (TextView) warrantyView.findViewById(R.id.warranty_cost);
//                        costTextView.setText();

                        TextView notesTextView = (TextView) warrantyView.findViewById(R.id.warranty_notes);
//                        notesTextView.setText();

                        ListView photoWarrantyListView = (ListView) warrantyView.findViewById(R.id.warranty_photos_list_view);
                        PhotosAdapter photosAdapter = new PhotosAdapter(MainActivity.this, photosArrayList);
                        photoWarrantyListView.setAdapter(photosAdapter);
                    }
                });

            }
        });
    }

    private void openMonthlyBillsList() {
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearArrayListAdapter yearArrayListAdapter = new YearArrayListAdapter(this, yearArrayList);
        listView.setAdapter(yearArrayListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                MonthlyBillsRowAdapter monthlyBillsRowAdapter = new MonthlyBillsRowAdapter(getApplicationContext(), yearArrayList);
                listView.setAdapter(monthlyBillsRowAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        nestedScrollView.removeAllViews();
                        View monthlyBillsView = getLayoutInflater().inflate(R.layout.monthly_bills, nestedScrollView, true);///

                        TextView categoryTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_category);
//                        categoryTextView.setText();

                        TextView dateTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_month_year);
//                        dateTextView.setText();

                        TextView sumTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_sum);
//                        sumTextView.setText();


                        TextView notesTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_notes);
//                        notesTextView.setText();

                        ListView photoMonthlyBillsListView = (ListView) monthlyBillsView.findViewById(R.id.monthly_bills_photos_list_view);
                        PhotosAdapter photosAdapter = new PhotosAdapter(MainActivity.this, photosArrayList);
                        photoMonthlyBillsListView.setAdapter(photosAdapter);
                    }
                });

            }
        });
    }

    private void fillArrays() {
        yearArrayList = new ArrayList<>();
        yearArrayList.add("2016");
        yearArrayList.add("2015");
        yearArrayList.add("2014");
        yearArrayList.add("2013");
        yearArrayList.add("2012");
        yearArrayList.add("2011");

        photosArrayList = new ArrayList<>();
        photosArrayList.add("R.drawable.ic_dashboard_black_24dp");

        yearArr = new String[]{"2017", "2016", "2015", "2014"};
    }

    private void clearList() {
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearArrAdapter emptyAdapter = new YearArrAdapter(this, emptyArr);
        listView.setAdapter(emptyAdapter);
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

        // log out action pressed
        if (id == R.id.action_logOut) {

            logOutUser();
            return true;

        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void logOutUser() {

        mAuth.signOut();

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
//        final Button button = (Button) findViewById(R.id.ok_butten);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
    }
}
