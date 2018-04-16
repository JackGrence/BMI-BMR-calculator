package com.jackgrence.midtermexam;

/**
 * Created by jackgrence on 2018/4/14.
 */

public class InputCheck
{
    static int number(String text)
    {
        int converted;
        try
        {
            converted = Integer.parseInt(text);
        }
        catch (NumberFormatException e)
        {
            converted = -1;
        }
        return converted;
    }

    static double doubleNumber(String text)
    {
        double converted;
        try
        {
            converted = Double.parseDouble(text);
        }
        catch (NumberFormatException e)
        {
            converted = -1;
        }
        return converted;
    }

    static float floatNumber(String text)
    {
        float converted;
        try
        {
            converted = Float.parseFloat(text);
        }
        catch (NumberFormatException e)
        {
            converted = -1;
        }
        return converted;
    }
}

