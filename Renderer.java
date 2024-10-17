import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.FloatBuffer;

public class Renderer{
    public Camera camera = null;
    public BaseReal genesis = null;
    public IntVector2 size = new IntVector2(1024,512);
    public FloatBuffer buh = FloatBuffer.allocate(size.x*size.y);
    public BufferedImage finalf = new BufferedImage(size.x,size.y,BufferedImage.TYPE_INT_RGB); //TYPE_BYTE_GRAY
    public Float minbf = Float.POSITIVE_INFINITY;
    public Float maxbf = Float.NEGATIVE_INFINITY;
    private Boolean ZBMode = false;

    public Renderer(int w,int h, BaseReal sgenesis, Camera scamera){
        size.x = w;
        size.y = h;
        genesis = sgenesis;
        camera = scamera;
    }

    public void draw(Graphics g){

        for (int x=0;x<size.x;x++){
            for (int y=0;y<size.y;y++){
                buh.put(x+y*size.x, 1024.0f);
                finalf.setRGB(x, y, 255+255*256+255*256*256);
            }
        }

        RealMesh cube = genesis.getChildAs(1, RealMesh.class);
        ZBMode=true;
        for (int i=0;i<cube.mesh.faces.size(); i++){ // first pass to make zbuffer
            Face cface = cube.mesh.faces.get(i);

            Vector3[] projects= {new Vector3(),new Vector3(),new Vector3()};

            for (int vi=0;vi<3; vi++){
                Vertex cver = cube.mesh.vertices.get(cface.vertices[vi]);
                Vector3 projected = camera.Project(cube.transform.MultiplyWV(cver.p));//cube.transform.MultiplyWV(cver.p));

                projects[vi]=new Vector3(projected.x+size.x*0.5,projected.y+size.y*0.5,projected.z);
            }

            Vector2 uvs[] = {cube.mesh.vtexcoords.get(cface.vtexcoords[0]),cube.mesh.vtexcoords.get(cface.vtexcoords[1]),cube.mesh.vtexcoords.get(cface.vtexcoords[2])};
            splitTriangle(g, projects[0], projects[1], projects[2],cube.mesh.shader, uvs);
        }

        //g.drawImage(buh,0,0,null);
        ZBMode = false;
        for (int i=0;i<cube.mesh.faces.size(); i++){
            Face cface = cube.mesh.faces.get(i);
            Vector3[] projects= {new Vector3(),new Vector3(),new Vector3(),new Vector3()};
            IntVector2[] IVprojects= {new IntVector2(),new IntVector2(),new IntVector2(),new IntVector2()};

            //System.out.println(i);

            for (int vi=0;vi<3; vi++){
                Vertex cver = cube.mesh.vertices.get(cface.vertices[vi]);
                Vector3 projected = camera.Project(cube.transform.MultiplyWV(cver.p));//cube.transform.MultiplyWV(cver.p));

                //projects[vi]=new IntVector2((int)(projected.x)+(int)(size.x*0.5),(int)(projected.y)+(int)(size.y*0.5));
                projects[vi]=new Vector3(projected.x+size.x*0.5,projected.y+size.y*0.5,projected.z);
                IVprojects[vi]=projects[vi].ToIV2();
                if (projected.z>0.0){
                    g.setColor(Color.BLUE);
                    g.fillRect((int)(projected.x+(size.x*0.5)-2.0),(int)(projected.y+(size.y*0.5)-2.0),4,4);
                }
            }

            Vector2 uvs[] = {cube.mesh.vtexcoords.get(cface.vtexcoords[0]),cube.mesh.vtexcoords.get(cface.vtexcoords[1]),cube.mesh.vtexcoords.get(cface.vtexcoords[2])};
            splitTriangle(g, projects[0], projects[1], projects[2],cube.mesh.shader, uvs);

            g.setColor(Color.BLUE);
            g.drawString("v "+cface.vertices[0]+" "+projects[0].z,IVprojects[0].x,IVprojects[0].y);
            g.drawString("v "+cface.vertices[1]+" "+projects[1].z,IVprojects[1].x,IVprojects[1].y);
            g.drawString("v "+cface.vertices[2]+" "+projects[2].z,IVprojects[2].x,IVprojects[2].y);
            g.drawLine(IVprojects[0].x, IVprojects[0].y, IVprojects[1].x, IVprojects[1].y);
            g.drawLine(IVprojects[0].x, IVprojects[0].y, IVprojects[2].x, IVprojects[2].y);
            g.drawLine(IVprojects[2].x, IVprojects[2].y, IVprojects[1].x, IVprojects[1].y);
        }
        g.drawImage(finalf,0,0,null);
    }

