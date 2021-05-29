package org.tensorflow.lite.examples.detection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class Caller_connect extends Caller {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caller_connect);
    }


    public void call(View v){
        int id = v.getId();
        LinearLayout layout = (LinearLayout)v.findViewById(id);
        String tel = (String) layout.getTag();

        Uri number = Uri.parse("tel:" + tel);
        Intent intent = new Intent(Intent.ACTION_CALL, number);
        startActivity(intent);
    }
}


