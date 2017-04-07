package com.example.galtzemach.saveit.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.galtzemach.saveit.BL.Salary;
import com.example.galtzemach.saveit.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddSalaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddSalaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddSalaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // create progress dialog
    private ProgressDialog mProgressDialog;

    // create FireBaseDatabase feature + specific userRef
    private FirebaseDatabase mDataBase;
    private DatabaseReference mUserRef;

    private DatabaseReference newSalaryRef;

    // create StorageReference
    private StorageReference mStorageRef;

    // create FireBase auth feature
    private FirebaseAuth mAuth;

    private OnFragmentInteractionListener mListener;

    private Button mCreateSalaryBtn;
    private ImageButton mAddImageBtn;

    private Salary tempSalary;

    private ArrayList<Uri> uploadUriArr;
    private ArrayList<Uri> downloadUriArr;

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_INTENT = 2;

    public AddSalaryFragment() {
        // Required empty public constructor
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

        // initialize fire base features
        mAuth = FirebaseAuth.getInstance();
        mDataBase = FirebaseDatabase.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        uploadUriArr = new ArrayList<>();
        downloadUriArr = new ArrayList<>();

        mProgressDialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       ConstraintLayout constraintLayout = (ConstraintLayout) inflater.inflate(R.layout.fragment_add_salary, container, false);

        // create correct specific user ref
        String user_id = mAuth.getCurrentUser().getUid();
        mUserRef = mDataBase.getReference().child("Users").child(user_id); ///.getRef();

        mAddImageBtn = (ImageButton) constraintLayout.findViewById(R.id.imageButtonAddImage);
        mAddImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // take image from gallery
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_INTENT);

            }
        });

        mCreateSalaryBtn = (Button) constraintLayout.findViewById(R.id.createSalarybtn);
        mCreateSalaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressDialog.setMessage("create new salary item...");
                mProgressDialog.show();

                createNewSalary();



            }
        });

        return constraintLayout;

    }

    private void createNewSalary() {

        newSalaryRef = mUserRef.child("Salary").push();

        //upload photo to storage
        for (int i = 0; i < uploadUriArr.size(); i++) {

            final int finalI = i;

            StorageReference filePath = mStorageRef.child("Photos").child(uploadUriArr.get(i).getLastPathSegment());

            filePath.putFile(uploadUriArr.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    downloadUriArr.add(taskSnapshot.getDownloadUrl());

                    String tempUri = taskSnapshot.getDownloadUrl().toString();

                    newSalaryRef.child("downloadUri").push().setValue(tempUri);

                    if (finalI == uploadUriArr.size()-1) {

                        mProgressDialog.dismiss();
                        Toast.makeText(getContext(), "Photo upload finished", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
        // create new salary object and save it on data base
        tempSalary = new Salary("Intel", 2017, 01, 20000, 15000, "Good place");
        newSalaryRef.setValue(tempSalary);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){

            Uri uri = data.getData();

            uploadUriArr.add(uri);

            Toast.makeText(getContext(), "add uri to upload Arr", Toast.LENGTH_SHORT).show();
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
