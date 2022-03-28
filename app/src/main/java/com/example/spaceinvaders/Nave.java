package com.example.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

public class Nave {

    RectF rect;

    //Nave representada en un bitmap
    private Bitmap bitmap;

    private final float largo;
    private final float alto;

    private float x;
    private final float y;

    private final float velocidad;


    private int movimiento = 0;

    public Nave(Context context, float screenX, float screenY){

        rect = new RectF();

        largo = screenX/15;
        alto = screenY/15;

        x = screenX / 2;
        y = screenY-10;

        //inicializo el bitmap
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.nave);
        //acomodo el personaje a la pantalla
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (largo), (int) (alto), false);
        velocidad = 800;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public float getX(){
        return x;
    }

    public float getLargo(){
        return largo;
    }

    public void mover(int dir){
        movimiento = dir;
    }

    public void actualizar(long fps){
        int IZQ = 1;
        int DER = 2;

        if(movimiento == IZQ)
            if (x > largo *.5) x = x - velocidad / fps;

        if(movimiento == DER)
            if (x < largo *9.5) x = x + velocidad / fps;

        // update rect which is used to detect hits
        rect.top = y;
        rect.bottom = y + alto;
        rect.left = x;
        rect.right = x + largo;
        movimiento = 0;
    }
}