import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.io.FileNotFoundException;

public class thing extends JFrame {
    public MPanel uh = null;
    public shit mythread = null;
    public BaseReal genesis = new BaseReal("Root");
    public Camera camera = null;
    public IntVector2 size = new IntVector2(1024,512);
    //public double ZBuff[] = new double[size.x*size.y];
    public BufferedImage buh = new BufferedImage(size.x,size.y,BufferedImage.TYPE_BYTE_GRAY);
    
    public thing() throws InterruptedException{
        System.out.print("SHUDGQDIUSQ\n");
        //setLayout(new FlowLayout());
        camera = new Camera();
        camera.transform.FromVector3(new Vector3(0.0,0.0,4.0));
        genesis.Append(camera);

        Mesh cube = null;
        try{
            //cube = Mesh.FromOBJ("JohnCube.obj");
            cube = Mesh.FromOBJ("SimpleTri.obj");
        } catch (FileNotFoundException e){
            System.err.println(e);
        }

        RealMesh realcube = new RealMesh(cube);
        realcube.transform.FromAxisRotation('y', Math.PI/2.0);
        genesis.Append(realcube); // JohnCube the second child of genesis

        setTitle("Test");
        setSize(size.x,size.y);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        shit thread = new shit();
        thread.start();
        mythread=thread;

        uh = new MPanel(thread);
        addKeyListener(uh);
        add(uh);
        pack();

    }

    public class shit extends Thread{
        public void run(){
            long lastTime = System.nanoTime();
            final double ns = 1000000000.0/60.0;
            double delta = 0;
            System.out.println("This thread is STARTING!!!");

            //Transform rot = (new Transform()).FromAxisRotation('z',.011);//.Multiply((new Transform()).FromAxisRotation('x',0.0031415));

            RealMesh rm = genesis.getChildAs(1, RealMesh.class);

            while (true){
                long now = System.nanoTime();
                delta += (now-lastTime)/ns;
                lastTime = now;
                while (delta>=1){
                    //rm.setTransform(rm.transform.Multiply(new Transform().FromAxisRotation('y', delta*0.03)));
                    //System.out.println(thingy.x);
                    uh.repaint(0,0,size.x,size.y);
                    delta--;
                }
            }
        }
    }

    class MPanel extends JPanel implements KeyListener{

        /*private int squareX = 40;
        private int squareY = 40;
        private int squareW = 50;
        private int squareH = 50;*/

        public MPanel(shit thready){
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            /*addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    moveSquare(e.getX(),e.getY());
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    moveSquare(e.getX(),e.getY());
                }
            });*/
        }

        public void keyPressed(KeyEvent e){
            int key = e.getKeyCode();
            Transform move = new Transform();
            double speed = 0.2;
            //System.out.println("Woah");
            //System.out.println(key);
            switch(key){
                case KeyEvent.VK_RIGHT:
                    move.FromVector3(new Vector3(-speed,0.0,0.0));
                    move = camera.transform.GetRot().Multiply(move).GetTra();
                    //move = move.Multiply(camera.transform.GetRot()).GetTra();
                    camera.setTransform(camera.transform.Multiply(move));
                    break;
                case KeyEvent.VK_LEFT:
                    move.FromVector3(new Vector3(speed,0.0,0.0));
                    move = camera.transform.GetRot().Multiply(move).GetTra();
                    //move = move.Multiply(camera.transform.GetRot()).GetTra();
                    camera.setTransform(camera.transform.Multiply(move));
                    break;
                case KeyEvent.VK_UP:
                    move.FromVector3(new Vector3(0.0,0.0,-speed));
                    move = camera.transform.GetRot().Multiply(move).GetTra();
                    //move = move.Multiply(camera.transform.GetRot()).GetTra();
                    camera.setTransform(camera.transform.Multiply(move));
                    break;
                case KeyEvent.VK_DOWN:
                    move.FromVector3(new Vector3(0.0,0.0,speed));
                    move = camera.transform.GetRot().Multiply(move).GetTra();
                    //move = move.Multiply(camera.transform.GetRot()).GetTra();
                    camera.setTransform(camera.transform.Multiply(move));
                    break;
                case KeyEvent.VK_NUMPAD6:
                    
                    //move.FromAxisRotation('y', 3.14159/20.0);
                    //camera.setTransform(camera.transform.GetTra().Multiply(camera.transform.GetRot().Multiply(move)));
                    //camera.setTransform(camera.transform.Multiply(move));
                    break;
                case KeyEvent.VK_NUMPAD4:

                    //move.FromAxisRotation('y', -3.14159/20.0);
                    //camera.setTransform(camera.transform.GetTra().Multiply(camera.transform.GetRot().Multiply(move)));
                    //camera.setTransform(camera.transform.Multiply(move));
                    break;
            }
            System.out.println("NewcamT:");
            camera.transform.Print();
        }
        public void keyReleased(KeyEvent e){

        }
        public void keyTyped(KeyEvent e){

        }

        public Dimension getPreferredSize(){
            return new Dimension(size.x,size.y);
        }

