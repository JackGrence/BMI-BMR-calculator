package com.jackgrence.midtermexam;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.jackgrence.midtermexam.UserInfo.Birthday;
import static com.jackgrence.midtermexam.UserInfo.Gender;
import static com.jackgrence.midtermexam.UserInfo.Height;
import static com.jackgrence.midtermexam.UserInfo.UserName;
import static com.jackgrence.midtermexam.UserInfo.UserPREFERENCES;
import static com.jackgrence.midtermexam.UserInfo.Waist;
import static com.jackgrence.midtermexam.UserInfo.Weight;

public class CalcBMR extends AppCompatActivity
{
    private int mYear, mMonth, mDay;

    EditText mEdtxName, mEdtxHeight, mEdtxWeight, mEdtxBirthday;
    RadioGroup mRdoGenderGroup;
    SharedPreferences userInfo;
    TextToSpeech mTTS;
    final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.TAIWAN);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_bmr);

        mRdoGenderGroup = findViewById(R.id.rdoGenderGroup);
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

    public void birthdayPicker(View v)
    {
        final Calendar c = Calendar.getInstance();

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
                String birthdayStr;
                c.set(year, month, day);
                birthdayStr = dateFormatter.format(c.getTime());

                Calendar now = Calendar.getInstance();
                int age = now.get(Calendar.YEAR) - c.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) < c.get(Calendar.DAY_OF_YEAR))
                    age--;

                birthdayStr = birthdayStr + " (" + String.valueOf(age) + "歲)";

                mEdtxBirthday.setText(birthdayStr);
            }

        }, mYear,mMonth, mDay).show();
    }

    public void startCalc(View v)
    {
        DecimalFormat df = new DecimalFormat("#.##");
        String msg;
        UserInfo user;
        Calendar c = Calendar.getInstance();
        try
        {
            Date d = dateFormatter.parse(mEdtxBirthday.getText().toString());
            c.setTime(d);
        }
        catch (ParseException e)
        {
            // fail
            showBirthdayFailDialog();
            return;
        }
        user = new UserInfo(mEdtxName, mRdoGenderGroup, mEdtxHeight, mEdtxWeight, c);

        if (user.checkBMRValid())
        {
            if (user.age < 0)
            {
                showBirthdayFailDialog();
                return;
            }

            storeToReference(user);

            msg = user.getHelloMsg() + "\n您的 BMR = ";
            msg += df.format(user.BMR) + "大卡";

            mTTS.speak(msg, TextToSpeech.QUEUE_FLUSH, null, null);
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(getString(R.string.calc_result))
                    .setContentText(msg)
                    .show();
        }
        else
        {
            showCalcFailDialog();
        }
    }

    public void exit_activity(View v)
    {
        finish();
    }

    private void showBirthdayFailDialog()
    {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.oops))
                .setContentText(getString(R.string.birthday_fail))
                .show();
        return;
    }

    private void showCalcFailDialog()
    {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.oops))
                .setContentText(getString(R.string.calc_BMR_fail))
                .show();
    }

    private void storeToReference(UserInfo user)
    {
        userInfo = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString(UserName, user.name);
        editor.putString(Gender, String.valueOf(user.genderRadioId));
        editor.putString(Height, String.valueOf(user.height));
        editor.putString(Weight, String.valueOf(user.weight));
        editor.putString(Birthday, dateFormatter.format(user.birthday.getTime()));
        editor.commit();  //存檔
    }

    private void fillFromReference()
    {
        int radioId;
        userInfo = getSharedPreferences(UserPREFERENCES, Context.MODE_PRIVATE);
        mEdtxName.setText(userInfo.getString(UserName, null));
        radioId = InputCheck.number(userInfo.getString(Gender, null));
        if ( radioId != -1)
            findViewById(radioId).performClick();
        mEdtxHeight.setText(userInfo.getString(Height, null));
        mEdtxWeight.setText(userInfo.getString(Weight, null));
        mEdtxBirthday.setText(userInfo.getString(Birthday, null));
    }
}
