import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 * Author: Shane Birdsall
 * ID: 14870204
 * Date: 27/04/2017.
 * This class combines all of the Submarines components and adds them together using a tree structure
 */
public class Submarine implements Drawable {

    private SubmarineComponent root;
    private double radius, height;

    Submarine(float size) {
        radius = size * 0.4;
        height = size * 0.25;

        root = new SubmarineBody(radius, height, RotationAxis.X);

        SubmarineSail sail = new SubmarineSail(radius, height, RotationAxis.X);
        sail.setTranslations(0, (9*height)/10, 0);
        sail.setRotation(-90);

        SubmarinePeriscope periscope = new SubmarinePeriscope(radius, height, RotationAxis.X);
        periscope.setTranslations(0, 0, height/3);
        sail.setRotation(-90);

        SubmarinePeriscope periscopePart = new SubmarinePeriscope(radius, height, RotationAxis.Y);
        periscopePart.setTranslations(radius/15,0, height/2);
        periscopePart.setRotation(-90);
        periscope.addChild(periscopePart);
        sail.addChild(periscope);

        root.addChild(sail);

        SubmarineConnector connector = new SubmarineConnector(radius, height, RotationAxis.Y);
        connector.setRotation(90);
        connector.setTranslations((9*radius)/10, 0, 0);

        SubmarinePropeller propeller1 = new SubmarinePropeller(radius, height, RotationAxis.Y);
        propeller1.setTranslations(0, 0, radius/4);
        connector.addChild(propeller1);

        SubmarinePropeller propeller2 = new SubmarinePropeller(radius, height, RotationAxis.Z);
        propeller2.setTranslations(0, 0, radius/4);
        propeller2.setRotation(90);
        connector.addChild(propeller2);

        root.addChild(connector);
    }

    @Override
    public void draw(GL2 gl2, GLU glu, GLUquadric quadric) {
        root.draw(gl2, glu, quadric);
    }

    private class SubmarineBody extends SubmarineComponent {

        SubmarineBody(double radius, double height, RotationAxis axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode (GL2 gl2, GLU glu, GLUquadric quadric){
            gl2.glPushMatrix();
            // Set component colour
            gl2.glColor3f(1,0.2f,0);

            // Set the size of the component (X, Y, Z)
            gl2.glScaled(radius, height, height);

            // Draw the component
            glu.gluSphere(quadric,1,25,20);
            gl2.glPopMatrix();
        }
    }

    private class SubmarineConnector extends SubmarineComponent {

        private GLUT glut;

        SubmarineConnector(double radius, double height, RotationAxis axis) {
            super(radius, height, axis);
            glut = new GLUT();
        }

        @Override
        void drawNode(GL2 gl2, GLU glu, GLUquadric quadric) {
            gl2.glPushMatrix();
            // Set component colour
            gl2.glColor3f(1,0.05f,0);

            // Set the size of the component (X, Y, Z)
            gl2.glScaled(height/5, height/5, radius/4);

            // Draw the component
            glu.gluCylinder(quadric, 1, 1, 1, 5, 5);

            gl2.glTranslated(0, 0, height*3);

            gl2.glColor3f(1,0.25f,0);
            glut.glutSolidCone(1.5f,0.75f,5,5);
            gl2.glPopMatrix();
        }
    }

    private class SubmarinePeriscope extends SubmarineComponent {

        SubmarinePeriscope(double radius, double height, RotationAxis axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode (GL2 gl2, GLU glu, GLUquadric quadric){
            gl2.glPushMatrix();
            // Set component colour
            gl2.glColor3f(1,0.25f,0);

            // Set the size of the component (X, Y, Z)
            gl2.glScaled(radius/15, radius/15, height/2);

            // Draw the component
            glu.gluCylinder(quadric, 1, 1, 1,8, 6);
            gl2.glPopMatrix();
        }
    }

    private class SubmarinePropeller extends SubmarineComponent {

        SubmarinePropeller(double radius, double height, RotationAxis axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode(GL2 gl2, GLU glu, GLUquadric quadric) {
            gl2.glPushMatrix();
            // Set component colour
            gl2.glColor3f(1,0.25f,0);

            // Rotate
            gl2.glRotated(90, 1, 0, 0);

            // Set the size of the component (X, Y, Z)
            gl2.glScaled(radius/5, radius/8, 1);

            // Draw the component
            glu.gluSphere(quadric, radius, 15, 15);

            gl2.glPopMatrix();
        }
    }

    private class SubmarineSail extends SubmarineComponent {

        SubmarineSail(double radius, double height, RotationAxis axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode (GL2 gl2, GLU glu, GLUquadric quadric){
            gl2.glPushMatrix();
            // Set component colour
            gl2.glColor3f(1,0.2f,0);

            // Set the size of the component (X, Y, Z)
            gl2.glScaled(radius, height, height);

            // Draw the component
            glu.gluCylinder(quadric, radius, radius/1.5f, height*1.5,4, 4);
            gl2.glPopMatrix();
        }
    }
}
