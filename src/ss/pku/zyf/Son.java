package ss.pku.zyf;

public class Son extends Father{

	public Son() {
		System.out.println("This is Son constructor!");
	}
	
	@Override
	public void smile() {
		System.out.println("Son smile...");
	}
	
	public static void main(String[] args) {
		Father s = new Son();
		s.smile();
		
		System.out.println(comAngle(12,30,0));
	}
	
	static float comAngle(int hour, int min, int sec) {
		float res;
		float a1 = (sec/60f + min) * 6;
		float a2 = ((sec/60f + min) / 60f + hour) * 30;
		res = a1 > a2 ? a1-a2 : a2-a1;
		if(res > 180) {
			res = 360 - 180;
		}
		return res;
	}
}
