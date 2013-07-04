package gameGUI.graphics.dynamic;

import java.awt.*;

class DynamicO implements DynamicObject {

    private int currentTick = 0;

    private Graphics g;
    private int width;
    private int height;

    private final int diameterIncrement = 2;
    private int firstDiameter;
    private int secondDiameter;

    public DynamicO(Graphics g, Point endOfSector) {
        this.g = g;
        width = endOfSector.x;
        height = endOfSector.y;
        firstDiameter = (2*width)/3 - diameterIncrement*totalTicks;
        secondDiameter = firstDiameter + diameterIncrement;
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

        g.setColor(Color.BLUE);
        g.drawOval(
                (width - secondDiameter)/2, (height - secondDiameter)/2,
                secondDiameter, secondDiameter
        );
        g.fillOval(
                 (width - secondDiameter)/2, (height - secondDiameter)/2,
                secondDiameter, secondDiameter
        );

        g.setColor(Color.WHITE);
        g.drawOval(
                (width - firstDiameter)/2, (height - firstDiameter)/2,
                firstDiameter, firstDiameter
        );
        g.fillOval(
                (width - firstDiameter)/2, (height - firstDiameter)/2,
                firstDiameter, firstDiameter
        );

        firstDiameter += diameterIncrement;
        secondDiameter += diameterIncrement;
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
