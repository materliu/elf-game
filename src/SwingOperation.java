import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class SwingOperation {
	public static void run(final JFrame jf, final int WIDTH, final int HEIGHT) {
		SwingUtilities.invokeLater(new Thread() {
			public void run() {
				jf.setSize(WIDTH, HEIGHT);
				jf.setVisible(true);
				jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}
}
