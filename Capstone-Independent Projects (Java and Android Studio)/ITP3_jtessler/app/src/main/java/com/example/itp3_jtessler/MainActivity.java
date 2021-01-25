package com.example.itp3_jtessler;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {

    double input1 = 0, input2 = 0;
    TextView edt1;
    boolean Add, Sub, Multi, Div, Deci;
    Button Calc1, Calc2, Calc3, Calc4, Calc5, Calc6, Calc7,
            Calc8, Calc9, Calc0, Multiplication, Division,
            Addition, Subtraction, Clear, Decimal, Equals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//variables that represent the buttons
        Calc0 = (Button) findViewById(R.id.Calc0);
        Calc1 = (Button) findViewById(R.id.Calc1);
        Calc2 = (Button) findViewById(R.id.Calc2);
        Calc3 = (Button) findViewById(R.id.Calc3);
        Calc4 = (Button) findViewById(R.id.Calc4);
        Calc5 = (Button) findViewById(R.id.Calc5);
        Calc6 = (Button) findViewById(R.id.Calc6);
        Calc7 = (Button) findViewById(R.id.Calc7);
        Calc8 = (Button) findViewById(R.id.Calc8);
        Calc9 = (Button) findViewById(R.id.Calc9);
        Multiplication = (Button) findViewById(R.id.Multiplication);
        Division = (Button) findViewById(R.id.Division);
        Addition = (Button) findViewById(R.id.Addition);
        Subtraction = (Button) findViewById(R.id.Subtraction);
        Clear = (Button) findViewById(R.id.Clear);
        Decimal = (Button) findViewById(R.id.Decimal);
        Equals = (Button) findViewById(R.id.Equals);
        String x = "jtessler";

//variable used to edit what the textview shows
        edt1 = (TextView) findViewById(R.id.results);

        //sets each button so when clicked it displays that specific number
        Calc0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "0");
            }
        });

        Calc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "1");
            }
        });

        Calc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "2");
            }
        });

        Calc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "3");
            }
        });

        Calc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "4");
            }
        });

        Calc5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "5");
            }
        });

        Calc6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "6");
            }
        });

        Calc7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "7");
            }
        });

        Calc8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "8");
            }
        });

        Calc9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edt1.setText(edt1.getText() + "9");
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////

//edits the textview so that if there is a number in place that isn't zero, then when the symbol is clicked
        //it doesn't show anything. This goes for addtition, subtraction, multiplication, and divison
        Addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().length() != 0) {
                    input1 = Float.parseFloat(edt1.getText() + "");
                    Add = true;
                    Deci = false;
                    edt1.setText(null);
                }
            }
        });

        Subtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().length() != 0) {
                    input1 = Float.parseFloat(edt1.getText() + "");
                    Sub = true;
                    Deci = false;
                    edt1.setText(null);
                }
            }
        });

        Multiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().length() != 0) {
                    input1 = Float.parseFloat(edt1.getText() + "");
                    Multi = true;
                    Deci = false;
                    edt1.setText(null);
                }
            }
        });

        Division.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt1.getText().length() != 0) {
                    input1 = Float.parseFloat(edt1.getText() + "");
                    Div = true;
                    Deci = false;
                    edt1.setText(null);
                }
            }
        });

        //////////////////////////////////////////////////////////////////////////////////////////////

//an if statement that states if the addition, subtraction, multiplication, division buttons are selected,
        //then it will print out the results between those two numbers
        Equals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Add || Sub || Multi || Div) {
                    input2 = Float.parseFloat(edt1.getText() + "");
                }
                if (Add) {
                    edt1.setText(input1 + input2 + "");
                    Add = false;
                }
                if (Sub) {
                    edt1.setText(input1 - input2 + "");
                    Sub = false;
                }
                if (Multi) {
                    edt1.setText(input1 * input2 + "");
                    Multi = false;
                }
                if (Div) {
                    edt1.setText(input1 / input2 + "");
                    Div = false;
                }

            }
        });

        //this works with the clear button and clears all the numbers on the calculator
        Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                edt1.setText("");
                input1 = 0.0;
                input2 = 0.0;
            }
        });

        //this is for the decimal button and allows the decimal to be placed when the button is clicked
        Decimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                if (Deci) {
                } else {
                    edt1.setText(edt1.getText() + ".");
                    Deci = true;
                }

            }

        });

        ////////////////////////////////////////////////////////////////////////////////////////////


//These if statements show that if the length of x which, represents my username, is divisble by two/even
        //then the even numbers show, but if it is not divisible by two and doesn't equal zero, then odd numbers show
        View a = findViewById(R.id.Calc0);
        if (x.length() % 2 != 0) {
            a.setVisibility(View.GONE);
        }

        View b = findViewById(R.id.Calc1);
        if (x.length() % 2 == 0) {
            b.setVisibility(View.GONE);
        }

        View c = findViewById(R.id.Calc2);
        if (x.length() % 2 != 0) {
            c.setVisibility(View.GONE);
        }

        View d = findViewById(R.id.Calc3);
        if (x.length() % 2 == 0) {
            d.setVisibility(View.GONE);
        }

        View e = findViewById(R.id.Calc4);
        if (x.length() % 2 != 0) {
            e.setVisibility(View.GONE);
        }

        View f = findViewById(R.id.Calc5);
        if (x.length() % 2 == 0) {
            f.setVisibility(View.GONE);
        }

        View g = findViewById(R.id.Calc6);
        if (x.length() % 2 != 0) {
            g.setVisibility(View.GONE);
        }

        View h = findViewById(R.id.Calc7);
        if (x.length() % 2 == 0) {
            h.setVisibility(View.GONE);
        }

        View i = findViewById(R.id.Calc8);
        if (x.length() % 2 != 0) {
            i.setVisibility(View.GONE);
        }

        View j = findViewById(R.id.Calc9);
        if (x.length() % 2 == 0) {
            j.setVisibility(View.GONE);
        }

        ////////////////////////////////////////////////////////////////////////////////////////

//these represent if you can see the buttons for the following
        View k = findViewById(R.id.Multiplication);
        View l = findViewById(R.id.Division);
        View m = findViewById(R.id.Addition);
        View n = findViewById(R.id.Subtraction);
        //this represents the username at index 0, so the first letter
        char ZeroIndex = x.charAt(0);

        //this if statement represents that if the first letter in the username is a through m, then it will not allow the subtraction and multiplication button to show
        //but will allow the addition and division button to show
        if (ZeroIndex == 'a' || ZeroIndex == 'b' || ZeroIndex == 'c' || ZeroIndex == 'd' || ZeroIndex == 'e' || ZeroIndex == 'f' || ZeroIndex == 'g'
                || ZeroIndex == 'h' || ZeroIndex == 'i' || ZeroIndex == 'j' || ZeroIndex == 'k' || ZeroIndex == 'l' || ZeroIndex == 'm') {
            k.setVisibility(View.GONE);
            n.setVisibility(View.GONE);
        } else {
            l.setVisibility(View.GONE);
            m.setVisibility(View.GONE);

        }
    }
}



