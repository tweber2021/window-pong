import javax.swing.*;
import java.awt.*;



public class WindowPong{

    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrW = (int)screenSize.getWidth();
        int scrH = (int)screenSize.getHeight();
        JFrame[] frames = new JFrame[3];
        float[] x = new float[frames.length];
        float[] y = new float[frames.length];
        float vbx=0,vby=0; // Set ball velocity to zero
        int score1=0,score2 = 0;
        for(int i=0;i<frames.length;i++){
            frames[i]=new JFrame("WindowPong");
            frames[i].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frames[i].setAlwaysOnTop( true );
        }

        // Set up paddles
        x[0]=0;y[0]=scrH/4;x[1]=scrW-(scrW/40);y[1]=(scrH/4);
        frames[0].setBounds((int)x[0],(int)y[0], scrW/40,scrH/4);
        JLabel label1 = new JLabel(String.valueOf(score1));
        label1.setFont(new Font(label1.getFont().getName(), Font.PLAIN, 24));
        frames[0].getContentPane().add(label1);
        frames[0].setVisible(true);
        frames[1].setBounds((int)x[1],(int)y[1], scrW/40,scrH/4);
        JLabel label2 = new JLabel(String.valueOf(score2));
        label2.setFont(new Font(label2.getFont().getName(), Font.PLAIN, 24));
        frames[1].getContentPane().add(label2);
        frames[1].setVisible(true);

        // Set up ball
        x[2]=(scrW/2)-scrW/20;y[2]=(scrH/2)-scrW/20;
        frames[2].setBounds((int)x[2],(int)y[2], scrW/40,scrW/40);
        frames[2].setVisible(true);

        vbx = 1; // Get the ball rolling

        while(true){ // Game Loop
            // Get mouse coordinates
            PointerInfo mouse = MouseInfo.getPointerInfo();
            Point mxy = mouse.getLocation();
            int my = (int) mxy.getY();

            y[0]=my-(scrH/8);
            frames[0].setLocation((int)x[0],(int)y[0]); // Set player's paddle to mouse y

            if(Math.abs(y[1]-(y[2])-(scrH/8))>10 && x[2]>scrW/6 && vbx>0){if(y[1]>y[2]-(scrH/8)){y[1]-=rubberBand(score1-score2)+1;}else{y[1]+=rubberBand(score1-score2)+1;}} // CPU
            frames[1].setLocation((int)x[1],(int)y[1]);

            x[2]+=vbx;y[2]+=vby;
            frames[2].setLocation((int)x[2],(int)y[2]); // Actually get the ball rolling

            // Paddle Collision
            // Player
            if(collision((int)x[0],(int)y[0],scrW/40,scrH/4,(int)x[2],(int)y[2],scrW/40,scrW/40)){
                if(vbx<=0){vbx*=-1;}
                if(vbx>=0){vbx+=vrnd(0.4f);}
                else{vbx-=vrnd(0.4f);}
                vby+=vrnd(2f)-1;
            }
            // CPU
            if(collision((int)x[1],(int)y[1],scrW/40,scrH/4,(int)x[2],(int)y[2],scrW/40,scrW/40)){
                if(vbx>=0){vbx*=-1;}
                if(vbx>=0){vbx+=vrnd(0.4f);}
                else{vbx-=vrnd(0.4f);}
                vby+=vrnd(2f)-1;
            }
            // Ceiling/Floor Collision
            if(y[2]>=scrH-(scrH/40)*2||y[2]<=0){
                vby*=-1;
            }

            if(x[2]<=0){ // Enemy Scores
                // Reset Ball
                frames[2].setVisible(false);
                score2++;
                System.out.println("CPU scores! "+score1+"-"+score2);
                label2.setText(String.valueOf(score2));
                stop(2000);
                x[2]=(scrW/2)-scrW/20;y[2]=(scrH/2)-scrW/20;
                vbx=-1*(rubberBand(score1-score2)+1);
                vby=rubberBand(score1-score2);
                frames[2].setLocation((int)x[2],(int)y[2]);
                frames[2].setVisible(true);
            }
            if(x[2]>scrW-(scrW/40)){ // Player Scores
                // Reset Ball
                frames[2].setVisible(false);
                score1++;
                System.out.println("You scored! "+score1+"-"+score2);
                label1.setText(String.valueOf(score1));
                stop(2000);
                x[2]=(scrW/2)-scrW/20;y[2]=(scrH/2)-scrW/20;
                vbx=-1*(rubberBand(score1-score2)+1);
                vby=rubberBand(score1-score2);
                frames[2].setLocation((int)x[2],(int)y[2]);
                frames[2].setVisible(true);
            }
            if(score1>=10||score2>=10){endGame(score1,score2,frames);}

            stop(2); // Make sure the computer has room to breathe

        }
    }
    static void stop(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static int rnd(int maxValue){
        return (int)(Math.random()*maxValue);
    }

    static float vrnd(float maxValue){
        return (float)(Math.random()*maxValue);
    }

    static boolean collision(int x1, int y1, int w1, int h1, int x2, int y2 ,int w2,int h2){
        if(x1<x2+w2 && x1+w1>x2 && y1<y2+h2 && y1+h1>y2){return true;}
        else{return false;}
    }

    static int rubberBand(int diff){  // Makes CPU catch up with player
        if(diff<=0){return 0;}else{return diff;}
    }

    static void endGame(int playerScore, int CpuScore, JFrame[] frames){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int scrW = (int)screenSize.getWidth();
        int scrH = (int)screenSize.getHeight();

        frames[0].setVisible(false);frames[1].setVisible(false);frames[2].setVisible(false);
        JFrame end = new JFrame("WindowPong - End");
        end.setBounds(scrW/2-100,scrH/2-100,200,200);
        end.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel text = new JLabel();
        text.setFont(new Font(text.getFont().getName(), Font.PLAIN, 24));
        end.getContentPane().add(text);
        if(playerScore>=CpuScore){ // Win
            end.getContentPane().setBackground(new Color(128,128,255));
            text.setText("You Win! "+playerScore+"-"+CpuScore);
        }
        else{ // Lose
            end.getContentPane().setBackground(new Color(255,63,63));
            text.setText("You Lose... "+playerScore+"-"+CpuScore);
        }
        end.setVisible(true);
        while(true);
    }

}
