package hjzgg.main;

import hjzgg.layer.TheLayer;
import hjzgg.map.TankMap;
import hjzgg.set.MySet;
import hjzgg.size.TheSize;
import hjzgg.tank.MyTank;
import hjzgg.tank.ShellThread;
import hjzgg.tank.EnemyTankThread;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;


class MapDialog extends JDialog{
	public static int chooseMap = -1;
	public MapDialog(Dialog owner, String title, boolean modal){
		super(owner, title, modal);
		int len = TankMap.tankmap.length;
		setSize(500, 500);
		setResizable(false);
		JPanel mapPane = new JPanel();
		mapPane.setLayout(new BoxLayout(mapPane, BoxLayout.Y_AXIS));
		JScrollPane scrollpane = new JScrollPane(mapPane);
		add(scrollpane);
		for(int i=1; i<=len; ++i){
			JPanel p = new JPanel();
			ShapePane tankmap = new ShapePane("tankmap/map" + i + ".jpg", 350, 350);
			tankmap.setPreferredSize(new Dimension(350, 350));
			JButton btnChoose = new JButton("地图"+i);
			final int choose = i-1;
			btnChoose.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					chooseMap = choose;
					MapDialog.this.dispose();
				}
			});
			p.add(tankmap);
			p.add(btnChoose);
			mapPane.add(p);
		}
		setVisible(true);
	}
}

class BeginPanel extends JPanel{
	private String img_path = null;
	private int width, height;
	private boolean isBorder = false;
	public BeginPanel(String path, int w, int h){
		this.img_path = path;
		this.width = w;
		this.height = h;
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		if(img_path==null) return;
		g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, width, height, this);
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		 if(isBorder){
			 float lineWidth = 5.0f;
			 ((Graphics2D)g).setStroke(new BasicStroke(lineWidth));
			 ((Graphics2D)g).setColor(Color.RED);
			 ((Graphics2D)g).drawRect(0, 0, 100, 50);
		 }
	}
	public void setIsBorder(boolean f){
		this.isBorder = f;
	}
	public void setImgPath(String path){
		this.img_path = path;
	}
}

public class BeginFrame extends Frame{
	private int index = 0;
	private BeginPanel pic1 = new BeginPanel("tank_battle.png", 800, 200);
	private BeginPanel[] tank = new BeginPanel[2];
	private BeginPanel begin = new BeginPanel("begin.png", 100, 50);
	private BeginPanel continuing = new BeginPanel("continue.png", 100, 50);
	private JPanel pmenu1 = new JPanel();
	private JPanel pmenu2 = new JPanel();
	private JLabel l1 = new JLabel("HJZGG");
	private JLabel l2 = new JLabel("@2015-2-4 hjz");
	private JLabel l3 = new JLabel("ALL RIGHTS RESERVED");
	private JLabel l4 = new JLabel("qq:2570230521");
	private JLabel l5 = new JLabel("tel:18337174540");
	
	private void changTank(int i){
		tank[i].setImgPath("tank/mytank_right.gif");
		tank[i].updateUI();
		
		tank[i^1].setImgPath(null);
		tank[i^1].updateUI();
	}
	
	private void borderPaint(BeginPanel x, boolean f){
		x.setIsBorder(f);
		x.updateUI();
	}
	
	class BorderChange implements Runnable{
		private BeginPanel tmp = null;
		private int index = 0;
		public BorderChange(BeginPanel x, int index){
			tmp = x;
			this.index = index;
		}
		public void run() {
			try {
				borderPaint(tmp, true);
				Thread.sleep(300);
				borderPaint(tmp, false);
				Thread.sleep(300);
				borderPaint(tmp, true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if(index == 0){
				//选择地图
				MapDialog md = new MapDialog(null, "请选择地图!", true);
				
				TankFrame tf = new TankFrame(TankMap.tankmap[MapDialog.chooseMap]);
				ShellThread st = new ShellThread();
				new Thread(st).start();//炮弹线程
				MySet.getInstance().setSt(st);
				EnemyTankThread ett = new EnemyTankThread(tf);
				new Thread(ett).start();//坦克出现 线程
				MySet.getInstance().setEtt(ett);
				
				JLayeredPane jlp = tf.getJlp();
				MyTank mt = new MyTank("mytank", "tank/mytank_up.gif", 0, tf);
				MySet.getInstance().getTankSet().add(mt);
				mt.setBounds(280, 630, TheSize.tank_width, TheSize.tank_height);
				jlp.add(mt);
				jlp.setLayer(mt, TheLayer.tank);
				tf.setVisible(true);
				mt.requestFocus();//在窗口显示之后将焦点移到MyTank上，否则不能监听键盘消息
			} else {
				SelfConfigFrame scf = new SelfConfigFrame();
				scf.setResizable(false);
				scf.setVisible(true);
			}
			BeginFrame.this.dispose();
		}
	}
	
	public BeginFrame(){
		
		  addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent arg0) {
					System.exit(0);
				}
			  
		  });
		  
