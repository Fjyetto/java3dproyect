public class Vector3 {

	public double x=0.0;
	public double y=0.0;
	public double z=0.0;

	public Vector3(double sx, double sy, double sz){
		x=sx;
		y=sy;
		z=sz;
	}

	public double Magnitude(){
		return Math.sqrt((x*x+y*y+z*z));
	}

	public Vector3 Add(Vector3 v1){
		Vector3 res = new Vector3(x+v1.x,y+v1.y,z+v1.z);
		return res;
	}
}
