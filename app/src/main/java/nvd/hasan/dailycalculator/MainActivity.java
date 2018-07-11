package nvd.hasan.dailycalculator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;
import com.fathzer.soft.javaluator.DoubleEvaluator;

public class MainActivity extends AppCompatActivity {

    private TextView resultView;
    static String display = "";
    private String currentOperator = "";
    private String result = "";
    static boolean isDotClicked = false;
    static String fullRxpression = "";
    DoubleEvaluator evaluator = new DoubleEvaluator();


    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        resultView = findViewById(R.id.resultView);
        resultView.setText(display);
        dbHelper = new DBHelper(this);
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
                fullRxpression += b.getText();
                updateScreen();
            }
            else {
                return;
            }
        }
        else{
            display += b.getText();
            fullRxpression += b.getText();
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

        if (result != ""){
            String _display = result;
            clear();
            display = _display;
        }

        if(display.charAt(0) == '-'){
            return;
        }

        if (display.charAt(display.length() - 1) == '.'){
            isDotClicked = true;
            return;
        }
        else {
            isDotClicked = false;
        }

        if (currentOperator != ""){
            if(isOperator(display.charAt(display.length() - 1))){
                display = display.replace(display.charAt(display.length() - 1), b.getText().charAt(0));
                updateScreen();
                return;
            }
            else {
//                getResult();
                try{
                    result = evaluator.evaluate(display).toString();
                }
                catch (Exception e){
                    System.out.println (display+" is an invalid expression");
                }

                display = result;
                result = "";
            }
            currentOperator = b.getText().toString();
        }

        display += b.getText();
        fullRxpression += b.getText();
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
        fullRxpression = "";
    }

    public boolean getResult(){
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (currentOperator == "") return false;
        if (operation.length < 2) return false;
        result = String.valueOf(operator(operation[0], operation[1], currentOperator));
        return true;
    }

    public void onClickEqual(View v){
        Context context = getApplicationContext();
        if (display == "")return;
//        if (!getResult())return;
        try{
            result = evaluator.evaluate(display).toString();
            fullRxpression += " = " + result;
            dbHelper.insert("dailyCalculator", fullRxpression);
        }
        catch (Exception e){
            System.out.println (display+" is an invalid expression");
        }
        resultView.setText(String.valueOf(result));
        Toast.makeText(context, fullRxpression, Toast.LENGTH_LONG).show();

    }

    public void onClickBackspace(View v){
        if (display != "" && display.length() > 0) {
            if(isOperator(display.charAt(display.length() - 1))){
                display = display.substring(0, display.length() - 1);
                fullRxpression = fullRxpression.substring(0, fullRxpression.length()-1);
                updateScreen();
                currentOperator = "";
                if (!isDotClicked){
                    isDotClicked = true;
                }
            }
            else{
                display = display.substring(0, display.length() - 1);
                fullRxpression = fullRxpression.substring(0, fullRxpression.length()-1);
                updateScreen();
            }
        }
    }

    public void onClickHistory(View v){
        Intent intent = new Intent(this, History.class);
        intent.putExtra("calcName", "dailyCalculator");
        startActivity(intent);
    }

    public void onClickMemoryPlus(View v){
        Context context = getApplicationContext();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String saved_value = sharedPref.getString("saved_result", "");

        if (saved_value.isEmpty()){
            if (display.isEmpty()){
                Toast.makeText(context, "No value stored.", Toast.LENGTH_LONG).show();
            }
            else{
                editor.putString("saved_result", display);
                editor.commit();
                Toast.makeText(context, "Saved", Toast.LENGTH_LONG).show();
            }
        }
        else{
            if (display != ""){
                Double newValue = (Double.valueOf(saved_value) + Double.valueOf(display));
                editor.putString("saved_result", String.valueOf(newValue));
                editor.commit();
                display = String.valueOf(newValue);
                updateScreen();
            }
            else {
                Double newValue = (Double.valueOf(saved_value) + Double.valueOf(saved_value));
                editor.putString("saved_result", String.valueOf(newValue));
                editor.commit();
                display = String.valueOf(newValue);
                updateScreen();
            }
        }
    }

    public void onClickMemoryMinus(View v){
        Context context = getApplicationContext();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String saved_value = sharedPref.getString("saved_result", "");
        if (saved_value.isEmpty()){
            Toast.makeText(context, "No value saved yet", Toast.LENGTH_LONG).show();
        }
        else {
            if (display.isEmpty()){
                Toast.makeText(context, "Write some value first", Toast.LENGTH_LONG).show();
            }
            else {
                Double newValue = (Double.valueOf(saved_value) - Double.valueOf(display));
                editor.putString("saved_result", String.valueOf(newValue));
                editor.commit();
                display = String.valueOf(newValue);
                updateScreen();
            }
        }
    }

    public void onClickMemoryRestore(View v){
        Context context = getApplicationContext();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        String saved_value = sharedPref.getString("saved_result", "");
        if (saved_value.isEmpty()){
            Toast.makeText(context, "No saved value", Toast.LENGTH_LONG).show();
        }
        display = String.valueOf(saved_value);
        updateScreen();
    }

    public void onClickMemoryClear(View v){
        Context context = getApplicationContext();
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("saved_result", "");
        editor.commit();
        Toast.makeText(context, "Memory cleared", Toast.LENGTH_LONG).show();
        clear();
        updateScreen();
    }
}
