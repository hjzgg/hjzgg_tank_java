package hjzgg.set;

import java.util.Set;
import java.util.TreeSet;

import javax.swing.JPanel;

import hjzgg.main.ShapePane;
import hjzgg.tank.EnemyTankThread;
import hjzgg.tank.PanelShell;
import hjzgg.tank.ShellThread;
import hjzgg.tank.Tank;
public class MySet {
	private Set<Tank> tankSet = new TreeSet<Tank>();//坦克的集合
	private EnemyTankThread ett = null;
	private ShellThread st = null;
	public Set<Tank> getTankSet() {
		return tankSet;
	}

	public Set<ShapePane> getOtherSet() {
		return otherSet;
	}


	public Set<PanelShell> getShellSet() {
		return shellSet;
	}

	private Set<ShapePane> otherSet = new TreeSet<ShapePane>();//静止障碍的集合
	private Set<PanelShell> shellSet = new TreeSet<PanelShell>();//炮弹的集合
	
	private static MySet myset = null;
	
	private MySet(){}
	
	public static MySet getInstance(){
		if(myset == null) myset = new MySet();
		return myset;
	}
	
	public static void resetMySet(){
		myset = null;
	}

	public EnemyTankThread getEtt() {
		return ett;
	}

	public void setEtt(EnemyTankThread ett) {
		this.ett = ett;
	}

	public ShellThread getSt() {
		return st;
	}

	public void setSt(ShellThread st) {
		this.st = st;
	}
	
}
