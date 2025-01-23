package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialiser l'ImageView et GestureDetector
        ImageView imageView = findViewById(R.id.my_image_view);
        imageView.setImageResource(R.drawable.logo);

        gestureDetector = new GestureDetector(this, new GestureListener());

        // Détecter le double-tap sur l'image
        imageView.setOnTouchListener((v, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    // Classe interne pour détecter les gestes
    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // Lorsque le double-tap est détecté, naviguer vers l'écran du scanner
            Intent intent = new Intent(MainActivity.this, Scanner.class);
            startActivity(intent);
            return true;
        }
    }
}
