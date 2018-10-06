package com.lethergo.tempconverter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    final protected String error_celsius = "Celsius can't be less than -273.15°C";
    final protected String error_fahrenheit = "Fahrenheit can't be less than -459,67°F";
    final protected String error_kelvin = "Kelvin can't be less than 0°K";
    final protected String error_rankine = "Rankine can't be less than 0°R";
    final protected String error_input = "Input can't be null!";
    final protected String celsius = "Celsius";
    final protected String fahrenheit = "Fahrenheit";
    final protected String kelvin = "Kelvin";
    final protected String rankine = "Rankine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar Title = findViewById(R.id.toolbar);
        Title.setTitle("Simple Temperature Converter");
        setSupportActionBar(Title);

        final Button btnConvert = findViewById(R.id.btnConvert);
        final ImageButton reverseTemp = findViewById(R.id.reverse);
        final EditText input_txt = findViewById(R.id.input);
        final EditText output_txt = findViewById(R.id.output);

        final Spinner SpinnerFrom = findViewById(R.id.spinnerFrom);
        final ArrayAdapter<CharSequence> adapterFrom = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
        adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerFrom.setAdapter(adapterFrom);

        final Spinner SpinnerTo = findViewById(R.id.spinnerTo);
        ArrayAdapter<CharSequence> adapterTo = ArrayAdapter.createFromResource(this, R.array.temp_array, android.R.layout.simple_spinner_item);
        adapterTo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerTo.setAdapter(adapterFrom);

        btnConvert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (input_txt.getText().length() > 0) {
                    double input = Double.parseDouble(input_txt.getText().toString());
                    String from = SpinnerFrom.getSelectedItem().toString();
                    String to = SpinnerTo.getSelectedItem().toString();
                    try {
                        double result = converter(input,from,to);
                        String output = result + "°" + to.charAt(0);
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
    }

    protected double converter(double input, String from, String to) throws IllegalArgumentException {

        double result = 0;

        if(from.equals(celsius)) {
            if(input >=-273.15) {
                switch (to){
                    case celsius:
                        result = input;
                        break;
                    case fahrenheit:
                        result = input * 1.8 + 32;
                        break;
                    case kelvin:
                        result = input + 273.15;
                        break;
                    case rankine:
                        result = (input + 273.15) * 1.8;
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
                        result = (input - 32) * 5 / 9;
                        break;
                    case fahrenheit:
                        result = input;
                        break;
                    case kelvin:
                        result = (input - 32) * 5 / 9 + 273.15;
                        break;
                    case rankine:
                        result = input + 459.67;
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
                        result = input - 273.15;
                        break;
                    case fahrenheit:
                        result = (input - 273.15) * 9 / 5 + 32;
                        break;
                    case kelvin:
                        result = input;
                        break;
                    case rankine:
                        result = input * 9 / 5;
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
                        result = (input - 491.67) * 5 / 9;
                        break;
                    case fahrenheit:
                        result = input - 459.67;
                        break;
                    case kelvin:
                        result = input * 5 / 9;
                        break;
                    case rankine:
                        result = input;
                        break;
                }
            } else {
                throw new IllegalArgumentException(error_rankine);
            }
        }

        return result;
    }

}







