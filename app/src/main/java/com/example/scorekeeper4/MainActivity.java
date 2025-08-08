package com.example.scorekeeper4;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scorekeeper4.databinding.ActivityMainBinding;
import com.example.scorekeeper4.databinding.PlayerCardBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final int[] scores = new int[]{0, 0, 0, 0};

    private SharedPreferences prefs;
    private static final String PREFS = "scorekeeper_prefs";
    private static final String KEY_S0 = "score_0";
    private static final String KEY_S1 = "score_1";
    private static final String KEY_S2 = "score_2";
    private static final String KEY_S3 = "score_3";
    private static final String KEY_N0 = "name_0";
    private static final String KEY_N1 = "name_1";
    private static final String KEY_N2 = "name_2";
    private static final String KEY_N3 = "name_3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getSharedPreferences(PREFS, MODE_PRIVATE);

        setSupportActionBar(binding.toolbar);
        binding.toolbar.setOnMenuItemClickListener(this::onToolbarItemSelected);

        setupPlayer(binding.player1, 0);
        setupPlayer(binding.player2, 1);
        setupPlayer(binding.player3, 2);
        setupPlayer(binding.player4, 3);

        loadState();
    }

    private void setupPlayer(PlayerCardBinding card, int index) {
        card.btnPlus5.setOnClickListener(v -> add(index, 5, card.scoreText));
        card.btnPlus10.setOnClickListener(v -> add(index, 10, card.scoreText));
        card.btnPlus20.setOnClickListener(v -> add(index, 20, card.scoreText));
        card.btnPlus40.setOnClickListener(v -> add(index, 40, card.scoreText));

        card.btnMinus10.setOnClickListener(v -> add(index, -10, card.scoreText));
        card.btnMinus20.setOnClickListener(v -> add(index, -20, card.scoreText));
        card.btnMinus40.setOnClickListener(v -> add(index, -40, card.scoreText));

        card.nameEdit.addTextChangedListener(new SimpleTextWatcher() {
            @Override public void afterTextChanged(Editable s) { saveNames(); }
        });

        updateScoreText(card.scoreText, scores[index]);
    }

    private void add(int index, int delta, TextView scoreText) {
        scores[index] += delta;
        updateScoreText(scoreText, scores[index]);
        saveScores();
    }

    private void updateScoreText(TextView tv, int value) {
        tv.setText(String.valueOf(value));
    }

    private boolean onToolbarItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset_points) {
            resetOnlyPoints();
            return true;
        } else if (id == R.id.action_reset_all) {
            resetAll();
            return true;
        }
        return false;
    }

    private void resetOnlyPoints() {
        scores[0] = scores[1] = scores[2] = scores[3] = 0;
        updateScoreText(binding.player1.scoreText, 0);
        updateScoreText(binding.player2.scoreText, 0);
        updateScoreText(binding.player3.scoreText, 0);
        updateScoreText(binding.player4.scoreText, 0);
        saveScores();
    }

    private void resetAll() {
        resetOnlyPoints();
        binding.player1.nameEdit.setText("");
        binding.player2.nameEdit.setText("");
        binding.player3.nameEdit.setText("");
        binding.player4.nameEdit.setText("");
        saveNames();
    }

    private void loadState() {
        scores[0] = prefs.getInt(KEY_S0, 0);
        scores[1] = prefs.getInt(KEY_S1, 0);
        scores[2] = prefs.getInt(KEY_S2, 0);
        scores[3] = prefs.getInt(KEY_S3, 0);

        updateScoreText(binding.player1.scoreText, scores[0]);
        updateScoreText(binding.player2.scoreText, scores[1]);
        updateScoreText(binding.player3.scoreText, scores[2]);
        updateScoreText(binding.player4.scoreText, scores[3]);

        binding.player1.nameEdit.setText(prefs.getString(KEY_N0, ""));
        binding.player2.nameEdit.setText(prefs.getString(KEY_N1, ""));
        binding.player3.nameEdit.setText(prefs.getString(KEY_N2, ""));
        binding.player4.nameEdit.setText(prefs.getString(KEY_N3, ""));
    }

    private void saveScores() {
        prefs.edit()
            .putInt(KEY_S0, scores[0])
            .putInt(KEY_S1, scores[1])
            .putInt(KEY_S2, scores[2])
            .putInt(KEY_S3, scores[3])
            .apply();
    }

    private void saveNames() {
        prefs.edit()
            .putString(KEY_N0, binding.player1.nameEdit.getText().toString())
            .putString(KEY_N1, binding.player2.nameEdit.getText().toString())
            .putString(KEY_N2, binding.player3.nameEdit.getText().toString())
            .putString(KEY_N3, binding.player4.nameEdit.getText().toString())
            .apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveScores();
        saveNames();
    }

    abstract static class SimpleTextWatcher implements TextWatcher {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override public void afterTextChanged(Editable s) {}
    }
}
