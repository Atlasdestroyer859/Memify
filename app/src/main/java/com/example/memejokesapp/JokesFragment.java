package com.example.memejokesapp;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.MemeJokesApp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class JokesFragment extends Fragment {

    private TextView jokeFactTextView;
    private ProgressBar progressBar;
    private Button jokeButton, factButton, speakButton;
    private TextToSpeech textToSpeech;
    private RequestQueue requestQueue;
    private String currentText = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jokes, container, false);

        jokeFactTextView = view.findViewById(R.id.jokeFactTextView);
        progressBar = view.findViewById(com.example.MemeJokesApp.R.id.progressBar);
        jokeButton = view.findViewById(R.id.jokeButton);
        factButton = view.findViewById(R.id.factButton);
        speakButton = view.findViewById(R.id.speakButton);

        requestQueue = Volley.newRequestQueue(requireContext());

        textToSpeech = new TextToSpeech(requireContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                textToSpeech.setLanguage(Locale.US);
            }
        });

        jokeButton.setOnClickListener(v -> fetchJoke());
        factButton.setOnClickListener(v -> fetchFact());
        speakButton.setOnClickListener(v -> speakText());

        return view;
    }

    private void fetchJoke() {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://v2.jokeapi.dev/joke/Any?type=single";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        currentText = response.getString("joke");
                        jokeFactTextView.setText(currentText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error loading joke!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    Toast.makeText(requireContext(), "Failed to load joke!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });

        requestQueue.add(request);
    }

    private void fetchFact() {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://uselessfacts.jsph.pl/random.json?language=en";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        currentText = response.getString("text");
                        jokeFactTextView.setText(currentText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error loading fact!", Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                },
                error -> {
                    Toast.makeText(requireContext(), "Failed to load fact!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });

        requestQueue.add(request);
    }

    private void speakText() {
        if (!currentText.isEmpty()) {
            textToSpeech.speak(currentText, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Toast.makeText(requireContext(), "No text to speak!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
