package com.jackgrence.midtermexam;

import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.jackgrence.midtermexam.UserInfo.Gender;
import static com.jackgrence.midtermexam.UserInfo.Height;
import static com.jackgrence.midtermexam.UserInfo.UserName;
import static com.jackgrence.midtermexam.UserInfo.UserPREFERENCES;
import static com.jackgrence.midtermexam.UserInfo.Waist;
import static com.jackgrence.midtermexam.UserInfo.Weight;


public class CalcBMI extends AppCompatActivity
{
    EditText mEdtxName, mEdtxHeight, mEdtxWeight, mEdtxWaist;
    RadioGroup mRdoGenderGroup;
    SharedPreferences userInfo;
    TextToSpeech mTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_bmi);
        setTitle(getString(R.string.BMI_title));

        mRdoGenderGroup = findViewById(R.id.rdoGenderGroup);

        mEdtxName = findViewById(R.id.edtxName);
        mEdtxHeight = findViewById(R.id.edtxHeight);
        mEdtxWeight = findViewById(R.id.edtxWeight);
        mEdtxWaist = findViewById(R.id.edtxWaist);

        mTTS = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if (status != TextToSpeech.ERROR)
                {
                    mTTS.setLanguage(Locale.CHINA);
                }
            }
        });
        fillFromReference();
    }

    public void startCalc(View v)
    {
        int dialogType;
        float BMI;
        DecimalFormat df = new DecimalFormat("#.##");
        String msg;
        UserInfo user = new UserInfo(mEdtxName, mRdoGenderGroup, mEdtxHeight, mEdtxWeight, mEdtxWaist);
        if (user.checkBMIValid())
        {
            dialogType = SweetAlertDialog.WARNING_TYPE;
            if (user.BMI >= 35)
                msg = getString(R.string.severely_obese);
            else if (user.BMI > 30)
                msg = getString(R.string.moderately_obese);
            else if (user.BMI > 27)
                msg = getString(R.string.mild_obese);
            else if (user.BMI > 24)
                msg = getString(R.string.overweight);
            else if (user.BMI >= 18.5)
            {
                msg = getString(R.string.normal);
                dialogType = SweetAlertDialog.SUCCESS_TYPE;
            }
            else
                msg = getString(R.string.underweight);

            msg = user.getHelloMsg() +
                    "\n您的 BMI = " + df.format(user.BMI) +
                    "(" + msg + ")\n" +
                    "腰圍 " + String.valueOf(user.waist) + " 公分";
            if (user.genderRadioId == R.id.rdoMan)
            {
                if (user.waist >= 90)
                    msg += "(超過標準)";
            }
            else if (user.genderRadioId == R.id.rdoFemale)
            {
                if (user.waist >= 80)
                    msg += "(超過標準)";
            }

            storeToReference(user);

            mTTS.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
            new SweetAlertDialog(this, dialogType)
                    .setTitleText(getString(R.string.calc_result))
                    .setContentText(msg)
                    .show();
        }
        else
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText(getString(R.string.oops))
                    .setContentText(getString(R.string.calc_BMI_fail))
                    .show();
        }
    }

    public void exit_activity(View v)
    {
        finish();
    }

    @Override
    protected void onDestroy()
    {
        if (mTTS != null)
        {
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
    }

    private void storeToReference(UserInfo user)
    {
        userInfo = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(UserName, user.name);
        editor.putString(Gender, String.valueOf(user.genderRadioId));
        editor.putString(Height, String.valueOf(user.height));
        editor.putString(Weight, String.valueOf(user.weight));
        editor.putString(Waist, String.valueOf(user.waist));
        editor.commit();  //存檔
    }

    private void fillFromReference()
    {
        int radioId;
        userInfo = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        mEdtxName.setText(userInfo.getString(UserName, null), TextView.BufferType.EDITABLE);
        radioId = InputCheck.number(userInfo.getString(Gender, null));
        if ( radioId != -1)
            findViewById(radioId).performClick();
        mEdtxHeight.setText(userInfo.getString(Height, null), TextView.BufferType.EDITABLE);
        mEdtxWeight.setText(userInfo.getString(Weight, null), TextView.BufferType.EDITABLE);
        mEdtxWaist.setText(userInfo.getString(Waist, null), TextView.BufferType.EDITABLE);
    }
}
