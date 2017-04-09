package com.example.galtzemach.saveit.DB;

import android.net.Uri;
import android.util.Log;

import com.example.galtzemach.saveit.BL.MonthlyBills;
import com.example.galtzemach.saveit.BL.Salary;
import com.example.galtzemach.saveit.BL.Warranty;
import com.example.galtzemach.saveit.UI.DataReadyListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

/**
 * Created by Tal on 07/04/2017.
 */

public class DataBase{

    private final String TAG = this.getClass().toString();

    // create FireBaseDatabase feature + specific userRef
    private FirebaseDatabase mDataBase;
    private DatabaseReference mUserRef;

    private DatabaseReference newItemRef;

    // create StorageReference
    private StorageReference mStorageRef;

    // listeners list when results is ready
    private ArrayList<DataReadyListener> listeners;

    // list for results
    private ArrayList<String> employersList;
    private ArrayList<Integer> yearsList;
    private ArrayList<Salary> salaryList;
    private ArrayList<Warranty> warrantyList;
    private ArrayList<String> categoryList;
    private ArrayList<MonthlyBills> monthlyBillsList;


    public DataBase() {

        mDataBase = FirebaseDatabase.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();

        // initialize the lists
        employersList = new ArrayList<>();
        yearsList = new ArrayList<>();
        salaryList = new ArrayList<>();
        warrantyList = new ArrayList<>();
        categoryList = new ArrayList<>();
        monthlyBillsList = new ArrayList<>();

        listeners = new ArrayList<>();

    }

    public void registerListener(DataReadyListener dataReadyListener) {
        listeners.add(dataReadyListener);
    }

//    public ArrayList<Image> getPhotoArrPerItem(String user_id, String category, ) {
//
//        ArrayList<Image> photoArr = new ArrayList<>();
//
//    }


    public void createNewSalary(String user_id, final Salary salary, final ArrayList<Uri> uploadUriArr) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        newItemRef = mUserRef.child("Salary").push();

        final ArrayList<String> downloadArr = new ArrayList<>();

