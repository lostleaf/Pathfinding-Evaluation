package ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import storage.Graph;

public class ImagePanel extends JPanel implements MouseMotionListener,
		MouseListener, MouseWheelListener {

	private static final long serialVersionUID = 1L;
	private BufferedImage image = null;
	private Point startPoint = null, endPoint = null, mP = null;
	private int path[] = null;
	private int ixImage, iyImage, sRate = 5;
	private int highLightPath = -1, highLightPoint = -1;
	private ControlPanel ctrl = null;

	ImagePanel() throws IOException {
		URL url = getClass().getResource("../image/3.png");
		image = ImageIO.read(url);
		this.setPreferredSize(new Dimension(800, 600));
		addMouseMotionListener(this);
		addMouseListener(this);
		addMouseWheelListener(this);
	}

	public void setPath(int path[]) {
		this.path = path;
		repaint();
	}

	public void setControlPanel(ControlPanel cp) {
		ctrl = cp;
	}

	public void setHighLight(int index) throws NumberFormatException,
			IOException {
		if (index > path.length || index < 0) {
			highLightPath = -1;
			return;
		}
		highLightPath = path[index];
		Point p = Graph.getPosition(Graph.getEdgeFrom(highLightPath));
		doMove((-ixImage - p.x + 300) * sRate / 5, (-iyImage - p.y + 300)
				* sRate / 5);
		repaint();
	}

	protected void min(Point p) {

		if (sRate > 1) {
			doMove(-p.x, -p.y);
			sRate--;
			doMove(p.x, p.y);
			repaint();
		}
	}

	protected void max(Point p) {

		if (sRate < 9) {
			doMove(-p.x, -p.y);
			sRate++;
			doMove(p.x, p.y);
			repaint();
		}
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		ixImage = 0;
		iyImage = 0;
		repaint();
	}

	private void paintPath(Graphics g) throws NumberFormatException,
			IOException {
		g.setColor(new Color(0, 64, 64));
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(sRate<<1));
		if (path != null)
			for (int i = 0; i < path.length; i++) {
				Point point[] = Graph.getEdgePoints(path[i]);
				for (int j = 0; j < point.length - 1; j++) {
					Point p1 = transform(point[j]);
					Point p2 = transform(point[j + 1]);
					g.drawLine(p1.x, p1.y, p2.x, p2.y);
				}
			}
	}

	private void paintHighLight(Graphics g) throws NumberFormatException,
			IOException {
		g.setColor(Color.red);
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(sRate<<1));
		if (highLightPoint > -1) {
			//System.out.println("doit");
			Point p = transform(Graph.getPosition(highLightPoint));
			g.fillOval(p.x - 5, p.y - 5, 2 * 5, 2 * 5);
		}
		if (highLightPath != -1) {

			Point point[] = Graph.getEdgePoints(highLightPath);
			for (int j = 0; j < point.length - 1; j++) {
				// System.out.println("i");
				Point p1 = transform(point[j]);
				Point p2 = transform(point[j + 1]);
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
		}
	}

	private void paintPointsAround(Graphics g) throws NumberFormatException,
			IOException {
		// System.out.println("aaaaa");
		if (mP == null)
			return;
		g.setColor(Color.blue);
		for (int i = 0; i < Graph.getNum(); i++) {
			Point p = transform(Graph.getPosition(i));
			if (sqrDist(mP, p) <= 100) {
				g.fillOval(p.x - 5, p.y - 5, 2 * 5, 2 * 5);
				String name = Graph.getPointName(i);
				if (name.length() < 1)
					name = String.valueOf(i);
				g.drawString(name, p.x, p.y - 6);
			}
		}
	}

	public void paint(Graphics g) {
		super.paint(g);
		g.setClip(0, 0, this.getWidth(), this.getWidth());
		if (ixImage > 0)
			ixImage = 0;
		if (iyImage > 0)
			iyImage = 0;
		if (ixImage < 5 * this.getWidth() / sRate - image.getWidth())
			ixImage = 5 * this.getWidth() / sRate - image.getWidth();
		if (iyImage < 5 * this.getHeight() / sRate - image.getHeight())
			iyImage = 5 * this.getHeight() / sRate - image.getHeight();
		g.drawImage(image, ixImage * sRate / 5, iyImage * sRate / 5,
				image.getWidth() * sRate / 5, image.getHeight() * sRate / 5,
				getParent());
		try {
			paintPath(g);
			paintHighLight(g);
			paintPointsAround(g);
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	protected void doMove(int x, int y) {
		ixImage += x * 5 / sRate;
		iyImage += y * 5 / sRate;
	}

	public void mouseDragged(MouseEvent e) {
		endPoint = e.getPoint();
		doMove(endPoint.x - startPoint.x, endPoint.y - startPoint.y);
		repaint();
		startPoint = endPoint;
	}

	private static void createAndShowGUI() throws IOException {
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImagePanel p = new ImagePanel();
		f.add(p);
		f.setSize(800, 600);
		f.setVisible(true);
	}

	public static void main(String args[]) {
		Runnable doCreateAndShowGUI = new Runnable() {
			public void run() {
				try {
					createAndShowGUI();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		SwingUtilities.invokeLater(doCreateAndShowGUI);
	}

	public void mousePressed(MouseEvent e) {
		startPoint = e.getPoint();
	}

	public void mouseWheelMoved(MouseWheelEvent e) {

		if (e.getWheelRotation() < 0)
			max(e.getPoint());
		else
			min(e.getPoint());
	}

	public void mouseMoved(MouseEvent e) {
		// System.out.println("eee");
		mP = new Point(e.getPoint());
		if (mP.x == 0 || mP.y == 0)
			mP = null;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {
		if (ctrl == null)
			return;
		int ch = -1;
		mP = new Point(e.getPoint());
		try {
			for (int i = 0; i < Graph.getNum(); i++) {
				Point p = transform(Graph.getPosition(i));
				if (sqrDist(mP, p) <= 100)
					if (ch > -1)
						return;
					else
						ch = i;

			}
			if (ch == -1)
				return;
			// System.out.println(ch);
			if (e.getModifiers() == 16)
				ctrl.setSrc(ch);
			else
				ctrl.setTar(ch);

		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	private Point transform(Point p) {
		return new Point((p.x + ixImage) * sRate / 5, (p.y + iyImage) * sRate
				/ 5);
	}

	private int sqrDist(Point a, Point b) {
		return (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
	}

	public void highlightPoint(int index) throws NumberFormatException,
			IOException {
		if (index > Graph.getNum() || index < 0) {
			highLightPoint = -1;
			return;
		}
		//System.out.println(index);
		highLightPoint = index;
		Point p = Graph.getPosition(index);
		doMove((-ixImage - p.x + 300) * sRate / 5, (-iyImage - p.y + 300)
				* sRate / 5);
		repaint();

	}
}