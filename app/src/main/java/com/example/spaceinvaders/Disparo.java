package com.example.spaceinvaders;

import android.graphics.RectF;

public class Disparo {

    private float x;
    private float y;

    private final RectF rect;

    private int dir;
    float speed =  350;

    private final float alto;

    private boolean estaActivo;

    public Disparo(float screenY) {

        alto = screenY / 30;
        estaActivo = false;

        rect = new RectF();
    }

    public RectF getRect(){
        return  rect;
    }

    public boolean getEstado(){
        return estaActivo;
    }

    public void setInactivo(){
        estaActivo = false;
    }

    public float getImpactoY(){
        int ABAJO = 1;
        if (dir == ABAJO)
            return y + alto;
        else
            return  y;
    }

    public boolean disparo(float startX, float startY, int direction) {
        if (!estaActivo) {
            x = startX;
            y = startY;
            dir = direction;
            estaActivo = true;
            return true;
        }
        return false;
    }

    public void actualizar(long fps){

        // direccion
        int ARRIBA = 0;
        if(dir == ARRIBA)
            y = y - speed / fps;
        else
            y = y + speed / fps;

        rect.left = x;
        rect.right = x + 5;
        rect.top = y;
        rect.bottom = y + alto;
    }
}