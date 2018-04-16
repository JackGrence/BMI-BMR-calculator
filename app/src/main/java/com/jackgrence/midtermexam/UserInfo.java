package com.jackgrence.midtermexam;

import android.widget.EditText;
import android.widget.RadioGroup;

import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by jackgrence on 2018/4/15.
 */

public class UserInfo
{
    public static final String UserPREFERENCES = "UserPrefs" ;
    public static final String UserName = "usernameKey";
    public static final String Height = "heightKey";
    public static final String Weight = "weightKey";
    public static final String Waist = "waistKey";
    public static final String Gender = "genderKey";
    public static final String Birthday = "birthdayKey";
    public float height;
    public float weight;
    public float waist;
    public double BMI;
    public double BMR;
    public int genderRadioId;
    public int age;
    public String name;
    public Calendar birthday;

    public UserInfo(String name, int gender, float height, float weight, float waist)
    {
        this.name = name;
        this.genderRadioId = gender;
        this.height = height;
        this.weight = weight;
        this.waist = waist;
    }

    public UserInfo(EditText name, RadioGroup gender, EditText height, EditText weight)
    {
        this.name = name.getText().toString();
        this.genderRadioId = gender.getCheckedRadioButtonId();
        this.height = getFLoatFromEditText(height);
        this.weight = getFLoatFromEditText(weight);
    }

    public UserInfo(EditText name, RadioGroup gender, EditText height, EditText weight, EditText waist)
    {
        this(name, gender, height, weight);
        this.waist = getFLoatFromEditText(waist);
    }

    public UserInfo(EditText name, RadioGroup gender, EditText height, EditText weight, Calendar birthday)
    {
        this(name, gender, height, weight);
        this.birthday = birthday;

        Calendar now = Calendar.getInstance();
        this.age = now.get(Calendar.YEAR) - this.birthday.get(Calendar.YEAR);
        if (now.get(Calendar.DAY_OF_YEAR) < this.birthday.get(Calendar.DAY_OF_YEAR))
            this.age--;
    }

    private float getFLoatFromEditText(EditText edtx)
    {
        String userInput;
        userInput = edtx.getText().toString();
        return InputCheck.floatNumber(userInput);
    }

    public String getHelloMsg()
    {
        String msg;
        msg = this.name;
        if (this.genderRadioId == R.id.rdoMan)
            msg += "先生您好";
        else if (this.genderRadioId == R.id.rdoFemale)
            msg += "小姐您好";
        return msg;
    }

    public boolean checkBMIValid()
    {
        if (this.name.isEmpty())
            return false;
        if (this.height <= 0)
            return false;
        if (this.weight <= 0)
            return false;
        if (this.waist <= 0)
            return false;

        calcBMI();
        return true;
    }

    public boolean checkBMRValid()
    {
        if (this.name.isEmpty())
            return false;
        if (this.height <= 0)
            return false;
        if (this.weight <= 0)
            return false;
        if (this.birthday == null)
            return false;

        calcBMR();
        return true;
    }

    private void calcBMI()
    {
        this.BMI = this.weight / Math.pow((this.height / 100), 2);
    }

    private void calcBMR()
    {
        if (this.genderRadioId == R.id.rdoMan)
            this.BMR = 13.7 * this.weight + 5 * this.height - 6.8 * this.age + 66;
        else if (this.genderRadioId == R.id.rdoFemale)
            this.BMR = 9.6 * this.weight + 1.8 * this.height - 4.7 * this.age + 655;
    }
}
