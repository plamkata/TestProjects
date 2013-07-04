package gameGUI.graphics.dynamic;

import java.awt.*;

class DynamicX implements DynamicObject {

    private int currentTick = 0;

    private Graphics g;
    private int width;
    private int height;

    private static final int angleIncrement = 360/totalTicks;
    private int firstAngle;

    public DynamicX(Graphics g, Point endOfSector) {
        this.g = g;
        width = endOfSector.x;
        height = endOfSector.y;
        firstAngle = 45;
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
        g.fillRect(1, 1, width - 1, height - 1);

        g.setColor(Color.RED);
        g.fillArc(
                width/5, height/5,
                (2*width)/3, (2*height)/3,
                firstAngle, angleIncrement
        );
        int tmpAngle = (firstAngle < 180) ? firstAngle + 180 : firstAngle - 180;
        g.fillArc(
                width/5, height/5,
                (2*width)/3, (2*height)/3,
                tmpAngle, angleIncrement
        );
        tmpAngle = (firstAngle < 270) ? firstAngle + 90 : firstAngle - 270;
        g.fillArc(
                width/5, height/5,
                (2*width)/3, (2*height)/3,
                tmpAngle, angleIncrement
        );
        tmpAngle = (firstAngle < 90) ? firstAngle + 270 : firstAngle - 90;
        g.fillArc(
                width/5, height/5,
                (2*width)/3, (2*height)/3,
                tmpAngle, angleIncrement
        );

        firstAngle += angleIncrement;
        if (firstAngle > 360) firstAngle -= 360;
    }

    private void setCurrentTick(int tick) {
        currentTick = tick;
        repaint();
    }

    private void repaint() {
        g.clearRect(1, 1, width - 1, height - 1);
        paint();
    }

}
