import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
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
    //public BufferedImage buh = new BufferedImage(size.x,size.y,BufferedImage.TYPE_BYTE_GRAY);
    public Renderer renderer = null;

    public thing() throws InterruptedException{
        System.out.print("SHUDGQDIUSQ\n");
        //setLayout(new FlowLayout());
        camera = new Camera();
        camera.transform.FromVector3(new Vector3(0.0,0.0,4.0));
        camera.scale = 128.0;
        genesis.Append(camera);
        renderer = new Renderer(size.x,size.y,genesis,camera);

        Mesh cube = null;
        try{
            cube = Mesh.FromOBJ("JohnCube.obj");
            //cube = Mesh.FromOBJ("JohnCubeNLessTLess.obj");
            //cube = Mesh.FromOBJ("FourTris.obj");
            //cube = Mesh.FromOBJ("SimpleTri.obj");
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
                    rm.setTransform(rm.transform.Multiply(new Transform().FromAxisRotation('y', delta*0.006)));
                    //System.out.println(thingy.x);
                    uh.repaint(0,0,size.x,size.y);
                    delta--;
                }
            }
        }
    }

    class MPanel extends JPanel implements KeyListener{

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

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawString("holy moly",10,30);

            renderer.draw(g);

            g.setColor(Color.BLUE);
        }
    }
    
    public static void main(String args[]) throws InterruptedException{
        thing app = new thing();
    }
}