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
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import soga2d.events.KeyListener;
import soga2d.events.MouseClickListener;

/**
 * The graphic object is a rectangular item located on a graphic board, e.g. an
 * image or a text.
 * 
 * In subclasses of this class, do this when modifying the size, position or
 * content:
 * <ul>
 * <li>Call the <code>beforeChange() method.</li>
 * <li>Do the desired changes. For example, set the <code>x</code>,
 * <code>y</code> and <code>image</code> properties, draw the 
 * content into the graphics obtained by the <code>image.createGraphics()</code>
 * method, etc.</li>
 * <li>Call the <code>afterChange()</code> method.</li>
 * </ul>
 * <em>Note:</em> Calling <code>beforeChange()</code> and
 * <code>afterChange()</code> in a constructor is not necessary.
 * @author Matúš Sulír
 */
public abstract class GraphicObject { 
    /**
     * The x position on the board.
     */
    protected int x;
    
    /**
     * The y position on the board.
     */
    protected int y;
    
    /**
     * The internal bitmap image without any transformations applied.
     * 
     * Contains also the width and height of this graphical object.
     */
    protected BufferedImage image;
    
    private BufferedImage transformedImage;
    private Rectangle oldRectangle = new Rectangle();
    private GraphicBoard board;
    private MouseClickListener mouseClickListener;
    private KeyListener keyListener;
    private List<Detector> detectors = new ArrayList<Detector>();
    private int angle = 0;
    private List<GraphicObject> subobjects = new ArrayList<GraphicObject>();
    private boolean dragDropEnabled = false;
    
    /**
     * Constructs an object with x = 0, y = 0, width = 1 and height = 1.
     */
    protected GraphicObject() {
        this(0, 0);
    }
    
    /**
     * Constructs an object with width = 1 and height = 1.
     * @param x the x coordinate (the leftmost is 0)
     * @param y the y coordinate (the topmost is 0)
     */
    protected GraphicObject(int x, int y) {
        this(x, y, 1, 1);
    }
    
