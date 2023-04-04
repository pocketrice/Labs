import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Renderer extends JFrame /* DEBUG STUFF -- PLEASE IGNORE! --> */ implements MouseMotionListener, MouseListener {

    Color iceberg = Color.decode("#a5ced5");
    Color paleWhite = Color.decode("#f3ede7");
    Color mahogany = Color.decode("#292220");


    // Polyline coords derived with help from http://www.cs.toronto.edu/~noam/c-generator.html
    int[] leftEyeX = {115, 115, 116, 116, 117, 118, 118, 119, 119, 119, 119, 119, 118, 117, 115, 114, 112, 111, 111, 111, 111, 111, 111, 111, 112, 113, 114, 115, 116, 117, 117, 117, 117, 117, 117, 117, 117, 116, 116, 116, 116, 116, 116, 116, 115, 115, 115, 114, 113, 113, 113, 113, 113, 113, 113, 113, 114, 114, 114};
    int[] rightEyeX = {190, 191, 191, 191, 192, 192, 193, 194, 194, 195, 195, 195, 195, 194, 193, 192, 191, 190, 189, 189, 189, 189, 189, 189, 189, 189, 190, 190, 191, 192, 193, 194, 194, 195, 195, 196, 196, 196, 196, 195, 193, 191, 189, 188, 187, 187, 187, 187, 187, 188, 189, 189, 190, 190, 191, 191, 191, 192, 192, 192, 193, 193, 193, 193, 193, 192, 192, 191, 191, 192, 193, 194, 195, 195, 195, 195, 195, 195, 195, 195, 195, 194, 194, 194, 194, 194, 194, 194};
    int[] leftEyeY = {146, 146, 146, 146, 146, 146, 146, 146, 146, 146, 147, 148, 149, 149, 150, 150, 150, 150, 150, 150, 150, 149, 149, 148, 148, 147, 147, 147, 147, 147, 147, 147, 147, 148, 150, 151, 152, 153, 153, 153, 153, 152, 151, 151, 150, 150, 150, 151, 151, 151, 151, 151, 151, 151, 151, 151, 150, 150, 150};
    int[] rightEyeY = {139, 139, 139, 139, 139, 139, 139, 139, 139, 139, 139, 139, 140, 140, 141, 141, 141, 141, 141, 141, 141, 140, 140, 140, 140, 139, 139, 139, 138, 138, 138, 138, 138, 138, 138, 138, 138, 138, 138, 139, 139, 140, 140, 140, 140, 140, 140, 139, 138, 137, 136, 135, 134, 134, 134, 134, 134, 134, 134, 134, 134, 134, 135, 135, 136, 136, 136, 136, 136, 137, 137, 138, 139, 140, 140, 139, 138, 137, 136, 136, 136, 136, 136, 136, 136, 136, 135, 135};
    int[] mouthX = {88, 92, 92, 93, 94, 96, 98, 102, 105, 109, 113, 117, 121, 124, 127, 129, 131, 132, 134, 135, 137, 139, 140, 142, 144, 146, 148, 149, 151, 153, 154, 155, 157, 158, 160, 161, 163, 165, 167, 169, 172, 174, 176, 178, 180, 181, 183, 184, 185, 187, 188, 189, 191, 192, 193, 195, 196, 198, 200, 201, 203, 205, 207, 209, 210, 212, 214, 216, 218, 219, 220, 221, 222, 223, 224, 225, 225, 226, 226, 226, 226};
    int[] mouthY = {176, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 177, 176, 176, 176, 176, 176, 175, 175, 175, 175, 175, 175, 174, 174, 174, 174, 173, 173, 173, 172, 172, 172, 172, 171, 171, 171, 171, 171, 171, 170, 170, 170, 170, 169, 169, 168, 168, 168, 167, 167, 166, 166, 166, 165, 165, 165, 165, 164, 164, 164, 164, 164, 164, 164};
    int[] dimpleX = {149, 149, 149, 149, 150, 151, 152, 152, 153, 154, 155, 155, 156, 157, 157, 158, 158, 159, 160, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 171, 172, 174, 175, 177, 178, 179, 179, 180, 181, 182, 183, 185, 187, 188, 190, 191, 193, 194, 195, 196, 196, 197, 198, 198, 198, 198, 199, 199, 199, 200, 200};
    int[] dimpleY = {188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 188, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 187, 186, 186, 186, 186, 185, 185, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 184, 185, 185, 186, 184};

    public Renderer() { // Constructor
        super("'The Programmer'");
        JPanel panel = new JPanel();
        setSize(600,600);
        setBackground(iceberg);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //panel.addMouseMotionListener(this);
    }





    /*void mouseTracker(Point mouseInfo) <-- DEBUG
    {
        System.out.println(mouseInfo);
    }*/

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g; // Cast to Graphics2D (engine with more tools/power)
        /*g2d.setColor(paleWhite);
        g2d.fillRect(300, 300, 50, 50); <-- DEBUG */

        g2d.setColor(mahogany);
        g2d.setStroke(smoothStroke(3));
        g2d.drawPolyline(leftEyeX, leftEyeY, leftEyeX.length);

        g2d.setStroke(smoothStroke(5));
        g2d.drawPolyline(rightEyeX, rightEyeY, rightEyeX.length);

        g2d.setStroke(smoothStroke(3));
        g2d.drawPolyline(mouthX, mouthY, mouthX.length);

        g2d.setStroke(smoothStroke(2));
        g2d.drawPolyline(translate(dimpleX, -10), translate(dimpleY, 1), dimpleX.length);

        gradientWidthArc(g2d, 50, 70, 190, 160, 360, 8, 3);

        g2d.setFont(Font.decode("Monaco"));
        g2d.drawString("The humble programmer.", 400, 550);
    }

    public BasicStroke smoothStroke(int width) // This is pretty redundant but it's a Lazy Programmer's Shortcut™
    {
        return new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }


    public int[] translate(int[] coords, int displacement)
    {
        int[] postCoords = coords;

        for (int i = 0; i < coords.length; i++)
        {
           postCoords[i] = coords[i] + displacement;
        }

        return postCoords;
    }

    public void gradientWidthArc(Graphics2D g2d, int arcX, int arcY, int arcWidth, int arcHeight, int arcAngle, int iterationCount, int initialStroke)
    {
        int strokeSize = initialStroke;
        int sumAngle = arcAngle; // The total angle amount

        for (int i = 0; i < iterationCount; i++)
        {
            strokeSize += 2;
            int cleavedAngle = sumAngle/iterationCount; // The angle is CLEAVED! into [iterationCount] pieces

            /* DEBUG
            System.out.println("Stroke " + strokeSize);
            System.out.println("CA " + cleavedAngle);
            System.out.println("SA " + (sumAngle/iterationCount)*i);*/

            if (i == iterationCount - 2) // Hard-coded smoothing at the ends
            {
                strokeSize = initialStroke + 7;
            }

            if (i == iterationCount - 1)
            {
                strokeSize = initialStroke + 3;
            }


            g2d.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
            g2d.drawArc(arcX, arcY, arcWidth, arcHeight, cleavedAngle*(i+1), cleavedAngle);

        }

    }

    public static void main(String[] args)
    {
        new Renderer();
    }

    // region
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println(MouseInfo.getPointerInfo().getLocation()); // DEBUG
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // endregion
}

