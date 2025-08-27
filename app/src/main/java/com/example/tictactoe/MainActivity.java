package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    Button btnRestart, btnReset;
    TextView tvXScore, tvOScore;
    String b1, b2, b3, b4, b5, b6, b7, b8, b9;
    int clickFlag = 0, clickCounter = 0;
    int xScore = 0, oScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
        btn7 = findViewById(R.id.btn7);
        btn8 = findViewById(R.id.btn8);
        btn9 = findViewById(R.id.btn9);

        btnRestart = findViewById(R.id.btnRestart);
        btnReset = findViewById(R.id.btnReset);

        tvXScore = findViewById(R.id.tvXScore);
        tvOScore = findViewById(R.id.tvOScore);

        updateScoreDisplay();
    }

    public void check(View v) {
        Button currentButton = (Button) v;

        if (currentButton.getText().toString().equals("")) {
            clickCounter++;

            if (clickFlag == 0) {
                currentButton.setText("X");
                currentButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                clickFlag = 1;
            } else {
                currentButton.setText("O");
                currentButton.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                clickFlag = 0;
            }

            if (clickCounter > 4) {
                b1 = btn1.getText().toString();
                b2 = btn2.getText().toString();
                b3 = btn3.getText().toString();
                b4 = btn4.getText().toString();
                b5 = btn5.getText().toString();
                b6 = btn6.getText().toString();
                b7 = btn7.getText().toString();
                b8 = btn8.getText().toString();
                b9 = btn9.getText().toString();

                // Check for winner
                String winner = checkWinner();
                if (winner != null) {
                    Toast.makeText(MainActivity.this, winner + " won the game!!", Toast.LENGTH_LONG).show();
                    updateScore(winner);
                    newGame();
                    return;
                }

                if (clickCounter > 8) {
                    Toast.makeText(MainActivity.this, "Game Draw!!!", Toast.LENGTH_LONG).show();
                    newGame();
                }
            }
        }
    }

    private String checkWinner() {
        if (b1.equals(b2) && b2.equals(b3) && !b1.equals("")) return b1;
        if (b4.equals(b5) && b5.equals(b6) && !b4.equals("")) return b4;
        if (b7.equals(b8) && b8.equals(b9) && !b7.equals("")) return b7;

        if (b1.equals(b4) && b4.equals(b7) && !b1.equals("")) return b1;
        if (b2.equals(b5) && b5.equals(b8) && !b2.equals("")) return b2;
        if (b3.equals(b6) && b6.equals(b9) && !b3.equals("")) return b3;

        if (b1.equals(b5) && b5.equals(b9) && !b1.equals("")) return b1;
        if (b3.equals(b5) && b5.equals(b7) && !b3.equals("")) return b3;

        return null;
    }

    private void updateScore(String winner) {
        if (winner.equals("X")) {
            xScore++;
        } else if (winner.equals("O")) {
            oScore++;
        }
        updateScoreDisplay();
    }

    private void updateScoreDisplay() {
        tvXScore.setText(String.valueOf(xScore));
        tvOScore.setText(String.valueOf(oScore));
    }

    public void newGame() {
        btn1.setText("");
        btn2.setText("");
        btn3.setText("");
        btn4.setText("");
        btn5.setText("");
        btn6.setText("");
        btn7.setText("");
        btn8.setText("");
        btn9.setText("");

        resetButtonColors();

        clickCounter = 0;
        clickFlag = 0;
    }

    private void resetButtonColors() {
        int defaultColor = getResources().getColor(android.R.color.primary_text_light);
        btn1.setTextColor(defaultColor);
        btn2.setTextColor(defaultColor);
        btn3.setTextColor(defaultColor);
        btn4.setTextColor(defaultColor);
        btn5.setTextColor(defaultColor);
        btn6.setTextColor(defaultColor);
        btn7.setTextColor(defaultColor);
        btn8.setTextColor(defaultColor);
        btn9.setTextColor(defaultColor);
    }

    public void restartGame(View v) {
        newGame();
        Toast.makeText(this, "New game started!", Toast.LENGTH_SHORT).show();
    }

    public void resetScore(View v) {
        xScore = 0;
        oScore = 0;
        updateScoreDisplay();
        newGame();
        Toast.makeText(this, "Scores reset! New game started!", Toast.LENGTH_SHORT).show();
    }
}