        //upload photo to storage
        for (int i = 0; i < uploadUriArr.size(); i++) {

            final int finalI = i;

            StorageReference filePath = mStorageRef.child("Photos").child(uploadUriArr.get(i).getLastPathSegment());

            filePath.putFile(uploadUriArr.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String tempUri = taskSnapshot.getDownloadUrl().toString();
                    downloadArr.add(tempUri);

                    // complete upload photo
                    if (finalI == uploadUriArr.size() - 1) {

                        salary.setDownloadUriArr(downloadArr);

                        // save the new salary on data base
                        newItemRef.setValue(salary);

                        // notify that complete
                        for (DataReadyListener dataReadyListener : listeners) {

                            dataReadyListener.onAddSalaryComplete();
                        }
                    }
                }
            });
        }
    }


    public void createNewWarranty(String user_id, final Warranty warranty, final ArrayList<Uri> uploadUriArr) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        newItemRef = mUserRef.child("Warranty").push();

        final ArrayList<String> downloadArr = new ArrayList<>();

        //upload photo to storage
        for (int i = 0; i < uploadUriArr.size(); i++) {

            final int finalI = i;

            StorageReference filePath = mStorageRef.child("Photos").child(uploadUriArr.get(i).getLastPathSegment());

            filePath.putFile(uploadUriArr.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String tempUri = taskSnapshot.getDownloadUrl().toString();
                    downloadArr.add(tempUri);

                    // complete upload photo
                    if (finalI == uploadUriArr.size() - 1) {

                        warranty.setDownloadUriArr(downloadArr);

                        // save the new salary on data base
                        newItemRef.setValue(warranty);

                        // notify that complete
                        for (DataReadyListener dataReadyListener : listeners) {

                            dataReadyListener.onAddSalaryComplete();
                        }
                    }
                }
            });
        }
    }

    public void createNewMonthlyBills(String user_id, final MonthlyBills monthlyBills, final ArrayList<Uri> uploadUriArr) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        newItemRef = mUserRef.child("MonthlyBills").push();

        final ArrayList<String> downloadArr = new ArrayList<>();

        //upload photo to storage
        for (int i = 0; i < uploadUriArr.size(); i++) {

            final int finalI = i;

            StorageReference filePath = mStorageRef.child("Photos").child(uploadUriArr.get(i).getLastPathSegment());

            filePath.putFile(uploadUriArr.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String tempUri = taskSnapshot.getDownloadUrl().toString();
                    downloadArr.add(tempUri);

                    if (finalI == uploadUriArr.size() - 1) {

                        monthlyBills.setDownloadUriArr(downloadArr);

                        // save the new salary on data base
                        newItemRef.setValue(monthlyBills);

                        // notify that complete
                        for (DataReadyListener dataReadyListener : listeners) {

                            dataReadyListener.onAddMonthlyBillsComplete();
                        }
                    }
                }
            });
        }
    }

    public void getEmployersPerUser(String user_id) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> salaryItems = dataSnapshot.child("Salary").getChildren();

                for (DataSnapshot salary : salaryItems) {

                    String tempEmployer = salary.getValue(Salary.class).getEmployer();

                    if (! employersList.contains(tempEmployer) ) {

                        employersList.add(tempEmployer);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onEmployersListReady(employersList);
                        employersList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getYearsPerUserAndEmloyer(String user_id, final String employer) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> salaryItems = dataSnapshot.child("Salary").getChildren();

                for (DataSnapshot salary : salaryItems) {

                    int tempYear = salary.getValue(Salary.class).getYear();
                    String tempEmployer = salary.getValue(Salary.class).getEmployer();

                    if (! yearsList.contains(tempYear) && tempEmployer.equals(employer)) {
                        yearsList.add(tempYear);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onYearsListReady_Salary(yearsList);
                        yearsList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getSalaryPerUserAndYear(String user_id, final int year) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> salaryItems = dataSnapshot.child("Salary").getChildren();

                for (DataSnapshot salary : salaryItems) {

                    Salary tempSalary = salary.getValue(Salary.class);

                    if (tempSalary.getYear() == year) {
                        salaryList.add(tempSalary);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onSalaryListReady(salaryList);
                        salaryList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getYearsPerUser_Warranty(String user_id) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> warrantyItems = dataSnapshot.child("Warranty").getChildren();

                for (DataSnapshot warranty : warrantyItems) {

                    int tempYear = warranty.getValue(Warranty.class).getPurchaseDate().getYear();

                    if (! yearsList.contains(tempYear) ) {

                        yearsList.add(tempYear);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onYearsListReady_Warranty(yearsList);
                        yearsList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getWarrantyPerUserAndTYear(String user_id, final int year) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> warrantyItems = dataSnapshot.child("Warranty").getChildren();

                for (DataSnapshot warranty : warrantyItems) {

                    Warranty tempWarranty = warranty.getValue(Warranty.class);

                    if (tempWarranty.getPurchaseDate().getYear() == year) {

                        warrantyList.add(tempWarranty);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onWarrantyListReady(warrantyList);
                        warrantyList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getCategoryPerUser(String user_id) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> monthlyBillsItem = dataSnapshot.child("MonthlyBills").getChildren();

                for (DataSnapshot monthlyBills : monthlyBillsItem) {

                    String tempCategory = monthlyBills.getValue(MonthlyBills.class).getCategory();

                    if ( ! categoryList.contains(tempCategory)) {

                        categoryList.add(tempCategory);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onCategoryListReady(categoryList);
                        categoryList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getYearslistPerUserAndCategory(String user_id, final String category) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> monthlyBillsItem = dataSnapshot.child("MonthlyBills").getChildren();

                for (DataSnapshot monthlyBills : monthlyBillsItem) {

                    MonthlyBills tempMonthlyBills = monthlyBills.getValue(MonthlyBills.class);

                    if ( ! yearsList.contains(tempMonthlyBills.getYear()) && tempMonthlyBills.getCategory().equals(category) ) {

                        yearsList.add(tempMonthlyBills.getYear());
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onYearsListReady_MonthlyBills(yearsList);
                        yearsList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }

    public void getMonthlyBillsListPerUserAndYear(String user_id, final int year, final String category) {

        mUserRef = mDataBase.getReference().child("Users").child(user_id).getRef();
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> monthlyBillsItem = dataSnapshot.child("MonthlyBills").getChildren();

                for (DataSnapshot monthlyBills : monthlyBillsItem) {

                    MonthlyBills tempMonthlyBills = monthlyBills.getValue(MonthlyBills.class);

                    if ( tempMonthlyBills.getYear() == year && tempMonthlyBills.getCategory().equals(category)) {

                        monthlyBillsList.add(tempMonthlyBills);
                    }
                }

                // notify that complete
                for (DataReadyListener dataReadyListener : listeners) {

                        dataReadyListener.onMonthlyBillsListReady(monthlyBillsList);
                        monthlyBillsList.clear();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "The query failed!");
            }
        });
    }


}
