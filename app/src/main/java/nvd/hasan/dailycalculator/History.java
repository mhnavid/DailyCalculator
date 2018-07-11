package nvd.hasan.dailycalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class History extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);
        getSupportActionBar().setTitle("History");
    }

}
