package com.example.spaceinvaders;

import android.graphics.RectF;

public class Pared {

    private final RectF rect;

    private boolean esVisible;

    public Pared(int fil, int col, int numParedes, float screenX, float screenY){

        float ancho = screenX / 90;
        float alto = screenY / 40;

        esVisible = true;

        float distParedes = screenX / 9;
        float alturaInicial = screenY - (screenY /7 * 2);

        rect = new RectF(
                col * ancho + (distParedes * numParedes) + distParedes + distParedes * numParedes,
                fil * alto  + alturaInicial,
                col * ancho + ancho + (distParedes * numParedes) + distParedes + distParedes * numParedes,
                fil * alto + alto + alturaInicial);
    }
    public RectF getRect(){
        return this.rect;
    }

    public void setInvisible(){
        esVisible = false;
    }

    public boolean getVisibilidad(){
        return esVisible;
    }
}