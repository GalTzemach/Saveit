package com.example.galtzemach.saveit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.galtzemach.saveit.BL.MonthlyBills;
import com.example.galtzemach.saveit.BL.MonthlyBillsRowAdapter;
import com.example.galtzemach.saveit.BL.PhotosAdapter;
import com.example.galtzemach.saveit.BL.Salary;
import com.example.galtzemach.saveit.BL.SalaryRowAdapter;
import com.example.galtzemach.saveit.BL.Warranty;
import com.example.galtzemach.saveit.BL.WarrantyRowAdapter;
import com.example.galtzemach.saveit.BL.YearsMonthlyBillsAdapter;
import com.example.galtzemach.saveit.BL.YearsSalaryAdapter;
import com.example.galtzemach.saveit.BL.YearsWarrantyAdapter;
import com.example.galtzemach.saveit.DB.DataBase;
import com.example.galtzemach.saveit.UI.AddMonthlyBillsFragment;
import com.example.galtzemach.saveit.UI.AddSalaryFragment;
import com.example.galtzemach.saveit.UI.AddWarrantyFragment;
import com.example.galtzemach.saveit.UI.DataReadyListener;
import com.example.galtzemach.saveit.UI.YearArrAdapter;
import com.example.galtzemach.saveit.UI.YearArrayListAdapter;
import com.example.galtzemach.saveit.UI.dummy.DummyContent;
import com.example.galtzemach.saveit.UI.dummy.SalaryFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import static com.example.galtzemach.saveit.UI.dummy.SalaryFragment.OnListFragmentInteractionListener;

public class MainActivity extends AppCompatActivity implements OnListFragmentInteractionListener, AddSalaryFragment.OnFragmentInteractionListener, AddWarrantyFragment.OnFragmentInteractionListener, AddMonthlyBillsFragment.OnFragmentInteractionListener, DataReadyListener {

    private boolean isFirst = true;
    private final String TAG = this.getClass().toString();

    // create FireBase auth feature
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static DataBase dataBase;

    private ArrayList<Salary> salaryArrayList;
    private ArrayList<Warranty> warrantyArrayList;
    private ArrayList<MonthlyBills> monthlyBillsArrayList;
    private ArrayList<String> yearsMonthlyBillsArraylist;

    private enum Category {salaryList, salaryMonth, salaryItem, warrantyList, warrantyName, warrantyItem, monthlyBillsList, monthlyBillsYear, monthlyBillsMonth, monthlyBillsItem};
    private Category currentCategory;
    private enum Mode {pull, pushh};
    private Mode currentMode;


    public static FloatingActionButton fab;
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

    public static String user_id;

    // create progress dialog
    private ProgressDialog mProgressDialog;
    private ProgressBar mProgressBar;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // create instance of data base class and register as listener
        dataBase = new DataBase();
        dataBase.registerListener(this);

        //user_id = mAuth.getCurrentUser().getUid();
        user_id = null;

        mProgressDialog = new ProgressDialog(this);
        mProgressBar = new ProgressBar(this);

        mProgressDialog.setMessage("Verifying permission");
        mProgressDialog.show();

