package com.jackgrence.midtermexam;

import android.widget.EditText;
import android.widget.RadioGroup;

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
    public float height;
    public float weight;
    public float waist;
    public double BMI;
    public int genderRadioId;
    public String name;

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

    private float getFLoatFromEditText(EditText edtx)
    {
        String userInput;
        userInput = edtx.getText().toString();
        return InputCheck.floatNumber(userInput);
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

    private void calcBMI()
    {
        this.BMI = this.weight / Math.pow((this.height / 100), 2);
    }
}
