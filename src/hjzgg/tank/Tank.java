package hjzgg.tank;

import hjzgg.Rect.Rect;
import hjzgg.id.IDblock;
import hjzgg.layer.TheLayer;
import hjzgg.set.MySet;
import hjzgg.size.TheSize;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Set;
import hjzgg.main.ShapePane;
import hjzgg.main.TankFrame;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public abstract class Tank extends JPanel implements Comparable<Tank>{
	//坦克的类型 
	private String tank_type = null;
	//坦克的id
	public int ID;
	//父面板
	private TankFrame tf = null;
	private JLayeredPane jlp = null;
	//坦克出现闪光记录次数
	public boolean tmpFlag = true;

	//线程终止条件
	public boolean flag = true;
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	private int cnt_appear = 0;
	//坦克的图片地址
	private String img_path = "坦克出现.gif", tmp_path=null;
	//坦克的炮弹
	private PanelShell ps = null;
	
	@Override
	public int compareTo(Tank tank) {
		return this.ID - tank.ID;
	}
	
	public Tank(String type, String path, int id, TankFrame tf){
		tank_type = type;
		tmp_path = path;
		this.ID = id;
		this.tf = tf;
		jlp = tf.getJlp();
	}
	//绘制背景
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(img_path != null)
			g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, TheSize.tank_width, TheSize.tank_height, this);
		if(++cnt_appear  >= 200 && tmpFlag){
			tmpFlag = false;
			turn(tmp_path);
		}
	}
	
	//坦克的四个方向
	public static final int left = 1;
	public static final int up = 2;
	public static final int down = 3;
	public static final int right = 4;
	
	//坦克的当前方向
	private int curDir = 2;
	
	//坦克是否可以移动
	public boolean if_can_move(Rect rect){
		Set<Tank> tankSet = MySet.getInstance().getTankSet();
		Set<ShapePane> otherSet = MySet.getInstance().getOtherSet();
		if(rect.x1<0 || rect.x2>980 || rect.y1<0 || rect.y2>700) return false;
		for(Tank x : tankSet)
			if(x!=this && Rect.isCorss(rect, new Rect(x.getX(), x.getY(), x.getX()+x.getWidth(), x.getY()+x.getHeight())))
				 return false;
		for(ShapePane x : otherSet)
			if(Rect.isCorss(rect, new Rect(x.getX(), x.getY(), x.getX()+x.getWidth(), x.getY()+x.getHeight())) && x.getId() != IDblock.grass)
				 return false;
		return true;
	}
	//移动
	public void move(int speed){
			switch(curDir){
				case Tank.down:
					if(if_can_move(new Rect(getX(), getY()+speed, getX()+getWidth(), getY()+speed+getHeight())))
						setBounds(getX(), getY()+speed, getWidth(), getHeight());
					break;
				case Tank.left:
					if(if_can_move(new Rect(getX()-speed, getY(), getX()-speed+getWidth(), getY()+getHeight())))
						setBounds(getX()-speed, getY(), getWidth(), getHeight());
					break;
				case Tank.right:
					if(if_can_move(new Rect(getX()+speed, getY(), getX()+speed+getWidth(), getY()+getHeight())))
						setBounds(getX()+speed, getY(), getWidth(), getHeight());
					break;
				case Tank.up:
					if(if_can_move(new Rect(getX(), getY()-speed, getX()+getWidth(), getY()-speed+getHeight())))
						setBounds(getX(), getY()-speed, getWidth(), getHeight());
					break;
			}
	}
	
	class BumpDemo implements Runnable{
		private PanelBump pb = null;
		private String dir = null;
		public BumpDemo(PanelBump pb, String direction){
			this.pb = pb;
			this.dir = direction;
		}
		public void run(){
			for(int i=1; i<=14; ++i){
				try {
					pb.setBumpPath("爆炸/" + dir + "/爆炸" + i + ".png");
					pb.updateUI();
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			pb.setVisible(false);
			jlp.remove(pb);
		}
	}
	
	//发射炮弹
	public void shell(){
				String dir = null;
				switch(curDir){
					case Tank.down:
						dir = "down";
						break;
					case Tank.left:
						dir = "left";
						break;
					case Tank.right:
						dir = "right";
						break;
					case Tank.up:
						dir = "up";
						break;
				}
				if(dir != null){
					PanelBump pb = null;
					String path = "炮弹/" + dir + "/";
					if(tank_type.equals("mytank"))
						path += "炮弹enemy4.png";
					else path += "炮弹" + tank_type + ".png";
					ps = new PanelShell(path, ID, dir, tf);//添加炮弹
					ps.setOpaque(false);
					
					int x = getX();
					int y = getY();
					int x1=0, y1=0;
					switch(getCurDir()){
						case Tank.down:
							x += getWidth()/2 - 6;
							y += getHeight() - TheSize.shell_width/2;
							x1 = x - 15;
							y1 = y;
							break;
						case Tank.left:
							x -= TheSize.shell_width/2;
							y += getHeight()/2 - 8;
							x1 = x-22;
							y1 = y-20;
							break;
						case Tank.right:
							x += getWidth() - TheSize.shell_width/2;
							y += getHeight()/2 - 8;
							x1 = x+15;
							y1 = y-20;
							break;
						case Tank.up:
							x += getWidth()/2 - 6;
							y -= TheSize.shell_height;
							x1 = x-20;
							y1 = y-30;
							break;
					}
					if(dir.equals("left") || dir.equals("right"))
					   ps.setBounds(x, y, TheSize.shell_width, TheSize.shell_height);//设置炮弹的初始位置
					else 
					   ps.setBounds(x, y, TheSize.shell_height, TheSize.shell_width);
					try{
						jlp.add(ps, TheLayer.shell, -1);
						MySet.getInstance().getShellSet().add(ps);//添加到炮弹集合中去
						pb = new PanelBump(null, 1);//爆炸图片
						pb.setOpaque(false);
						pb.setBounds(x1, y1, TheSize.pumb_width, TheSize.pumb_height);//设置炮弹射出时冒烟图片位置
						jlp.add(pb, TheLayer.pumb, -1);
						new Thread(new BumpDemo(pb, dir)).start();
					} catch (Exception e) {
						//ArrayIndexOutOfBoundsException: No such child: 86
						//java.lang.IllegalArgumentException: illegal component position
						e.printStackTrace();
					}
				}
	}
	//转向
	public void turn(String path){
		this.img_path = path;
		updateUI();
	}
	
	public String getTankType(){
		return tank_type;
	}
	
	public void setCurDir(int dir){
		this.curDir = dir;
	}
	
	public int getCurDir() {
		return curDir;
	}
	
}
