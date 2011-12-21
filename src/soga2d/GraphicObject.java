/*
 * Soga2D
 *
 * Copyright 2011 Matúš Sulír.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package soga2d;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import soga2d.events.KeyPressListener;
import soga2d.events.MouseClickListener;

/**
 * The graphic object is a rectangular item located on a graphic board, e.g. an
 * image or a text.
 * @author Matúš Sulír
 */
public abstract class GraphicObject {
    protected int width;
    protected int height;
    protected int x;
    protected int y;
    private GraphicBoard board;
    private MouseClickListener mouseClickListener;
    private KeyPressListener keyPressListener;
    
    /**
     * Constructs an object with x = 0, y = 0, width = 32 and height = 32.
     */
    public GraphicObject() {
        this(0, 0, 32, 32);
    }
    
    /**
     * Contructs an object with a custom position and size.
     * @param x the x coordinate (the leftmost is 0)
     * @param y the y coordinate (the topmost is 0)
     * @param width the width
     * @param height the height
     */
    public GraphicObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    /**
     * Called when the graphic board is being repainted.
     * 
     * The concrete object should paint its contents in this method. 
     * @param g the Graphics2D object representing the area where the object
     * is located. The coordinates x = 0 and y = 0 represent the top-left point
     * of the object.
     */
    public abstract void paint(Graphics2D g);

    /**
     * Assigns the object to the concrete board.
     * 
     * This causes the object to be drawn on the board whenever needed.
     * @param board 
     */
    void assignBoard(GraphicBoard board) {
        this.board = board;
        update();
    }
    
    /**
     * Returns the current object's width.
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the current object's height.
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the size of the rectangular area representing the object.
     * @param width the new width
     * @param height the new height
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
        update();
    }
    
    /**
     * Returns an x coordinate of the object located on a graphic board.
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns an y coordinate of the object located on a graphic board.
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Moves the object to a new location on its associated board.
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
        update();
    }
    
    /**
     * Moves an object on its associated board.
     * @param deltaX the x coordinate change (can be either negative or positive)
     * @param deltaY the y coordinate change (can be either negative or positive)
     */
    public void moveBy(int deltaX, int deltaY) {
        x += deltaX;
        y += deltaY;
        update();
    }
    
    /**
     * Moves this object in front of an another object along the Z-axis.
     * @param what the object which will be behind the first one (this objet will not be moved)
     */
    public void moveInFrontOf(GraphicObject what) {
        if (board != null)
            board.moveInFrontOf(this, what);
        
        update();
    }
    
    /**
     * Moves this object behind all other objects along the Z-axis.
     */
    public void sendToBackground() {
        if (board != null)
            board.sendToBackground(this);
        
        update();
    }
    
    /**
     * Moves this object in front of all other objecs along the Z-axis.
     */
    public void bringToForeground() {
        if (board != null)
            board.bringToForeground(this);
       
        update();
    }
    
    /**
     * Registers a mouse click listener (can be only one).
     * @param listener the object which will react on mouse-clicks
     */
    public void setMouseClickListener(MouseClickListener listener) {
        mouseClickListener = listener;
    }
    
    /**
     * Registers a key press listener (can be only one).
     * @param listener the object which will react on key-presses
     */
    public void setKeyPressListener(KeyPressListener listener) {
        keyPressListener = listener;
    }
    
    /**
     * Called when the user clicked on an area where this component is located.
     */
    void mouseClicked() {
        if (mouseClickListener != null)
            mouseClickListener.onClick();
    }
    
    /**
     * Called when the user pressed a key while the component bound to the
     * board had focus.
     * @param event the key event object
     */
    void keyPressed(KeyEvent event) {
        if (keyPressListener != null)
            keyPressListener.onKeyPress(event);
    }
    
    /**
     * Repaints this object on a board and other (or all) objects if necessary
     * (depends on an implementation).
     */
    private void update() {
        if (board != null)
            board.repaintAll();
    }
}
