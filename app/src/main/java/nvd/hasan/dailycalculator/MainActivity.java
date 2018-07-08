package nvd.hasan.dailycalculator;

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
    private String display = "";
    private String currentOperator = "";
    private String result = "";


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
        display += b.getText();
        updateScreen();
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

        if (currentOperator != ""){
            if(isOperator(display.charAt(display.length() - 1))){
                display = display.replace(display.charAt(display.length() - 1), b.getText().charAt(0));
                updateScreen();
                return;
            }
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

    public void clear(){
        display = "";
        currentOperator = "";
        result = "";
    }

    public void onClickClear(View v){
        clear();
        updateScreen();
    }

    public double operator(String a, String b, String op){
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
                    return Double.valueOf(a) / Double.valueOf(b);
                }
                catch (Exception e){
                    Log.d("Cal0", e.getMessage());
                }
                default:
                    return -1;
        }
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
}
