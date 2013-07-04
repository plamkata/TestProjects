package gameGUI.graphics;

import java.awt.*;

public class BlindGraphics implements GameGraphics {

    private Graphics g;
    private boolean isXVisible;

    public BlindGraphics(Graphics g) {
        this.g = g;
        isXVisible = true;
    }

    public void drawXX(Point endOfSector) {
        if (isXVisible) {
            g.setColor(Color.RED);
            int x = endOfSector.x / 4;
            int y = endOfSector.y / 4;
            int wide = endOfSector.x / 2;
            int height = endOfSector.y / 2;
            g.drawLine(x, y, x + wide, y + height);
            g.drawLine(x, y + height, x + wide, y);
        }
    }

    public void drawOO(Point endOfSector) {
        if (!isXVisible) {
            g.setColor(Color.BLUE);
            int x = endOfSector.x / 4;
            int y = endOfSector.y / 4;
            int wide = endOfSector.x / 2;
            int height = endOfSector.y / 2;
            g.drawOval(x, y, x + wide/2, y + height/2);
        }
    }

    public void setXVisible(boolean visible) {
        isXVisible = visible;
    }
}