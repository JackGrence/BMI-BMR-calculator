package com.jackgrence.midtermexam;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CalcBMI extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_bmi);
    }

    public void startCalc(View v)
    {
    }

    public void exit_activity(View v)
    {
        finish();
    }
}
