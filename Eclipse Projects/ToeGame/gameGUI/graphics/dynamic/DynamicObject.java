package gameGUI.graphics.dynamic;

interface DynamicObject {

    int totalTicks = 60;
    long timeOut = 8; // in millis

    void draw();
}
