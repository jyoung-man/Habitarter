package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Main extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent_detector = new Intent(this, DetectorActivity.class);
        Intent intent_caller = new Intent(this, Caller.class);

        Button button1 = (Button) findViewById(R.id.button_detector);
        Button button2 = (Button) findViewById(R.id.button_caller);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(intent_detector);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(intent_caller);
            }
        });
    }



}
