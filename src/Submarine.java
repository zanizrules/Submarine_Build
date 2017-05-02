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

    Submarine(float size) {
        double radius = size * 0.4;
        double height = size * 0.25;

        root = new SubmarineBody(radius, height, ROTATION_AXIS.X);

        // Sail -> Child of root/body
        SubmarineSail sail = new SubmarineSail(radius, height, ROTATION_AXIS.X);
        sail.setTranslations(0, (7.5* height)/10, 0);
        sail.setRotation(-90);

        // Periscope -> Child of Sail
        SubmarinePeriscope periscope = new SubmarinePeriscope(radius, height, ROTATION_AXIS.X);
        periscope.setTranslations(0, 0, (2.5* height)/10);
        SubmarinePeriscope periscopeExtension = new SubmarinePeriscope(radius, height, ROTATION_AXIS.X);
        periscopeExtension.setTranslations(0,0, height/3);
        periscope.addChild(periscopeExtension);
        SubmarinePeriscope periscopePart = new SubmarinePeriscope(radius, height, ROTATION_AXIS.Y);
        periscopePart.setTranslations(radius /15,0, height /2);
        periscopePart.setRotation(-90);
        periscopeExtension.addChild(periscopePart);
        sail.addChild(periscope);

        root.addChild(sail);

        // Propeller -> Propeller is the child of a connector which is the child of root
        SubmarineConnector connector = new SubmarineConnector(radius, height, ROTATION_AXIS.Y);
        connector.setRotation(90);
        connector.setTranslations((9* radius)/10, 0, 0);
        SubmarinePropeller propeller1 = new SubmarinePropeller(radius, height, ROTATION_AXIS.Y);
        propeller1.setTranslations(0, 0, radius /4);
        connector.addChild(propeller1);
        SubmarinePropeller propeller2 = new SubmarinePropeller(radius, height, ROTATION_AXIS.Z);
        propeller2.setTranslations(0, 0, radius /4);
        propeller2.setRotation(90);
        connector.addChild(propeller2);

        root.addChild(connector);
    }

    @Override
    public void draw(GL2 gl2, GLU glu, GLUquadric quadric, boolean filled) {
        root.draw(gl2, glu, quadric, filled);
    }

    private class SubmarineBody extends SubmarineComponent {

        SubmarineBody(double radius, double height, ROTATION_AXIS axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode (GL2 gl2, GLU glu, GLUquadric quadric, boolean filled){
            gl2.glPushMatrix();
                gl2.glColor3f(1,0.2f,0);
                gl2.glScaled(radius, height, height);
                glu.gluSphere(quadric,1,25,20);
            gl2.glPopMatrix();
        }
    }

    private class SubmarineConnector extends SubmarineComponent {

        private GLUT glut;

        SubmarineConnector(double radius, double height, ROTATION_AXIS axis) {
            super(radius, height, axis);
            glut = new GLUT();
        }

        @Override
        void drawNode(GL2 gl2, GLU glu, GLUquadric quadric, boolean filled) {
            gl2.glPushMatrix();
                // Draw Cylinder
                gl2.glColor3f(1,0.05f,0);
                gl2.glScaled(height/5, height/5, radius/4);
                glu.gluCylinder(quadric, 1, 1, 1, 5, 5);

                // Draw Cone
                gl2.glTranslated(0, 0, height*3); // Move
                gl2.glColor3f(1,0.25f,0);
                if(filled) {
                    glut.glutSolidCone(1.5f,0.75f,5,5);
                } else {
                    glut.glutWireCone(1.5f,0.75f,5,5);
                }
            gl2.glPopMatrix();
        }
    }

    private class SubmarinePeriscope extends SubmarineComponent {

        SubmarinePeriscope(double radius, double height, ROTATION_AXIS axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode (GL2 gl2, GLU glu, GLUquadric quadric, boolean filled){
            gl2.glPushMatrix();
                gl2.glColor3f(1,0.25f,0);
                gl2.glScaled(radius/15, radius/15, height/2);
                glu.gluCylinder(quadric, 1, 1, 1,8, 6);
            gl2.glPopMatrix();
        }
    }

    private class SubmarinePropeller extends SubmarineComponent {

        SubmarinePropeller(double radius, double height, ROTATION_AXIS axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode(GL2 gl2, GLU glu, GLUquadric quadric, boolean filled) {
            gl2.glPushMatrix();
                gl2.glColor3f(1,0.25f,0);
                gl2.glRotated(90, 1, 0, 0);
                gl2.glScaled(radius/5, radius/8, 1);
                glu.gluSphere(quadric, radius, 15, 15);
            gl2.glPopMatrix();
        }
    }

    private class SubmarineSail extends SubmarineComponent {

        SubmarineSail(double radius, double height, ROTATION_AXIS axis) {
            super(radius, height, axis);
        }

        @Override
        void drawNode (GL2 gl2, GLU glu, GLUquadric quadric, boolean filled) {
            gl2.glPushMatrix();
                gl2.glColor3f(1,0.2f,0);
                gl2.glScaled(radius, height, height);
                glu.gluCylinder(quadric, radius, radius/1.5f, height*2,4, 4);
            gl2.glPopMatrix();
        }
    }
}
