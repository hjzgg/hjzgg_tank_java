package hjzgg.main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ShapePane extends JPanel implements Comparable{
	private String img_path = null;
	private int w, h, id=-1;
	public ShapePane(String path, int w, int h){
		this.img_path = path;
		this.w = w;
		this.h = h;
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(img_path != null)
			g.drawImage(new ImageIcon(img_path).getImage(), 0, 0, w, h, this);
		else{
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, w, h);
		}
	}
	public void setImgPath(String path){
		this.img_path = path;
	}
	@Override
	public int compareTo(Object o) {
		return o.hashCode() - this.hashCode();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
