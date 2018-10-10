package com.lethergo.tempconverter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    // Settings
    final int decimalPlaces = 2;
    final int defaultFromTemp = 0;
    final int defaultToTemp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar Title = findViewById(R.id.toolbar);
        Title.setTitle(getString(R.string.app_name));
        setSupportActionBar(Title);

        final Button btnConvert = findViewById(R.id.btnConvert);
        final ImageButton reverseTemp = findViewById(R.id.reverse);
        final EditText input_txt = findViewById(R.id.input);
        final EditText output_txt = findViewById(R.id.output);

        final Spinner SpinnerFrom = findViewById(R.id.spinnerFrom);
        final ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerFrom.setAdapter(adapterFrom);
        SpinnerFrom.setSelection(defaultFromTemp);

        final Spinner SpinnerTo = findViewById(R.id.spinnerTo);
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTo.setAdapter(adapterFrom);
        SpinnerTo.setSelection(defaultToTemp);

        // Makes the conversion
        btnConvert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (input_txt.getText().length() > 0) {
                    double input = Double.parseDouble(input_txt.getText().toString());
                    String from = SpinnerFrom.getSelectedItem().toString();
                    String to = SpinnerTo.getSelectedItem().toString();
                    try {
                        double result = converter(input,from,to, decimalPlaces);
                        String output;
                        if(to.equals(getString(R.string.kelvin))) {
                            output = Double.toString (result) + to.charAt(0);
                        } else {
                            output = result + "°" + to.charAt(0);
                        }
                        output_txt.setText(output);
                    }catch (IllegalArgumentException e) {
                        Toast.makeText(MainActivity.this, e.getLocalizedMessage(),
                                Toast.LENGTH_SHORT).show();
                        output_txt.setText(getString(R.string.error));
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_input),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Switches the two selected temperature units
        reverseTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int sFrom = SpinnerFrom.getSelectedItemPosition();
                int sTo = SpinnerTo.getSelectedItemPosition();
                SpinnerTo.setSelection(sFrom);
                SpinnerFrom.setSelection(sTo);
            }
        });

        // Empties the output text when it detects a change on the input
        input_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                output_txt.setText(null);
            }
        });

        // Copies the converted temperature when the output field is clicked
        output_txt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!output_txt.getText().toString().isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("temperature", output_txt.getText());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Copied to clipboard!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Nothing to copy!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    // Handles the conversions
    protected double converter(double input, String from, String to, int decimalPlaces) throws IllegalArgumentException {

        double result = 0;

        if(from.equals(getString(R.string.celsius))) {
            if(input >=-273.15) {
                if (to.equals(getString(R.string.celsius))) {
                    result = rounder(input, decimalPlaces);

                } else if (to.equals(getString(R.string.fahrenheit))) {
                    result = rounder(input * 1.8 + 32, decimalPlaces);

                } else if (to.equals(getString(R.string.kelvin))) {
                    result = rounder(input + 273.15, decimalPlaces);

                } else if (to.equals(getString(R.string.rankine))) {
                    result = rounder((input + 273.15) * 1.8, decimalPlaces);

                }
            } else {
                throw new IllegalArgumentException(getString(R.string.error_celsius));
            }
        }
        else if(from.equals(getString(R.string.fahrenheit))) {
            if(input >=-459.67) {
                if (to.equals(getString(R.string.celsius))) {
                    result = rounder((input - 32) * 5 / 9, decimalPlaces);

                } else if (to.equals(getString(R.string.fahrenheit))) {
                    result = rounder(input, decimalPlaces);

                } else if (to.equals(getString(R.string.kelvin))) {
                    result = rounder((input - 32) * 5 / 9 + 273.15, decimalPlaces);

                } else if (to.equals(getString(R.string.rankine))) {
                    result = rounder(input + 459.67, decimalPlaces);

                }
            } else {
                throw new IllegalArgumentException(getString(R.string.error_fahrenheit));
            }
        }
        else if((from.equals(getString(R.string.kelvin)))) {
            if(input >=0) {
                if (to.equals(getString(R.string.celsius))) {
                    result = rounder(input - 273.15, decimalPlaces);

                } else if (to.equals(getString(R.string.fahrenheit))) {
                    result = rounder((input - 273.15) * 9 / 5 + 32, decimalPlaces);

                } else if (to.equals(getString(R.string.kelvin))) {
                    result = rounder(input, decimalPlaces);

                } else if (to.equals(getString(R.string.rankine))) {
                    result = rounder(input * 9 / 5, decimalPlaces);

                }
            } else {
                throw new IllegalArgumentException(getString(R.string.error_kelvin));
            }
        }
        else if((from.equals(getString(R.string.rankine)))) {
            if(input >=0) {
                if (to.equals(getString(R.string.celsius))) {
                    result = rounder((input - 491.67) * 5 / 9, decimalPlaces);

                } else if (to.equals(getString(R.string.fahrenheit))) {
                    result = rounder(input - 459.67, decimalPlaces);

                } else if (to.equals(getString(R.string.kelvin))) {
                    result = rounder(input * 5 / 9, decimalPlaces);

                } else if (to.equals(getString(R.string.rankine))) {
                    result = rounder(input, decimalPlaces);

                }
            } else {
                throw new IllegalArgumentException(getString(R.string.error_rankine));
            }
        }

        return result;
    }

    // Handles the rounding
    protected double rounder(double number, int decPoints) {
        return BigDecimal.valueOf(number).setScale(decPoints, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}







