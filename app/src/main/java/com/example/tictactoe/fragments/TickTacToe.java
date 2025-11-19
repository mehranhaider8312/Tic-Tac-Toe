package com.example.tictactoe.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tictactoe.R;

public class TickTacToe extends Fragment {

    private Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn9;
    private Button btnRestart, btnReset;
    private TextView tvXScore, tvOScore;
    private String b1, b2, b3, b4, b5, b6, b7, b8, b9;
    private int clickFlag = 0, clickCounter = 0;
    private int xScore = 0, oScore = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tick_tak_toe, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeViews(view);
        setupClickListeners();
    }

    private void initializeViews(View view) {
        btn1 = view.findViewById(R.id.btn1);
        btn2 = view.findViewById(R.id.btn2);
        btn3 = view.findViewById(R.id.btn3);
        btn4 = view.findViewById(R.id.btn4);
        btn5 = view.findViewById(R.id.btn5);
        btn6 = view.findViewById(R.id.btn6);
        btn7 = view.findViewById(R.id.btn7);
        btn8 = view.findViewById(R.id.btn8);
        btn9 = view.findViewById(R.id.btn9);

        btnRestart = view.findViewById(R.id.btnRestart);
        btnReset = view.findViewById(R.id.btnReset);

        tvXScore = view.findViewById(R.id.tvXScore);
        tvOScore = view.findViewById(R.id.tvOScore);

        updateScoreDisplay();
    }

    private void setupClickListeners() {
        btn1.setOnClickListener(v -> check(btn1));
        btn2.setOnClickListener(v -> check(btn2));
        btn3.setOnClickListener(v -> check(btn3));
        btn4.setOnClickListener(v -> check(btn4));
        btn5.setOnClickListener(v -> check(btn5));
        btn6.setOnClickListener(v -> check(btn6));
        btn7.setOnClickListener(v -> check(btn7));
        btn8.setOnClickListener(v -> check(btn8));
        btn9.setOnClickListener(v -> check(btn9));

        btnRestart.setOnClickListener(v -> restartGame());
        btnReset.setOnClickListener(v -> resetScore());
    }

    private void check(Button currentButton) {
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

                String winner = checkWinner();
                if (winner != null) {
                    Toast.makeText(getContext(), winner + " won the game!!", Toast.LENGTH_LONG).show();
                    updateScore(winner);
                    newGame();
                    return;
                }

                if (clickCounter > 8) {
                    Toast.makeText(getContext(), "Game Draw!!!", Toast.LENGTH_LONG).show();
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

    private void newGame() {
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

    private void restartGame() {
        newGame();
        Toast.makeText(getContext(), "New game started!", Toast.LENGTH_SHORT).show();
    }

    private void resetScore() {
        xScore = 0;
        oScore = 0;
        updateScoreDisplay();
        newGame();
        Toast.makeText(getContext(), "Scores reset! New game started!", Toast.LENGTH_SHORT).show();
    }
}