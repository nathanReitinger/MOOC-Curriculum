package Week2;
import edu.princeton.cs.algs4.StdIn;

public class Subset {
	public static void main(String[] args)
	{
		int k = Integer.parseInt(args[0]);
		
		RandomizedQueue<String> toPrint = new RandomizedQueue<String>();
		
		while (!StdIn.isEmpty()) {
			String toRead = StdIn.readString();
			toPrint.enqueue(toRead);
		}
		
		for (int q = 0; q < k; q++) {
			System.out.println(toPrint.dequeue());
		}
	}
}
