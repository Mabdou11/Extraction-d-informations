package ext;

import javax.swing.JFrame;

public class Affiche1 extends Asp4Vidal {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static String Name;
	protected static int A;
	protected static int B;

	public Affiche1(String name, int a, int b) {
		Affiche1.A = a;
		Affiche1.B = b;
		Affiche1.Name = name;
	}

	static JFrame f;

	public static void F() {

		f.setSize(Affiche1.A, Affiche1.B);
		f.setName(Name);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(500, 500);
		f.setVisible(true);
	}
}
