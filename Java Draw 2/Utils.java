import static java.lang.Math.*;
import java.awt.*;
public class Utils{

    //distance from ax+by+c=0     to (x, y)
    public static double distance(double a, double b, double c, double x, double y){
	return abs(a*x + b* y + c) / sqrt(a*a + b*b);
    }

    public static double distance(double a, double b, double c, Point start, Point end, double x, double y){

	//Figure out the bound from start / end
	//if (x, y) is in bound, call distance(....)

	//else call  Min( dist(start, p) ,  dist(end, p)
	return 0.0;
    }

    public static void main(String[] args){
	double dist = distance(1, -1, 0,  0,1);
	System.out.println(dist);
    }
}
