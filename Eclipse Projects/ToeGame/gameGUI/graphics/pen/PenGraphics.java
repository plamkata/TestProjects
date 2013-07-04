package gameGUI.graphics.pen;

import gameGUI.graphics.GameGraphics;

import java.awt.*;

public class PenGraphics implements GameGraphics {

    private Graphics g;

    public PenGraphics(Graphics g) {
        this.g = g;
    }

    public void drawXX(Point endOfSector) {
        PenObject x = new PenX(g, endOfSector);
        x.draw();
    }

    public void drawOO(Point endOfSector) {
        PenObject o = new PenO(g, endOfSector);
        o.draw();
    }

}
