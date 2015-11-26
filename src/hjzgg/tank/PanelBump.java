package hjzgg.tank;

import hjzgg.size.TheSize;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class PanelBump extends JPanel{
	private String bumpPath = null;
	private int type = 0;
	public PanelBump(String bumpPath, int type) {
		super();
		this.bumpPath = bumpPath;
		this.type = type;
 	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);	
		if(bumpPath != null){
			switch(type){
				case 1:	
				    g.drawImage(new ImageIcon(bumpPath).getImage(), 0, 0, TheSize.pumb_width, TheSize.pumb_height, this);
				    break;
				case 2:
					g.drawImage(new ImageIcon(bumpPath).getImage(), 0, 0, TheSize.tank_width, TheSize.tank_height, this);
					break;
				case 3:
					g.drawImage(new ImageIcon(bumpPath).getImage(), 0, 0, TheSize.block_width, TheSize.block_height, this);
					break;
				case 4:
					g.drawImage(new ImageIcon(bumpPath).getImage(), 0, 0, TheSize.home_width, TheSize.home_height, this);
					break;
				case 5:
					g.drawImage(new ImageIcon(bumpPath).getImage(), 0, 0, TheSize.shell_height, TheSize.shell_height, this);
					break;	
			}
		}
	}
	
	public void setBumpPath(String path){
		this.bumpPath = path;
	}
}