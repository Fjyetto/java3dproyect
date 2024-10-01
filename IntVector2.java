public class IntVector2 {

	public int x=0.0;
	public int y=0.0;

	public IntVector2(int sx, int sy){
		x=sx;
		y=sy;
	}
	public IntVector2(){
	}

	public int Magnitude(){
		return (int)Math.sqrt((x*x+y*y));
	}
	public IntVector2 Copy(){
		IntVector2 res = new IntVector2(x,y);
		return res;
	}
	public IntVector2 Plus(IntVector2 v1){
		IntVector2 res = new IntVector2(x+v1.x,y+v1.y);
		return res;
	}
	public IntVector2 Substract(IntVector2 v1){
		IntVector2 res = new IntVector2(x-v1.x,y-v1.y);
		return res;
	}

	public IntVector2 Multiply(double s){
		IntVector2 res = new IntVector2(x*s,y*s);
		return res;
	}
	public IntVector2 Multiply(IntVector2 other){
		IntVector2 res = new IntVector2(x*other.x,y*other.y);
		return res;
	}
}