        public void fillTopTriangle(Graphics g, Vector3 vv1, Vector3 vv2, Vector3 vv3){

            IntVector2 v1 = new IntVector2((int)vv1.x,(int)vv1.y);
            IntVector2 v2 = new IntVector2((int)vv2.x,(int)vv2.y);
            IntVector2 v3 = new IntVector2((int)vv3.x,(int)vv3.y);

            int l = v1.y-v3.y;
            if (l==0){
                return;
            }

            /*System.out.print("filling top tri: ");
            v1.Print();
            v2.Print();
            v3.Print();*/
            for (int y=0; y<Math.abs(l)+1;y++){
                double kk = (double)y/Math.abs(l);
                double jj=1-kk;

                int x1 = (int) ( kk*(double)v3.x + jj*(double)v1.x );
                int x2 = (int) ( kk*(double)v3.x + jj*(double)v2.x );
                //System.out.println(x2);

                /*if (l<=0){
                    double t = kk;
                    kk = jj;
                    jj = t;
                }*/
                int yop = (int) (v1.y-Math.copySign(y, l));
                
                g.drawLine(x2,yop,x1,yop);
                if (yop>=0 && yop<size.y){
                if (x1>x2){
                    for (int x=0; x<(int)(x1-x2);x++){
                        int zf = 255-(int)Math.min(255,((kk*vv3.z+jj*vv1.z))*0.75-160);
                        if (x+x2>=0 && x+x2<size.x){
                            buh.setRGB(x+x1,yop,Math.max(zf+zf*256+zf*256*256,buh.getRGB(x+x1,yop)));
                        }
                        //buh.setRGB(x+x2,yop,Math.max(zf+zf*256+zf*256*256,buh.getRGB(x+x2,yop)));
                        /*if (ZBuff.length>x+x2+yop*size.x){
                            ZBuff[x+yop*size.x]=kk*vv3.z+jj*vv1.z;
                        }*/
                    }
                }else{
                    for (int x=0; x<(int)(x2-x1);x++){
                        int zf = 255-(int)Math.min(255,((kk*vv3.z+jj*vv1.z))*0.75-160);
                        if (x+x1>=0 && x+x1<size.x){
                            buh.setRGB(x+x1,yop,Math.max(zf+zf*256+zf*256*256,buh.getRGB(x+x1,yop)));
                        }
                        /*if (ZBuff.length>x+x1+yop*size.x){
                            ZBuff[x+yop*size.x]=kk*vv3.z+jj*vv1.z;
                        }*/
                    }
                }}
                
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
                    double hfl = (v2.y-v3.y)/(v1.y-v3.y);
                    Vector3 vm = new Vector3(
                        ((v1.x-v3.x)*hfl)+v3.x,
                        v2.y,
                        ((v1.z-v3.z)*hfl)+v3.z
                        );
                    fillTopTriangle(g, v2, vm, v3);
                    g.setColor(Color.RED);
                    fillTopTriangle(g, v2, vm, v1);
                }else if ((v3.y<v1.y && v3.y>v2.y) || (v3.y>v1.y && v3.y<v2.y)){ // v3 between v2 and v3
                    double hfl = (v3.y-v2.y)/(v1.y-v2.y);
                    Vector3 vm = new Vector3(
                        ((v1.x-v2.x)*hfl)+v2.x,
                        v3.y,
                        ((v1.z-v2.z)*hfl)+v2.z
                        );
                    fillTopTriangle(g, v3, vm, v1);
                    g.setColor(Color.RED);
                    fillTopTriangle(g, v3, vm, v2);
                }else{ // v1 between v3 and v2
                    double hfl = (v1.y-v3.y)/(v2.y-v3.y);
                    Vector3 vm = new Vector3(
                        ((v2.x-v3.x)*hfl)+v3.x,
                        v1.y,
                        ((v2.z-v3.z)*hfl)+v3.z
                        );
                    fillTopTriangle(g, v1, vm, v2);
                    g.setColor(Color.RED);
                    fillTopTriangle(g, v1, vm, v3);
                }
            }
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawString("holy moly",10,30);

            Camera cam = genesis.getChildAs(0,Camera.class);
            cam.scale = 128.0;
            RealMesh cube = genesis.getChildAs(1,RealMesh.class);
            
            /*Graphics bg = buh.createGraphics();
            bg.setColor(Color.BLACK);
            bg.drawRect(0, 0, size.x, size.y);
            */
            for (int x=0;x<size.x;x++){
                for (int y=0;y<size.y;y++){
                    buh.setRGB(x, y, 0);
                }
            }

            for (int i=0;i<cube.mesh.faces.size(); i++){
                Face cface = cube.mesh.faces.get(i);
                Vector3[] projects= {new Vector3(),new Vector3(),new Vector3(),new Vector3()};
                IntVector2[] IVprojects= {new IntVector2(),new IntVector2(),new IntVector2(),new IntVector2()};

                //System.out.println(cface.vertices);

                for (int vi=0;vi<3; vi++){
                    Vertex cver = cube.mesh.vertices.get(cface.vertices[vi]-1);
                    Vector3 projected = cam.Project(cube.transform.MultiplyWV(cver.p));//cube.transform.MultiplyWV(cver.p));

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
                g.drawLine(IVprojects[0].x, IVprojects[0].y, IVprojects[1].x, IVprojects[1].y);
                g.drawLine(IVprojects[0].x, IVprojects[0].y, IVprojects[2].x, IVprojects[2].y);
                g.drawLine(IVprojects[2].x, IVprojects[2].y, IVprojects[1].x, IVprojects[1].y);
            }

            g.setColor(Color.BLUE);

            /*for (int zbi = 0; zbi<ZBuff.length; zbi++){
                int x = zbi%size.x;
                int y = zbi/size.y;

                g.setColor(new Color((float)ZBuff[zbi],(float)ZBuff[zbi],(float)ZBuff[zbi]));
                g.drawImage()
            }*/
            //g.drawImage(buh,0,0,null);
        }
    }
    
    public static void main(String args[]) throws InterruptedException{
        thing app = new thing();
    }
}