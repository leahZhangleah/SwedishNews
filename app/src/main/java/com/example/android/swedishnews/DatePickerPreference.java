package com.example.android.swedishnews;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerPreference extends DialogPreference {
    //private Context context;
    private final static String DEFAULT_VALUE = "2000-01-01";
    private DatePicker mDatePicker;
    private String mDate = DEFAULT_VALUE;
    private final String LOG_TAG = DatePickerPreference.class.getName();

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.date_picker_dialog);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");
    }

    public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, android.R.attr.preferenceStyle);
        //this.context = context;
    }

    @Override
    protected void onBindDialogView(View view) {
        mDatePicker = (DatePicker) view.findViewById(R.id.date_picker);
        long minDate = stringToMilliseconds(DEFAULT_VALUE);
        long currentSystemDate = Calendar.getInstance().getTimeInMillis();
        int[] dateInteger = stringToInt(mDate);
        mDatePicker.setMinDate(minDate);
        mDatePicker.setMaxDate(currentSystemDate);
        mDatePicker.updateDate(dateInteger[0],dateInteger[1],dateInteger[2]);
        super.onBindDialogView(view);
    }

    //helper method to convert string to milliseconds
    private long stringToMilliseconds(String dateInString){
        long dateInMilliseconds = 0;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date mDateFormattedFromString = formatter.parse(dateInString);
            dateInMilliseconds = mDateFormattedFromString.getTime();
        }catch(ParseException e){
            Log.e(LOG_TAG,"error in parsing date string");
        }
        return dateInMilliseconds;
    }
    //helper method to convert datepicker's date to string
    private String dateToString(DatePicker datePicker){
        String dateString = datePicker.getYear() + "-"+(datePicker.getMonth()+1) +"-"+datePicker.getDayOfMonth();
        return dateString;
    }

    //helper method to convert string to integer for date picker
    private int[] stringToInt(String date){
        String[] dates = date.split("-");
        int currentYear = Integer.parseInt(dates[0]);
        int currentMonth = Integer.parseInt(dates[1])-1;
        int currentDay = Integer.parseInt(dates[2]);
        int[] datesInteger = new int[]{currentYear,currentMonth, currentDay};
        return datesInteger;
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if (restorePersistedValue){
            mDate = getPersistedString(DEFAULT_VALUE);
        }else{
            mDate = (String) defaultValue;
            persistString(mDate);
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);
        if (positiveResult){
            mDate = dateToString(mDatePicker);
            if (callChangeListener(mDate)){
                persistString(mDate);
            }
        }
    }
}
