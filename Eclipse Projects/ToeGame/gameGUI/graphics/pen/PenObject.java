package gameGUI.graphics.pen;

interface PenObject {

    int totalTicks = 60; // less than 65
    long timeOut = 8; // in millis

    void draw();

}
