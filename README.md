Soga2D - Simple Object-oriented 2D Graphics Abstraction
=======================================================

Soga2D is a small Java library providing more object-oriented abstraction over standard Graphics2D class. It is suitable for applications where standard GUI items do not suffice and OpenGL is overkill. For instance, a diagramming application or a simple 2D game.

Motivation
----------
The majority of 2D graphics libraries, including Java 2D, offer an interface similar to this: You are given a method (e.g. paintComponent()) with an argument representing the area, called every time the area is going to be redrawn. You can draw pixels, primitives, etc. by calling member methods of this argument, like g.drawImage(), g.drawRectangle().

This is not a really object-oriented solution and it brings some problems. If you want to move the rectangle afterwards, during the next redraw you usually clear the whole area and draw it again at a new position. Alternatively, you manually detect and invalidate only the subarea which needs to be updated. To find out which rectangle the user clicked, you must store the coordinates and sizes somewhere and then manually compute whether the event coordinates belong to one of the rectangles.

Why not to create a reusable abstraction over these and similar tasks?

Aim
---
Soga2D should work like this:

You are given a graphic board which you associate with JPanel, for example. Graphic objects (primitives, images) are put on the board. Each graphic object is really an object - it remembers its properties. You can call e.g. move() on the particular object and redrawing the needed area is accomplished automatically.

A graphic object can contain other objects. If the outer one moves or rotates, the inner ones follow these movements.

Each object has a modifiable Z-index (Z-order). Object located closer to the viewer is drawn after the further one.

If you click somewhere on the board, only the object located at this position will receive the message. Objects can also register to receive key events.

Additional features can be implemented as needed.

Description
-----------

### Graphic Board

A "graphic board" represent canvas where all graphical objects can be placed on. You are able to:

* add objects to the board
* remove object at any time
* add or remove multiple object at once (using one method call)
* modify their relative z-order: send them to the background, foreground or move in front of an another object
* replace an object by another one while preserving the z-order
* clear the whole board at once
* lock the board and thus disable repainting until unlock() is called – this can be used either for efficiency or better user experience
* register a key-press event listener for the board itself

### Graphic Component

A graphic component bounds a graphic board to a GUI (AWT, Swing,...) component. Currently there is only one implementaion – the graphic panel, which extends JPanel.

### Graphic Object

Probably the most important class is an abstract class GraphicObject, from which all graphical objects are inherited. It provides both a common interface and core functionality. You can:

* set and get the x and y coordinate, width and height
* move the object, either absolutely or relatively to the current location
* rotate it and set the rotation angle
* set a mouse click listener – the library automatically recognizes when a user clicks on a particular object and sends it a notification
* register a key-press listener for a particular object – it will be called only when the object is currently on the board
* enable drag&drop with only one simple method call
* find out whether this object collides with another using a pixel-perfect collision detection
* attach a “sub-object” to an object – when an outer object is e.g. moved, the inner object is also moved

When you change anything, you do not need to worry about repainting and possible interaction with other objects – this is accomplished automatically. In addition, only areas which need to be repainted are redrawn.

Graphic objects also feature per-object double buffering – their content is saved as a bitmap image in memory, so when the object does not change between two repaints, it is only bit-blitted to the screen.

### Detectors

Soga2D uses an interesting concept of a “detector”. It monitors one or more graphical objects (in fact, the objects themselves send notifications to detectors) for any relevant changes and sends a notification to the client when a particular event occurs. At this moment, two detectors are implemented:

* collision detector with transparent pixel support
* proximity detector – can detect when the distance between two graphic object becomes less then specified

### Graphic Object Examples

Only a few graphical objects are currently implemented, but they are generally useful.

* Animation
  * an animation made of multiple images
  * can be started and stopped
  * can contain a static image to be shown when it is stopped
* Picture
  * a simple, static picture
  * the graphic object size is set automatically according to the image size
* Rectangle
  * can have a different outline an fill color
* Text
  * the size adjusts automatically, following the content change
  * supports multiple-line strings and thus overcomes the Java 2D API limitation
* Texture
  * an image repeated multiple times through the canvas