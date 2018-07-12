package com.realty.drake.newyorktimessearcher.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;

import com.realty.drake.newyorktimessearcher.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FilterDialogFragment extends DialogFragment {

    public FilterDialogFragment() {} //Empty Constructor is required for DialogFragment

    public static FilterDialogFragment newInstance(String title) {

        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        // = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
        assert getArguments() != null;
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
    }

    @Override
    public void onResume() {
        super.onResume();
        //Change FilterFragmentDialog size
        int width = getResources().getDimensionPixelSize(R.dimen.popup_width);
        int height = getResources().getDimensionPixelSize(R.dimen.popup_height);
        getDialog().getWindow().setLayout(width, height);
    }

    // Calendar calendar = Calendar.getInstance();
   // EditText etDate = (EditText) View.findViewById(R.id.etDate);
   // DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
   //     @Override
   //     public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
   //         calendar.set(Calendar.YEAR, year);
   //         calendar.set(Calendar.MONTH, month);
   //         calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
   //         updateLabel();
   //     }
   // };
//
   // etDate.setOnClickListener(new View.OnClickListener(){
   //     @Override
   //     public void OnClick(View v) {
   //         new DatePickerDialog(FilterDialogFragment.this, date,
   //                 calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
   //                 calendar.get(Calendar.DAY_OF_MONTH).show());
   //     }
   // });
//
   // private  void updateLabel(){
   //     String dateFormat = "MM/dd/yy";
   //     SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);
   //     etDate.setText(simpleDateFormat.format(calendar.getTime()));
   // }

}

