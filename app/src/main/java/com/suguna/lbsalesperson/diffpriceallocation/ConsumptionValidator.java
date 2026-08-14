package com.suguna.lbsalesperson.diffpriceallocation;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Pattern;

public class ConsumptionValidator implements TextWatcher {

    Pattern mPattern;
    EditText edtxtConsumed;
    //private final Pattern sPattern = Pattern.compile("^([0-9]{1,3})?(\\.[0-9]{0,3}?)?$");
    private Pattern sPattern = null;

    public ConsumptionValidator(EditText edtxtFeedConsumed) {
        this.edtxtConsumed = edtxtFeedConsumed;
        sPattern = Pattern.compile("^([0-9]{1,3})?(\\.[0-9]{0,3}?)?$");
    }

    public ConsumptionValidator(EditText edtxtFeedConsumed, int beforedecimal, int afterdecimal) {
        this.edtxtConsumed = edtxtFeedConsumed;
        sPattern = Pattern.compile("^([0-9]{1," + beforedecimal + "})?(\\.[0-9]{0,"
                + afterdecimal + "}?)?$");
    }

    private boolean isValid(CharSequence s) {
        return sPattern.matcher(s).matches();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() > 0) {
            if (!isValid(s)) {
                try
                {
                    String string = s.subSequence(0, s.length() - 1).toString();
                    edtxtConsumed.setText(string);
                    edtxtConsumed.setSelection(s.length() - 1);
                }
                catch (Exception e)
                {

                }
            }
        }
    }
}