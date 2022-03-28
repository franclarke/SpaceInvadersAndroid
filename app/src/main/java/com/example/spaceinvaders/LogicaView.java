
package com.example.spaceinvaders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

@SuppressLint("ViewConstructor")
public class LogicaView extends SurfaceView implements Runnable {
    private final Context context;
    private Thread gameThread = null;
    private final SurfaceHolder holder;

    private boolean jugando;
    //el juego arranca pausado
    private boolean pausado = true;

    private final Paint paint;

    private long fps;
    private final float screenX;
    private final float screenY;

    private Nave nave;
    private int numDisparos;
    private int proxAlienDisparo;
    private int numAliens = 0;
    private int puntaje = 0;
    private int numParedes;
    private int vidas = 5;
    private final Disparo[] disparosNave = new Disparo[250];
    private final Disparo[] disparosAlien = new Disparo[50];
    private final Alien[] aliens = new Alien[30];
    private final Pared[] paredes = new Pared[200];



    public LogicaView(Context context, int x, int y) {
        super(context);
        this.context = context;
        holder = getHolder();
        paint = new Paint();
        screenX = x;
        screenY = y;
        preparar();
    }

    private void preparar() {
        pausado = true;
        puntaje = 0;
        numDisparos = 0;
        vidas = 5;

        nave = new Nave(context, screenX, screenY);

        for (int i = 0; i < disparosNave.length; i++)
            disparosNave[i] = new Disparo(screenY);

        for (int i = 0; i < disparosAlien.length; i++)
            disparosAlien[i] = new Disparo(screenY);

        numAliens = 0;
        for (int col = 0; col < 6; col++)
            for (int fil = 0; fil < 5; fil++)
                aliens[numAliens++] = new Alien(context, fil, col, screenX, screenY);

        numParedes = 0;
        for (int numPared = 0; numPared < 4; numPared++) {
            for (int col = 0; col < 10; col++)
                for (int fil = 0; fil < 5; fil++)
                    paredes[numParedes++] = new Pared(fil, col, numPared, screenX, screenY);

        }
    }


    public void run() {
        while (jugando) {
            long startTime = System.currentTimeMillis();

            if (!pausado) actualizar();
            draw();
            long timeNow = System.currentTimeMillis() - startTime;
            if (timeNow >= 1)
                fps = 700 / timeNow;
            }
        }

