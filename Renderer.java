import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

public class Renderer{
    public Camera camera = null;
    public BaseReal genesis = null;
    public IntVector2 size = new IntVector2(1024,512);
    public BufferedImage buh = new BufferedImage(size.x,size.y,BufferedImage.TYPE_BYTE_GRAY); //TYPE_BYTE_GRAY
    public BufferedImage finalf = new BufferedImage(size.x,size.y,BufferedImage.TYPE_INT_RGB); //TYPE_BYTE_GRAY
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
                buh.setRGB(x, y, 255+255*256+255*256*256);
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

            splitTriangle(g, projects[0], projects[1], projects[2]);
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

            splitTriangle(g, projects[0], projects[1], projects[2]);

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

    public void fillTopTriangle(Graphics g, Vector3 vv1, Vector3 vv2, Vector3 vv3){

        // vv1.y == vv2.y

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
                if (yop>=0 && yop<size.y){
                    if (x1>x2){
                        for (int x=0; x<(int)(x1-x2);x++){
                            //int zf = 255-(int)Math.min(255,((kk*vv3.z+jj*vv1.z))*0.75-160);
                            double ll = ((double)x) / Math.abs(x1-x2);
                            double z = (vv3.z)*(kk) + (vv1.z*(ll) + vv2.z*(1-ll))*jj;
                            //double z = (vv2.z)*(jj) + (vv3.z*(ll) + vv1.z*(1-ll))*kk;
                            int zf = (int)Math.min(255,(z*0.5)-300);
                            int xf = x+x2;
                            if (xf>=0 && xf<size.x){
                                //System.out.println(buh.getRGB(xf,yop));
                                buh.setRGB(xf,yop,Math.min(zf+zf*256+zf*256*256,buh.getRGB(xf,yop)));
                            }
                        }
                        g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                    }else{
                        for (int x=0; x<(int)(x2-x1);x++){
                            //int zf = 255-(int)Math.min(255,((kk*vv3.z+jj*vv1.z))*0.75-160);
                            double ll = ((double)x) / Math.abs(x1-x2);
                            double z = (vv3.z)*(kk) + (vv1.z*(1-ll) + vv2.z*(ll))*jj;
                            //double z = (vv2.z)*(jj) + (vv3.z*(1-ll) + vv1.z*(ll))*kk;
                            int zf = (int)Math.min(255,(z*0.5)-300);
                            int xf = x+x1;
                            if (xf>=0 && xf<size.x){
                                buh.setRGB(xf,yop,Math.min(zf+zf*256+zf*256*256,buh.getRGB(xf,yop)));
                            }
                        }
                        g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                    }
                }
            }else{
                if (x1>x2){
                    for (int x=0; x<(int)(x1-x2);x++){
                        double ll = ((double)x) / Math.abs(x1-x2);
                        double z = (vv3.z)*(kk) + (vv1.z*(ll) + vv2.z*(1-ll))*jj;
                        int zf = (int)Math.min(255,(z*0.5)-300);
                        int zc = zf+zf*256+zf*256*256;
                        int xf = x+x2;

                        if (xf>=0 && xf<size.x && buh.getRGB(xf,yop)>=zc){
                            finalf.setRGB(xf,yop,255);
                        }
                    }
                    g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                }else{
                    for (int x=0; x<(int)(x2-x1);x++){
                        double ll = ((double)x) / Math.abs(x1-x2);
                        double z = (vv3.z)*(kk) + (vv1.z*(1-ll) + vv2.z*(ll))*jj;
                        int zf = (int)Math.min(255,(z*0.5)-300);
                        int zc = zf+zf*256+zf*256*256;
                        int xf = x+x1;

                        if (xf>=0 && xf<size.x && buh.getRGB(xf,yop)>=zc){
                            finalf.setRGB(xf,yop,255*256);
                        }
                    }
                    g.drawString("vm "+vv2.z,v2.x-40,v2.y);
                }
            }
        }
    }

    public void splitTriangle(Graphics g, Vector3 v1, Vector3 v2, Vector3 v3){

        g.setColor(Color.GREEN);
        if (v1.y==v2.y){
            fillTopTriangle(g, v1, v2, v3);
        }else if(v1.y==v3.y){
            fillTopTriangle(g, v1, v3, v2);
        }else if(v2.y==v3.y){
            fillTopTriangle(g, v2, v3, v1);
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
                fillTopTriangle(g, v2, vm, v3);
                g.setColor(Color.RED);
                fillTopTriangle(g, v2, vm, v1);
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
                fillTopTriangle(g, v3, vm, v1);
                g.setColor(Color.RED);
                fillTopTriangle(g, v3, vm, v2);
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
                fillTopTriangle(g, v1, vm, v2);
                g.setColor(Color.RED);
                fillTopTriangle(g, v1, vm, v3);
            }
        }
    }
}