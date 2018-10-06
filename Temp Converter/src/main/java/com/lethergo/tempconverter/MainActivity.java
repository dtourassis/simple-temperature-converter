package com.lethergo.tempconverter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    final protected String error_celsius = "Celsius can't be less than -273.15°C";
    final protected String error_fahrenheit = "Fahrenheit can't be less than -459,67°F";
    final protected String error_kelvin = "Kelvin can't be less than 0K";
    final protected String error_rankine = "Rankine can't be less than 0°R";
    final protected String error_input = "Input can't be null!";
    final protected String celsius = "Celsius";
    final protected String fahrenheit = "Fahrenheit";
    final protected String kelvin = "Kelvin";
    final protected String rankine = "Rankine";
    final protected String title = "Simple Temperature Converter";

    // Settings
    final protected int decimalPlaces = 2;
    final protected String defaultFromTemp = celsius;
    final protected String defaultToTemp = fahrenheit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar Title = findViewById(R.id.toolbar);
        Title.setTitle(title);
        setSupportActionBar(Title);

        final Button btnConvert = findViewById(R.id.btnConvert);
        final ImageButton reverseTemp = findViewById(R.id.reverse);
        final EditText input_txt = findViewById(R.id.input);
        final EditText output_txt = findViewById(R.id.output);

        final Spinner SpinnerFrom = findViewById(R.id.spinnerFrom);
        final ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerFrom.setAdapter(adapterFrom);
        SpinnerFrom.setSelection(0);

        final Spinner SpinnerTo = findViewById(R.id.spinnerTo);
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTo.setAdapter(adapterFrom);
        SpinnerTo.setSelection(1);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (input_txt.getText().length() > 0) {
                    double input = Double.parseDouble(input_txt.getText().toString());
                    String from = SpinnerFrom.getSelectedItem().toString();
                    String to = SpinnerTo.getSelectedItem().toString();
                    try {
                        double result = converter(input,from,to);
                        String output;
                        if(to.equals(kelvin)) {
                            output = Double.toString (result) + to.charAt(0);
                        } else {
                            output = result + "°" + to.charAt(0);
                        }
                        output_txt.setText(output);
                    }catch (IllegalArgumentException e) {
                        output_txt.setText(e.getLocalizedMessage());
                    }
                } else {
                    output_txt.setText(error_input);
                }
            }
        });

        reverseTemp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int sFrom = SpinnerFrom.getSelectedItemPosition();
                int sTo = SpinnerTo.getSelectedItemPosition();
                SpinnerTo.setSelection(sFrom);
                SpinnerFrom.setSelection(sTo);
            }
        });

        output_txt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("converted temperature", output_txt.getText());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "Copied to clipboard!",
                        Toast.LENGTH_SHORT).show();
            }
        });


    }

    protected double converter(double input, String from, String to) throws IllegalArgumentException {

        double result = 0;

        if(from.equals(celsius)) {
            if(input >=-273.15) {
                switch (to){
                    case celsius:
                        result = rounder(input, decimalPlaces);
                        break;
                    case fahrenheit:
                        result = rounder(input * 1.8 + 32, decimalPlaces);
                        break;
                    case kelvin:
                        result = rounder(input + 273.15, decimalPlaces);
                        break;
                    case rankine:
                        result = rounder((input + 273.15) * 1.8, decimalPlaces);
                        break;
                }
            } else {
                throw new IllegalArgumentException(error_celsius);
            }
        }
        else if(from.equals(fahrenheit)) {
            if(input >=-459.67) {
                switch (to){
                    case celsius:
                        result = rounder((input - 32) * 5 / 9, decimalPlaces);
                        break;
                    case fahrenheit:
                        result = rounder(input, decimalPlaces);
                        break;
                    case kelvin:
                        result = rounder((input - 32) * 5 / 9 + 273.15, decimalPlaces);
                        break;
                    case rankine:
                        result = rounder(input + 459.67, decimalPlaces);
                        break;
                }
            } else {
                throw new IllegalArgumentException(error_fahrenheit);
            }
        }
        else if(from.equals(kelvin)) {
            if(input >=0) {
                switch (to){
                    case celsius:
                        result = rounder(input - 273.15, decimalPlaces);
                        break;
                    case fahrenheit:
                        result = rounder((input - 273.15) * 9 / 5 + 32, decimalPlaces);
                        break;
                    case kelvin:
                        result = rounder(input, decimalPlaces);
                        break;
                    case rankine:
                        result = rounder(input * 9 / 5, decimalPlaces);
                        break;
                }
            } else {
                throw new IllegalArgumentException(error_kelvin);
            }
        }
        else if(from.equals(rankine)) {
            if(input >=0) {
                switch (to){
                    case celsius:
                        result = rounder((input - 491.67) * 5 / 9, decimalPlaces);
                        break;
                    case fahrenheit:
                        result = rounder(input - 459.67, decimalPlaces);
                        break;
                    case kelvin:
                        result = rounder(input * 5 / 9, decimalPlaces);
                        break;
                    case rankine:
                        result = rounder(input, decimalPlaces);
                        break;
                }
            } else {
                throw new IllegalArgumentException(error_rankine);
            }
        }

        return result;
    }

    protected double rounder(double number, int decPoints) {
        return BigDecimal.valueOf(number).setScale(decPoints, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}







