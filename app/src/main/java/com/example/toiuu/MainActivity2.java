package com.example.toiuu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    private Cheeses cheeses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);

        cheeses = new Cheeses(5000);

        setupButton(R.id.btnStringPlus, R.id.tvStringPlusTime, () -> cheeses.populateWithStringPlus());
        setupButton(R.id.btnStringFormat, R.id.tvStringFormatTime, () -> cheeses.populateWithStringFormat());
        setupButton(R.id.btnStringBuilder, R.id.tvStringBuilderTime, () -> cheeses.populateWithStringBuilder());
        setupButton(R.id.btnCompileStatement, R.id.tvCompileStatementTime, () -> cheeses.populateWithCompileStatement());
        setupButton(R.id.btnContentValues, R.id.tvContentValuesTime, () -> cheeses.populateWithContentValues());
        setupButton(R.id.btnCompileStatementOneTransaction, R.id.tvCompileStatementOneTransactionTime, () -> cheeses.populateWithCompileStatementOneTransaction());
        setupButton(R.id.btnIterateBothColumns, R.id.tvIterateBothColumnsTime, () -> cheeses.iterateBothColumns());
        setupButton(R.id.btnIterateFirstColumn, R.id.tvIterateFirstColumnTime, () -> cheeses.iterateFirstColumn());
    }

    private void setupButton(int buttonId, int textViewId, java.util.concurrent.Callable<Long> method) {
        Button button = findViewById(buttonId);
        TextView textView = findViewById(textViewId);

        button.setOnClickListener(v -> {
            try {
                long duration = method.call();
                textView.setText("Time: " + duration + " ms");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
