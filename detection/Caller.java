package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Caller extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caller);

        Intent intent_register = new Intent(this, Caller_register.class);
        Intent intent_call = new Intent(this, Caller_connect.class);

        Button button1 = (Button) findViewById(R.id.button_register);
        Button button2 = (Button) findViewById(R.id.button_call);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(intent_register);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                startActivity(intent_call);
            }
        });



    }



}
