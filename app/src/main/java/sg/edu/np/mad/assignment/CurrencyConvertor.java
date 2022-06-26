package sg.edu.np.mad.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class CurrencyConvertor extends AppCompatActivity {

    Spinner spFrom, spTo;
    EditText editAmount;
    Button btnConvert;
    TextView currency_converted;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_convertor);

        spFrom = findViewById(R.id.sp_from);
        spTo = findViewById(R.id.sp_to);
        editAmount = findViewById(R.id.edit_amount);
        btnConvert = findViewById(R.id.btnConvert);
        currency_converted = findViewById(R.id.currency_converted);


        String[] from = {"USD", "SGD", "CNY", "MYR", "EUR", "JPD", "Pounds", "AUD"};
        ArrayAdapter<String> ad = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, from);
        spFrom.setAdapter(ad);


        String[] to = {"AUD", "Pounds", "JPD", "EUR", "MYR", "CNY", "SGD", "USD"};
        ArrayAdapter<String> ad1 = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, to);
        spTo.setAdapter(ad1);

        btnConvert.setOnClickListener(v -> {
            double tot;
            double amt = Double.parseDouble(editAmount.getText().toString());

            if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 1.42;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.81;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 134.42;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 0.95;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 4.40;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 6.71;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 1.39;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("USD") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }

            else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 1.02;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.59;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 96.85;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 0.69;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 3.17;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 4.83;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("SGD") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 0.72;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }

            else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 0.21;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.12;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 20.04;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 0.14;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 0.66;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 0.21;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("CNY") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 0.15;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }


            else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 0.32;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.18;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 30.32;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 0.21;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 1.52;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 0.32;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("MYR") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 0.23;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }


            else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 1.49;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.85;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 141.39;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 4.63;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 7.06;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 1.46;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("EUR") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 1.05;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }


            else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 0.011;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.0060;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 0.0071;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 0.033;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 0.050;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 0.010;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("JPD") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 0.0074;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }


            else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt * 1.75;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 165.49;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 1.17;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 5.42;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 8.26;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 1.71;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("Pounds") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 1.23;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }

            else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("AUD")) {
                tot = amt;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("Pounds")) {
                tot = amt * 0.58;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("JPD")) {
                tot = amt * 94.70;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            } else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("EUR")) {
                tot = amt * 0.67;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("MYR")) {
                tot = amt * 3.12;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("CNY")) {
                tot = amt * 4.75;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("SGD")) {
                tot = amt * 0.98;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }else if (spFrom.getSelectedItem().toString().equals("AUD") && spTo.getSelectedItem().toString().equals("USD")) {
                tot = amt * 0.71;
                String totalPrice = "TOTAL :: " + Double.toString(tot);
                currency_converted.setText(totalPrice);
            }
        });
    }
}