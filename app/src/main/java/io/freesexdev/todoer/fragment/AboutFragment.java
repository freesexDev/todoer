package io.freesexdev.todoer.fragment;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import io.freesexdev.todoer.R;

public class AboutFragment extends Fragment {

    private int layout;
    private int click;
    static MediaPlayer player;

    public AboutFragment() {
        super();
        layout = R.layout.fragment_about;
        click = 0;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout, container, false);
        player = MediaPlayer.create(getActivity(), R.raw.b);
        final ImageView i = (ImageView) view.findViewById(R.id.logo);
        i.animate().rotation(15).setDuration(200);
        i.animate().rotation(-30).setDuration(200);
        i.animate().rotation(30).setDuration(200);
        i.animate().rotation(-30).setDuration(200);
        i.animate().rotation(15).setDuration(200);
        i.setImageResource(R.drawable.web_hi_res_512);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click == 5) {
                    player.start();
                    i.animate().rotation(180).setDuration(500);
                    i.animate().rotation(180).setDuration(500);
                    i.animate().rotation(180).setDuration(500);
                    i.animate().rotation(180).setDuration(500);
                    i.animate().rotation(180).setDuration(500);
                    i.animate().rotation(180).setDuration(500);
                    click = 0;
                } else {
                    click++;
                    String clicks = Integer.toString(click);
                    if (click == 1) {
                        Toast.makeText(getActivity(), "Нажми на меня ещё " + clicks + " раз", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Нажми на меня ещё " + clicks + " раза", Toast.LENGTH_SHORT).show();
                        if (click == 5) {
                            Toast.makeText(getActivity(), "Нажми на меня ещё " + clicks + " раз", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        player.stop();
    }
}
