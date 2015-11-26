package hjzgg.tank;

import hjzgg.main.TankFrame;

import java.util.Random;

import javax.swing.JLayeredPane;

public class EnemyTank extends Tank implements Runnable{
	//坦克的速度e
	private int speed = 0;
	private int f_sleep = 0;//坦克移动的频率
	public EnemyTank(String type, String path, int id, TankFrame tf){
		super(type, path, id, tf);
		flag = true;
		if(type.equals("enemy3")) {
			speed = 15;
			f_sleep = 100;
		}
		else {
			speed = 10;
			f_sleep = 500;
		}
	}
	public void run(){
		Random rd = new Random();
		while(flag){
			int key = Math.abs(rd.nextInt())%6;
				switch(key){
					case 0:
					case 1:
					case 2:
						if(getCurDir()==Tank.down) move(speed);
						break;
					case 3://向当前方向移动
						move(speed);
						break;
					case 4://转向
						int dir = Math.abs(rd.nextInt())%4 + 1;
						if(dir == getCurDir()){
							move(speed);
							break;
						}
						String dirStr = null;
						switch(dir){
							case 1:
								dirStr = "left";
								break;
							case 2:
								dirStr = "up";
								break;
							case 3:
								dirStr = "down";
								break;
							case 4:
								dirStr = "right";
								break;
						}
						if(dirStr != null && flag){
							setCurDir(dir);
							turn("tank/" + getTankType() + "_" + dirStr + ".png");
						}
						break;
					default://保持不动
						key = Math.abs(rd.nextInt())%100;
						if(key % 5 ==0)
							shell();//发射炮弹
						break;
				}
				try {
					Thread.sleep(f_sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
}
