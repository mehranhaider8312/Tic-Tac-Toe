package com.example.tictactoe.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.tictactoe.databinding.FragmentGuessMasterBinding;

import java.util.Random;

public class GuessMaster extends Fragment {

    private FragmentGuessMasterBinding binding;
    private int ORIGINAL_NUMBER;
    private int USER_GUESSED;
    private int attemptCount = 0;
    private int minRange = 1;
    private int maxRange = 100;
    private Random random;
    private boolean gameStarted = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuessMasterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        random = new Random();
        resetGame();
        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.btnStart.setOnClickListener(v -> {
            if (gameStarted) {
                resetGame();
            } else {
                startGame();
            }
        });

        binding.tvGuessOne.setOnClickListener(v -> handleGuess(binding.tvGuessOne.getText().toString()));
        binding.tvGuessTwo.setOnClickListener(v -> handleGuess(binding.tvGuessTwo.getText().toString()));
        binding.tvGuessThree.setOnClickListener(v -> handleGuess(binding.tvGuessThree.getText().toString()));
        binding.tvGuessFour.setOnClickListener(v -> handleGuess(binding.tvGuessFour.getText().toString()));
    }

    private void startGame() {
        String minText = binding.etRangeMin.getText().toString().trim();
        String maxText = binding.etRangeMax.getText().toString().trim();

        if (minText.isEmpty() || maxText.isEmpty()) {
            Toast.makeText(getContext(), "Please enter both min and max values", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            minRange = Integer.parseInt(minText);
            maxRange = Integer.parseInt(maxText);

            if (minRange >= maxRange) {
                Toast.makeText(getContext(), "Max must be greater than Min", Toast.LENGTH_SHORT).show();
                return;
            }

            if (maxRange - minRange < 3) {
                Toast.makeText(getContext(), "Range must be at least 4 numbers", Toast.LENGTH_SHORT).show();
                return;
            }

            ORIGINAL_NUMBER = random.nextInt((maxRange - minRange) + 1) + minRange;
            setUpGuess(minRange, maxRange);

            binding.animThinkingMonkey.setVisibility(View.GONE);
            binding.cardGuess.setVisibility(View.VISIBLE);
            binding.guessContainer.setVisibility(View.VISIBLE);
            binding.tvResultText.setText("🤔 Pick the number I'm thinking of!");
            binding.tvResultText.setTextSize(18);
            binding.btnStart.setText("🔄 Play Again");
            binding.animFireWorks.setVisibility(View.GONE);

            attemptCount = 0;
            gameStarted = true;

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpGuess(int min, int max) {
        int guess1 = random.nextInt((max - min) + 1) + min;
        int guess2 = random.nextInt((max - min) + 1) + min;
        int guess3 = random.nextInt((max - min) + 1) + min;

        while (guess1 == ORIGINAL_NUMBER) {
            guess1 = random.nextInt((max - min) + 1) + min;
        }

        while (guess2 == ORIGINAL_NUMBER || guess2 == guess1) {
            guess2 = random.nextInt((max - min) + 1) + min;
        }

        while (guess3 == ORIGINAL_NUMBER || guess3 == guess1 || guess3 == guess2) {
            guess3 = random.nextInt((max - min) + 1) + min;
        }

        int correctPosition = random.nextInt(4) + 1;

        switch (correctPosition) {
            case 1:
                binding.tvGuessOne.setText(String.valueOf(ORIGINAL_NUMBER));
                binding.tvGuessTwo.setText(String.valueOf(guess1));
                binding.tvGuessThree.setText(String.valueOf(guess2));
                binding.tvGuessFour.setText(String.valueOf(guess3));
                break;
            case 2:
                binding.tvGuessOne.setText(String.valueOf(guess1));
                binding.tvGuessTwo.setText(String.valueOf(ORIGINAL_NUMBER));
                binding.tvGuessThree.setText(String.valueOf(guess2));
                binding.tvGuessFour.setText(String.valueOf(guess3));
                break;
            case 3:
                binding.tvGuessOne.setText(String.valueOf(guess1));
                binding.tvGuessTwo.setText(String.valueOf(guess2));
                binding.tvGuessThree.setText(String.valueOf(ORIGINAL_NUMBER));
                binding.tvGuessFour.setText(String.valueOf(guess3));
                break;
            case 4:
                binding.tvGuessOne.setText(String.valueOf(guess1));
                binding.tvGuessTwo.setText(String.valueOf(guess2));
                binding.tvGuessThree.setText(String.valueOf(guess3));
                binding.tvGuessFour.setText(String.valueOf(ORIGINAL_NUMBER));
                break;
        }
    }

    private void handleGuess(String guessText) {
        try {
            USER_GUESSED = Integer.parseInt(guessText);
            attemptCount++;

            if (USER_GUESSED == ORIGINAL_NUMBER) {
                String victoryMessage = "";

                if (attemptCount == 1) {
                    String[] firstTryMessages = {
                            "🏆 UNBELIEVABLE! First try?! You're a LEGEND!",
                            "🤯 WHAT?! First guess?! Are you a mind reader?!",
                            "👑 BOW DOWN! First attempt! You're the KING/QUEEN!",
                            "⚡ INSANE! Got it on first try! Pure genius!",
                            "🎯 BULLSEYE! First shot! You're unstoppable!"
                    };
                    victoryMessage = firstTryMessages[random.nextInt(firstTryMessages.length)];
                } else if (attemptCount == 2) {
                    String[] secondTryMessages = {
                            "🌟 WOW! Second try! You're pretty sharp!",
                            "💪 Not bad! Two attempts! Impressive skills!",
                            "✨ Nice! Got it on second guess! Well done!",
                            "🎊 Sweet! Two tries! You're good at this!",
                            "🔥 Solid! Second attempt! Respect!"
                    };
                    victoryMessage = secondTryMessages[random.nextInt(secondTryMessages.length)];
                } else if (attemptCount == 3) {
                    String[] thirdTryMessages = {
                            "👍 Alright! Three attempts! Not too shabby!",
                            "😊 Finally! Third try! Better late than never!",
                            "🎉 There we go! Got it in three! Decent!",
                            "🙂 OK! Three guesses! You got there eventually!",
                            "✌️ Cool! Third time's the charm!"
                    };
                    victoryMessage = thirdTryMessages[random.nextInt(thirdTryMessages.length)];
                } else {
                    String[] fourthTryMessages = {
                            "😅 FINALLY! Took you long enough! 4 tries seriously?!",
                            "🤦 About time! 4 attempts?! My grandma is faster!",
                            "😂 At last! 4 guesses?! Were you sleeping?!",
                            "🙄 PHEW! 4 tries! Glad you made it before next year!",
                            "🤭 Well well! 4 attempts! Did you need a map?!",
                            "😏 Congrats! Only took 4 tries! Participation trophy? 🏅",
                            "🫠 Success! 4 guesses! That was... painful to watch!",
                            "😬 You did it! 4 attempts! Cutting it close there buddy!",
                            "🤨 Finally! 4 tries! I was getting bored waiting!",
                            "😵‍💫 Yay! 4 attempts! My patience almost ran out!"
                    };
                    victoryMessage = fourthTryMessages[random.nextInt(fourthTryMessages.length)];
                }

                binding.tvResultText.setText(victoryMessage);
                binding.tvResultText.setTextSize(20);
                binding.animFireWorks.setVisibility(View.VISIBLE);
                binding.cardGuess.setVisibility(View.GONE);
                binding.guessContainer.setVisibility(View.GONE);
                binding.animThinkingMonkey.setVisibility(View.VISIBLE);
                binding.btnStart.setText("🔄 Play Again");

                gameStarted = false;
            } else {
                Log.d("WrongGuess", "Entered");
                String[] roastMessages = {
                        "😂 Are you even trying? My grandma picks better!",
                        "🤦 Seriously? A monkey could do better than this!",
                        "💀 That's your guess? Did you close your eyes?",
                        "🙈 Wow! That was embarrassingly wrong!",
                        "😬 Even a broken calculator would do better!",
                        "🤡 Nice try, genius! NOT!",
                        "👎 That's the worst guess I've ever seen!",
                        "🥴 Did you pick randomly? Because it shows!",
                        "😤 Come on! My pet rock has better intuition!",
                        "🤨 Really? THAT was your best shot?",
                        "😤 Really? A monkey🙈 would have guessed that one",
                        "😆 I've seen toddlers make smarter choices!",
                        "🫣 Ouch! That guess was painful to watch!",
                        "😏 Is that the best your brain can do?",
                        "🙄 Wrong again! Shocking... NOT!",
                        "🤭 Did you mean to be this bad?",
                        "😵 That guess made me lose brain cells!",
                        "🤪 Random guessing much? Try thinking!",
                        "😱 How did you get it SO wrong?!",
                        "🫠 That's... impressively terrible!",
                        "😒 Pathetic! Try using your brain this time!"
                };

                binding.tvResultText.setVisibility(View.VISIBLE);
                String hint = USER_GUESSED < ORIGINAL_NUMBER ? "higher ⬆️" : "lower ⬇️";
                String roast = roastMessages[random.nextInt(roastMessages.length)];
                binding.tvResultText.setText(roast + "\nTry " + hint + "!");
                binding.tvResultText.setTextSize(16);
                Log.d("WrongGuess", roast + "\nTry " + hint + "!");
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid guess", Toast.LENGTH_SHORT).show();
        }
    }

    private void resetGame() {
        binding.animThinkingMonkey.setVisibility(View.VISIBLE);
        binding.animFireWorks.setVisibility(View.GONE);
        binding.cardGuess.setVisibility(View.GONE);
        binding.guessContainer.setVisibility(View.GONE);
        binding.tvResultText.setText("🎮 Set your range and start guessing!");
        binding.tvResultText.setTextSize(18);
        binding.btnStart.setText("🚀 Start Game");
        attemptCount = 0;
        gameStarted = false;

        if (binding.etRangeMin.getText().toString().isEmpty()) {
            binding.etRangeMin.setText("1");
        }
        if (binding.etRangeMax.getText().toString().isEmpty()) {
            binding.etRangeMax.setText("100");
        }
    }
}