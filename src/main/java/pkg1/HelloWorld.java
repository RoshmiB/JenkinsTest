package pkg1;

public class HelloWorld {
	
	public int add(int x, int y) {
		
		int sum = x+y;
		return sum;
		
	}

	public static void main(String[] args) {
		
		System.out.println("Simple hello world");
		HelloWorld hw = new HelloWorld();
		hw.add(5,7);

	}

}
