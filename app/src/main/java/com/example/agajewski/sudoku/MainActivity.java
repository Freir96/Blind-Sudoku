package com.example.agajewski.sudoku;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;

import java.util.Locale;

import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnTouchListener {
    TextToSpeech t1;
    private TextToSpeech textToSpeech;
    //EditText ed1;
    int[] buttonValues;
    Button[] button;
    int sudokuSize;
    boolean[] longClicked;
    SmallSudoku sudoku;
    AccessibilityManager am;
    String instructions;
    //SwipeGestureListener gestureListener;

    //private GestureDetector gestureDetector;
    private float x1, x2, y1, y2;
    static final int MIN_DISTANCE = 150;

    TextView tv;
    int cellX, cellY;
    private static final int NONE = 0;
    private static final int SWIPE = 1;
    private int mode = NONE;
    private float startX, stopX, startY, stopY;
    // We will only detect a swipe if the difference is at least 100 pixels
    private static final int TRESHOLD = 100;

    private void talk(String s) {
        int speechStatus = textToSpeech.speak(s, TextToSpeech.QUEUE_FLUSH, null);

        if (speechStatus == TextToSpeech.ERROR) {
            Log.e("TTS", "Error in converting Text to Speech!");
        }
    }

    private void setNumbers() {
        /*for (int j = 0; j < 3; j++)
            for (int i = 0; i < 2; i++)
                button[i + j].setText("" + sudoku.getMatrixToSolve(i + cellX, j + cellY));*/
        for (int i = 0; i < 6; i++)
            button[i].setText("" + sudoku.getMatrixToSolve(convertX(i), convertY(i)));
    }

    int convertX(int conv) {
        return (conv % 2) + cellX * 2;
    }

    int convertY(int conv) {
        return ((conv) / 2) + cellY * 3;
    }

    void cancelLongClick() {
        for (boolean cell : longClicked)
            cell = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
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

        setContentView(R.layout.instruction);
        instructions = "Tap to play";
        initInstructions();
        tv = findViewById(R.id.textView);
        tv.setText(instructions);
        talk(instructions);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_main);
               // textToSpeech.stop();
                initSudoku();
            }
        });
        talk(instructions);

    }

    void initSudoku() {
        //ed1=(EditText)findViewById(R.id.editText);
        Context mContext = getApplicationContext();

        // gestureDetector = new GestureDetector(myContext, new GestureListener());
        sudoku = new SmallSudoku();
        cellX = 0;
        cellY = 0;
        sudokuSize = 6;
        buttonValues = new int[6];
        longClicked = new boolean[6];
        button = new Button[6];
        button[0] = (Button) findViewById(R.id.button);
        button[1] = (Button) findViewById(R.id.button2);
        button[2] = (Button) findViewById(R.id.button3);
        button[3] = (Button) findViewById(R.id.button4);
        button[4] = (Button) findViewById(R.id.button5);
        button[5] = (Button) findViewById(R.id.button6);

        setNumbers();

        for (int i = 0; i < sudokuSize; i++) {
            button[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
                    boolean isAccessibilityEnabled = am.isEnabled();
                    boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
                    //String toSpeak = ed1.getText().toString();
                    //Toast.makeText(getApplicationContext(), toSpeak,Toast.LENGTH_SHORT).show();
                    int i = Integer.parseInt(v.getTag().toString());
                    if (!isAccessibilityEnabled) {
                        if (longClicked[i]) {
                            Log.d("Click", "button: " + i + " x: " + convertX(i) + " y: " +
                                    "" + convertY(i) + " value: " + sudoku.getMatrixToSolve(convertX(i), convertY(i)));
                        /*buttonValues[i]++;
                        if (buttonValues[i] > sudokuSize)
                            buttonValues[i] = 1;
                        button[i].setText("" + buttonValues[i]);*/
                            int tmp = sudoku.getMatrixToSolve(convertX(i), convertY(i));
                            tmp++;
                            if (tmp > sudokuSize)
                                tmp = 0;
                            button[i].setText("" + tmp);
                            sudoku.setMatrixToSolve(tmp, convertX(i), convertY(i));
                        }
                        // t1.speak("" + buttonValues[i], TextToSpeech.QUEUE_FLUSH, null);
                        talk("" + sudoku.getMatrixToSolve(convertX(i), convertY(i)));
                    }
                }
            });
            button[i].setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View v) {
                    int i = Integer.parseInt(v.getTag().toString());
                    if (!sudoku.getTemplate(convertX(i), convertY(i))) {
                        longClicked[i] = !longClicked[i];
                    } else
                        talk("Fixed number");
                    if (sudoku.isFinished())
                        if (sudoku.isCorrect())
                            talk("Sudoku solved successfully!");
                        else
                            talk("Please correct your sudoku.");
                    return true;
                }
            });
            button[i].setOnHoverListener(new View.OnHoverListener() {
                @Override
                public boolean onHover(View v, MotionEvent event) {
                    am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
                    if (am.isTouchExplorationEnabled() && event.getPointerCount() == 1) {
                        final int action = event.getAction();
                        switch (action) {
                            case MotionEvent.ACTION_HOVER_ENTER: {//single tap
                                int i = Integer.parseInt(v.getTag().toString());
                                if (longClicked[i]) {
                                    Log.d("Click", "button: " + i + " x: " + convertX(i) + " y: " +
                                            "" + convertY(i) + " value: " + sudoku.getMatrixToSolve(convertX(i), convertY(i)));
                                    int tmp = sudoku.getMatrixToSolve(convertX(i), convertY(i));
                                    tmp++;
                                    if (tmp > sudokuSize)
                                        tmp = 0;
                                    button[i].setText("" + tmp);
                                    sudoku.setMatrixToSolve(tmp, convertX(i), convertY(i));
                                }
                                // t1.speak("" + buttonValues[i], TextToSpeech.QUEUE_FLUSH, null);
                                talk("" + sudoku.getMatrixToSolve(convertX(i), convertY(i)));
                                event.setAction(MotionEvent.ACTION_DOWN);
                            }
                            break;
                            case MotionEvent.ACTION_HOVER_MOVE: {
                                event.setAction(MotionEvent.ACTION_MOVE);
                            }
                            break;
                            case MotionEvent.ACTION_HOVER_EXIT: {
                                event.setAction(MotionEvent.ACTION_UP);
                            }
                            break;
                        }
                        return onTouchEvent(event);
                    }
                    return true;
                }
            });
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        Toast.makeText(this, "left right swipe", Toast.LENGTH_SHORT).show();
                        if (deltaX > 0) {
                            if (cellX > 0)
                                cellX--;
                            else
                                talk("Left Bound");
                        } else {
                            if (cellX < 2)
                                cellX++;
                            else
                                talk("Right bound");
                        }
                        talk("Cell " + (cellX) + " " + cellY);
                        setNumbers();
                        cancelLongClick();
                    } else {
                        // consider as something else - a screen tap for example
                    }
                } else {
                    if (Math.abs(deltaY) > MIN_DISTANCE) {
                        Toast.makeText(this, "up down swipe", Toast.LENGTH_SHORT).show();
                        if (deltaY > 0) {
                            if (cellY > 0)
                                cellY--;
                            else
                                talk("Upper bound");
                        } else {
                            if (cellY < 1)
                                cellY++;
                            else
                                talk("Lower bound");
                        }
                        talk("Cell " + (cellX) + " " + cellY);
                        setNumbers();
                    } else {
                        // consider as something else - a screen tap for example
                    }
                }

                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onStop() {
        if (t1 != null) {
            t1.stop();
        }
        super.onStop();
    }

    public void onPause() {
        if (t1 != null) {
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                y2 = event.getY();
                float deltaX = x2 - x1;
                float deltaY = y2 - y1;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show();
                        if (deltaX > 0) {
                            if (cellX > 0)
                                cellX--;
                        } else {
                            if (cellX < 2)
                                cellX++;
                        }
                        talk("Cell " + (cellX) + " " + cellY);
                    } else {
                        // consider as something else - a screen tap for example
                    }
                }
            {
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    Toast.makeText(this, "up2down swipe", Toast.LENGTH_SHORT).show();
                    if (deltaY > 0) {
                        if (cellY > 0)
                            cellY--;
                    } else {
                        if (cellY < 3)
                            cellY++;
                    }
                    talk("Cell " + (cellX) + " " + cellY);
                } else {
                    // consider as something else - a screen tap for example
                }
            }

            break;
        }

        //return super.onTouchEvent(event);
        return true;
    }

    private void initInstructions() {
        instructions = "Welcome to blind Sudoku!\nFill in the boxes so every number will be placed only" +
                "once in every row, column, and cell. Swipe your fingers to navigate between the cells," +
                "and tap a box to hear its content. Long tap the box to enable editing the number, and tap" +
                "the box to increment the number. Double tap to continue. Have fun!";
        //findViewById(R.id.textView);
    }
}