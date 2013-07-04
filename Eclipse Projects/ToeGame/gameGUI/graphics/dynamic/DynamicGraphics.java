package gameGUI.graphics.dynamic;

import gameGUI.graphics.GameGraphics;
import java.awt.*;

public class DynamicGraphics implements GameGraphics {

    private Graphics g;

    public DynamicGraphics(Graphics g) {
        this.g = g;
    }

    public void drawXX(Point endOfSector) {
        DynamicObject x = new DynamicX(g, endOfSector);
        x.draw();
    }

    public void drawOO(Point endOfSector) {
        DynamicObject o = new DynamicO(g, endOfSector);
        o.draw();
    }

}
