package com.example.spaceinvaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;

import java.util.Random;

public class Alien {

    RectF rect;

    Random random = new Random();

    private Bitmap alien;
    private final float largo;
    private final float altura;
    private float x;
    private float y;

    private float velocidad;

    private final int IZQ = 1;
    private final int DER = 2;

    private int movimiento = DER;

    boolean isVisible;
    public Alien(Context context, int fil, int col, float screenX, float screenY) {

        rect = new RectF();

        largo = screenX / 20;
        altura = screenY / 20;

        isVisible = true;

       float sep = screenX / 25;

        x = col * (largo + sep);
        y = fil * (largo + sep/4);

        alien = BitmapFactory.decodeResource(context.getResources(), R.drawable.alien);

        //acomoda el bitmap a la resolucion de la pantalla
        alien = Bitmap.createScaledBitmap(alien, (int) (largo), (int) (altura), false);
        velocidad = 85;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibilidad(){
        return isVisible;
    }

    public RectF getRect(){
        return rect;
    }

    public Bitmap getBitmap(){
        return alien;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getLargo(){
        return largo;
    }

    public void actualizar(long fps){
        if(movimiento == IZQ)
            x = x - velocidad / fps;
        if(movimiento == DER)
            x = x + velocidad / fps;

        rect.top = y;
        rect.bottom = y + altura;
        rect.left = x;
        rect.right = x + largo;
    }
    public void bajarAlterno(){
        if(movimiento == IZQ)
            movimiento = DER;
        else
            movimiento = IZQ;

        y = y + altura;

        velocidad = velocidad * 1.2f;
    }
    public boolean elegirDisparo(float NaveX, float LargoNave){

        int rand;
        if((NaveX + LargoNave > x && NaveX + LargoNave < x + largo) ||
                (NaveX > x && NaveX < x + largo)) {
            rand = random.nextInt(300);
            if(rand == 0)
                return true;
        }
        rand = random.nextInt(2000);
        return rand == 0;
    }
}