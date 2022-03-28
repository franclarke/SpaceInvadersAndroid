package com.example.spaceinvaders;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class LogicaActivity extends Activity {

    LogicaView spaceInvadersView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //display para acceder a detalles de pantalla
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        spaceInvadersView = new LogicaView(this, size.x, size.y);
        setContentView(spaceInvadersView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        spaceInvadersView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        spaceInvadersView.pause();
    }

}