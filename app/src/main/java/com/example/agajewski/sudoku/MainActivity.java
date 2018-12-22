package com.example.agajewski.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextToSpeech t1;
    //EditText ed1;
    int[] buttonValues;
    Button[] button;
    int sudokuSize;
    boolean[] longClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ed1=(EditText)findViewById(R.id.editText);
        sudokuSize = 6;
        buttonValues = new int[6];
        longClicked = new boolean[6];
        button = new Button[6];
        button[0] = (Button)findViewById(R.id.button);
        button[1] = (Button)findViewById(R.id.button2);
        button[2] = (Button)findViewById(R.id.button3);
        button[3] = (Button)findViewById(R.id.button4);
        button[4] = (Button)findViewById(R.id.button5);
        button[5] = (Button)findViewById(R.id.button6);

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
        for(int i = 0; i < sudokuSize; i++){
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String toSpeak = ed1.getText().toString();
                    //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                    int i = Integer.parseInt(v.getTag().toString());
                    if(longClicked[i]){
                        buttonValues[i]++;
                        if(buttonValues[i] > sudokuSize)
                            buttonValues[i] = 1;
                        button[i].setText("" + buttonValues[i]);
                    }
                    t1.speak("" + buttonValues[i], TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            button[i].setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    int i = Integer.parseInt(v.getTag().toString());
                    //longclick();
                    longClicked[i] = !longClicked[i];
                    return true;
                }
            });
        }

    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }
}