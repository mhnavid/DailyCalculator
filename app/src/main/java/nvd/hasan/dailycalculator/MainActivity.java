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
    private String currentOperator;
    private String[] operation;

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
        Button b = (Button) v;
        display += b.getText();
        updateScreen();
    }

    public void onClickOperator(View v){
        Button b = (Button) v;
        if(resultView.getText() != ""){
            display += b.getText();
            currentOperator += b.getText().toString();
            updateScreen();
        }
        else{
            Toast.makeText(this, "Please value first.", Toast.LENGTH_SHORT);
            clear();
            updateScreen();
        }
    }

    public void clear(){
        display = "";
        currentOperator = "";
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
                return Double.valueOf(a) * Double.valueOf(b);
            case "/":
                try {
                    return Double.valueOf(a) / Double.valueOf(b);
                }
                catch (Exception e){
                    Log.d("Calc", e.getMessage());
                }
            case "%":
                return Double.valueOf(a) * ( Double.valueOf(b) / 100 );
                default:
                    return 0;
        }
    }

    public void onClickEqual(View v){
        if (resultView.getText() != ""){
            operation = display.split(Pattern.quote(currentOperator));
            if (operation.length < 2) return;

            Double result = operator(operation[0], operation[1], currentOperator);
            clear();
            resultView.setText(String.valueOf(result));
            display = String.valueOf(result);
            updateScreen();
        }
        else{
            clear();
            updateScreen();
        }


    }

    public void onClickBackspace(View v){
        if (display != null && display.length() > 0) {
            display = display.substring(0, display.length() - 1);
        }
        updateScreen();
    }
}
