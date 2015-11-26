package hjzgg.tank;

import java.util.Iterator;
import java.util.Set;

import hjzgg.set.MySet;

public class ShellThread implements Runnable{
	private boolean flag = true;
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	@Override
	public void run() {
		while(flag){
			try{
				Set<PanelShell> shellSet = MySet.getInstance().getShellSet();
				Object[] ss = shellSet.toArray();
				for(Object ps : ss)
					((PanelShell)ps).move();
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch(Exception e){
				e.printStackTrace();//ConcurrentModificationException сп╢Щ╫Б╬Ж
			}
		}
	}
}
