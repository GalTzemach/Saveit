package com.example.galtzemach.saveit.UI;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.galtzemach.saveit.MainActivity;
import com.example.galtzemach.saveit.R;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddMonthlyBillsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddMonthlyBillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMonthlyBillsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;

    private MonthlyBills.Category category;
    private int year = -1;
    private int month = -1;
    private float sum = -1;
    private String notes;

    private Button addButton;
    private Button removeButton;
    private Button okButton;
    private Spinner categorySpinner;
    private EditText yearEditText;
    private Spinner monthSpinner;
    private EditText sumEditText;
    private EditText notesEditText;
    private TextView numAddedTextView;

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
        addButton = (Button) view.findViewById(R.id.m_add);
        removeButton = (Button) view.findViewById(R.id.m_remove);
        numAddedTextView = (TextView) view.findViewById(R.id.m_num_photos_added);
        okButton = (Button) view.findViewById(R.id.m_ok);


        categorySpinner.setAdapter(new ArrayAdapter<MonthlyBills.Category>(getContext(), android.R.layout.simple_spinner_item, MonthlyBills.Category.values()));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
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
//                            photoFromCamera();
                            Toast.makeText(getContext(), "item " + addPhotoPopupMenu.getMenu().getItem(0).getTitle() + " pressed", Toast.LENGTH_LONG).show();
                        } else { //from gallery
//                            photoFromGallery();
                            Toast.makeText(getContext(), "item " + addPhotoPopupMenu.getMenu().getItem(1).getTitle() + " pressed", Toast.LENGTH_LONG).show();
                        }

                        ///if size of arr photos >= 1
                        numAddedTextView.setVisibility(View.VISIBLE);
                        numAddedTextView.setText("X Photo added");
                        removeButton.setVisibility(View.VISIBLE);


                        ///just if some photo added
                        addButton.setText("Add more photos");

                        return true;
                    }
                });
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton.setText("Add photo");
                numAddedTextView.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
                ///remove photos
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAllFields() ){
                    createMonthlyBillsObject();
//                    send new salary to db ///
                }
            }
        });

        return view;
    }

    private void createMonthlyBillsObject() {
        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
        MonthlyBills newMonthlyBills = new MonthlyBills(category, year, month, sum, null, notes);
        MainActivity.dataBase.createNewMonthlyBills(MainActivity.user_id, newMonthlyBills, null);
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