		  addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				 switch(arg0.getKeyCode()){
				 	case KeyEvent.VK_UP:
				 		index = (index+1)%2;
				 		changTank(index);
				 		break;
				 	case KeyEvent.VK_DOWN:
				 		index = (index-1 + 2)%2;
				 		changTank(index);
				 		break;
				 	case KeyEvent.VK_ENTER:
				 		if(index==0){
				 			borderPaint(continuing, false);
				 			new Thread(new BorderChange(begin, index)).start();
				 		}
				 		else{
				 			borderPaint(begin, false);
				 			new Thread(new BorderChange(continuing, index)).start();
				 		}
				 		break;
				 	default:
				 		break;
				 }
			}
			  
		  });
		  tank[0] = new BeginPanel("tank/mytank_right.gif", 50, 50);
		  tank[1] = new BeginPanel(null, 50, 50);
		  setBackground(Color.BLACK);
		  setSize(800,600);
		  setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		  pic1.setBackground(Color.BLACK);
		  pic1.setPreferredSize(new Dimension(800, 200));
		  add(pic1);
		  
		  pmenu1.setPreferredSize(new Dimension(800, 50));
		  tank[0].setBackground(Color.BLACK);
		  tank[0].setPreferredSize(new Dimension(100, 50));
		  begin.setBackground(Color.BLACK);
		  begin.setPreferredSize(new Dimension(100, 50));
		  pmenu1.setBackground(Color.BLACK);
		  pmenu1.add(tank[0]);
		  pmenu1.add(begin);
		  add(pmenu1);
		  
		  pmenu2.setBackground(Color.BLACK);
		  pmenu2.setPreferredSize(new Dimension(800, 50));
		  tank[1].setBackground(Color.BLACK);
		  tank[1].setPreferredSize(new Dimension(100, 50));
		  continuing.setBackground(Color.BLACK);
		  continuing.setPreferredSize(new Dimension(100, 50));
		  pmenu2.add(tank[1]);
		  pmenu2.add(continuing);
		  add(pmenu2);
		  
		  l1.setFont(new Font("华文彩云", Font.BOLD, 25));
		  l1.setForeground(Color.RED);
		  JPanel px = new JPanel();
		  px.setBackground(Color.BLACK);
		  px.add(l1);
		  add(px);
		  
		  l2.setFont(new Font("黑体", Font.BOLD, 15));
		  px = new JPanel();
		  px.setBackground(Color.BLACK);
		  px.add(l2);
		  add(px);
		  
		  l3.setFont(new Font("黑体", Font.BOLD, 15));
		  px = new JPanel();
		  px.setBackground(Color.BLACK);
		  px.add(l3);
		  add(px);
		  
		  l4.setFont(new Font("宋体", Font.ITALIC, 15));
		  l4.setForeground(Color.green);
		  px = new JPanel();
		  px.setBackground(Color.BLACK);
		  px.add(l4);
		  add(px);
		  
		  l5.setFont(new Font("黑体", Font.ITALIC, 15));
		  l5.setForeground(Color.green);
		  px = new JPanel();
		  px.setBackground(Color.BLACK);
		  px.add(l5);
		  add(px);
	}
	
	public static void main(String[] args){
		BeginFrame bf = new BeginFrame();
		bf.setVisible(true);
	}
}