package hjzgg.Rect;

public class Rect {
	public int x1,x2, y1,y2;
	public Rect(int x1, int y1, int x2, int y2){
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}
	
	public static boolean isCorss(Rect r1, Rect r2){
		 int cx1 = Math.max(r1.x1, r2.x1);
		 int cy1 = Math.max(r1.y1, r2.y1);
		 int cx2 = Math.min(r1.x2, r2.x2);
		 int cy2 = Math.min(r1.y2, r2.y2);
		 
		 if(cx1 > cx2) return false;
		 if(cy1 > cy2) return false;
		 if( (cx2-cx1)*(cy2-cy1) == 0) return false;
		 
		 return true;
	}
}