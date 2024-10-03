public class Vector3 {

	public double x=0.0;
	public double y=0.0;
	public double z=0.0;

	public Vector3(double sx, double sy, double sz){
		x=sx;
		y=sy;
		z=sz;
	}
	public Vector3(){
	}

	public void Print(){
		System.out.println(x+", "+y+", "+z);
	}

	public IntVector2 ToIV2(){
		return new IntVector2((int)x,(int)y);
	}

	public double Magnitude(){
		return Math.sqrt((x*x+y*y+z*z));
	}
	public Vector3 Copy(){
		Vector3 res = new Vector3(x,y,z);
		return res;
	}
	public Vector3 Plus(Vector3 v1){
		Vector3 res = new Vector3(x+v1.x,y+v1.y,z+v1.z);
		return res;
	}
	public Vector3 Substract(Vector3 v1){
		Vector3 res = new Vector3(x-v1.x,y-v1.y,z-v1.z);
		return res;
	}

	public Vector3 Multiply(double s){
		Vector3 res = new Vector3(x*s,y*s,z*s);
		return res;
	}
	public Vector3 Multiply(Vector3 other){
		Vector3 res = new Vector3(x*other.x,y*other.y,z*other.z);
		return res;
	}
}
