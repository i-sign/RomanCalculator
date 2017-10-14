package com.signin.calculator.roman.romancalculator;

import android.Manifest;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private SharedPreferences SessionManagement;
    private SharedPreferences.Editor mySession;

    private Converter mRoman = new Converter();

    private TextView txtTotal;

    private Button btnMinus;
    private Button btnPlus;
    private Button btnEqual;

    final private String OPERAND1 = "OPERAND1";
    final private String OPERAND2 = "OPERAND2";
    final private String OPERATOR = "OPERATOR";
    final private String OPERATOR_PLUS = "OPERATOR_PLUS";
    final private String OPERATOR_MINUS = "OPERATOR_MINUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            SessionManagement = getSharedPreferences(
                    getResources().getString(R.string.app_name), MODE_PRIVATE);
            mySession = SessionManagement.edit();

            txtTotal = (TextView) findViewById(R.id.txtTotal);

            btnMinus = (Button) findViewById(R.id.btnMinus);
            btnPlus = (Button) findViewById(R.id.btnPlus);
            btnEqual = (Button) findViewById(R.id.btnEqual);

            txtTotal.setText("");

            mRoman = new Converter();
            mySession.clear();
            mySession.commit();

        } catch (Exception ex) {
            Log.e(TAG, "onCreate : " + ex.toString());
        }
    }


    public void btnNumeric_Clicked(View v) {

        try {
            String reInputFormat = txtTotal.getText().toString() + "" + ((Button) v).getText().toString();

            int inputRoman = mRoman.toNumerical(reInputFormat);

            String reFormatRoman = mRoman.toRoman(inputRoman);

            txtTotal.setText(reFormatRoman);

        } catch (Exception ex) {
            Log.e(TAG, "btnNumeric_Clicked : " + ex.toString());
        }
    }

    public void btnEqual(View v) {

        try {

            if (!SessionManagement.getString(OPERAND1, "").equals("") && SessionManagement.getString(OPERAND2, "").equals("")) {
                mySession.putString(OPERAND2, String.valueOf(mRoman.toNumerical(txtTotal.getText().toString())));
                mySession.commit();
            }

            if (!SessionManagement.getString(OPERAND1, "").equals("") && !SessionManagement.getString(OPERAND2, "").equals("")) {
                DoOperation();
            }

        } catch (Exception ex) {
            Log.e(TAG, "btnEqual : " + ex.toString());
        }
    }

    public void btnClear(View v) {

        try {

            btnMinus.setTextColor(getResources().getColor(R.color.White));
            btnPlus.setTextColor(getResources().getColor(R.color.White));

            txtTotal.setText("");
            mySession.clear();
            mySession.commit();

        } catch (Exception ex) {
            Log.e(TAG, "btnClear : " + ex.toString());
        }
    }

    public void btnOperator_Clicked(final View v) {

        try {
            //If have value to operation
            if (!SessionManagement.getString(OPERAND1, "").equals("") && !SessionManagement.getString(OPERATOR, "").equals("") && !txtTotal.getText().toString().equals("")) {
                DoOperation();
                txtTotal.setText("");
                ((Button) v).setTextColor(getResources().getColor(R.color.Pressed));
            }


            //Assign Operator
            if (v.getId() == R.id.btnPlus) {
                mySession.putString(OPERATOR, OPERATOR_PLUS);

            } else if (v.getId() == R.id.btnMinus) {
                mySession.putString(OPERATOR, OPERATOR_MINUS);
            }
            mySession.commit();

            //Check for continue operator
            if (SessionManagement.getString(OPERAND1, "").equals("")) {

                ((Button) v).setTextColor(getResources().getColor(R.color.Pressed));

                if (SessionManagement.getString(OPERAND1, "").equals("")) {

                    mySession.putString(OPERAND1, String.valueOf(mRoman.toNumerical(txtTotal.getText().toString())));
                    mySession.commit();
                }

                txtTotal.setText("");

            } else {


                if (!SessionManagement.getString(OPERAND2, "").equals("")) {
                    DoOperation();

                } else {
                    // btnEqual.performClick();
                    txtTotal.setText("");
                }
            }

        } catch (Exception ex) {
            Log.e(TAG, "btnOpertor_Clicked : " + ex.toString());
        }
    }


    private void DoOperation() {

        try {

            if (SessionManagement.getString(OPERAND1, "").equals("") && SessionManagement.getString(OPERATOR, "").equals("") && txtTotal.getText().toString().equals("")) {
                return;
            }

            if (SessionManagement.getString(OPERAND2, "").equals("")) {
                mySession.putString(OPERAND2, String.valueOf(mRoman.toNumerical(txtTotal.getText().toString())));
                mySession.commit();
            }

            String mOperator = SessionManagement.getString(OPERATOR, "");

            int Op1 = Integer.valueOf(SessionManagement.getString(OPERAND1, "0"));
            int Op2 = Integer.valueOf(SessionManagement.getString(OPERAND2, "0"));
            int Result = 0;

            if (mOperator.equals(OPERATOR_PLUS)) {

                Result = Op1 + Op2;

            } else if (mOperator.equals(OPERATOR_MINUS)) {

                if (Op2 > Op1) {

                    Toast.makeText(MainActivity.this, "Can't operation negative value", Toast.LENGTH_SHORT).show();
                    txtTotal.setText("");
                    mySession.clear();
                    mySession.commit();
                    return;

                } else {
                    Result = Op1 - Op2;
                }
            }

            txtTotal.setText(mRoman.toRoman(Result));
            mySession.putString(OPERAND1, String.valueOf(Result));
            mySession.putString(OPERAND2, "");
            mySession.putString(OPERATOR, "");
            mySession.commit();

        } catch (Exception ex) {
            Log.e(TAG, "DoOperation : " + ex.toString());
        } finally {

            btnMinus.setTextColor(getResources().getColor(R.color.White));
            btnPlus.setTextColor(getResources().getColor(R.color.White));

        }
    }

}
