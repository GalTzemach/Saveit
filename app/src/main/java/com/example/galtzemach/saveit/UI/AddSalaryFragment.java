package com.example.galtzemach.saveit.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galtzemach.saveit.BL.MonthlyBills;
import com.example.galtzemach.saveit.BL.Salary;
import com.example.galtzemach.saveit.BL.Warranty;
import com.example.galtzemach.saveit.MainActivity;
import com.example.galtzemach.saveit.R;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static com.example.galtzemach.saveit.R.layout.fragment_add_salary;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddSalaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddSalaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSalaryFragment extends Fragment implements DataReadyListener {

    private final String TAG = this.getClass().toString();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // create progress dialog
    private ProgressDialog mProgressDialog;

    private OnFragmentInteractionListener mListener;

    private View view;
    private String employerField = null;
    private int yearField = -1;
    private int monthField = -1;
    private float grossRevenueField = -1;
    private  float netRevenueField = -1;
    private String notesField;

    private TextView numAddedTextView;
    private Button removeAllPhotosButton;
    private Button addPhotoButton;

    private ArrayList<Uri> uploadUriArr;

    private static final int CAMERA_INTENT = 1;
    private static final int GALLERY_INTENT = 2;


    public AddSalaryFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddSalaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddSalaryFragment newInstance(String param1, String param2) {
        AddSalaryFragment fragment = new AddSalaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        uploadUriArr = new ArrayList<>();

        mProgressDialog = new ProgressDialog(getContext());

        // register as listener to DataBase
        MainActivity.dataBase.registerListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        view = inflater.inflate(fragment_add_salary, container, false);

        //removeAllPhotosButton
        removeAllPhotosButton = (Button) view.findViewById(R.id.s_remove_photos);

        //numAddedTextView
        numAddedTextView = (TextView) view.findViewById(R.id.s_num_added_photos);

        //addPhotoButton
        addPhotoButton = (Button) view.findViewById(R.id.s_add_photos);

        //monthSpinner
        Spinner monthSpinner = (Spinner) view.findViewById(R.id.s_month_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        //addPhotoButton.setOnClickListener
        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                //open addPhotoPopupMenu
                final PopupMenu addPhotoPopupMenu = new PopupMenu(getContext(), view);
                addPhotoPopupMenu.getMenuInflater().inflate(R.menu.add_photo_options, addPhotoPopupMenu.getMenu());
                addPhotoPopupMenu.show();
                addPhotoPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.from_camera) {//from camera
                            photoFromCamera();
                        } else { //from gallery
                            photoFromGallery();
                        }

                        return true;
                    }
                });
            }
        });

        removeAllPhotosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadUriArr.clear();
                addPhotoButton.setText("Add photo");
                numAddedTextView.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
            }
        });

        //okButton
        Button okButton = (Button) view.findViewById(R.id.s_ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields() ){

                    createSalaryObject();

                }
            }
        });


        return view;
    }

    private void photoFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        this.startActivityForResult(galleryIntent, GALLERY_INTENT);
    }

    private void photoFromCamera() {

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_INTENT);

    }

    private void createSalaryObject() {

        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Uploading to cloud...");
        mProgressDialog.show();

        Salary newSalary = new Salary(employerField, yearField, monthField, grossRevenueField, netRevenueField, notesField);
        MainActivity.dataBase.createNewSalary(MainActivity.user_id, newSalary, uploadUriArr);
    }

    private boolean checkAllFields() {
        boolean resBool = true;

        //employer
        EditText employer = (EditText) view.findViewById(R.id.s_employer);
        if (employer.getText().length() != 0)
            employerField = employer.getText().toString();

        //year
        EditText year = (EditText) view.findViewById(R.id.s_year);
        if (year.getText().length() != 0)
            yearField = Integer.parseInt(year.getText().toString());

        //month
        Spinner monthSpinner = (Spinner) view.findViewById(R.id.s_month_spinner);

        //grossRevenue
        EditText grossRevenue = (EditText) view.findViewById(R.id.s_gross_revenue);
        if (grossRevenue.getText().length() != 0)
            grossRevenueField = Float.parseFloat(grossRevenue.getText().toString());

        //netRevenue
        EditText netRevenue = (EditText) view.findViewById(R.id.s_net_revenue);
        if (netRevenue.getText().length() != 0)
            netRevenueField = Float.parseFloat(netRevenue.getText().toString());

        //notes
        EditText notes = (EditText) view.findViewById(R.id.s_row_notes);

        Calendar calendar = Calendar.getInstance();
        /// Toast.makeText(getContext(), calendar.get(Calendar.DATE) + "", Toast.LENGTH_LONG).show();

        //check employer
        if (employerField == null) {
            employer.setHintTextColor(Color.RED);
            resBool = false;
        }

        //check year
        if (yearField == -1) {
            year.setHintTextColor(Color.RED);
            resBool = false;
        } else if (yearField > calendar.get(Calendar.YEAR) || yearField < calendar.get(Calendar.YEAR) - 120) {
            year.setTextColor(Color.RED);
            resBool = false;
        } else
            year.setTextColor(Color.BLACK);

        //check gross & net revenue
        if(grossRevenueField == -1 && netRevenueField == -1){
            grossRevenue.setHintTextColor(Color.RED);
            netRevenue.setHintTextColor(Color.RED);
            resBool = false;
        }else if (grossRevenueField == -1){
            grossRevenue.setHintTextColor(Color.RED);
            resBool = false;
        }else if (netRevenueField == -1) {
            netRevenue.setHintTextColor(Color.RED);
            resBool = false;
        }else if (grossRevenueField < netRevenueField) {
            grossRevenue.setTextColor(Color.RED);
            netRevenue.setTextColor(Color.RED);
            resBool = false;
        }else{
            grossRevenue.setTextColor(Color.BLACK);
            netRevenue.setTextColor(Color.BLACK);
        }

        monthField = Integer.parseInt(monthSpinner.getSelectedItem().toString());

        notesField = notes.getText().toString();

        return resBool;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            uploadUriArr.add(uri);

        } else if (requestCode == CAMERA_INTENT && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            uploadUriArr.add(uri);
        }

        if(uploadUriArr.size() >= 1) {
            numAddedTextView.setVisibility(View.VISIBLE);
            numAddedTextView.setText((uploadUriArr.size() + " Photo added"));
            removeAllPhotosButton.setVisibility(View.VISIBLE);
            addPhotoButton.setText("Add more photos");
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateSalaryComplete() {

        Toast.makeText(getContext(), "Salary successfully added", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        MainActivity.fab.callOnClick();
    }

    @Override
    public void onEmployersListReady(ArrayList<String> employersList) {

    }

    @Override
    public void onYearsListReady_Salary(ArrayList<String> yearsList) {

    }

    @Override
    public void onSalaryListReady(ArrayList<Salary> salaryList) {

    }

    @Override
    public void onCreateWarrantyComplete() {

    }

    @Override
    public void onYearsListReady_Warranty(ArrayList<String> yearsList) {

    }

    @Override
    public void onWarrantyListReady(ArrayList<Warranty> warrantyList) {

    }

    @Override
    public void onCreateMonthlyBillsComplete() {

    }

    @Override
    public void onCategoryListReady(ArrayList<String> CategoryList) {

    }

    @Override
    public void onYearsListReady_MonthlyBills(ArrayList<String> yearsList) {

    }

    @Override
    public void onMonthlyBillsListReady(ArrayList<MonthlyBills> monthlyBillsList) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}