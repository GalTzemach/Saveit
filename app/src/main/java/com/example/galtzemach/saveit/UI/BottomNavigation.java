//package com.example.galtzemach.saveit.UI;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.design.widget.BottomNavigationView;
//import android.support.v7.app.AppCompatActivity;
//import android.view.MenuItem;
//import android.widget.TextView;
//
//import com.example.galtzemach.saveit.R;
//
//public class BottomNavigation extends AppCompatActivity {
//
//    private TextView mTextMessage;
//
//    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
//            = new BottomNavigationView.OnNavigationItemSelectedListener() {
//
//        @Override
//        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.navigation_warranty:
//                    mTextMessage.setText(R.string.title_warranty);
//                    return true;
//                case R.id.navigation_monthly_bills:
//                    mTextMessage.setText(R.string.title_monthly_bills);
//                    return true;
//                case R.id.navigation_salary:
//                    mTextMessage.setText(R.string.title_salary);
//                    return true;
//            }
//            return false;
//        }
//
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bottom_navigation);
//
//        mTextMessage = (TextView) findViewById(R.id.message);
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//    }
//
//}
