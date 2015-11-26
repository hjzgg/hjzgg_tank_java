package hjzgg.main;

import hjzgg.id.IDblock;
import hjzgg.layer.TheLayer;
import hjzgg.set.MySet;
import hjzgg.size.TheSize;
import hjzgg.tank.EnemyTankThread;
import hjzgg.tank.MyTank;
import hjzgg.tank.ShellThread;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class MyLayeredPane extends JLayeredPane{
	private SelfConfigFrame scf = null;
	public MyLayeredPane(SelfConfigFrame f){
		this.scf = f;
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				scf.getSp().setVisible(false);
			}
		});
	}
}


class BlockPanel extends JPanel{
	private String img_path = null;
	private int height=0, width=0;
	private SelfConfigFrame scf = null;
	
	private void fillShape(MouseEvent e){
		 int x = e.getX();
		 int y = e.getY();
		 Point ptf = SwingUtilities.convertPoint(BlockPanel.this, new Point(x, y), getParent());
		 //块索引
		 int j = ptf.x / TheSize.block_height;
		 int i = ptf.y / TheSize.block_width;
		 
		 ShapePane sp = scf.getSp();
		 MyLayeredPane parent = scf.getMyLayeredPane();
		 String path = null;
		 if(scf.getSta()[i][j] != IDblock.home && scf.getSta()[i][j] != IDblock.tank_appear && SelfConfigFrame.choose != -1){
			 switch(SelfConfigFrame.choose){
			 	case IDblock.grass:
			 		path = "草地.png";
			 		break;
			 	case IDblock.steel_wall:
			 		path = "钢墙.jpg";
			 		break;
			 	case IDblock.wall:
			 		path = "砖墙.jpg";
			 		break;
			 	case IDblock.water:
			 		path = "水.jpg";
			 		break;
			 	case IDblock.not:
			 		path = null;
			 		break;
			 	default:
			 }
			 scf.getSta()[i][j] = SelfConfigFrame.choose;//当前 块 填充的类型
			 scf.getBlock()[i][j].setImgPath(path);
			 scf.getBlock()[i][j].update(scf.getBlock()[i][j].getGraphics());
			 sp.setBounds(ptf.x+5, ptf.y+5, TheSize.shapepane_width, TheSize.shapepane_height);
		 }
	}
	
	public BlockPanel(SelfConfigFrame f, String path, int w, int h){
		this.img_path = path;
		height = h;
		width = w;
		scf = f;
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				 int x = e.getX();
				 int y = e.getY();
				 Point ptf = SwingUtilities.convertPoint(BlockPanel.this, new Point(x, y), getParent());
				 ShapePane sp = scf.getSp();
				 MyLayeredPane parent = scf.getMyLayeredPane();
				 String path = null;
				 if(SelfConfigFrame.choose != -1){
					 if(!sp.isVisible()){
						 switch(SelfConfigFrame.choose){
						 	case IDblock.grass:
						 		path = "草地.png";
						 		break;
						 	case IDblock.steel_wall:
						 		path = "钢墙.jpg";
						 		break;
						 	case IDblock.wall:
						 		path = "砖墙.jpg";
						 		break;
						 	case IDblock.water:
						 		path = "水.jpg";
						 		break;
						 	case IDblock.not:
						 		path = null;
						 		break;
						 	default:
						 }
						 sp.setVisible(true);
						 sp.setImgPath(path);
					 }
					 sp.setBounds(ptf.x+5, ptf.y+5, TheSize.shapepane_width, TheSize.shapepane_height);
					 parent.updateUI();
				 }
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				fillShape(e);
			}
		});
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fillShape(e);
			}
		});
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		if(img_path==null) return;
		g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, width, height, this);
	}
	
	@Override
	protected void paintBorder(Graphics g) {
		 float lineWidth = 2.0f;
		 ((Graphics2D)g).setStroke(new BasicStroke(lineWidth));
		 ((Graphics2D)g).setColor(Color.GREEN);
		 ((Graphics2D)g).drawRect(0, 0, width, height);
	}
 
	public void setImgPath(String path){
		this.img_path = path;
	}
}

class ShapeButton extends JButton{
	private String img_path = null;
	public ShapeButton(String path){
		this.img_path = path;
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SelfConfigFrame.choose = Integer.parseInt(e.getActionCommand());
			}
		});
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		if(img_path==null) return;
		g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, TheSize.shapebtn_width, TheSize.shapebtn_height, this);
	}
	
}

public class SelfConfigFrame extends JFrame{
	
	public static int choose = -1;
	private ShapePane sp = new ShapePane(null, TheSize.shapepane_width, TheSize.shapepane_height);
	private MyLayeredPane p = null;
	private JPanel pl = null;
	private JPanel pr = null;
	private JPanel pu = null;
	
	private JButton beginGame = new JButton("开始游戏");
	private JButton reset = new JButton("重置自定义");
	public ShapePane getSp() {
		return sp;
	}

	public MyLayeredPane getMyLayeredPane() {
		return p;
	}

	public  int[][] getSta() {
		return sta;
	}

	private JPanel pd = null;
	
	private JPanel home = null;
	