        // check if user sign in
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    user_id = mAuth.getCurrentUser().getUid();
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                    if(isFirst == true){
                        isFirst = false;
                        dataBase.getYearsPerUser_salary(user_id);
                    }
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
        listView.setPadding(15, 15, 15, 15);

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
                    case salaryList:
                        if (currentMode == Mode.pushh) {
                            currentMode = Mode.pull;
                            dataBase.getYearsPerUser_salary(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        else {
                            currentMode = Mode.pushh;
                            openAddSalaryFragment();
                        }
                        break;


                    case warrantyList:
                        if (currentMode == Mode.pushh) {
                            currentMode = Mode.pull;
                            dataBase.getYearsPerUser_Warranty(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        else {
                            currentMode = Mode.pushh;
                            openAddWarrantyFragment();
                        }
                        break;


                    case monthlyBillsList:
                        if (currentMode == Mode.pushh) {
                            currentMode = Mode.pull;
                            dataBase.getCategoryPerUser(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        else {
                            currentMode = Mode.pushh;
                            openAddMonthlyBillsFragment();
                        }
                        break;
                }

                if (currentMode == Mode.pull) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_add_white));
                }else
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_action_backe_white));

                }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_salary:
                        currentCategory = Category.salaryList;

                        if (currentMode == Mode.pushh)
                            openAddSalaryFragment();
                        else {
                            dataBase.getYearsPerUser_salary(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        return true;


                    case R.id.navigation_warranty:
                        currentCategory = Category.warrantyList;

                        if (currentMode == Mode.pushh)
                            openAddWarrantyFragment();
                        else {
                            dataBase.getYearsPerUser_Warranty(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        return true;


                    case R.id.navigation_monthly_bills:
                        currentCategory = Category.monthlyBillsList;

                        if (currentMode == Mode.pushh)
                            openAddMonthlyBillsFragment();
                        else {
                            dataBase.getCategoryPerUser(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        return true;
                }
                return false;
            }
        });

        initialDefault();
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
        currentCategory = Category.salaryList;
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


    @Override
    public void onBackPressed() {

        if(currentMode == Mode.pull) {
            switch (currentCategory) {
                case salaryList:
                    finish();
                    break;

                case salaryMonth:
                    dataBase.getYearsPerUser_salary(user_id);
                    break;

                case salaryItem:
                    dataBase.getYearsPerUser_salary(user_id);
                    break;

                case warrantyList:
                    finish();
                    break;

                case warrantyName:
                    dataBase.getYearsPerUser_Warranty(user_id);
                    break;

                case warrantyItem:
                    dataBase.getYearsPerUser_Warranty(user_id);
                    break;

                case monthlyBillsList:
                    finish();
                    break;

                case monthlyBillsYear:
                    dataBase.getCategoryPerUser(user_id);
                    break;

                case monthlyBillsMonth:
                    dataBase.getCategoryPerUser(user_id);

                    break;

                case monthlyBillsItem:
                    dataBase.getCategoryPerUser(user_id);
                    break;
            }
        }else{
            fab.callOnClick();
        }
    }

    private void openSalaryList(final ArrayList<String> yearsSalaryArrayList) {
        currentCategory = Category.salaryList;
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearsSalaryAdapter yearAdapter = new YearsSalaryAdapter(this, yearsSalaryArrayList);
        listView.setAdapter(yearAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dataBase.getSalaryPerUserAndYear(user_id, Integer.parseInt(yearsSalaryArrayList.get(position)));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        openSalaryItem(position);
                    }
                });
            }
        });
    }

    private void openSalaryItem(int position) {
        currentCategory = Category.salaryItem;
        nestedScrollView.removeAllViews();
        View salaryView = getLayoutInflater().inflate(R.layout.salary, nestedScrollView, true);

        TextView employerTextView = (TextView) salaryView.findViewById(R.id.salary_employer);
                        employerTextView.setText("Employer: " + salaryArrayList.get(position).getEmployer());

        TextView mYDateTextView = (TextView) salaryView.findViewById(R.id.salary_m_y);
                        mYDateTextView.setText("Date: " + salaryArrayList.get(position).getMonth() + "/" + salaryArrayList.get(position).getYear());

        TextView grossNetTextView = (TextView) salaryView.findViewById(R.id.salary_gross_net);
                        grossNetTextView.setText(salaryArrayList.get(position).getGrossRevenue() + "$  |  " + salaryArrayList.get(position).getNetRevenue() + "$");

        TextView notesTextView = (TextView) salaryView.findViewById(R.id.salary_notes);
                        notesTextView.setText("Notes: " + salaryArrayList.get(position).getNotes());

        if(salaryArrayList.get(position).getDownloadUriArr() != null) {
            ListView photoSalaryListView = (ListView) salaryView.findViewById(R.id.salary_photos_list_view);
            photoSalaryListView.setNestedScrollingEnabled(true);
            PhotosAdapter photosAdapter = new PhotosAdapter(MainActivity.this, salaryArrayList.get(position).getDownloadUriArr());
            photoSalaryListView.setAdapter(photosAdapter);
        }
    }

    private void openSalaryMonthList(ArrayList<Salary> salaryList) {
        currentCategory = Category.salaryMonth;
        salaryArrayList = salaryList;
        SalaryRowAdapter salaryRowAdapter = new SalaryRowAdapter(getApplicationContext(), salaryList);
        listView.setAdapter(salaryRowAdapter);
    }


    private void openWarrantyList(final ArrayList<String> yearsList) {
        currentCategory = Category.warrantyList;
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearsWarrantyAdapter yearsWarrantyAdapter = new YearsWarrantyAdapter(this, yearsList);
        listView.setAdapter(yearsWarrantyAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dataBase.getWarrantyPerUserAndTYear(user_id, Integer.parseInt(yearsList.get(position)));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        openWarrantyItem(warrantyArrayList, position);
                    }
                });

            }
        });
    }

    private void openWarrantyItem(ArrayList<Warranty> warrantyArrayList, int position) {
        currentCategory = Category.warrantyItem;
        nestedScrollView.removeAllViews();
        View warrantyView = getLayoutInflater().inflate(R.layout.warranty, nestedScrollView, true);///

        TextView nameTextView = (TextView) warrantyView.findViewById(R.id.warranty_name);
                        nameTextView.setText("Name: " + warrantyArrayList.get(position).getName().toString());

        TextView periodInMonthsTextView = (TextView) warrantyView.findViewById(R.id.warranty_in_months);
                        periodInMonthsTextView.setText("Period in months: " + warrantyArrayList.get(position).getPeriodInMonths());

        TextView purchaseDateTextView = (TextView) warrantyView.findViewById(R.id.warranty_purchas_date);
                        purchaseDateTextView.setText("purchase Date: " + warrantyArrayList.get(position).getPurchaseDate().getYear() +"/"+ warrantyArrayList.get(position).getPurchaseDate().getMonth() +"/"+ warrantyArrayList.get(position).getPurchaseDate().getDay() + "  | ");

        TextView expireDateTextView = (TextView) warrantyView.findViewById(R.id.warranty_exp_date);
                        expireDateTextView.setText("Exp Date: " + warrantyArrayList.get(position).getExpiryDate().getYear() +"/"+ warrantyArrayList.get(position).getExpiryDate().getMonth() +"/"+ warrantyArrayList.get(position).getExpiryDate().getDay());

        TextView costTextView = (TextView) warrantyView.findViewById(R.id.warranty_cost);
                        costTextView.setText("Cost: " + warrantyArrayList.get(position).getCost());

        TextView notesTextView = (TextView) warrantyView.findViewById(R.id.warranty_notes);
                        notesTextView.setText("Notes:" + warrantyArrayList.get(position).getNotes());

        if(photosArrayList != null) {
            ListView photoWarrantyListView = (ListView) warrantyView.findViewById(R.id.warranty_photos_list_view);
            photoWarrantyListView.setNestedScrollingEnabled(true);
            PhotosAdapter photosAdapter = new PhotosAdapter(MainActivity.this, photosArrayList);
            photoWarrantyListView.setAdapter(photosAdapter);
        }
    }

    private void openWarrantyNameList(ArrayList<Warranty> warrantyList) {
        currentCategory = Category.warrantyName;
        warrantyArrayList = warrantyList;
        WarrantyRowAdapter warrantyRowAdapter = new WarrantyRowAdapter(getApplicationContext(), warrantyList);
        listView.setAdapter(warrantyRowAdapter);
    }

    private void openMonthlyBillsCategoryList(final ArrayList<String> categoryList) {
        currentCategory = Category.monthlyBillsList;
        nestedScrollView.removeAllViews();
        nestedScrollView.addView(listView);
        YearArrayListAdapter yearArrayListAdapter = new YearArrayListAdapter(this, categoryList);
        listView.setAdapter(yearArrayListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dataBase.getYearslistPerUserAndCategory(user_id, categoryList.get(position));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                        dataBase.getMonthlyBillsListPerUserAndYear(user_id, Integer.parseInt(yearsMonthlyBillsArraylist.get(position)), categoryList.get(position));

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                openMonthlyBillsItem(position);
                            }
                        });
                    }
                });
            }
        });
    }

    private void openMonthlyBillsMonthList(ArrayList<MonthlyBills> monthlyBillsList) {
        currentCategory = Category.monthlyBillsMonth;
        this.monthlyBillsArrayList = monthlyBillsList;
        MonthlyBillsRowAdapter monthlyBillsRowAdapter = new MonthlyBillsRowAdapter(this, monthlyBillsList);
        listView.setAdapter(monthlyBillsRowAdapter);
    }

    private void openMonthlyBillsYearList(ArrayList<String> yearsList) {
        currentCategory = Category.monthlyBillsYear;
        this.yearsMonthlyBillsArraylist = yearsList;
        YearsMonthlyBillsAdapter monthlyBillsRowAdapter = new YearsMonthlyBillsAdapter(this, yearsList);
        listView.setAdapter(monthlyBillsRowAdapter);
    }


    private void openMonthlyBillsItem(int position) {
        currentCategory = Category.monthlyBillsItem;
        nestedScrollView.removeAllViews();
        View monthlyBillsView = getLayoutInflater().inflate(R.layout.monthly_bills, nestedScrollView, true);///

        TextView categoryTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_category);
                        categoryTextView.setText("Category: " + monthlyBillsArrayList.get(position).getCategory());

        TextView dateTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_month_year);
                        dateTextView.setText("Date: " + monthlyBillsArrayList.get(position).getMonth()+"/"+monthlyBillsArrayList.get(position).getYear());

        TextView sumTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_sum);
                        sumTextView.setText("Sum: " + monthlyBillsArrayList.get(position).getSum()+"");


        TextView notesTextView = (TextView) monthlyBillsView.findViewById(R.id.monthly_bills_notes);
                        notesTextView.setText("Notes: " + monthlyBillsArrayList.get(position).getNotes());

        if(photosArrayList != null){
            ListView photoMonthlyBillsListView = (ListView) monthlyBillsView.findViewById(R.id.monthly_bills_photos_list_view);
            photoMonthlyBillsListView.setNestedScrollingEnabled(true);
            PhotosAdapter photosAdapter = new PhotosAdapter(MainActivity.this, photosArrayList);
            photoMonthlyBillsListView.setAdapter(photosAdapter);
        }
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

    @Override
    public void onCreateSalaryComplete() {

        this.currentMode = Mode.pull;
    }

    @Override
    public void onYearsListReady_Salary(ArrayList<String> yearsList) {

        mProgressDialog.dismiss();
        openSalaryList(yearsList);
    }

    @Override
    public void onSalaryListReady(ArrayList<Salary> salaryList) {

        mProgressDialog.dismiss();
        openSalaryMonthList(salaryList);
    }

    @Override
    public void onCreateWarrantyComplete() {

        this.currentMode = Mode.pull;
    }

    @Override
    public void onYearsListReady_Warranty(ArrayList<String> yearsList) {

        mProgressDialog.dismiss();
        openWarrantyList(yearsList);
    }


    @Override
    public void onWarrantyListReady(ArrayList<Warranty> warrantyList) {

        mProgressDialog.dismiss();
        openWarrantyNameList(warrantyList);
    }

    @Override
    public void onCreateMonthlyBillsComplete() {

        this.currentMode = Mode.pull;
    }

    @Override
    public void onCategoryListReady(ArrayList<String> categoryList) {

        mProgressDialog.dismiss();
        openMonthlyBillsCategoryList(categoryList);
    }

    @Override
    public void onYearsListReady_MonthlyBills(ArrayList<String> yearsList) {

        mProgressDialog.dismiss();
        openMonthlyBillsYearList(yearsList);
    }


    @Override
    public void onMonthlyBillsListReady(ArrayList<MonthlyBills> monthlyBillsList) {

        mProgressDialog.dismiss();
        openMonthlyBillsMonthList(monthlyBillsList);
    }


}