    public void fillTopTriangle(Graphics g, Vector3 vv1, Vector3 vv2, Vector3 vv3, Shader shader, Vector2[] UVs){

        // vv1.y == vv2.y

        if (vv1.z<0 || vv3.z<0) return;

        IntVector2 v1 = new IntVector2((int)vv1.x,(int)vv1.y);
        IntVector2 v2 = new IntVector2((int)vv2.x,(int)vv2.y);
        IntVector2 v3 = new IntVector2((int)vv3.x,(int)vv3.y);

        int l = v1.y-v3.y;
        if (l==0){
            return;
        }

        for (int y=0; y<Math.abs(l)+1;y++){
            double kk = (double)y/Math.abs(l);
            double jj=1-kk;

            int x2 = (int) ( kk*vv3.x + jj*vv2.x );
            int x1 = (int) ( kk*vv3.x + jj*vv1.x );
            //System.out.println(x2);

            int yop = (int) (v1.y-Math.copySign(y, l));
            
            if (ZBMode){
                //minbf = Float.POSITIVE_INFINITY;
                //maxbf = Float.NEGATIVE_INFINITY;
                if (yop>=0 && yop<size.y){
                    if (x1>x2){
                        for (int x=0; x<(int)(x1-x2);x++){
                            //int zf = 255-(int)Math.min(255,((kk*vv3.z+jj*vv1.z))*0.75-160);
                            double ll = ((double)x) / Math.abs(x1-x2);
                            double z = (vv3.z)*(kk) + (vv1.z*(ll) + vv2.z*(1-ll))*jj;
                            //int zf = (int)Math.min(255,(z*0.5)-300);
                            int xf = x+x2;
                            if (xf>=0 && xf<size.x && yop>=0 && yop<size.y && z>30){
                                /*if (zf<200) System.out.println(buh.getRGB(xf,yop));
                                buh.setRGB(xf,yop,Math.min(zf+zf*256+zf*256*256,buh.getRGB(xf,yop)));*/
                                buh.put(xf+yop*size.x,Math.min((float)z,buh.get(xf+yop*size.x)));
                                minbf = (float)Math.min(minbf,z);

                                maxbf = (float)Math.max(maxbf,z);
                            }
                        }
                        //g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                    }else{
                        for (int x=0; x<(int)(x2-x1);x++){
                            //int zf = 255-(int)Math.min(255,((kk*vv3.z+jj*vv1.z))*0.75-160);
                            double ll = ((double)x) / Math.abs(x1-x2);
                            double z = (vv3.z)*(kk) + (vv1.z*(1-ll) + vv2.z*(ll))*jj;
                            //int zf = (int)Math.min(255,(z*0.5)-300);
                            int xf = x+x1;
                            if (xf>=0 && xf<size.x && yop>=0 && yop<size.y && z>30){
                                //buh.setRGB(xf,yop,Math.min(zf+zf*256+zf*256*256,buh.getRGB(xf,yop)));
                                buh.put(xf+yop*size.x,Math.min((float)z,buh.get(xf+yop*size.x)));
                                minbf = Math.min(minbf,(float)z);
                                maxbf = Math.max(maxbf,(float)z);
                                /*System.out.println("brah:! ");
                                System.out.println(z);
                                System.out.println(maxbf);*/
                            }
                        }
                        //g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                    }
                }
            }else{
                if (x1>x2){
                    for (int x=0; x<(int)(x1-x2);x++){
                        double ll = ((double)x) / Math.abs(x1-x2);
                        float z = (float)((vv3.z)*(kk) + (vv1.z*(ll) + vv2.z*(1-ll))*jj);
                        int zf = (int)Math.min(255,(z*0.5)-300);
                        int zc = zf+zf*256+zf*256*256;
                        int xf = x+x2;

                        
                        Vector2 UVP = UVs[2].Multiply(kk).Plus(
                            UVs[0].Multiply(ll).Plus(UVs[1].Multiply(1-ll)).Multiply(jj)
                        );

                        if (xf>=0 && xf<size.x && yop>=0 && yop<size.y && buh.get(xf+yop*size.x)>=z-4.0 && z>30){
                            //float rage = maxbf-minbf;
                            //float v = ((buh.get(xf+yop*size.x)-minbf)/rage);
                            //UVP.print();
                            finalf.setRGB(xf, yop, shader.shade(UVP,new IntVector2(xf/4,yop/4)));
                            //finalf.setRGB(xf,yop,255);
                            //finalf.setRGB(xf,yop,(int)(v*256));
                        }
                    }
                    g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                }else{
                    for (int x=0; x<(int)(x2-x1);x++){
                        double ll = ((double)x) / Math.abs(x1-x2);
                        float z = (float)((vv3.z)*(kk) + (vv1.z*(1-ll) + vv2.z*(ll))*jj);
                        int zf = (int)Math.min(255,(z*0.5)-300);
                        int zc = zf+zf*256+zf*256*256;
                        int xf = x+x1;

                        Vector2 UVP = UVs[2].Multiply(kk).Plus(
                            UVs[0].Multiply(1-ll).Plus(UVs[1].Multiply(ll)).Multiply(jj)
                        );

                        /*System.out.println(maxbf);
                        System.out.println(minbf);*/
                        //System.out.println(v);
                        
                        if (xf>=0 && xf<size.x && yop>=0 && yop<size.y && buh.get(xf+yop*size.x)>=z-4.0 && z>30){
                            //float rage = maxbf-minbf;
                            //float v = ((buh.get(xf+yop*size.x)-minbf)/rage);
                            finalf.setRGB(xf, yop, shader.shade(UVP,new IntVector2(xf/4+1,yop/4)));
                            //finalf.setRGB(xf,yop,255*256);
                            //finalf.setRGB(xf,yop,(int)(v*256));
                            
                        }
                    }
                    g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                }
            }
        }
    }

    public void splitTriangle(Graphics g, Vector3 v1, Vector3 v2, Vector3 v3, Shader shader, Vector2[] UVs){

        g.setColor(Color.GREEN);
        if (v1.y==v2.y){
            fillTopTriangle(g, v1, v2, v3, shader, UVs);
        }else if(v1.y==v3.y){
            fillTopTriangle(g, v1, v3, v2, shader, UVs);
        }else if(v2.y==v3.y){
            fillTopTriangle(g, v2, v3, v1, shader, UVs);
        }else{
            g.setColor(Color.CYAN);
            if ((v2.y<v1.y && v2.y>v3.y) || (v2.y>v1.y && v2.y<v3.y)){ // v2 between v1 and v3
                // og is v3
                
                double hfl = (v2.y-v3.y)/(v1.y-v3.y);
                /*Vector3 vm = new Vector3(
                    ((v1.x-v3.x)*hfl)+v3.x,
                    v2.y,
                    (((v1.z-v3.z)*hfl)+v3.z)
                    );*/
                Vector3 vm = new Vector3(
                    ((v1.x-v3.x)*hfl)+v3.x,
                    v2.y,
                    1/(hfl*(1/v1.z)+(1-hfl)*(1/v3.z))
                    );
                Vector2 uvm = new Vector2(UVs[0].x*hfl+UVs[2].x*(1-hfl),UVs[0].y*hfl+UVs[2].y*(1-hfl));
                /*double ufl = (hfl*v1.z)/((1-hfl)*v2.z+hfl*v1.z);
                Vector2 uvm = new Vector2(
                    ((UVs[0].x)*ufl+(1/UVs[2].x)*(1-ufl)),
                    ((UVs[0].y)*ufl+(1/UVs[2].y)*(1-ufl))
                    );*/
                Vector2 newuv0[] = {UVs[1],uvm,UVs[2]};
                Vector2 newuv1[] = {UVs[1],uvm,UVs[0]};
                
                fillTopTriangle(g, v2, vm, v3, shader, newuv0);
                g.setColor(Color.RED);
                fillTopTriangle(g, v2, vm, v1, shader, newuv1);
            }else if ((v3.y<v1.y && v3.y>v2.y) || (v3.y>v1.y && v3.y<v2.y)){ // v3 between v1 and v2
                // og is v2
                double hfl = (v3.y-v2.y)/(v1.y-v2.y);
                /*Vector3 vm = new Vector3(
                    ((v1.x-v2.x)*hfl)+v2.x,
                    v3.y,
                    (((v1.z-v2.z)*hfl)+v2.z)
                    );*/
                
                Vector3 vm = new Vector3(
                    ((v1.x-v2.x)*hfl)+v2.x,
                    v3.y,
                    1/((hfl*(1/v1.z)+(1-hfl)*(1/v2.z)))
                    );

                Vector2 uvm = new Vector2(UVs[0].x*hfl+UVs[1].x*(1-hfl),UVs[0].y*hfl+UVs[1].y*(1-hfl));
                /*Vector2 uvm = new Vector2(
                    1/((1/UVs[0].x)*hfl+(1/UVs[1].x)*(1-hfl)),
                    1/((1/UVs[0].y)*hfl+(1/UVs[1].y)*(1-hfl))
                    );*/
                
                Vector2 newuv0[] = {UVs[2],uvm,UVs[0]};
                Vector2 newuv1[] = {UVs[2],uvm,UVs[1]};
                
                fillTopTriangle(g, v3, vm, v1, shader, newuv0);
                g.setColor(Color.RED);
                fillTopTriangle(g, v3, vm, v2, shader, newuv1);
            }else{ // v1 between v3 and v2
                // og is v3
                double hfl = (v1.y-v3.y)/(v2.y-v3.y);
                /*Vector3 vm = new Vector3(
                    ((v2.x-v3.x)*hfl)+v3.x,
                    v1.y,
                    (((v2.z-v3.z)*hfl)+v3.z)
                    );*/
                Vector3 vm = new Vector3(
                    ((v2.x-v3.x)*hfl)+v3.x,
                    v1.y,
                    1/((hfl*(1/v2.z)+(1-hfl)*(1/v3.z)))
                    );

                Vector2 uvm = new Vector2(UVs[1].x*hfl+UVs[2].x*(1-hfl),UVs[1].y*hfl+UVs[2].y*(1-hfl));
                /*Vector2 uvm = new Vector2(
                    1/((1/UVs[1].x)*hfl+(1/UVs[2].x)*(1-hfl)),
                    1/((1/UVs[1].y)*hfl+(1/UVs[2].y)*(1-hfl))
                    );*/
                
                Vector2 newuv0[] = {UVs[0],uvm,UVs[1]};
                Vector2 newuv1[] = {UVs[0],uvm,UVs[2]};

                fillTopTriangle(g, v1, vm, v2, shader, newuv0);
                g.setColor(Color.RED);
                fillTopTriangle(g, v1, vm, v3, shader, newuv1);
            }
        }
    }
}