	public  int[][] sta = new int[20][28];
	private BlockPanel[][] block = new BlockPanel[20][28];
	
	public BlockPanel[][] getBlock() {
		return block;
	}
	
	public void staInit(){
		//家所在的位置不能填充
		sta[17][12]=sta[17][13]=sta[17][14]=sta[17][15]=
		sta[18][12]=sta[18][13]=sta[18][14]=sta[18][15]=
		sta[19][12]=sta[19][13]=sta[19][14]=sta[19][15]=IDblock.home;
		
		//坦克出现的位置不能填充
		sta[0][0]=sta[0][1]=sta[1][1]=sta[1][0]=IDblock.tank_appear;
		sta[0][26]=sta[0][27]=sta[1][27]=sta[1][26]=IDblock.tank_appear;
		
		sta[18][9]=sta[19][9]=sta[19][8]=sta[18][8]=IDblock.tank_appear;
	}

	public void init(){
		for(int i=0; i<block.length; ++i)
			for(int j=0; j<block[i].length; ++j){
				block[i][j] = new BlockPanel(this, null, TheSize.block_width, TheSize.block_height);
				block[i][j].setBounds(j*TheSize.block_width, i*TheSize.block_height, TheSize.block_width, TheSize.block_height);
				block[i][j].setBackground(Color.BLACK);
				p.add(block[i][j]);
				p.setLayer(block[i][j], 1);
			}
		//图片随鼠标移动
		sp.setVisible(false);
		sp.setBounds(0, 0, TheSize.shapepane_width, TheSize.shapepane_height);
		p.add(sp);
		p.setLayer(sp, 3);
		//home图片
		home = new BlockPanel(this, "家.jpg", TheSize.block_width*4, TheSize.block_height*3);
		home.setBounds(12*TheSize.block_width, 17*TheSize.block_height, TheSize.block_width*4, TheSize.block_height*3);
		p.add(home);
		p.setLayer(home, 2);
		
		staInit();
	}
	
	public SelfConfigFrame(){
		p = new MyLayeredPane(this);
		pl = new JPanel();
		pr = new JPanel();
		pu = new JPanel();
		pd = new JPanel();
		
		setSize(1127, 800);
		p.setOpaque(true);
		p.setBackground(Color.BLACK);
		pl.setPreferredSize(new Dimension(0, 0));
		pr.setPreferredSize(new Dimension(130, 0));
		pu.setPreferredSize(new Dimension(0, 20));
		pd.setPreferredSize(new Dimension(0, 30));
		p.setPreferredSize(new Dimension(990, 700));
		p.setLayout(null);
		
		//添加障碍物图片
		JPanel title = new JPanel();
		title.setPreferredSize(new Dimension(100,50));
		title.add(new JLabel("自定义障碍物:"));
		pr.add(title);
		ShapeButton btn = new ShapeButton("草地.png");
		btn.setPreferredSize(new Dimension(TheSize.shapebtn_width, TheSize.shapebtn_height));
		btn.setActionCommand("2");
		pr.add(btn);
		pr.add(new JLabel("草地"));
		btn = new ShapeButton("钢墙.jpg");
		btn.setPreferredSize(new Dimension(TheSize.shapebtn_width, TheSize.shapebtn_height));
		btn.setActionCommand("3");
		pr.add(btn);
		pr.add(new JLabel("钢墙"));
		btn = new ShapeButton("水.jpg");
		btn.setPreferredSize(new Dimension(TheSize.shapebtn_width, TheSize.shapebtn_height));
		btn.setActionCommand("5");
		pr.add(btn);
		pr.add(new JLabel("    水"));
		btn = new ShapeButton("砖墙.jpg");
		btn.setPreferredSize(new Dimension(TheSize.shapebtn_width, TheSize.shapebtn_height));
		btn.setActionCommand("4");
		pr.add(btn);
		pr.add(new JLabel("   砖墙     "));
		btn = new ShapeButton(null);
		btn.setBackground(Color.BLACK);
		btn.setText("擦      除");
		btn.setPreferredSize(new Dimension(TheSize.shapebtn_width, TheSize.shapebtn_height/2));
		btn.setActionCommand("0");
		pr.add(btn);
		
		beginGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TankFrame tf = new TankFrame(sta);//将创建的地图传递到游戏中去
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
				SelfConfigFrame.this.dispose();
			}
		});
		
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 for(int i=0; i<sta.length; ++i) Arrays.fill(sta[i], 0);
				 staInit();
				 for(int i=0; i<block.length; ++i)
						for(int j=0; j<block[i].length; ++j){
							block[i][j].setImgPath(null);
							block[i][j].updateUI();
						}
			}
		});
		
		pr.add(beginGame);
		pr.add(reset);
		
		setLayout(new BorderLayout());
		add(pl, BorderLayout.WEST);
		add(pr, BorderLayout.EAST);
		add(pd, BorderLayout.SOUTH);
		add(pu, BorderLayout.NORTH);
		add(p, BorderLayout.CENTER);
		
		init();
	}
	
//	public static void main(String[] args){
//		SelfConfigFrame scf = new SelfConfigFrame();
//		scf.setResizable(false);
//		scf.setVisible(true);
//	}
}
