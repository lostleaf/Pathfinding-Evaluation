package ui;

//    Last Change:  2012-01-08 11:38:41
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class UI extends JFrame {

	private static final long serialVersionUID = -9191726739099594691L;
	private ControlPanel ctrl;
	private ImagePanel img;

	public UI() throws IOException {
		super("Gps系统");
		ctrl = new ControlPanel();
		img = new ImagePanel();

		// ctrl.setFinder(new Finder());
		ctrl.setImgPanel(img);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(img, BorderLayout.EAST);
		this.getContentPane().add(ctrl, BorderLayout.CENTER);
		ctrl.validate();

		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("帮助");
		JMenuItem item = new JMenuItem("帮助");

		item.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					File directory = new File(this.getClass().getResource("")
							.toURI());

					System.out.println();
					Runtime.getRuntime().exec("cmd /c 帮助文件.pdf", null,
							directory.getParentFile());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// System.out.println(System.getProperty("user.home"));
				catch (URISyntaxException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}

		});
		menu.add(item);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		this.pack();
		this.setVisible(true);
	}

	public static void main(String args[]) {
		Runnable doCreateAndShowGUI = new Runnable() {
			public void run() {
				try {
					new UI();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(doCreateAndShowGUI);

	}
}
