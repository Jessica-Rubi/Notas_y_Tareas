package com.example.notasytareas;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class Video extends AppCompatActivity {
    VideoView vio;
    ImageButton star;
    boolean play = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_video);
        String valor = getIntent().getExtras().getString("urlVid");
        Uri myUri = Uri.parse(valor);
        vio = (VideoView) findViewById(R.id.vioVio);
        star = (ImageButton) findViewById(R.id.imageBtnStarVideo);
        vio.setVideoURI(myUri);
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (play){
                    vio.start();
                    star.setImageResource(android.R.drawable.ic_media_pause);
                }else {
                    vio.stopPlayback();
                    star.setImageResource(android.R.drawable.ic_media_play);
                }
                play = !play;
            }
        });
    }
}
