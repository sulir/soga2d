Soga2D - Simple Object-oriented 2D Graphics Abstraction
=======================================================

Soga2D is a small Java library providing more object-oriented abstraction over standard Graphics2D class. It is suitable for applications where standard GUI items do not suffice and OpenGL is overkill. For instance, a diagramming application or a simple 2D game.

Motivation
----------
The majority of 2D graphics libraries offer an interface similar to this: You are given a method (e.g. `onDraw`) with an argument representing the area, called every time the area is going to be redrawn. You can draw pixels, primitives, etc. by calling member methods of this argument, like this.

If you want to move the rectangle afterwards, during the next redraw you usually clear the whole area and draw it again at a new position. Alternatively, you manually detect and invalidate only the subarea which needs to be updated.

To find out which rectangle the user clicked, you must store the coordinates and sizes somewhere and then manually compute whether the event coordinates belong to one of the rectangles.

This offers a great deal of flexibility. But why not to create a reusable abstraction over these and similar tasks?

Aim
---
Soga2D should work like this:

You are given a graphic board which you associate with JPanel, for example. Graphic objects (primitives, images) are put on the board. Each graphic object is really an object - it remembers its properties. You can call e.g. move() on the particular object and redrawing the needed area is accomplished automatically.

A graphic object can contain other objects. If the outer one moves or rotates, the inner ones follow these movements.

Each object has a modifiable Z-index (Z-order). Object located closer to the viewer is drawn after the further one.

If you click somewhere on the board, only the object located at this position will receive the message. Objects can also register to receive key events.

Additional features will be implemented as needed.