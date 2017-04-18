package com.example.galtzemach.saveit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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

import com.example.galtzemach.saveit.DB.DataBase;
import com.example.galtzemach.saveit.MonthlyBills.AddMonthlyBillsFragment;
import com.example.galtzemach.saveit.MonthlyBills.MonthlyBills;
import com.example.galtzemach.saveit.MonthlyBills.MonthlyBillsRowAdapter;
import com.example.galtzemach.saveit.MonthlyBills.YearsMonthlyBillsAdapter;
import com.example.galtzemach.saveit.Salary.AddSalaryFragment;
import com.example.galtzemach.saveit.Salary.Salary;
import com.example.galtzemach.saveit.Salary.SalaryRowAdapter;
import com.example.galtzemach.saveit.Salary.YearsSalaryAdapter;
import com.example.galtzemach.saveit.Warranty.AddWarrantyFragment;
import com.example.galtzemach.saveit.Warranty.Warranty;
import com.example.galtzemach.saveit.Warranty.WarrantyRowAdapter;
import com.example.galtzemach.saveit.Warranty.YearsWarrantyAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity implements AddSalaryFragment.OnFragmentInteractionListener, AddWarrantyFragment.OnFragmentInteractionListener, AddMonthlyBillsFragment.OnFragmentInteractionListener, DataReadyListener {

    private boolean isFirst = true;
    private final String TAG = this.getClass().toString();

    // create FireBase auth feature
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public static DataBase dataBase;

    private ArrayList<Salary> salaryArrayList;
    private ArrayList<Warranty> warrantyArrayList;
    private ArrayList<MonthlyBills> monthlyBillsArrayList;
    private ArrayList<String> yearsMonthlyBillsArrayList;

    private enum Category {salaryList, salaryMonth, salaryItem, warrantyList, warrantyName, warrantyItem, monthlyBillsList, monthlyBillsYear, monthlyBillsMonth, monthlyBillsItem};
    private Category currentCategory;
    public enum Mode {pull, pushh};
    public static Mode currentMode;


    public static FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private TabLayout tabLayout;
    private NestedScrollView nestedScrollView;
    private ListView listView;
    private AddSalaryFragment addSalaryFragment;
    private AddWarrantyFragment addWarrantyFragment;
    private AddMonthlyBillsFragment addMonthlyBillsFragment;

    private ArrayList<String> photosArrayList;

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

        mProgressDialog.setMessage("Verifying authentication");
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

        listView = (ListView) findViewById(R.id.main_list_view);
        listView.setPadding(15, 15, 15, 15);
        listView.setNestedScrollingEnabled(true);

        nestedScrollView = (NestedScrollView) findViewById(R.id.NestedScrollView_main);
        nestedScrollView.setNestedScrollingEnabled(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_main);
        bottomNavigationView.setNestedScrollingEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        fab = (FloatingActionButton) findViewById(R.id.fab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(currentCategory == Category.salaryMonth || currentCategory == Category.salaryItem)
                    currentCategory = Category.salaryList;
                else if (currentCategory == Category.warrantyName || currentCategory == Category.warrantyItem)
                    currentCategory = Category.warrantyList;
                else if (currentCategory == Category.monthlyBillsYear || currentCategory == Category.monthlyBillsMonth || currentCategory == Category.monthlyBillsItem)
                    currentCategory = Category.monthlyBillsList;

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

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentCategory = Category.salaryList;

                        if (currentMode == Mode.pushh)
                            openAddSalaryFragment();
                        else {
                            dataBase.getYearsPerUser_salary(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        break;

                    case 1:
                        currentCategory = Category.warrantyList;

                        if (currentMode == Mode.pushh)
                            openAddWarrantyFragment();
                        else {
                            dataBase.getYearsPerUser_Warranty(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        break;

                    case 2:
                        currentCategory = Category.monthlyBillsList;

                        if (currentMode == Mode.pushh)
                            openAddMonthlyBillsFragment();
                        else {
                            dataBase.getCategoryPerUser(user_id);

                            mProgressDialog.setMessage("Loading data..");
                            mProgressDialog.show();
                        }
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

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

        TextView grossTextView = (TextView) salaryView.findViewById(R.id.salary_gross);
        grossTextView.setText("Gross: " + salaryArrayList.get(position).getGrossRevenue());

        TextView netTextView = (TextView) salaryView.findViewById(R.id.salary_net);
        netTextView.setText("Net: " + salaryArrayList.get(position).getNetRevenue());

        TextView notesTextView = (TextView) salaryView.findViewById(R.id.salary_notes);
                        notesTextView.setText("Notes: " + salaryArrayList.get(position).getNotes());

        if(salaryArrayList.get(position).getDownloadUriArr() != null) {
            ListView photoSalaryListView = (ListView) salaryView.findViewById(R.id.salary_photos_list_view);
            photoSalaryListView.setNestedScrollingEnabled(true);
            PhotosAdapter salaryPhotosAdapter = new PhotosAdapter(MainActivity.this, salaryArrayList.get(position).getDownloadUriArr());
            photoSalaryListView.setAdapter(salaryPhotosAdapter);
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
        View warrantyView = getLayoutInflater().inflate(R.layout.warranty, nestedScrollView, true);

        TextView nameTextView = (TextView) warrantyView.findViewById(R.id.warranty_name);
                        nameTextView.setText("Name: " + warrantyArrayList.get(position).getName().toString());

        TextView purchaseDateTextView = (TextView) warrantyView.findViewById(R.id.warranty_purchas_date);
        purchaseDateTextView.setText("Purchase Date: " + warrantyArrayList.get(position).getPurchaseDate().getYear() +"/"+ (warrantyArrayList.get(position).getPurchaseDate().getMonth()+1) +"/"+ getDayOfMonth(warrantyArrayList.get(position).getPurchaseDate()));

        TextView periodInMonthsTextView = (TextView) warrantyView.findViewById(R.id.warranty_in_months);
                        periodInMonthsTextView.setText("Period in months: " + warrantyArrayList.get(position).getPeriodInMonths());

        TextView expireDateTextView = (TextView) warrantyView.findViewById(R.id.warranty_exp_date);
                        expireDateTextView.setText("Exp Date: " + warrantyArrayList.get(position).getExpiryDate().getYear() +"/"+ (warrantyArrayList.get(position).getExpiryDate().getMonth()+1) +"/"+ getDayOfMonth(warrantyArrayList.get(position).getExpiryDate()));

        TextView costTextView = (TextView) warrantyView.findViewById(R.id.warranty_cost);
                        costTextView.setText("Cost: " + warrantyArrayList.get(position).getCost());

        TextView notesTextView = (TextView) warrantyView.findViewById(R.id.warranty_notes);
                        notesTextView.setText("Notes:" + warrantyArrayList.get(position).getNotes());

        if(warrantyArrayList.get(position).getDownloadUriArr() != null) {
            ListView photoWarrantyListView = (ListView) warrantyView.findViewById(R.id.warranty_photos_list_view);
            photoWarrantyListView.setNestedScrollingEnabled(true);
            PhotosAdapter warrantyPhotosAdapter = new PhotosAdapter(MainActivity.this, warrantyArrayList.get(position).getDownloadUriArr());
            photoWarrantyListView.setAdapter(warrantyPhotosAdapter);
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
        YearsMonthlyBillsAdapter yearArrayListAdapter = new YearsMonthlyBillsAdapter(this, categoryList);
        listView.setAdapter(yearArrayListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                final String monthlyBillsCategory = categoryList.get(position);
                dataBase.getYearslistPerUserAndCategory(user_id, categoryList.get(position));

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                        dataBase.getMonthlyBillsListPerUserAndYear(user_id, Integer.parseInt(yearsMonthlyBillsArrayList.get(position)), monthlyBillsCategory);

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
        this.yearsMonthlyBillsArrayList = yearsList;
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

        if(monthlyBillsArrayList.get(position).getDownloadUriArr() != null){
            ListView photoMonthlyBillsListView = (ListView) monthlyBillsView.findViewById(R.id.monthly_bills_photos_list_view);
            photoMonthlyBillsListView.setNestedScrollingEnabled(true);
            PhotosAdapter monthlyBillsPhotosAdapter = new PhotosAdapter(MainActivity.this, monthlyBillsArrayList.get(position).getDownloadUriArr());
            photoMonthlyBillsListView.setAdapter(monthlyBillsPhotosAdapter);
        }
    }

    public static int getDayOfMonth(Date aDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
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

        if(id == R.id.action_exit){
            finish();

        }

        return super.onOptionsItemSelected(item);
    }

    private void logOutUser() {

        mAuth.signOut();
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

    }

    @Override
    public void onCategoryListReady(ArrayList<String> categoryList) {

        mProgressDialog.dismiss();
        openMonthlyBillsCategoryList(categoryList);
    }

    @Override
    public void onYearsListReady_MonthlyBills(ArrayList<String> yearsList) {
        mProgressDialog.dismiss();
        HashSet<String> hashSet = new HashSet<String>();
        hashSet.addAll(yearsList);
        yearsList.clear();
        yearsList.addAll(hashSet);
        openMonthlyBillsYearList(yearsList);
    }


    @Override
    public void onMonthlyBillsListReady(ArrayList<MonthlyBills> monthlyBillsList) {

        mProgressDialog.dismiss();
        openMonthlyBillsMonthList(monthlyBillsList);
    }


}