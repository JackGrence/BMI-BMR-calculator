package com.jackgrence.midtermexam;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalcBMR extends AppCompatActivity
{
    private int mYear, mMonth, mDay;

    EditText mEdtxName, mEdtxHeight, mEdtxWeight, mEdtxBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_bmr);

        mEdtxName = findViewById(R.id.edtxName);
        mEdtxHeight = findViewById(R.id.edtxHeight);
        mEdtxWeight = findViewById(R.id.edtxWeight);
        mEdtxBirthday = findViewById(R.id.edtxBirthday);

        mEdtxBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                {
                    birthdayPicker(v);
                }
            }
        });
    }

    public void birthdayPicker(View v)
    {
        final Calendar c = Calendar.getInstance();

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
        try
        {
            Date d = dateFormatter.parse(mEdtxBirthday.getText().toString());
            c.setTime(d);
        }
        catch (ParseException e)
        {
            // fail
        }


        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String birthday;
                c.set(year, month, day);
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);
                birthday = dateFormatter.format(c.getTime());
                mEdtxBirthday.setText(birthday);
            }

        }, mYear,mMonth, mDay).show();
    }
}
