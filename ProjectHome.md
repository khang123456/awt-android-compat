# No Longer Active.  Too Many Issues #
## I won't respond to emails regarding this ##

# Status #
Unless it's not clear from the dates in svn, I'm not really actively pursuing this anymore.  Too many issues.

This project is an attempt to facilitate AWT rendering on the Android platform.

# Goal #
To create a compatibility layer between applications which use AWT rendering and the Android API.  This is to relieve _some_ coding, as opposed to being a drop-in-and-run compatibility library.

# Source #
Much of the AWT source comes from the OpenJDK project.  Currently, the java.awt.geom is working.  At certain points in the code, Android takes over and it becomes native API.

# Design #
The interface between the Android and java.awt.geom is what I'm attempting to navigate.  I see a few options:
  1. I could write software renderers and possibly an entire pipeline and simply rasterize with Android calls.
  1. I could, inside Graphics2D calls, convert java.awt.Shape to android.graphics.drawable.shapes.Shape (as an example).
  1. .....

# Issues #
This library **cannot** be a simple drop-in for the missing AWT packages.  There are several issues.  These may be ported later.
  * the AWT Component hierarchy would make no sense on such a small screen - not porting
  * the AWT Event hierarchy is made for a windowed system, making translation difficult - not porting
  * the package names could not be java.awt, Android wants _--core-library_ used, hence any using projects would still have to update `import` statements.  This point is also important because I think this means that Google has some of this code, the classes are just not exposed for general consumption.

# Ported #
  * java.awt.geom

# Main #
AWTView and Graphics2D are the core of the compatibility.  AWTView mimics a Component.  Is has a `paint(Graphics2D g2)` which is abstract.  The `onDraw(Canvas)` creates the Graphics2D object and passes on to `paint`.

Currently, Sun's Java2D trail is being used as a point of reference for this library.  I'm working on making slight code modifications (`import`s, `extend`s) and running the examples within the Android emulator.  The similarity of the results are a rough metric of how well the port is going.

Soon, I will start posting code samples of what the ported code looks like against the original.  **The library is quite a while from being even an alpha release**.  The source is not kept in Google Code's svn repository as of now.

Soon became half a year.  I'm hoping to ramp back up on this project and get it to a good point to release at least **something**!  Right now I need to get this project building again using the newer Eclipse plugin and Android 1.5.  We'll see how long it takes to get to the next step.