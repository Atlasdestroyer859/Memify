package com.example.memejokesapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.MemeJokesApp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MemeFragment extends Fragment {

    private ImageView memeImageView;
    private ProgressBar progressBar;
    private String currentMemeUrl;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(com.example.MemeJokesApp.R.layout.fragment_meme, container, false);

        memeImageView = view.findViewById(R.id.memeImageView);
        progressBar = view.findViewById(R.id.progressBar);
        Button nextButton = view.findViewById(R.id.nextButton);
        Button shareButton = view.findViewById(R.id.shareButton);

        requestQueue = Volley.newRequestQueue(requireContext());

        loadMeme();

        nextButton.setOnClickListener(v -> loadMeme());
        shareButton.setOnClickListener(v -> shareMeme());

        return view;
    }

    private void loadMeme() {
        progressBar.setVisibility(View.VISIBLE);
        String url = "https://meme-api.com/gimme";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        currentMemeUrl = response.getString("url");
                        Glide.with(requireContext())
                                .load(currentMemeUrl)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .skipMemoryCache(true)
                                .into(memeImageView);
                        progressBar.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(requireContext(), "Error loading meme!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                },
                error -> {
                    Toast.makeText(requireContext(), "Failed to load meme!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                });

        requestQueue.add(request);
    }

    private void shareMeme() {
        if (currentMemeUrl != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this meme: " + currentMemeUrl);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        } else {
            Toast.makeText(requireContext(), "No meme to share!", Toast.LENGTH_SHORT).show();
        }
    }
}
