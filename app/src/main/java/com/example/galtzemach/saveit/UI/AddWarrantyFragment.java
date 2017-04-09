package com.example.galtzemach.saveit.UI;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galtzemach.saveit.BL.Warranty;
import com.example.galtzemach.saveit.R;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddWarrantyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddWarrantyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddWarrantyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;

    private Warranty.Category category;
    private String name = null;
    private Date purchaseDate;
    private Date expireDate;
    private int months = -1;
    private float cost = -1;
    private String notes;
    private EditText nameEditText;
    private EditText purchaseDateEditText;
    private Button addButton;
    private Button removeButton;
    private TextView numAddedTextView;
    private Button okButton;
    private Spinner categorySpinner;
    private EditText monthsEditText;
    private EditText costEditText;
    private EditText notesEditText;


    public AddWarrantyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addWarrantyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddWarrantyFragment newInstance(String param1, String param2) {
        AddWarrantyFragment fragment = new AddWarrantyFragment();
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
        view = inflater.inflate(R.layout.fragment_add_warranty, container, false);

        //get fields
        nameEditText = (EditText) view.findViewById(R.id.w__row_name);
        purchaseDateEditText = (EditText) view.findViewById(R.id.w_purchase_date);
        addButton = (Button) view.findViewById(R.id.w_add_photos);
        removeButton = (Button) view.findViewById(R.id.w_remove_photos);
        numAddedTextView = (TextView) view.findViewById(R.id.w_num_added_photos);
        okButton = (Button) view.findViewById(R.id.w_ok);
        categorySpinner = (Spinner) view.findViewById(R.id.w_category);
        monthsEditText = (EditText) view.findViewById(R.id.w_period_in_months);
        costEditText = (EditText) view.findViewById(R.id.w_cost);
        notesEditText = (EditText) view.findViewById(R.id.w_nots);

        categorySpinner.setAdapter(new ArrayAdapter<Warranty.Category>(getContext(), android.R.layout.simple_spinner_item, Warranty.Category.values()));

        //get now date
        Calendar calendar = Calendar.getInstance();
        final int setY = calendar.get(Calendar.YEAR);
        final int setM = calendar.get(Calendar.MONTH);
        final int setD = calendar.get(Calendar.DAY_OF_MONTH);

        //purchaseDateEditText
        final DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int y, int m, int d) {
                Toast.makeText(getContext(), y + " " + m + " " + d, Toast.LENGTH_SHORT).show();
                purchaseDate = new Date(y,m,d);
                purchaseDateEditText.setText(y+"/"+(m+1)+"/"+d);
            }
        };
        purchaseDateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b == true) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), myDateListener, setY, setM, setD);
                    datePickerDialog.show();
                }
            }
        });

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
                    createWarrantyObject();
//                    send new salary to db ///
                }
            }
        });

        return view;
    }

    private boolean checkAllFields() {
        boolean resBool = true;

        //category
        category = (Warranty.Category) categorySpinner.getSelectedItem();


        //name
        if (nameEditText.getText().length() != 0)
            name = nameEditText.getText().toString();

        if(name == null) {
            nameEditText.setHintTextColor(Color.RED);
            resBool = false;
        }

        //purchaseDate
        if(purchaseDateEditText.getText().length() == 0){
            purchaseDateEditText.setHintTextColor(Color.RED);
            resBool = false;
        }

        //months
        if(monthsEditText.getText().length() != 0)
            months = Integer.parseInt(monthsEditText.getText().toString());

        if(months == -1){
            monthsEditText.setHintTextColor(Color.RED);
            resBool = false;
        }


        //cost
        if(costEditText.getText().length() != 0)
            cost = Float.parseFloat(costEditText.getText().toString());

        if(cost == -1){
            costEditText.setHintTextColor(Color.RED);
            resBool = false;
        }

        notes = notesEditText.getText().toString();

        return resBool;
    }

    private void createWarrantyObject() {
        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
        expireDate = purchaseDate;
        if(months != 0) {
            if (expireDate.getMonth() + months < 11) {
                expireDate.setMonth(expireDate.getMonth() + months);
            } else if ((months % 12) + expireDate.getMonth() <= 11) {
                expireDate.setYear(expireDate.getYear() + (months / 12));
                expireDate.setMonth(expireDate.getMonth() + (months % 12));
            } else {
                expireDate.setYear(expireDate.getYear() + (months / 12) + 1);
                expireDate.setMonth((expireDate.getMonth() + (months % 12)) % 11);
            }
        }
        Warranty newWarranty = new Warranty(category, name, months, purchaseDate, expireDate, cost, null, notes);
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
