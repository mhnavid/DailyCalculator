package nvd.hasan.dailycalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView resultView;
    static String display = "";
    private String currentOperator = "";
    private String result = "";
    private boolean isDotClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        resultView = findViewById(R.id.resultView);
        resultView.setText(display);
    }

    public void updateScreen(){
        resultView.setText(display);
    }

    public void onClickNumber(View v){
        if (result != ""){
            clear();
            updateScreen();
        }
        Button b = (Button) v;
        if (b.getText().toString().equals(".")){
            if (!isDotClicked){
                isDotClicked = true;
                display += b.getText();
                updateScreen();
            }
            else {
                return;
            }
        }
        else{
            display += b.getText();
            updateScreen();
        }
    }

    public double operator(String a, String b, String op){
        Context context = getApplicationContext();
        switch (op){
            case "+":
                return Double.valueOf(a) + Double.valueOf(b);
            case "-":
                return Double.valueOf(a) - Double.valueOf(b);
            case "x":
                try{
                    return Double.valueOf(a) * Double.valueOf(b);
                }
                catch (Exception e){
                    Log.d("Cal1", e.getMessage());
                }
            case "/":
                try {
                    if(Double.valueOf(a) == 0){
                        Toast.makeText(context, "Wrong calculation", Toast.LENGTH_LONG).show();
                        return 0;
                    }
                    else {
                        return Double.valueOf(a) / Double.valueOf(b);
                    }
                }
                catch (Exception e){
                    Log.d("Cal0", e.getMessage());
                }
            default:
                return -1;
        }
    }

    public boolean isOperator(char op){
        switch (op){
            case '+':
            case '-':
            case 'x':
            case '/':
                return true;
            default:
                return false;
        }
    }

    public void onClickOperator(View v){
        Button b = (Button) v;
        if (display == "")return;

        if (isDotClicked){
            isDotClicked = false;
        }

        if (result != ""){
            String _display = result;
            clear();
            display = _display;
        }

        if(display.charAt(0) == '-'){
            return;
        }

        if (currentOperator != ""){
            if(isOperator(display.charAt(display.length() - 1))){
                display = display.replace(display.charAt(display.length() - 1), b.getText().charAt(0));
                updateScreen();
                return;
            }
//            if(display.charAt(0) == '-'){
//                display = display.substring(1, display.length() - 1);
//                String[] operation = display.split(Pattern.quote(currentOperator));
//                Double firstValue = (-Double.valueOf(operation[1]));
//                result = String.valueOf(operator(String.valueOf(firstValue), operation[2], currentOperator));
//                display = result;
//                result = "";
//            }
            else {
                getResult();
                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }

        display += b.getText();
        currentOperator = b.getText().toString();
        updateScreen();
    }

    public void onClickPercent(View v){
        Button b = (Button) v;
        if (display == "")return;

        if (result != ""){
            String _display = result;
            clear();
            display = _display;
        }

        if (currentOperator != ""){
            if(isOperator(display.charAt(display.length() - 1))){
                return;
            }
            else {
                String[] operation = display.split(Pattern.quote(currentOperator));
                Double firstValue = Double.valueOf(operation[0]);
                Double secondValue = Double.valueOf(operation[1]);
                Double finalValue = (firstValue*secondValue)/100;
                result = String.valueOf(operator(operation[0], String.valueOf(finalValue), currentOperator));
                display = "";
                display = result;
                updateScreen();
            }
        }

        if (currentOperator == ""){
            return;
        }


    }

    public void clear(){
        display = "";
        currentOperator = "";
        result = "";
    }

    public void onClickClear(View v){
        clear();
        updateScreen();
    }

    public boolean getResult(){
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (currentOperator == "") return false;
        if (operation.length < 2) return false;
        result = String.valueOf(operator(operation[0], operation[1], currentOperator));
        return true;
    }

    public void onClickEqual(View v){
        if (display == "")return;
        if (!getResult())return;
        resultView.setText(String.valueOf(result));
    }

    public void onClickBackspace(View v){
        if (display != "" && display.length() > 0) {
            if(isOperator(display.charAt(display.length() - 1))){
                display = display.substring(0, display.length() - 1);
                updateScreen();
                currentOperator = "";
            }
            else{
                display = display.substring(0, display.length() - 1);
                updateScreen();
            }
        }
    }

    public void onClickMemoryPlus(View v){
//        Context context = getActivity();
//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putInt(getString(R.string.saved_high_score_key), newHighScore);
//        editor.commit();
    }
}
