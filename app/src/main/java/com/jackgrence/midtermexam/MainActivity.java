package com.jackgrence.midtermexam;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
{
    Button mBtnBMI, mBtnBMR;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void calcBMI(View v)
    {
        Intent it = new Intent(this, CalcBMI.class);
        startActivity(it);
    }

    public void calcBMR(View v)
    {
        Intent it = new Intent(this, CalcBMR.class);
        startActivity(it);
    }
}
