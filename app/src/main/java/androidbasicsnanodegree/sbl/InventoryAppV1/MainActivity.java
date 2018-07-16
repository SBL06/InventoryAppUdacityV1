package androidbasicsnanodegree.sbl.InventoryAppV1;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button dummyButton;
    String result = "";
    TextView display;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.displayResult);

        dummyButton = findViewById(R.id.DummyButton);

        // The dummyButton is used to insert dummy data in the DB to check if it works properly.
        dummyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DbMethods.insertData(getApplicationContext(), getString(R.string.dummyName), 10.10f, 1, getString(R.string.dummySupplier), 5142453568L);
                result = DbMethods.displayDatabaseInfo(getApplicationContext());
                display.setText(result);
            }

        });

        // Data contained in the DB are displayed in a basic textView
        result = DbMethods.displayDatabaseInfo(getApplicationContext());
        display.setText(result);
    }
}
