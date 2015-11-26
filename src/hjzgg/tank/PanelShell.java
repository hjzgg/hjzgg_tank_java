package hjzgg.tank;

import hjzgg.Rect.Rect;
import hjzgg.id.IDblock;
import hjzgg.layer.TheLayer;
import hjzgg.main.BeginFrame;
import hjzgg.main.ShapePane;
import hjzgg.main.TankFrame;
import hjzgg.set.MySet;
import hjzgg.size.TheSize;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class PanelShell extends JPanel implements Comparable<PanelShell>{
	public static boolean is_synchronized = false;
	private String img_path = null;
	public void setImg_path(String img_path) {
		this.img_path = img_path;
	}
	private int isGameOver = 0;
	private static int cnt_ett = 0;//记录击杀tank的数量
	private int ID = 0;
	private String dir = null;
	private TankFrame tf = null;
	private JLayeredPane jlp = null;
	
	
	//炮弹的速度e
	private int speed = 5;
	
	public PanelShell(String path, int id, String dir, TankFrame tf){
		this.img_path = path;
		this.ID = id;
		this.dir = dir;
		this.tf = tf;
		jlp = tf.getJlp();
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		if(dir.equals("left") || dir.equals("right"))
			g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, TheSize.shell_width, TheSize.shell_height, this);
		else 
			g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, TheSize.shell_height, TheSize.shell_width, this);
	}
	@Override
	public int compareTo(PanelShell ps) {
		return 1;//因为ID表示的是这个炮弹是属于哪一个坦克的，一个坦克可以有多个炮弹
	}
	
	class Bump3Demo implements Runnable{
		private PanelBump x = null;
		public Bump3Demo(PanelBump x){
			this.x = x;
		}
		@Override
		public void run() {
			try {
				Thread.sleep(500);
				if(isGameOver!=1){
					x.setVisible(false);
					jlp.remove(x);
				} else 
					x.setBumpPath("破坏之后.jpg");
				
				if(isGameOver != 0){//游戏结束，终止线程
					MySet.getInstance().getSt().setFlag(false);//终止炮弹的线程
					MySet.getInstance().getEtt().setFlag(false);//终止坦克出现的线程
					Set<Tank> tank = MySet.getInstance().getTankSet();
					for(Tank x : tank) x.setFlag(false);//终止tank的线程
					MySet.resetMySet();//置空
					String title = null;
					if(isGameOver == 3) title = "闯关成功";
					else title = "闯关失败";
					if(JOptionPane.YES_OPTION != JOptionPane.showConfirmDialog(null, 
							 "是否继续游戏？", title, JOptionPane.YES_NO_OPTION)){
						 JOptionPane.getFrameForComponent(jlp).dispose();
					} else {
						JOptionPane.getFrameForComponent(jlp).dispose();
						BeginFrame bf = new BeginFrame();
						bf.setVisible(true);
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	class Bump2Demo implements Runnable{
		private JComponent x = null;
		public Bump2Demo(JComponent x){
			this.x = x;
		}
		@Override
		public void run() {
			PanelBump pb = null;
			x.setVisible(false);
			jlp.remove(x);
			if(x instanceof Tank){//tank爆炸
				((Tank)x).setFlag(false);
				pb = new PanelBump("destory.gif", 2);
				pb.setBounds(x.getX(), x.getY(), x.getWidth(), x.getHeight());
				MySet.getInstance().getTankSet().remove(x);
				if(((Tank) x).ID == 0) isGameOver = 2;
				else{//将毁掉的tank数量加 1，并将modalTank添加到右面板上
					JPanel pr = tf.getPr();
					ShapePane sp = new ShapePane("modalTank.png", 50, 50);
					sp.setPreferredSize(new Dimension(50, 50));
					pr.add(sp);
					pr.updateUI();
					if(++cnt_ett % 20 == 0) isGameOver = 3;
				}
			}
			else if(x instanceof ShapePane){
				MySet.getInstance().getOtherSet().remove(x);
				if(((ShapePane)x).getId() == IDblock.home){//家爆炸
					pb = new PanelBump("destory.gif", 4);
					isGameOver = 1;
				}
				else //砖块爆炸
					pb = new PanelBump("destory.gif", 3);
				pb.setBounds(x.getX(), x.getY(), x.getWidth(), x.getHeight());
			}	
			else{//子弹爆炸
				pb = new PanelBump("destory.gif", 5);
				if(dir.equals("left"))
					  pb.setBounds(x.getX(), x.getY(), TheSize.shell_height, TheSize.shell_height);
				else if(dir.equals("right"))
					  pb.setBounds(x.getX()+x.getWidth(), x.getY(), TheSize.shell_height, TheSize.shell_height);
				else if(dir.equals("up"))
					  pb.setBounds(x.getX(), x.getY(), TheSize.shell_height, TheSize.shell_height);
				else if(dir.equals("down"))
					  pb.setBounds(x.getX(), x.getY()+x.getHeight(), TheSize.shell_height, TheSize.shell_height);
			}
			jlp.add(pb, TheLayer.pumb, -1);
			jlp.updateUI();
			new Thread(new Bump3Demo(pb)).start();
		}
	}
	
	//炮弹是否可以移动
	public boolean if_can_move(Rect rect){
		Set<Tank> tankSet = MySet.getInstance().getTankSet();
		Set<ShapePane> otherSet = MySet.getInstance().getOtherSet();
		Set<PanelShell> shellSet = MySet.getInstance().getShellSet();
		if(!Rect.isCorss(new Rect(0,0,980,700), new Rect(getX(),getY(), getX()+getWidth(), getY()+getHeight()))){
			shellSet.remove(this);
			jlp.remove(this);
			return false;
		}
		boolean is_cross = false;
		try{
			Object[] set = tankSet.toArray(); 
			for(Object x : set){
				Tank tankx = (Tank)x;
				if(isVisible() && Rect.isCorss(rect, new Rect(tankx.getX(), tankx.getY(), tankx.getX()+tankx.getWidth(), tankx.getY()+tankx.getHeight()))){
					 if( this.ID * tankx.ID == 0 && this.ID + tankx.ID != 0){//炮弹撞到敌方tank
						 is_cross = true;
						 new Thread(new Bump2Demo(tankx)).start();
						 break;
					 }
				}
			}
			set = otherSet.toArray();
			for(Object x : set){
				ShapePane spx = (ShapePane)x;
				if(isVisible() && Rect.isCorss(rect, new Rect(spx.getX(), spx.getY(), spx.getX()+spx.getWidth(), spx.getY()+spx.getHeight()))){
					if(spx.getId() == IDblock.wall || spx.getId() == IDblock.home){
						 is_cross = true;
						 new Thread(new Bump2Demo(spx)).start();
						 break;
					} else if(spx.getId() == IDblock.steel_wall){//子弹爆炸
						 is_cross = true;
						 new Thread(new Bump2Demo(this)).start();
						 break;
					}
				}
			}
			
			set = shellSet.toArray();
			for(Object x : set){//撞到敌方子弹
				PanelShell ssx = (PanelShell)x;
				if(isVisible() && Rect.isCorss(rect, new Rect(ssx.getX(), ssx.getY(), ssx.getX()+ssx.getWidth(), ssx.getY()+ssx.getHeight()))){
					if(this.ID * ssx.ID == 0 && this.ID + ssx.ID != 0){
						 is_cross = true;
						 new Thread(new Bump2Demo(ssx)).start();
						 new Thread(new Bump2Demo(this)).start();
						 break;						
					}
				}
			}
		} catch(Exception e){
			e.printStackTrace();//ConcurrentModificationException 同步问题有待解决
		}
		
		if(is_cross == true){
			setVisible(false);
			jlp.remove(this);
			shellSet.remove(this);
			return false;
		}
		return true;
	}
	
	public void move(){
			if(dir.equals("up") && if_can_move(new Rect(getX(), getY()-speed, getX()+getWidth(), getY()-speed+getHeight())))
				setBounds(getX(), getY()-speed, getWidth(), getHeight());
			else if(dir.equals("down") && if_can_move(new Rect(getX(), getY()+speed, getX()+getWidth(), getY()+speed+getHeight())))
				setBounds(getX(), getY()+speed, getWidth(), getHeight());
			else if(dir.equals("left") && if_can_move(new Rect(getX()-speed, getY(), getX()-speed+getWidth(), getY()+getHeight())))
				setBounds(getX()-speed, getY(), getWidth(), getHeight());
			else if(dir.equals("right") && if_can_move(new Rect(getX()+speed, getY(), getX()+speed+getWidth(), getY()+getHeight())))
				setBounds(getX()+speed, getY(), getWidth(), getHeight());
	}
}