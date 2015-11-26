package hjzgg.tank;

import hjzgg.main.TankFrame;
import hjzgg.size.TheSize;

import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

public class MyTank extends Tank{
	//坦克的速度e
    private int speed = 15;
	public MyTank(String type, String path, int id, TankFrame tf){
		super(type, path, id, tf);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				int dir = 0;
				switch(arg0.getKeyCode()){
					case KeyEvent.VK_LEFT:
						dir = 1;
						break;
					case KeyEvent.VK_RIGHT:
						dir = 4;
						break;
					case KeyEvent.VK_UP:
						dir = 2;
						break;
					case KeyEvent.VK_DOWN:
						dir = 3;
						break;
					case KeyEvent.VK_ENTER:
						shell();
						break;
					default:
						
				}
				if(dir == getCurDir() || dir == 0) {
					if(dir!=0) {
						setCurDir(dir);
						move(speed);
					}
					return;
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
				if(dirStr != null){
					setCurDir(dir);
					turn("tank/" + getTankType() + "_" + dirStr + ".gif");
				}
			}
		});
	}
}
