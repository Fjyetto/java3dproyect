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
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseMotionAdapter;
import java.io.FileNotFoundException;

public class thing extends JFrame {
    public MPanel uh = null;
    public shit mythread = null;
    public Real genesis = new Real("Root");
    
    public thing() throws InterruptedException{
        System.out.print("SHUDGQDIUSQ\n");
        //setLayout(new FlowLayout());
        Camera camera = genesis.Append(new Camera());

        Mesh cube = null;
        try{
            cube = Mesh.FromOBJ("JohnCube.obj");
        } catch (FileNotFoundException e){
            System.err.println(e);
        }

        genesis.Append(cube); // JohnCube the second child of genesis

        setTitle("Test");
        setSize(512,512);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        shit thread = new shit();
        thread.start();
        mythread=thread;

        uh = new MPanel(thread);
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

            while (true){
                long now = System.nanoTime();
                delta += (now-lastTime)/ns;
                lastTime = now;
                while (delta>=1){
                    
                    //System.out.println(thingy.x);
                    uh.repaint(0,0,512,512);
                    delta--;
                }
            }
        }
    }

    class MPanel extends JPanel {

        private int squareX = 40;
        private int squareY = 40;
        private int squareW = 50;
        private int squareH = 50;

        public MPanel(shit thready){
            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    moveSquare(e.getX(),e.getY());
                }
            });

            addMouseMotionListener(new MouseAdapter() {
                public void mouseDragged(MouseEvent e) {
                    moveSquare(e.getX(),e.getY());
                }
            });
        }

        private void moveSquare(int x, int y) {
            int OFFSET = 1;
            if ((squareX!=x) || (squareY!=y)) {
                repaint(squareX,squareY,squareW+OFFSET,squareH+OFFSET);
                squareX=x;
                squareY=y;
            } 
        }

        public Dimension getPreferredSize(){
            return new Dimension(512,512);
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawString("holy moly",10,30);

            g.setColor(Color.RED);
            g.fillRect(squareX,squareY,squareW,squareH);
            g.setColor(Color.BLACK);
            g.drawRect(squareX,squareY,squareW,squareH);

            //System.out.println(it.x*40.0);
            genesis.children.get(1);

            g.setColor(Color.BLUE);
        }
    }
    
    public static void main(String args[]) throws InterruptedException{
        thing app = new thing();
    }
}