    private void actualizar() {
        boolean choco = false;
        boolean perdio = false;

        nave.actualizar(fps);

        for (Disparo disparo: disparosNave)
            if (disparo.getEstado())
                disparo.actualizar(fps);

        for (Disparo disparo: disparosAlien)
            if (disparo.getEstado())
                disparo.actualizar(fps);

        for (Alien alien: aliens)
            if (alien.getVisibilidad()) {
                alien.actualizar(fps);
                if (alien.elegirDisparo(nave.getX(), nave.getLargo()))
                    if (disparosAlien[proxAlienDisparo].disparo(alien.getX() + alien.getLargo() / 2, alien.getY(), 1))
                        proxAlienDisparo++;
                // determina si al mover los aliens, chocan con un lado
                if (alien.getX() > screenX - alien.getLargo() || alien.getX() < 0)
                    choco = true;
            }


        // determina que hacer cuando los aliens llegan a un lado
        if (choco) {
            // baja alternado todos los aliens
            for (Alien alien: aliens) {
                alien.bajarAlterno();
                // los aliens llegaron a las paredes
                if (alien.getY() > screenY - screenY / 3.5)
                    perdio = true;
            }
        }

        if (perdio) preparar();

        // borra los disparos del jugador cuando se van de la pantalla
        for (Disparo disp: disparosNave) {
            if (disp.getImpactoY() < 0)
                disp.setInactivo();
        }

        // borra los disparos de los aliens cuando se van de la pantalla
        for (Disparo disp: disparosAlien) {
            if (disp.getImpactoY() > screenY)
                disp.setInactivo();
        }

        //determina si un disparo del jugador choca un alien
        for (Disparo disp: disparosNave)
            if (disp.getEstado())
                for (Alien alien: aliens)
                    if (alien.getVisibilidad() && RectF.intersects(disp.getRect(), alien.getRect())) {
                        alien.setInvisible();
                        disp.setInactivo();
                        int incremento = 10;
                        puntaje = puntaje + incremento;

                        if (puntaje == numAliens * incremento) {
                            preparar();
                            break;
                        }
                    }


        //que hacer si un disparo de un alien choca con una pared
        for (Disparo disparo: disparosAlien)
            if (disparo.getEstado())
                for (Pared pared: paredes)
                    if (pared.getVisibilidad() && (RectF.intersects(disparo.getRect(), pared.getRect()))) {
                        // Si hay una colision
                        disparo.setInactivo();
                        pared.setInvisible();
                    }


        //que hacer si un disparo del jugador choca con una pared
        for (Disparo disparo: disparosNave)
            if (disparo.getEstado())
                for (Pared pared: paredes)
                    if (pared.getVisibilidad() && RectF.intersects(disparo.getRect(), pared.getRect())) {
                        // Si hay una colision
                        disparo.setInactivo();
                        pared.setInvisible();
                    }


        for (Disparo alienDisparo : disparosAlien) {
            if (alienDisparo.getEstado())
                if (RectF.intersects(nave.getRect(), alienDisparo.getRect())) {
                    alienDisparo.setInactivo();
                    vidas--;
                    if (vidas == 0)
                        preparar();
                }
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {

            Canvas canvas = holder.lockCanvas();

            canvas.drawColor(Color.argb(255, 0, 0, 0));
            paint.setColor(Color.argb(255, 255, 255, 255));

            for (int i = 0; i < numAliens; i++)
                if (aliens[i].getVisibilidad())
                        canvas.drawBitmap(aliens[i].getBitmap(), aliens[i].getX(), aliens[i].getY(), paint);

            paint.setColor(Color.argb(255, 51, 255, 0));
            canvas.drawBitmap(nave.getBitmap(), nave.getX(), screenY-50, paint);

            for (int i = 0; i < numParedes; i++)
                if (paredes[i].getVisibilidad())
                    canvas.drawRect(paredes[i].getRect(), paint);
            paint.setColor(Color.argb(255, 255, 255, 255));

            for (Disparo dispNave : disparosNave) {
                if (dispNave.getEstado())
                    canvas.drawRect(dispNave.getRect(), paint);
            }

            for (Disparo dispAlien : disparosAlien)
                if (dispAlien.getEstado())
                    canvas.drawRect(dispAlien.getRect(), paint);

            paint.setColor(Color.argb(255, 51, 255, 0));
            paint.setTextSize(50);
            canvas.drawText("Score: " + puntaje + "   Lives: " + vidas, screenX/2, 40, paint);

            holder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        jugando = false;
        try {
            gameThread.join();
        }
        catch (InterruptedException ignored) {
        }
    }

    public void resume() {
        jugando = true;
        gameThread = new Thread(this);
        gameThread.start();
    }



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        boolean run = true;
        int j = 0;
        while (run && j < 100) {
            j++;
            int switchInt = motionEvent.getAction() & MotionEvent.ACTION_MASK;

            if (switchInt == MotionEvent.ACTION_DOWN) {
                pausado = false;
                //si toco en la parte inferior de la pantalla mover
                if (motionEvent.getY() > screenY * 3 / 4)
                    if (motionEvent.getX() > screenX / 2)
                        nave.mover(2);
                    else
                        nave.mover(1);

                if (motionEvent.getY() <= screenY * 3 / 4) {
                    Disparo disparo = new Disparo(screenY);
                    if (numDisparos < disparosNave.length) {
                        disparosNave[numDisparos] = disparo;
                        disparo.disparo(nave.getX() + nave.getLargo() / 2, screenY-20, 0);
                        numDisparos++;
                    } else
                        numDisparos = 0;
                    run = false;
                }
            }
        }
        return true;
    }
}