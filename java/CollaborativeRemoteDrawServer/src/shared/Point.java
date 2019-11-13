package shared;

//Shared class between the client and the server to advantage of the both side using java
//Represent a point in 2D space
public class Point {

    public float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    //calculates the distance between this point and the other
    public float distance(Point p){
        return (float) Math.sqrt((this.x - p.x)*(this.x - p.x) + (this.y - p.y)*(this.y - p.y));
    }
}
