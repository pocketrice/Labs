package polymath;

public class Point {
    private int x, y;


    public Point() {
        x = (int) (Math.random() * 100);
        y = (int) (Math.random() * 100);
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int[] getCoords() {
        return new int[]{x, y};
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void offsetCoords(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public static double getDistance(Point pointA, Point pointB) {
        int[] coordsA = pointA.getCoords();
        int[] coordsB = pointB.getCoords();

        return Math.sqrt(Math.pow(coordsA[0] - coordsB[0], 2) + Math.pow(coordsA[1] - coordsB[1], 2));
    }

    @Override
    public boolean equals(Object obj) {
        Point other = (Point) obj;
        return (this.x == other.x && this.y == other.y);
    }
}