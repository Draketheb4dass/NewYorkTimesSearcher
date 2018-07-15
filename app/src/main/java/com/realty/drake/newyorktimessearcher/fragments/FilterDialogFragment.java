package com.realty.drake.newyorktimessearcher.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.realty.drake.newyorktimessearcher.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class FilterDialogFragment extends DialogFragment  {
    private Button mBtnSubmitFilt;
    static private HashMap<String, String> filter = new HashMap<>();

    public FilterDialogFragment() {} //Empty Constructor is required for DialogFragment


    //Defines the listener interface with a method passing back data result.
    public interface FilterDialogListener{
        void onFinishFilterDialog(HashMap<String, String> filter);
    }

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
        final Spinner spnOrder=(Spinner) view.findViewById(R.id.spnOrder);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(getContext(), R.array.spinnerItems,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnOrder.setAdapter(adapter);

        final CheckBox sport = view.findViewById(R.id.chbSports);
        final CheckBox art = view.findViewById(R.id.chbArt);
        final CheckBox fashionAndStyle = view.findViewById(R.id.chbFashion);

        // Get field from view
        // = (EditText) view.findViewById(R.id.txt_your_name);
        // Fetch arguments from bundle and set title
        assert getArguments() != null;
        String title = getArguments().getString("title", "Filter Search");
        getDialog().setTitle(title);

        //Send data back to fragment
        mBtnSubmitFilt = (Button) view.findViewById(R.id.button);
        mBtnSubmitFilt.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO get DialogFragment data and put it in Hashmap filter
                        String sortOrder =  spnOrder.getSelectedItem().toString();
                        filter.put("sort",sortOrder);
                        //Initialize String variable for the checkboxes
                        String sportValue = "";
                        String artValue = "";
                        String fashionValue = "";
                        //Insert value depending if Checkbox is checked
                        if(sport.isChecked()){
                            //
                            sportValue = " \"Sport\" ";
                        }
                        if(art.isChecked()) { artValue = "\"Arts\"";
                        }
                        if(fashionAndStyle.isChecked()) { fashionValue = " \"Fashion & Style\" ";
                        }
                        //Check Emptiness of checkboxes
                        //If all checkboxes are null, the query will return error page :-\
                        if(!(sportValue.equals("") && artValue.equals("") && fashionValue.equals(""))) {
                            String newsDesk = "news_desk:(" + sportValue + artValue + fashionValue + ")";
                            filter.put("fq", newsDesk);
                        }

                        // Return HashMap back to activity through the implemented listener
                        FilterDialogListener listener = (FilterDialogListener) getActivity();
                        assert listener != null;
                        listener.onFinishFilterDialog(filter);
                        dismiss();

                    }
                }
        );


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

