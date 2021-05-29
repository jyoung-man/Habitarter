package org.tensorflow.lite.examples.detection;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Caller_register extends Caller {
    Intent intent;
    SpeechRecognizer mRecognizer;
    Button sttBtn;
    TextView textView;
    final int PERMISSION = 1;
    TextToSpeech tts;

    Map<String,String> data = new HashMap<String,String>();
    String name;
    String address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caller_register);

        if ( Build.VERSION.SDK_INT >= 23 ) { // 퍼미션 체크
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO}, PERMISSION);
        }
        // STT
        sttBtn = (Button) findViewById(R.id.sttStart);
        textView = (TextView)findViewById(R.id.sttResult);
        intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"ko-KR");
        sttBtn.setOnClickListener(v -> { mRecognizer=SpeechRecognizer.createSpeechRecognizer(this);
            mRecognizer.setRecognitionListener(listener);
            mRecognizer.startListening(intent);
        });

        // TTS 객체
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setPitch(0.7f);
                    tts.setLanguage(Locale.KOREAN);
                    tts.setSpeechRate(0.8f);
                }
            }
        });

    }
    public void saveNameNAddress() throws IOException {         // 내부 저장소에 이름과 전화번호 저장
        String fileName = "SmartRod";

        FileOutputStream os = openFileOutput(fileName, MODE_PRIVATE);
        os.write(data.toString().getBytes());
        os.close();

    }
    public void funcVoiceOut(String OutMsg){                    // 음성을 말해주는 기능
        if(OutMsg.length()<1)
            return;       // error
        if(!tts.isSpeaking()) {
            if(OutMsg.equals("다시 입력") || OutMsg.equals("다시입력")) {
                tts.speak("이름과 전화번호를 다시 입력해주세요.", TextToSpeech.QUEUE_FLUSH, null);
            }
            else {      // 제대로 된 데이터 입력 받은 경우
                tts.speak(OutMsg+"...."+"...입력한 내용이 맞나요? 아니라면, 다시 입력 이라고 말해주세요.", TextToSpeech.QUEUE_FLUSH, null);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(tts !=null){
            tts.stop();
            tts.shutdown();
        }
    }

    private RecognitionListener listener = new RecognitionListener() {

        @Override public void onReadyForSpeech(Bundle params) {
            Toast.makeText(getApplicationContext(),"음성인식을 시작합니다.",Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onBufferReceived(byte[] buffer) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onEndOfSpeech() {
            // TODO Auto-generated method stub
        }
        @Override
        public void onRmsChanged(float rmsdB) {         // 들리는 소리 크기가 변경되었을 때 호출
            // TODO Auto-generated method stub
        }
        @Override
        public void onPartialResults(Bundle partialResults) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onEvent(int eventType, Bundle params) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onBeginningOfSpeech() {
            // TODO Auto-generated method stub
        }

        @Override public void onError(int error) {      // 에러 발생 시 호출
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO: message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_CLIENT: message = "클라이언트 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS: message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK: message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT: message = "네트웍 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH: message = "찾을 수 없음";
                    break;
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY: message = "RECOGNIZER가 바쁨";
                    break;
                case SpeechRecognizer.ERROR_SERVER: message = "서버가 이상함";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT: message = "말하는 시간초과";
                    break;
                default: message = "알 수 없는 오류임";
                    break;
            }
            Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " +
                    message, Toast.LENGTH_SHORT).show();
        }

        @Override public void onResults(Bundle results) { // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줍니다.
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            StringBuffer word = new StringBuffer();

            for(int i = 0; i < matches.size() ; i++){
                if(matches.get(i).equals(" ")) {
                    System.out.println("공백 스킵됨");
                    continue;
                }
                System.out.println("matches.get(i) : " + matches.get(i));
                textView.setText(matches.get(i));
                word.append(matches.get(i));
            }
            if(word.toString().equals("다시 입력") || word.toString().equals("다시입력")) {
                address = "";
                name = "";
            }
            funcVoiceOut(word.toString());                                      // 음성 출력
            System.out.println(word);
            boolean check = word.toString().matches("[+-]?\\d*(\\.\\d+)?");
            if (check) {                                                            // 만약 word가 숫자라면 true
                System.out.println("전화번호 : "+ word);
                address = word.toString();
                System.out.println("name : "+ name);
                System.out.println("address : "+ address);
            }
            else {                                                                  // 이름을 받은 경우
                System.out.println("이름 : "+ word);
                name = word.toString();
                System.out.println("name : "+ name);
                System.out.println("address : "+ address);
            }

            if(name != null && address != null) {                            // 이름과 전화번호 모두 받은 경우
                try {
                    saveNameNAddress();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                name = "";
                address = "";
                Toast.makeText(getApplicationContext(), "저장된 이름과 전화번호 : " +
                        name + ": " + address, Toast.LENGTH_SHORT).show();
            }

        }
    };
}


