package com.example.agajewski.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;
import android.widget.Toast;

public class MainActivity extends Activity {
    TextToSpeech t1;
    private TextToSpeech textToSpeech;
    //EditText ed1;
    int[] buttonValues;
    Button[] button;
    int sudokuSize;
    boolean[] longClicked;
    int cellX, cellY;
    private static final int NONE = 0;
    private static final int SWIPE = 1;
    private int mode = NONE;
    private float startX, stopX, startY, stopY;
    // We will only detect a swipe if the difference is at least 100 pixels
    private static final int TRESHOLD = 100;

    private void talk(String s){
        int speechStatus = textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //ed1=(EditText)findViewById(R.id.editText);
        cellX = 0;
        cellY = 0;
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
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = textToSpeech.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.i("TTS", "Language Supported.");
                    }
                    Log.i("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
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
                    talk("" + buttonValues[i]);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            // MotionEvent.ACTION_DOWN means one finger.
            // MotionEvent.ACTION_POINTER_DOWN is two fingers.
            case MotionEvent.ACTION_POINTER_DOWN:

                // This happens when you touch the screen with two fingers
                mode = SWIPE;
                // You can also use event.getY(1) or the average of the two
                startX = event.getX(0);
                startY = event.getY(0);

                break;
            case MotionEvent.ACTION_POINTER_UP:

                // This happens when you release the second finger
                mode = NONE;
                System.out.println((startX - stopX) + " " + (startY - stopY));
                if (Math.abs(startX - stopX) > TRESHOLD && Math.abs(startX - stopX) > Math.abs(startY - stopY)) {

                    if (startX > stopX) {

                        if(cellX > 0)
                            cellX--;

                    } else {

                        // Swipe right.
                        if(cellX < 2)
                            cellX++;
                    }
                }else if (Math.abs(startX - stopX) > TRESHOLD && Math.abs(startX - stopX) < Math.abs(startY - stopY)) {

                    if (startY > stopY) {

                        if(cellY > 0)
                            cellY--;

                    } else {

                        // Swipe right.
                        if(cellY < 3)
                            cellY++;
                    }
                }
                t1.speak("Cell " + ("a" + cellX) + " " + cellY , TextToSpeech.QUEUE_FLUSH, null);
                talk("Cell " + ("a" + cellX) + " " + cellY);
                break;
            case MotionEvent.ACTION_MOVE:

                if (mode == SWIPE) {

                    stopX = event.getX(0);
                    stopY = event.getY(0);
                }

                break;
        }

        return true;
    }

    @Override
    public void onStop() {
        if (t1 != null) {
            t1.stop();
        }
        super.onStop();
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (t1 != null) {
            t1.shutdown();
        }
        super.onDestroy();
    }
}