package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import storage.Graph;
import filter.Filter;
import finder.Finder;

public class ControlPanel extends JPanel implements ActionListener {

	class StepList extends JList implements ListSelectionListener {

		private static final long serialVersionUID = 1L;
		private DefaultListModel model;

		public StepList() {
			model = new DefaultListModel();
			this.addListSelectionListener(this);
			this.setModel(model);
		}

		public void setStep(int result[]) throws NumberFormatException,
				IOException {
			model.removeAllElements();

			for (int i = 0; i < result.length; i++)
				model.addElement("从"
						+ Graph.getPointName(Graph.getEdgeFrom(result[i]))
						+ "出发，经过" + Graph.getEdgeDis(result[i]) * 10 + "米,到达"
						+ Graph.getPointName(Graph.getEdgeTo(result[i])));

		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() || e.getFirstIndex() == -1)
				return;
			if (img != null)
				try {
					img.setHighLight(this.getSelectedIndex());
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}

	}

	class AlgoChooser extends JComboBox implements ActionListener {

		private static final long serialVersionUID = 1L;

		public AlgoChooser() {
			super(Finder.algoList);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) {
			int index = this.getSelectedIndex();
			try {
				finder.setAlgorithm(index);
			} catch (NumberFormatException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	class ConfirmPanel extends JPanel implements ActionListener,
			ListSelectionListener {

		private static final long serialVersionUID = 1L;

		private String str[] = { "source", "target" };
		private JLabel label;
		private JButton button;
		private JList list;
		private int step;
		private DefaultListModel model;
		private int pIndex[];

		public ConfirmPanel() {
			step = 0;
			label = new JLabel("Please confirm the" + str[step]);
			button = new JButton("Confirm");
			model = new DefaultListModel();
			list = new JList(model);
			button.addActionListener(this);
			list.setPreferredSize(new Dimension(300, 200));
			list.addListSelectionListener(this);

			this.setLayout(new BorderLayout());
			this.add(label, BorderLayout.NORTH);
			this.add(list, BorderLayout.CENTER);
			this.add(button, BorderLayout.SOUTH);

			// this.setPreferredSize(new Dimension(300, 400));

		}

		public void reset() {
			// this.setVisible(true);
			srcIndex = tarIndex = -1;
			step = 0;
			label.setText("Please confirm the " + str[step]);
			try {
				pIndex = Filter.filter(src.getText());
				model.clear();
				for (int i = 0; i < pIndex.length; i++)
					model.addElement(Graph.getPointName(pIndex[i]));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// System.out.println("label2");
		}

		public void actionPerformed(ActionEvent e) {
			if (list.getSelectedIndex() == -1) {
				JOptionPane.showMessageDialog(this.getParent(), "请选择！");
				return;
			}
			if (step == 0) {
				int index = pIndex[list.getSelectedIndex()];
				srcIndex = index;

				try {

					src.setText(Graph.getPointName(index));
					pIndex = Filter.filter(tar.getText());

					model.clear();
					for (int i = 0; i < pIndex.length; i++)
						model.addElement(Graph.getPointName(pIndex[i]));

				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();

				}
				label.setText("Please confirm the " + str[++step]);
			} else {

				int index = pIndex[list.getSelectedIndex()];
				tarIndex = index;

				try {
					tar.setText(Graph.getPointName(index));
					this.setVisible(false);

					int result[] = finder.find(srcIndex, tarIndex);
					stepList.setStep(result);

					if (img != null) {
						img.setPath(result);
						img.setHighLight(-1);
					}

					jsp.setVisible(true);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		}

		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (e.getValueIsAdjusting() || list.getSelectedIndex() < 0)
				return;
			if (img != null)
				try {
					img.highlightPoint(pIndex[list.getSelectedIndex()]);
				} catch (NumberFormatException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
	}

	private static final long serialVersionUID = -5636955772550575980L;
	private JLabel srcLab, tarLab;
	private JTextField src, tar;
	private JButton button;
	private StepList stepList;
	private Finder finder = null;
	private ImagePanel img = null;
	private AlgoChooser chooser = null;
	private int srcIndex = -1, tarIndex = -1;
	private ConfirmPanel confirmPanel;
	private JScrollPane jsp;

	public void setImgPanel(ImagePanel p) {
		img = p;
		p.setControlPanel(this);
	}

	public void setSrc(int s) {
		System.out.println(s);
		srcIndex = s;
		try {
			src.setText(Graph.getPointName(s));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setTar(int s) {
		tarIndex = s;
		try {
			tar.setText(Graph.getPointName(s));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ControlPanel() throws NumberFormatException, IOException {
		finder = new Finder();
		this.srcLab = new JLabel("Source:");
		this.tarLab = new JLabel("Destination:");

		this.src = new JTextField();
		this.tar = new JTextField();

		this.button = new JButton();
		this.button.setText("compute");
		this.button.addActionListener(this);

		this.stepList = new StepList();
		this.confirmPanel = new ConfirmPanel();

		chooser = new AlgoChooser();

		jsp = new JScrollPane(stepList);
		jsp.setPreferredSize(new Dimension(300, 400));

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 80));
		panel.setLayout(new GridLayout(2, 3));
		panel.add(srcLab);
		panel.add(src);
		panel.add(button);
		panel.add(tarLab);
		panel.add(tar);
		panel.add(chooser);

		this.setLayout(new BorderLayout());
		this.add(panel, BorderLayout.NORTH);
		this.add(jsp, BorderLayout.EAST);
		this.add(confirmPanel, BorderLayout.WEST);
		confirmPanel.setVisible(false);
		// this.setPreferredSize(new Dimension(300, 500));
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		try {
			if (srcIndex > -1 && tarIndex > -1
					&& src.getText().equals(Graph.getPointName(srcIndex))
					&& tar.getText().equals(Graph.getPointName(tarIndex))) {
				int result[] = finder.find(srcIndex, tarIndex);
				confirmPanel.setVisible(false);
				jsp.setVisible(true);
				stepList.setStep(result);
				if (img != null)
					img.setPath(result);
			} else {
				// System.out.println("label1");
				jsp.setVisible(false);
				confirmPanel.reset();
				confirmPanel.setVisible(true);

			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

	}

	public static void main(String args[]) throws NumberFormatException,
			IOException {

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ControlPanel ctrl = new ControlPanel();
		frame.getContentPane().add(ctrl);
		// frame.getContentPane().add(ctrl.new ConfirmPanel());
		frame.pack();
		frame.setVisible(true);
	}

}
