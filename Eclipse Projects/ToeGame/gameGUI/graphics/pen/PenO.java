package gameGUI.graphics.pen;

import java.awt.*;

class PenO implements PenObject {

    private float percentDone = 0;
    private int currentTick = 0;

    private Graphics g;
    private int width;
    private int height;

    public PenO(Graphics g, Point endOfSector) {
        this.g = g;
        width = endOfSector.x;
        height = endOfSector.y;
    }

    public void draw() {
        int k = currentTick;
        while (k++ < totalTicks) {
            setCurrentTick(k);
            try {
                Thread.sleep(timeOut);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void paint() {
        g.setColor(Color.WHITE);
        g.fillRect(width/4, height/4, (3*width)/4, (3*height)/4);
        g.setColor(Color.BLUE);

        int start_angle = 90;
        int done_angle = (int) (percentDone * 360);
        g.drawArc(
                width/4, width/4,
                width/2, height/2,
                start_angle, done_angle
        );
    }

    private void repaint() {
        g.clearRect(width/4, height/4, (3*width)/4, (3*height)/4);
        paint();
    }

    private void setCurrentTick(int tick) {
        currentTick = tick;

        if (currentTick > totalTicks) {
            percentDone = 1;
        } else if (currentTick == 0) {
            percentDone = 0;
        } else {
            percentDone = (float) currentTick / (float) totalTicks;
        }

        repaint();
    }

}
