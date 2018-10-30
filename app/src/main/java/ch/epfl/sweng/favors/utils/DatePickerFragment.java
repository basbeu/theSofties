package ch.epfl.sweng.favors.utils;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Date;

public class DatePickerFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    private int year, month, day;
    private Date date;

    public DatePickerFragment() {}

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        date = Utils.getDateFromDatePicker(args.getInt("day"), args.getInt("month"), args.getInt("year"));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, ondateSet, year, month, day);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(int day, int month, int year) {
        date = Utils.getDateFromDatePicker(day, month, year);
    }

}