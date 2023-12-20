package com.example.liqid20;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class DecimalTextWatcher implements TextWatcher {

    private EditText editText;

    public DecimalTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        // Not used in this example
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        // Not used in this example
    }

    @Override
    public void afterTextChanged(Editable editable) {
        editText.removeTextChangedListener(this);

        String originalString = editable.toString();

        // Remove all non-numeric characters except a single dot
        String cleanString = originalString.replaceAll("[^\\d.]", "");

        // If there is more than one dot, remove the excess dots
        if (cleanString.indexOf(".") != cleanString.lastIndexOf(".")) {
            cleanString = cleanString.substring(0, cleanString.lastIndexOf(".")) +
                    cleanString.substring(cleanString.lastIndexOf(".") + 1);
        }

        editText.setText(cleanString);
        editText.setSelection(cleanString.length());

        editText.addTextChangedListener(this);
    }
}

