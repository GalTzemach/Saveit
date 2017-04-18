package com.example.galtzemach.saveit.MonthlyBills;

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

import com.example.galtzemach.saveit.Salary.Salary;
import com.example.galtzemach.saveit.DataReadyListener;
import com.example.galtzemach.saveit.Warranty.Warranty;
import com.example.galtzemach.saveit.MainActivity;
import com.example.galtzemach.saveit.R;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMonthlyBillsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMonthlyBillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMonthlyBillsFragment extends Fragment implements DataReadyListener {
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

    private MonthlyBills.Category category;
    private int year = -1;
    private int month = -1;
    private float sum = -1;
    private String notes;

    private Button addPhotoButton;
    private Button removeAllPhotosButton;
    private Button okButton;
    private Spinner categorySpinner;
    private EditText yearEditText;
    private Spinner monthSpinner;
    private EditText sumEditText;
    private EditText notesEditText;
    private TextView numAddedTextView;

    private ArrayList<Uri> uploadUriArr;

    private static final int CAMERA_INTENT = 1;
    private static final int GALLERY_INTENT = 2;

    public AddMonthlyBillsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddMonthlyBillsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddMonthlyBillsFragment newInstance(String param1, String param2) {
        AddMonthlyBillsFragment fragment = new AddMonthlyBillsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_monthly_bills, container, false);

        //get fields
        categorySpinner = (Spinner) view.findViewById(R.id.m_category_spinner);
        yearEditText = (EditText) view.findViewById(R.id.m_year);
        monthSpinner = (Spinner) view.findViewById(R.id.m_month_spinner);
        sumEditText = (EditText) view.findViewById(R.id.m_sum);
        notesEditText = (EditText) view.findViewById(R.id.m_notes);
        addPhotoButton = (Button) view.findViewById(R.id.m_add);
        removeAllPhotosButton = (Button) view.findViewById(R.id.m_remove);
        numAddedTextView = (TextView) view.findViewById(R.id.m_num_photos_added);
        okButton = (Button) view.findViewById(R.id.m_ok);


        categorySpinner.setAdapter(new ArrayAdapter<MonthlyBills.Category>(getContext(), android.R.layout.simple_spinner_item, MonthlyBills.Category.values()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

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

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields() ){

                    createMonthlyBillsObject();

                }
            }
        });

        return view;
    }

    private void photoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_INTENT);
    }

    private void photoFromCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_INTENT);
    }

    private void createMonthlyBillsObject() {

        mProgressDialog.setTitle("Please wait");
        mProgressDialog.setMessage("Uploading to cloud...");
        mProgressDialog.show();

        MonthlyBills newMonthlyBills = new MonthlyBills(category, year, month, sum, notes);
        MainActivity.dataBase.createNewMonthlyBills(MainActivity.user_id, newMonthlyBills, uploadUriArr);
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

    private boolean checkAllFields() {
        boolean resBool = true;

        //category
        category = (MonthlyBills.Category) categorySpinner.getSelectedItem();

        //year
        Calendar calendar = Calendar.getInstance();

        if (yearEditText.getText().length() != 0)
            year = Integer.parseInt(yearEditText.getText().toString());

        if (year == -1) {
            yearEditText.setHintTextColor(Color.RED);
            resBool = false;
        } else if (year > calendar.get(Calendar.YEAR) || year < calendar.get(Calendar.YEAR) - 120) {
            yearEditText.setTextColor(Color.RED);
            resBool = false;
        } else {
            yearEditText.setTextColor(Color.BLACK);
        }

        //month
        month = Integer.parseInt(monthSpinner.getSelectedItem().toString());

        //sum
        if(sumEditText.getText().length() != 0)
            sum = Float.parseFloat(sumEditText.getText().toString());

        if(sum == -1){
            sumEditText.setHintTextColor(Color.RED);
            resBool = false;
        }

        //notes
        notes = notesEditText.getText().toString();

        return resBool;
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

        Toast.makeText(getContext(), "Monthly bills successfully added", Toast.LENGTH_SHORT).show();
        mProgressDialog.dismiss();
        MainActivity.fab.callOnClick();
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