    /**
     * Contructs an object with a custom position and size.
     * @param x the x coordinate (the leftmost is 0)
     * @param y the y coordinate (the topmost is 0)
     * @param width the width
     * @param height the height
     */
    protected GraphicObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        
        createImage(width, height);
    }
    
    /**
     * Adds a subobject to this object.
     * 
     * Subobjects are contained in the parent objects and cannot exceed its
     * boundaries. If the parent moves, the subobject also moves. Other
     * behavior is not yet defined.
     * @param object the subobject to add
     */
    public void addSubobject(GraphicObject object) {
        subobjects.add(object);
        repaint();
    }
    
    /**
     * Removes the subobject of this object.
     * @param object the subobject to remove
     */
    public void removeSubObject(GraphicObject object) {
        subobjects.remove(object);
        repaint();
    }
    
    /**
     * Returns the current object's width.
     * @return the width
     */
    public int getWidth() {
        return image.getWidth();
    }

    /**
     * Returns the current object's height.
     * @return the height
     */
    public int getHeight() {
        return image.getHeight();
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
     * Returns a <code>Rectangle</code> representaion of the object's position
     * and size.
     * @return the rectangle
     */
    public Rectangle getRectangle() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
    
    /**
     * Moves the object to a new location on its associated board.
     * @param x the new x coordinate
     * @param y the new y coordinate
     */
    public void moveTo(int x, int y) {
        saveOldArea();
        
        this.x = x;
        this.y = y;
        
        repaint();
        notifyDetectors();
    }
    
    /**
     * Moves an object on its associated board.
     * @param deltaX the x coordinate change (can be either negative or positive)
     * @param deltaY the y coordinate change (can be either negative or positive)
     */
    public void moveBy(int deltaX, int deltaY) {
        moveTo(x + deltaX, y + deltaY);
    }
    
    /**
     * Sets the new rotation angle for this object.
     * @param angle the angle in degrees
     * @see #rotate(int)
     */
    public void setAngle(int angle) {
        this.angle = angle;
        
        applyTransformations();
        repaint();
        notifyDetectors();
    }
    
    /**
     * Rotates the object clockwise around its center.
     * 
     * Non-square objects may be cropped.
     * @param angle the angle in degrees
     */
    public void rotate(int angle) {
        setAngle(this.angle + angle);
    }
    
    /**
     * Moves this object in front of an another object along the Z-axis.
     * @param what the object which will be behind the first one (this objet will not be moved)
     */
    public void moveInFrontOf(GraphicObject what) {
        if (board != null)
            board.moveInFrontOf(this, what);
        
        repaint();
    }
    
    /**
     * Moves this object behind all other objects along the Z-axis.
     */
    public void sendToBackground() {
        if (board != null)
            board.sendToBackground(this);
        
        repaint();
    }
    
    /**
     * Moves this object in front of all other objecs along the Z-axis.
     */
    public void bringToForeground() {
        if (board != null)
            board.bringToForeground(this);
       
        repaint();
    }
    
    /**
     * Registers a mouse click listener (can be only one).
     * @param listener the object which will react on mouse-clicks
     */
    public void setMouseClickListener(MouseClickListener listener) {
        mouseClickListener = listener;
    }
    
    /**
     * Registers a key event listener (can be only one).
     * @param listener the object which will react on key events
     */
    public void setKeyListener(KeyListener listener) {
        keyListener = listener;
    }
    
    /**
     * Finds out whether this objects graphically colides with the specified object.
     * @param object the second object
     * @return true if the objects collide, false otherwise
     * @see CollisionDetector
     */
    public boolean collidesWith(GraphicObject object) {
        return new CollisionDetector(this, object).objectsCollide();
    }
    
    /**
     * Finds out whether this graphic object can be automatically dragged and
     * dropped by a user.
     * @return true if the object has automatic drag&drop enabled, false otherwise
     * @see #enableDragDrop()
     */
    public boolean isDragDropEnabled() {
        return dragDropEnabled;
    }
    
    /**
     * Enables the automatic drag&drop functionality.
     * 
     * From now the user will be able to drag and drop this object by mouse
     * if it is not overlapped by other objects.
     */
    public void enableDragDrop() {
        dragDropEnabled = true;
    }
    
    /**
     * Disables the automatic drag&drop functionality.
     * @see #enableDragDrop()
     */
    public void disableDragDrop() {
        dragDropEnabled = false;
    }
    
    /**
     * Returns the current content of the graphic object as an image.
     * @return the image representation
     */
    public BufferedImage getImage() {
        if (subobjects.isEmpty()) {
            return transformedImage;
        } else {
            BufferedImage resultImage = new BufferedImage(getWidth(), getHeight(), image.getType());
            Graphics2D g = resultImage.createGraphics();
            g.drawImage(image, null, 0, 0);
            
            for (GraphicObject object : subobjects)
                g.drawImage(object.getImage(), null, object.getX(), object.getY());
            
            return resultImage;
        }
    }

    /**
     * Assigns the object to the concrete board.
     * 
     * This causes the object to be drawn on the board whenever needed.
     * @param board 
     */
    void assignBoard(GraphicBoard board) {
        if (board == null) {
            this.board.repaintArea(getRectangle());
            this.board = null;
        } else {
            this.board = board;
            afterChange();
        }
    }
    
    /**
     * Initializes the internal image to an empty one (containing only
     * transparent pixels).
     * @param width the image width
     * @param height the image height
     */
    protected final void createImage(int width, int height) {
        transformedImage = image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }
    
    /**
     * Adds a detector to notify when the object changes (e.g. moves).
     * @param detector the detector (collision, proximity, ...)
     */
    void addDetector(Detector detector) {
        detectors.add(detector);
    }
    
    /**
     * Nofifies all associated detectors after the object changes.
     */
    private void notifyDetectors() {
        for (Detector detector : detectors)
            detector.objectChanged();
    }
    
    /**
     * Called when the user clicked on an area where this component is located.
     */
    void mouseClicked() {
        if (mouseClickListener != null)
            mouseClickListener.onClick();
    }
    
    /**
     * Called when the user fired a key event while the component bound to the
     * board had focus.
     * @param event the key event object
     */
    void keyEvent(KeyEvent event) {
        if (keyListener != null)
            keyListener.onKeyEvent(event);
    }
    
    /**
     * Call this method in a subclass before any change.
     */
    protected void beforeChange() {
        saveOldArea();
    }
    
    /**
     * Call this method in a subclass after any change.
     */
    protected void afterChange() {
        applyTransformations();
        repaint();
        notifyDetectors();
    }
    
    /**
     * Applies the currently selected transformations (e.g. rotation) to this image.
     */
    private void applyTransformations() {
        if (angle == 0) {
            transformedImage = image;
        } else {
            transformedImage = new BufferedImage(getWidth(), getHeight(), image.getType());
            Graphics2D g = transformedImage.createGraphics();
            g.rotate(Math.toRadians(angle), getWidth() / 2, getHeight() / 2);
            g.drawImage(image, null, 0, 0);
        }
    }
    
    /**
     * Repaints this object on a board and other (or all) objects if necessary
     * (depends on an implementation).
     */
    private void repaint() {
        if (board != null)
            board.repaintArea(oldRectangle.union(getRectangle()));
    }
    
    /**
     * Saves the old position and size.
     * 
     * This is used to repaint both the original and new area after the object
     * was moved.
     */
    private void saveOldArea() {
        if (board != null)
            oldRectangle = getRectangle();
    }
}
