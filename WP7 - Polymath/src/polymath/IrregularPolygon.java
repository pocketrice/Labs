package polymath;

import java.util.ArrayList;
import java.util.List;

public class IrregularPolygon {

    public enum Ngon {
        TRIANGLE(3),
        TETRAGON(4),
        PENTAGON(5),
        HEXAGON(6),
        SEPTAGON(7),
        OCTAGON(8),
        NONAGON(9),
        DECAGON(10),
        HENDECAGON(11),
        DODECAGON(12),
        TRIDECAGON(13),
        TETRADECAGON(14),
        PENTADECAGON(15);

        private final int sides;

        Ngon(int sides) {
            this.sides = sides;
        }


        @Override
        public String toString() {
            return name();
        }
    }

    private List<Point> points = new ArrayList<>();
    private Ngon ngon;

    public IrregularPolygon(Point... points) {
        assert(points.length > 2) : "An irrpoly cannot contain less than 3 points!";
        this.points = List.of(points);
        setNgon(points.length);
    }

    public IrregularPolygon(List<Point> points) {
        assert(points.size() > 2) : "An irrpoly cannot contain less than 3 points!";
        this.points = points;
        setNgon(points.size());
    }

    public IrregularPolygon(int sideCount) {
        assert(sideCount > 2) : "An irrpoly cannot contain less than 3 sides!";
        for (int i = 0; i < sideCount; i++) {
            Point newPoint = new Point();

            while (this.containsPoint(newPoint)) {
                newPoint = new Point();
            }
            this.addPoint(newPoint);
        }

        setNgon(sideCount);
    }

    public IrregularPolygon() {
        int sideCount = (int) proximityRandom(5, 2, 5);
        assert(sideCount > 2) : "Error: irrpoly auto-generated sides are too low (" + sideCount + ") -- must contain >2 sides.";
        for (int i = 0; i < sideCount; i++) {
            Point newPoint = new Point();

            while (this.containsPoint(newPoint)) {
                newPoint = new Point();
            }
            this.addPoint(newPoint);
        }

        setNgon(sideCount);
    }

    public void addPoint(Point point) {
        points.add(point);
        setNgon(points.size());
    }

    public void removePoint(int index) {
        assert(points.size() - 1 > 2) : "An irrpoly cannot contain less than 3 sides!";
        points.remove(index);
        setNgon(points.size());
    }

    public boolean containsPoint(Point point) {
        return points.stream().map(p -> p.equals(point)).toList().contains(true);
    }

    public static double proximityRandom(double base, double lowerOffset, double upperOffset) { // <+> APM
        return Math.random()*(lowerOffset + upperOffset) + base - lowerOffset;
    }

    public double perimeter() {
        double perimeter = 0;

        for (int i = 0; i < points.size(); i++) {
            perimeter += Point.getDistance(points.get(i), ((i+1 >= points.size()) ? points.get(0) : points.get(i + 1)));
        }

        return perimeter;
    }

    public double area() {
        double area = 0;

        for (int i = 0; i < points.size(); i++) {
            area += points.get(i).getX() * ((i+1 != points.size()) ? points.get(i+1).getY() : points.get(0).getY());
            area -= points.get(i).getY() * ((i+1 != points.size()) ? points.get(i+1).getX() : points.get(0).getX());
        }
        area = Math.abs(0.5 * area);

        return area;
    }

    public int sideCount() {
        return points.size();
    }

    public Point[] points() {
        return points.toArray(new Point[0]);
    }

    public void setNgon(int sides) {
        for (Ngon n : Ngon.values()) {
            if (n.sides == sides) {
                ngon = n;
            }
        }
    }

    @Override
    public String toString() {
        return ngon.toString();
    }
}