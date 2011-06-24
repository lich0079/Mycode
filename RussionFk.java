import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

class FkFrame extends JFrame {

    class ButtonAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("start")) {
                keyFlag = true;
                allFlag = true;
                start.setEnabled(false);
                stop.setEnabled(true);
                DownThread dthread = new DownThread();
                MakeFkThread mthread = new MakeFkThread();
                ClearThread cthread = new ClearThread();
                dthread.start();
                mthread.start();
                cthread.start();
            }
            if (e.getActionCommand().equals("stop")) {
                start.setEnabled(true);
                stop.setEnabled(false);
                allFlag = false;
                clearBackGround();
                panel.repaint();
                keyFlag = false;
            }
            if (e.getActionCommand().equals("pause") || e.getActionCommand().equals("continue")) {
                if (pauseFlag) {
                    pause.setText("continue");
                    System.out.println("aaa");
                    downFlag = false;
                    pauseFlag = false;
                    keyFlag = false;
                } else {
                    pause.setText("pause");
                    System.out.println("bbb");
                    downFlag = true;
                    pauseFlag = true;
                    keyFlag = true;
                }
            }
        }
    }
    class ClearThread extends Thread {
        int score = 0;

        public void run() {
            ss: while (true) {
                int c = 0;
                int z = 0;
                int d = 0;
                for (int j = 21; j >= 0; j--) {
                    c = 0;
                    if (z == j)
                        d++;

                    if (z != j && d != 0) {
                        switch (d) {
                        case 1: {
                            score += 100;
                            break;
                        }
                        case 2: {
                            score += 300;
                            break;
                        }
                        case 3: {
                            score += 600;
                            break;
                        }
                        case 4: {
                            score += 1000;
                            break;
                        }
                        }
                        d = 0;
                        scoreTf.setText("" + score);
                        if (score >= o * o * 500) {
                            o += 1;
                            levelTf.setText("" + o);
                        }
                    }
                    z = j;
                    for (int i = 0; i < 11; i++)
                        if (b[i][j] != 1)
                            c++;
                    if (c == 0) {

                        for (int x = j; x > 0; x--)
                            for (int y = 0; y < 11; y++)
                                b[y][x] = b[y][x - 1];
                        j++;

                    }
                    panel.repaint();

                }
                if (!allFlag) {
                    resetAll();
                    score = 0;
                    scoreTf.setText("0");
                    o = 1;
                    levelTf.setText("" + o);
                    break ss;
                }

            }
        }
    }
    class DownThread extends Thread {
        public void run() {
            fk1 = new Fk(2);
            ss: while (true) {
                while (fk1.isDown()) {
                    if (downFlag) {
                        if (!allFlag) {
                            resetAll();
                            break ss;
                        }
                        fk1.down();
                        fk1.paint();

                        panel.repaint();
                        try {
                            sleep(1000 - o * 100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                fk1.paint2();
                panel.repaint();
                int con = 0;
                for (int i = 3; i < 8; i++)
                    for (int j = 0; j < 3; j++)
                        if (b[i][j] == 1)
                            con++;
                if (!allFlag) {
                    resetAll();
                    break ss;
                }
                if (con != 0) {
                    panel.repaint();
                    try {
                        sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    start.setEnabled(true);
                    stop.setEnabled(false);
                    allFlag = false;
                    clearBackGround();
                    panel.repaint();
                    keyFlag = false;
                }
                resetPosition();
                fk1 = fk2;
                flag1 = true;
            }
        }
    }
    class Fk {
        private Color color;

        private int index = 0;

        private int[] model = new int[4];

        public Fk(int a) {
            switch (a) {
            case 1: {
                model[0] = 0x0e40;
                model[1] = 0x2620;
                model[2] = 0x4e00;
                model[3] = 0x4640;
                color = Color.BLUE;
                break;
            }
            case 2: {
                model[0] = 0x0cc0;
                model[1] = 0x0cc0;
                model[2] = 0x0cc0;
                model[3] = 0x0cc0;
                color = Color.green;
                break;
            }
            case 3: {
                model[0] = 0x0f00;
                model[1] = 0x1111;
                model[2] = 0x0f00;
                model[3] = 0x1111;
                color = Color.orange;
                break;
            }
            case 4: {
                model[0] = 0x0c60;
                model[1] = 0x2640;
                model[2] = 0x0c60;
                model[3] = 0x2640;
                color = Color.pink;
                break;
            }
            case 5: {
                model[0] = 0x0e20;
                model[1] = 0x2260;
                model[2] = 0x08e0;
                model[3] = 0x6440;
                color = Color.red;
                break;
            }
            case 6: {
                model[0] = 0x0e80;
                model[1] = 0x6220;
                model[2] = 0x2e00;
                model[3] = 0x4460;
                color = Color.yellow;
                break;
            }
            }
        }

        public void down() {
            n++;
        }

        public Color getColor() {
            return color;
        }

        public int getFirstModel() {
            return model[0];
        }

        public int getModel() {
            return model[index];
        }

        public boolean isDown() {
            int count = 0;
            int con = 0x8000;

            int x = 0;
            int y = 0;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if (((getModel() & con) == con)) {
                        if ((j == 21) || (b[i][j + 1] == 1))
                            count++;

                    }
                    con = con >>> 1;
                }
            if (count == 0)
                return true;
            else
                return false;
        }

        public boolean isLeft() {
            int con = 0x8000;
            int count = 0;
            int x = 12;
            int y = 0;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if ((getModel() & con) == con) {
                        if (i == 0 || b[i - 1][j] == 1)
                            count++;
                    }
                    con = con >>> 1;
                }

            if (count == 0)
                return true;
            else
                return false;
        }

        public boolean isLeftRevolve() {
            leftRevolve();
            int con = 0x8000;
            int count = 0;
            ss: for (;;) {
                con = 0x8000;
                for (int i = m; i < m + 4; i++)
                    for (int j = n; j < n + 4; j++) {
                        if ((getModel() & con) == con) {
                            if (i < 0) {
                                m++;
                                continue ss;
                            } else if (i > 10) {
                                m--;
                                continue ss;
                            }
                        }
                        con = con >>> 1;
                    }
                break;
            }
            con = 0x8000;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if ((getModel() & con) == con && b[i][j] == 1)
                        count++;
                    con = con >>> 1;
                }
            righRevolve();
            if (count == 0)
                return true;
            else
                return false;
        }

        public boolean isRighrevolveRevolve() {
            righRevolve();
            int con = 0x8000;
            int count = 0;
            ss: for (;;) {
                con = 0x8000;
                for (int i = m; i < m + 4; i++)
                    for (int j = n; j < n + 4; j++) {
                        if ((getModel() & con) == con) {
                            if (i < 0) {
                                m++;
                                continue ss;
                            } else if (i > 10) {
                                m--;
                                continue ss;
                            }
                        }
                        con = con >>> 1;
                    }
                break;
            }
            con = 0x8000;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if ((getModel() & con) == con && b[i][j] == 1)
                        count++;
                    con = con >>> 1;
                }
            leftRevolve();
            if (count == 0)
                return true;
            else
                return false;
        }

        public boolean isRight() {
            int count = 0;
            int con = 0x8000;
            int x = 0;
            int y = 0;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if ((getModel() & con) == con) {
                        if (i == 10 || b[i + 1][j] == 1)
                            count++;
                    }
                    con = con >>> 1;
                }

            if (count == 0)
                return true;
            else
                return false;
        }

        public void left() {
            m--;
        }

        public void leftRevolve() {
            if (index == 3)
                index = 0;
            else
                index++;
        }

        public void paint() {
            int con = 0x8000;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if (((getModel()) & con) == con) {
                        a[i][j] = 1;
                    }
                    con = con >>> 1;
                }
        }

        public void paint2() {
            int con = 0x8000;
            for (int i = m; i < m + 4; i++)
                for (int j = n; j < n + 4; j++) {
                    if (((getModel()) & con) == con) {
                        b[i][j] = 1;
                    }
                    con = con >>> 1;
                }
        }

        public void prePaint() {
            int con = 0x8000;
            for (int i = 4; i < 8; i++)
                for (int j = 1; j < 5; j++) {
                    if (((getFirstModel()) & con) == con) {
                        c[i][j] = 1;
                    }
                    con = con >>> 1;
                }
        }
        public void righRevolve() {
            if (index == 0)
                index = 3;
            else
                index--;
        }
        public void right() {
            m++;
        }
    }
    // 方块下落不分主面板类
    class FkPanel extends JPanel {
        // 长方形模型
        Rectangle2D[][] rec = new Rectangle2D.Double[11][22];

        public FkPanel() {
            for (int i = 0; i < 11; i++)
                for (int j = 0; j < 22; j++)
                    rec[i][j] = new Rectangle2D.Double(width / 11 * i, height / 22 * j, width / 11, height / 22);
        }

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D h = (Graphics2D) g;
            if (fk1 != null)
                h.setColor(fk1.getColor());

            // 画方块
            clearFk();
            if (allFlag) {
                if (fk1 != null)
                    fk1.paint();

                for (int i = 0; i < 11; i++)
                    for (int j = 0; j < 22; j++)
                        if (a[i][j] == 1)
                            h.fill(rec[i][j]);
                // 画背景
                h.setColor(Color.WHITE);
                for (int i = 0; i < 11; i++)
                    for (int j = 0; j < 22; j++)
                        if (b[i][j] == 1)
                            h.fill(rec[i][j]);
            } else
                h.setColor(Color.WHITE);
            for (int i = 0; i < 11; i++)
                for (int j = 0; j < 22; j++)
                    if (b[i][j] == 1)
                        h.fill(rec[i][j]);
        }

        // /public synchronized void makeFk(){
        // fk1=new Fk(2);
        // }

    }
    class FkPanel2 extends JPanel {
        double hei = 466;
        Rectangle2D[][] re = new Rectangle2D.Double[11][22];
        double wid = 245;

        public FkPanel2() {
            for (int i = 0; i < 11; i++)
                for (int j = 0; j < 22; j++)
                    re[i][j] = new Rectangle2D.Double(wid / 11 * i, hei / 22 * j, wid / 11, height / 22);

        }

        public void clearC() {
            for (int i = 0; i < 11; i++)
                for (int j = 0; j < 7; j++)
                    c[i][j] = 0;
        }

        public void paint(Graphics g) {
            super.paint(g);
            Graphics2D h = (Graphics2D) g;

            if (fk2 != null)
                h.setColor(fk2.getColor());
            // clearC();
            // if(fk1!=null)
            // fk1.prePaint();
            // for(int x=4;x<8;x++)
            // for(int y=1;y<2;y++)
            // c[x][y]=1;
            for (int x = 4; x < 8; x++)
                for (int y = 1; y < 5; y++)
                    if (c[x][y] == 1)
                        h.fill(re[x][y]);

        }
    }
    class KeyAction implements KeyListener {
        public void keyPressed(KeyEvent e) {
            if (keyFlag) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                    if (fk1.isLeft()) {
                        fk1.left();
                        panel.repaint();
                        System.out.println("left");
                    }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                    if (fk1.isRight()) {
                        fk1.right();
                        panel.repaint();
                        System.out.println("right");
                    }
                if (e.getKeyCode() == KeyEvent.VK_Z) {
                    if (fk1.isLeftRevolve()) {
                        fk1.leftRevolve();
                        panel.repaint();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_X) {
                    if (fk1.isRighrevolveRevolve()) {
                        fk1.righRevolve();
                        panel.repaint();
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                    if (fk1.isDown()) {
                        fk1.down();
                        panel.repaint();
                        System.out.println("down");
                    }
            }
        }

        public void keyReleased(KeyEvent arg0) {
        }

        public void keyTyped(KeyEvent arg0) {
        }
    }
    class MakeFkThread extends Thread {
        int u;

        public void run() {
            ss: while (true) {
                if (flag1) {
                    setU();
                    fk2 = new Fk(u);
                    flag1 = false;
                    panel3.clearC();
                    fk2.prePaint();
                    panel3.repaint();
                }
                if (!allFlag) {
                    resetAll();
                    break ss;
                }
                // panel3.repaint();
            }
            // System.out.println("asd");
        }

        public void setU() {
            u = (int) (Math.random() * 6 + 1);
        }
    }
    int[][] a = new int[11][22]; // 方块组模型坐标
    boolean allFlag = true; // 全程停止进程锁
    int[][] b = new int[11][22]; // 背景模型坐标
    int[][] c = new int[11][22];
    int DEFAULT_HEIGHT = 500;
    int DEFAULT_WIDTH = 500;
    boolean downFlag = true; // 下落锁
    Fk fk1; // 目前方块
    Fk fk2; // 准备方块
    boolean flag1 = true;
    double height = 470;
    boolean keyFlag = false; // 键盘锁
    JTextField levelTf;
    int m = 4;
    int n = 0;
    int o = 1;

    FkPanel panel;

    FkPanel2 panel3;

    JButton pause;

    boolean pauseFlag = true; // 暂停锁

    JTextField scoreTf;

    JButton start;

    JButton stop;

    double width = 245;

    public FkFrame() {

        this.setLayout(new GridLayout(1, 2));
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // 添加面板

        // 页面设计
        panel = new FkPanel();
        JPanel panel2 = new JPanel();
        panel3 = new FkPanel2();
        JPanel panel4 = new JPanel();
        // 主面板背景色
        panel.setBackground(Color.black);
        // 副面板布局
        this.add(panel);
        this.add(panel3);

        panel3.setLayout(new GridLayout(3, 1));
        panel4.setLayout(new GridLayout(3, 1));

        JPanel panel5 = new JPanel();
        JPanel panel6 = new JPanel();
        JPanel panel7 = new JPanel();
        JPanel panel8 = new JPanel();
        JPanel panel9 = new JPanel();
        JPanel panel10 = new JPanel();

        scoreTf = new JTextField(8);
        scoreTf.setText("0");
        scoreTf.setEditable(false);
        JLabel label = new JLabel("score");
        panel9.add(label);
        panel9.add(scoreTf);

        levelTf = new JTextField(8);
        levelTf.setEditable(false);
        levelTf.setText("" + o);
        label = new JLabel("level");
        panel10.add(label);
        panel10.add(levelTf);

        panel8.setLayout(new GridLayout(2, 1));
        panel8.add(panel9);
        panel8.add(panel10);

        panel2.setVisible(false);

        panel3.add(panel2);
        panel3.add(panel8);
        panel3.add(panel4);

        panel4.add(panel5);
        panel4.add(panel6);
        panel4.add(panel7);

        // 创建监听器

        ButtonAction action = new ButtonAction();
        KeyAction t = new KeyAction();

        // 创建按钮,设置监听器
        // 开始
        start = new JButton("start");
        start.addActionListener(action);
        start.addKeyListener(t);
        panel5.add(start);
        // 暂停
        pause = new JButton("pause");
        pause.addActionListener(action);
        pause.addKeyListener(t);
        panel6.add(pause);
        // 停止
        stop = new JButton("stop");
        stop.addActionListener(action);
        stop.addKeyListener(t);
        stop.setEnabled(false); // 起始设置为不可见;
        panel7.add(stop);
        // 设置可见性
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*
         * width=panel.getWidth(); height=panel.getHeight();
         * 
         * System.out.println(panel.getWidth());
         * System.out.println(panel.getHeight());
         * System.out.println(panel3.getWidth());
         * System.out.println(panel3.getHeight());
         */

    }

    // 清背景
    public void clearBackGround() {
        for (int i = 0; i < 11; i++)
            for (int j = 0; j < 22; j++)
                b[i][j] = 0;
    }

    // 清方块组
    public void clearFk() {
        for (int i = 0; i < 11; i++)
            for (int j = 0; j < 22; j++)
                a[i][j] = 0;
    }

    // 清屏幕设置起始位置
    public void resetAll() {
        m = 4;
        n = 0;
        clearFk();
        clearBackGround();
    }

    // 方块起始位置
    public void resetPosition() {
        m = 4;
        n = 0;
    }
}

public class RussionFk {

    public static void main(String[] args) {
        FkFrame frame = new FkFrame();
    }
}