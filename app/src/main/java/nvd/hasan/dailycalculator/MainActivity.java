package nvd.hasan.dailycalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private TextView resultView;
    private String display = "";
    private String currentOperator = "";

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
        display += b.getText();
        currentOperator += b.getText().toString();
        updateScreen();
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
                    return -1;
        }
    }

    public void onClickEqual(View v){
        String[] operation = display.split(Pattern.quote(currentOperator));
        if (operation.length < 2) return;

        Double result = operator(operation[0], operation[1], currentOperator);
        resultView.setText(String.valueOf(result));
    }
}
