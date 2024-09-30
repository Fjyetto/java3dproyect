public class Transform {
	public double matrix[] = {
		1.0,0.0,0.0,0.0,
		0.0,1.0,0.0,0.0,
		0.0,0.0,1.0,0.0,
		0.0,0.0,0.0,1.0,
	};

	public Transform(){
		/*
		              [0  1  2  3 ]
		              [4  5  6  7 ]
		              [8  9  10 11]
		              [12 13 14 15]
		 [0  1  2  3 ]
		 [4  5  6  7 ] multiplciation bojfbiovhnxclkjvnxhbjkvhbsdjkfv
		 [8  9  10 11]
		 [12 13 14 15]
		*/
	}

	public void FromVector3(Vector3 v3){
		matrix[3] = v3.x;
		matrix[7] = v3.y;
		matrix[11] = v3.z;
	}

	public Vector3 ToVector3(){
		Vector3 v = new Vector3(matrix[3],matrix[7],matrix[11]);
		return v;
	}

	public void Print(){
		int iter = 15;
        while (iter>=0){
            if ((16-iter)%4==0 && iter!=15){
                System.out.println(" "+matrix[15-iter]);
            }else{
                System.out.print(" "+matrix[15-iter]);
            }
            iter-=1;
        }
	}

	public Transform FromAxisRotation(Character axis, double angle){

		Transform nt = new Transform();
		double nm[] = nt.matrix;
		switch (axis){
		case 'x':
			nm[5]=Math.cos(angle);
			nm[6]=-Math.sin(angle);

			nm[9]=Math.sin(angle);
			nm[10]=Math.cos(angle);
			nm[0]=1.0;
			break;
		case 'y':
			nm[0]=Math.cos(angle);
			nm[2]=-Math.sin(angle);

			nm[8]=Math.sin(angle);
			nm[10]=Math.cos(angle);
			nm[5]=1.0;
			break;
		case 'z':
			nm[0]=Math.cos(angle);
			nm[1]=Math.sin(angle);

			nm[4]=-Math.sin(angle);
			nm[5]=Math.cos(angle);
			nm[10]=1.0;
			break;
		}
		matrix = nm;
		return nt;
	}

	public Transform GetRot(){
		Transform rotres = new Transform();

		rotres.matrix[0]=matrix[0];
		rotres.matrix[1]=matrix[1];
		rotres.matrix[2]=matrix[2];
		rotres.matrix[4]=matrix[4];
		rotres.matrix[5]=matrix[5];
		rotres.matrix[6]=matrix[6];
		rotres.matrix[10]=matrix[10];

		return rotres;
	}
	public Transform GetTra(){
		Transform trares = new Transform();
		trares.matrix[3] = matrix[3 ];
		trares.matrix[7] = matrix[7 ];
		trares.matrix[11]= matrix[11];

		return trares;
	}

	public Transform Inverse(){
		Transform rotres = new Transform();

		rotres.matrix[0]=matrix[0];
		rotres.matrix[1]=matrix[4];
		rotres.matrix[2]=matrix[8];
		rotres.matrix[4]=matrix[1];
		rotres.matrix[5]=matrix[5];
		rotres.matrix[6]=matrix[9];
		rotres.matrix[10]=matrix[10];

		//result.matrix[3]=matrix[8];
		Transform trares = new Transform();
		trares.matrix[3] = -matrix[3 ];
		trares.matrix[7] = -matrix[7 ];
		trares.matrix[11]= -matrix[11];

		return rotres.Multiply(trares);
	}

	public Transform Multiply(Transform t1){
		Transform result = new Transform();

		result.matrix[0] = t1.matrix[ 0]*matrix[ 0] + t1.matrix[ 4]*matrix[ 1] + t1.matrix[ 8]*matrix[ 2] + t1.matrix[12]*matrix[ 3];
		result.matrix[1] = t1.matrix[ 1]*matrix[ 0] + t1.matrix[ 5]*matrix[ 1] + t1.matrix[ 9]*matrix[ 2] + t1.matrix[13]*matrix[ 3];
		result.matrix[2] = t1.matrix[ 2]*matrix[ 0] + t1.matrix[ 6]*matrix[ 1] + t1.matrix[10]*matrix[ 2] + t1.matrix[14]*matrix[ 3];
		result.matrix[3] = t1.matrix[ 3]*matrix[ 0] + t1.matrix[ 7]*matrix[ 1] + t1.matrix[11]*matrix[ 2] + t1.matrix[15]*matrix[ 3];

		result.matrix[4] = t1.matrix[ 0]*matrix[ 4] + t1.matrix[ 4]*matrix[ 5] + t1.matrix[ 8]*matrix[ 6] + t1.matrix[12]*matrix[ 7];
		result.matrix[5] = t1.matrix[ 1]*matrix[ 4] + t1.matrix[ 5]*matrix[ 5] + t1.matrix[ 9]*matrix[ 6] + t1.matrix[13]*matrix[ 7];
		result.matrix[6] = t1.matrix[ 2]*matrix[ 4] + t1.matrix[ 6]*matrix[ 5] + t1.matrix[10]*matrix[ 6] + t1.matrix[14]*matrix[ 7];
		result.matrix[7] = t1.matrix[ 3]*matrix[ 4] + t1.matrix[ 7]*matrix[ 5] + t1.matrix[11]*matrix[ 6] + t1.matrix[15]*matrix[ 7];

		result.matrix[8] = t1.matrix[ 0]*matrix[ 8] + t1.matrix[ 4]*matrix[ 9] + t1.matrix[ 8]*matrix[10] + t1.matrix[12]*matrix[11];
		result.matrix[9] = t1.matrix[ 1]*matrix[ 8] + t1.matrix[ 5]*matrix[ 9] + t1.matrix[ 9]*matrix[10] + t1.matrix[13]*matrix[11];
		result.matrix[10]= t1.matrix[ 2]*matrix[ 8] + t1.matrix[ 6]*matrix[ 9] + t1.matrix[10]*matrix[10] + t1.matrix[14]*matrix[11];
		result.matrix[11]= t1.matrix[ 3]*matrix[ 8] + t1.matrix[ 7]*matrix[ 9] + t1.matrix[11]*matrix[10] + t1.matrix[15]*matrix[11];

		result.matrix[12]= t1.matrix[ 0]*matrix[12] + t1.matrix[ 4]*matrix[13] + t1.matrix[ 8]*matrix[14] + t1.matrix[12]*matrix[15];
		result.matrix[13]= t1.matrix[ 1]*matrix[12] + t1.matrix[ 5]*matrix[13] + t1.matrix[ 9]*matrix[14] + t1.matrix[13]*matrix[15];
		result.matrix[14]= t1.matrix[ 2]*matrix[12] + t1.matrix[ 6]*matrix[13] + t1.matrix[10]*matrix[14] + t1.matrix[14]*matrix[15];
		result.matrix[15]= t1.matrix[ 3]*matrix[12] + t1.matrix[ 7]*matrix[13] + t1.matrix[11]*matrix[14] + t1.matrix[15]*matrix[15];

		return result;
	}

	public Transform Add(Transform t1){
		Transform result = new Transform();

		result.matrix[0] = t1.matrix[ 0]+matrix[ 0];
		result.matrix[1] = t1.matrix[ 1]*matrix[ 1];
		result.matrix[2] = t1.matrix[ 2]*matrix[ 2];
		result.matrix[3] = t1.matrix[ 3]*matrix[ 3];

		result.matrix[4] = t1.matrix[ 4]*matrix[ 4];
		result.matrix[5] = t1.matrix[ 5]*matrix[ 5];
		result.matrix[6] = t1.matrix[ 6]*matrix[ 6];
		result.matrix[7] = t1.matrix[ 7]*matrix[ 7];

		result.matrix[8] = t1.matrix[ 8]*matrix[ 8];
		result.matrix[9] = t1.matrix[ 9]*matrix[ 9];
		result.matrix[10]= t1.matrix[10]*matrix[10];
		result.matrix[11]= t1.matrix[11]*matrix[11];

		result.matrix[12]= t1.matrix[12]*matrix[12];
		result.matrix[13]= t1.matrix[13]*matrix[13];
		result.matrix[14]= t1.matrix[14]*matrix[14];
		result.matrix[15]= t1.matrix[15]*matrix[15];

		return result;
	}

	public Vector3 MultiplyWV(Vector3 v){
		Vector3 result = new Vector3(0.0,0.0,0.0);

		result.x = v.x*matrix[0]+v.y*matrix[1]+v.z*matrix[2]+matrix[3];
		result.y = v.x*matrix[4]+v.y*matrix[5]+v.z*matrix[6]+matrix[7];
		result.z = v.x*matrix[8]+v.y*matrix[9]+v.z*matrix[10]+matrix[11]; 

		return result;
	}
}