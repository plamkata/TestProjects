package gameGUI.graphics.pen;

import java.awt.*;

class PenX implements PenObject {

    private int currentTick = 0;

    private Graphics g;
    private int width;
    private int height;

    private Point midFirst;
    private Point midSecond;
    private int widthIncrement;
    private int heightIncrement;

    public PenX(Graphics g, Point endOfSector) {
        this.g = g;
        width = endOfSector.x;
        height = endOfSector.y;
        midFirst = new Point(width/4, height/4);
        midSecond = new Point((3*width)/4, height/4);
        widthIncrement = (width)/totalTicks;
        heightIncrement = (height)/totalTicks;
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
        g.fillRect(width/4, height/4, (3*width)/4, (3*height/4));
        g.setColor(Color.RED);

        if (currentTick <= totalTicks/2) {
            midFirst = new Point(
                    midFirst.x + widthIncrement,
                    midFirst.y + heightIncrement
            );
            g.drawLine(
                    width/4, height/4,
                    midFirst.x,  midFirst.y
            );
        } else {
            g.drawLine(
                    width/4, height/4,
                    midFirst.x,  midFirst.y
            );
            midSecond = new Point(
                    midSecond.x - widthIncrement,
                    midSecond.y + heightIncrement
            );
            g.drawLine(
                    (3*width)/4, height/4,
                    midSecond.x,  midSecond.y
            );
        }
    }

    private void setCurrentTick(int tick) {
        currentTick = tick;
        repaint();
    }

    private void repaint() {
        g.clearRect(width/4, height/4, (3*width)/4, (3*height)/4);
        paint();
    }

}
