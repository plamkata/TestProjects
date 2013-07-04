/**
 * Created by IntelliJ IDEA.
 * User: plamen
 * Date: 2005-5-17
 * Time: 13:59:50
 * To change this template use File | Settings | File Templates.
 */
package gameGUI.battleField;

import constantsAndTypes.StateOfSector;
import constantsAndTypes.Weapon;
import constantsAndTypes.Skin;

import java.awt.*;
import java.awt.event.*;

import gameGUI.graphics.*;
import gameGUI.graphics.dynamic.DynamicGraphics;
import gameGUI.graphics.pen.PenGraphics;

class Sector extends Canvas {
	private static final long serialVersionUID = 7401150075121300914L;
	
	Skin skin;
    private StateOfSector state;
    /**
     * We need to differentiate the Sectors of the BattleField.
     * id = 3*i + j, where (i, j) are the coordinates of the Sector
     * (<code>this</code>) in the BattleField.
     */
    private int id;
    // we need to know what is the user weapon
    private Weapon userWeapon;

    Sector(int id, Skin skin) {
        this.skin = skin;
        this.state = StateOfSector.BLANK;
        this.id = id;
        this.userWeapon = null;
        this.addMouseListener(new MouseL());
    }

    public void paint(Graphics g) {
        // sector's beginning point and ending point
        Point beggin = new Point(0, 0);
        Point end = new Point(getSize().width - 1, getSize().height - 1);
        g.drawRect(beggin.x, beggin.y, end.x, end.y);
        g.setColor(Color.WHITE);
        g.fillRect(beggin.x, beggin.y, end.x, end.y);

        GameGraphics gg;
        if (skin == Skin.SIMPLE) {
            gg = new SimpleGraphics(g);
        } else if (skin == Skin.BLIND) {
            gg = new BlindGraphics(g);
            if (userWeapon == Weapon.XX) {
                ((BlindGraphics)gg).setXVisible(true);
            } else {
                ((BlindGraphics)gg).setXVisible(false);                    
            }
        } else if (skin == Skin.PEN) {
            gg = new PenGraphics(g);
        } else {
            gg = new DynamicGraphics(g);
        }

        if (state == StateOfSector.XX) {
            gg.drawXX(end);
        } else if (state == StateOfSector.OO) {
            gg.drawOO(end);
        }
    }

    public int getId() {
        return id;
    }

    public void setUserWeapon(Weapon weapon) {
        userWeapon = weapon;
    }

    // computer shoots with fireWith()
    public void fireWith(Weapon yourWeapon) {
        if (state != StateOfSector.BLANK) return;
        if (yourWeapon == Weapon.OO) {
            state = StateOfSector.OO;
        } else {
            state = StateOfSector.XX;
        }
        // make it unavaiable for the user
        removeListeners();
        // call repaint() to display your shot
        repaint();
    }

    public void removeListeners() {
        MouseListener[] listeners = this.getMouseListeners();
        for (int i = 0; i < listeners.length; i++) {
            this.removeMouseListener(listeners[i]);
        }
    }

    public void refresh() {
        state = StateOfSector.BLANK;
        repaint();
        this.addMouseListener(new MouseL());
    }

    private class MouseL extends MouseAdapter {

        // to be performed only the first time it occurs
        public void mousePressed(MouseEvent e) {
            // if this MouseAction occurs this means
            // that the user have choosen this sector.
            // We need to know what is the weapon of the user.
            if (userWeapon == Weapon.OO) {
                state = StateOfSector.OO;
            } else {
                state = StateOfSector.XX;
            }
            // visualize with repaint
            repaint();
            // remove the current MouseListener,
            // when the MouseEvent is performed.
            ((Sector)e.getSource()).removeMouseListener(this);
        }
    }

}
