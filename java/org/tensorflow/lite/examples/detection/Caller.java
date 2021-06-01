package org.tensorflow.lite.examples.detection;

import android.content.Intent;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Caller extends AppCompatActivity {

    TextToSpeech tts;
    SpeechRecognizer mRecognizer;
    Button button_register;
    Button button_conncet;
    int telCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caller);

        Intent intent_register = new Intent(this, Caller_register.class);
        Intent intent_call = new Intent(this, Caller_connect.class);

        button_register = (Button) findViewById(R.id.button_register);
        button_conncet = (Button) findViewById(R.id.button_call);

        button_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

                startActivity(intent_register);
            }
        });
        button_conncet.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click
                tts.setSpeechRate(1f);
                funcVoiceOut("등록했던 이름을 말해주세요");
                startActivity(intent_call);

            }
        });


        // TTS 객체
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setPitch(1f);
                    tts.setLanguage(Locale.KOREAN);
                    tts.setSpeechRate(0.95f);
                }
            }
        });
    }

    public void funcVoiceOut(String OutMsg){                    // 음성을 말해주는 기능
        if(OutMsg.length()<1)
            return;       // error
        if(!tts.isSpeaking()) {
            tts.speak(OutMsg, TextToSpeech.QUEUE_FLUSH, null);
        }
    }




}
