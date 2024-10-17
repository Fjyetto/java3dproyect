public class Vector2 {

	public double x=0.0;
	public double y=0.0;

	public Vector2(double sx, double sy){
		x=sx;
		y=sy;
	}
	public Vector2(){
	}

	public double Magnitude(){
		return Math.sqrt((x*x+y*y));
	}
	public Vector2 Copy(){
		Vector2 res = new Vector2(x,y);
		return res;
	}
	public Vector2 Plus(Vector2 v1){
		Vector2 res = new Vector2(x+v1.x,y+v1.y);
		return res;
	}
	public Vector2 Substract(Vector2 v1){
		Vector2 res = new Vector2(x-v1.x,y-v1.y);
		return res;
	}

	public Vector2 Multiply(double s){
		Vector2 res = new Vector2(x*s,y*s);
		return res;
	}
	public Vector2 Multiply(Vector2 other){
		Vector2 res = new Vector2(x*other.x,y*other.y);
		return res;
	}
	public Vector2 Invert(){
		return new Vector2(1.0/x,1.0/y);
	}
	public void print(){
		System.out.print(x+", ");
		System.out.println(y);
	}
}
