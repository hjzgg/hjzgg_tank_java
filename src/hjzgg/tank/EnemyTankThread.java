package hjzgg.tank;

import hjzgg.Rect.Rect;
import hjzgg.layer.TheLayer;
import hjzgg.main.TankFrame;
import hjzgg.set.MySet;
import hjzgg.size.TheSize;

import java.util.Random;
import java.util.Set;

import javax.swing.JLayeredPane;

public class EnemyTankThread implements Runnable{
	private TankFrame tf = null;
	private int cnt_ett = 0;
	private boolean flag = true;
	public EnemyTankThread(TankFrame tf){
		this.tf = tf;
	}
	@Override
	public void run() {
		JLayeredPane jlp = tf.getJlp();
		Random rd = new Random();
		int[][] sta = tf.getSta();
		String[] dir={"left", "up", "down", "right"};
		while(flag){
			Set<Tank> tankSet = MySet.getInstance().getTankSet();
			if(tankSet.size() < 11){//一共20个tank
				int type = Math.abs(rd.nextInt())%3 + 1;//产生enemyTank的类型
				int d = Math.abs(rd.nextInt())%4;
				EnemyTank etank = null;
				boolean is_cross = false;
			    for(Tank x : tankSet){
			    	if(Rect.isCorss(new Rect(0,0,70,70), new Rect(x.getX(), x.getY(), x.getX()+x.getWidth(), x.getY()+x.getHeight()))){
			    		is_cross = true;
			    		break;
			    	}
			    }
			    if(!is_cross){
				    	etank = new EnemyTank("enemy"+type, "tank/enemy"+ type + "_" + dir[d]+ ".png", ++TankFrame.ID, tf);
						etank.setBounds(0, 0, TheSize.tank_width, TheSize.tank_height);
				}else{
						is_cross = false;
						for(Tank x : tankSet){
					    	if(Rect.isCorss(new Rect(910,0,980,70), new Rect(x.getX(), x.getY(), x.getX()+x.getWidth(), x.getY()+x.getHeight()))){
					    		is_cross = true;
					    		break;
					    	}
					    }
						if(!is_cross){
							etank = new EnemyTank("enemy"+type, "tank/enemy"+ type + "_" + dir[d]+ ".png", ++TankFrame.ID, tf);
							etank.setBounds(910, 0, TheSize.tank_width, TheSize.tank_height);
						}
				}
				if(etank != null  && ++cnt_ett <= 20){
					try{
						jlp.add(etank, TheLayer.tank, -1);
						etank.setCurDir(d+1);
						tankSet.add(etank);
						try {//也就是当tank出现动画结束之后，才执行动作，否则图片冲突
							Thread.sleep(2000);
						} catch (InterruptedException e) {
						}
						new Thread(etank).start();
					} catch(Exception e){
						//IllegalArgumentException: illegal component position 有待解决
						e.printStackTrace();
					}
				}
			}
			try {//间隔一定的时间出现下一个tank
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
}
