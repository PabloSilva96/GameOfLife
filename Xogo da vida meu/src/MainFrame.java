import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainFrame extends JFrame implements ActionListener {
	private World world;
	private JButton startButton;
	private JButton stopButton;
	private JButton resetButton;
	private JButton updateButton;
	private JComboBox fpsCombo;
	private JLabel fpsLabel;
	private JComboBox insertCombo;
	private JLabel insertLabel;
	private JComboBox cellCombo;
	private JLabel cellLabel;
	private JButton infoButton;
	private Thread game;
	private FormListener formListener;

	public void setFormListener(FormListener formListener) {
		this.formListener = formListener;
	}

	public MainFrame() {
		super("Yes Game Yes Life");

		setLayout(new BorderLayout());

		world = new World();

		setFormListener(world);

		add(world, BorderLayout.CENTER);

		startButton = new JButton("START");
		stopButton = new JButton("STOP");
		resetButton = new JButton("RESET");
		updateButton = new JButton("UPDATE");
		infoButton = new JButton("???");

		startButton.addActionListener(this);
		stopButton.addActionListener(this);
		stopButton.setEnabled(false);
		resetButton.addActionListener(this);
		updateButton.addActionListener(this);
		infoButton.addActionListener(this);

		fpsCombo = new JComboBox();
		DefaultComboBoxModel fpsModel = new DefaultComboBoxModel();
		fpsModel.addElement("1");
		fpsModel.addElement("5");
		fpsModel.addElement("10");
		fpsModel.addElement("20");
		fpsModel.addElement("30");

		fpsCombo.setModel(fpsModel);
		fpsCombo.setSelectedIndex(3);

		fpsLabel = new JLabel("FPS");

		insertCombo = new JComboBox();
		DefaultComboBoxModel insertModel = new DefaultComboBoxModel();
		insertModel.addElement("point");
		insertModel.addElement("glider");
		insertModel.addElement("lightweightSpaceship");
		insertModel.addElement("gospersGliderGun");
		insertModel.addElement("blinker");
		insertModel.addElement("beehive");
		insertModel.addElement("toad");
		insertModel.addElement("beacon");
		insertModel.addElement("pulsar");
		insertModel.addElement("sans");

		insertCombo.setModel(insertModel);
		insertCombo.setSelectedIndex(0);

		insertLabel = new JLabel("Insert");
		
		cellCombo = new JComboBox();
		DefaultComboBoxModel cellModel = new DefaultComboBoxModel();
		cellModel.addElement("2");
		cellModel.addElement("3");
		cellModel.addElement("4");
		cellModel.addElement("5");
		cellModel.addElement("10");
		cellModel.addElement("15");
		cellModel.addElement("20");

		cellCombo.setModel(cellModel);
		cellCombo.setSelectedIndex(4);

		cellLabel = new JLabel("CellSize");

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(startButton);
		panel.add(stopButton);
		panel.add(resetButton);
		panel.add(fpsLabel);
		panel.add(fpsCombo);
		panel.add(insertLabel);
		panel.add(insertCombo);
		panel.add(cellLabel);
		panel.add(cellCombo);
		panel.add(updateButton);
		panel.add(infoButton);
		add(panel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton clickedButton = (JButton) e.getSource();

		if (clickedButton == startButton) {
			game = new Thread(world);
			game.start();
			startButton.setEnabled(false);
			stopButton.setEnabled(true);

		} else if (clickedButton == stopButton) {
			game.interrupt();
			stopButton.setEnabled(false);
			startButton.setEnabled(true);
		} else if (clickedButton == resetButton) {
			world.resetBoard();
			world.repaint();
		} else if (clickedButton == updateButton) {
			String fpsCat = (String) fpsCombo.getSelectedItem();
			String insertCat = (String) insertCombo.getSelectedItem();
			String cellCat = (String) cellCombo.getSelectedItem();
			
			FormEvent event = new FormEvent(this, fpsCat, insertCat, cellCat);

			if (formListener != null) {
				formListener.formEventOccurred(event);
			}
		} else if (clickedButton == infoButton) {
			JOptionPane.showMessageDialog(this,
					"Rigth click to kill cells \nAny other mouse button to draw cells \nWindow can be resized to change the game board \nAfter changing settings press UPDATE to apply them \nDisclaimer : lower cellSize cells might cause performance issues",
					"Info", JOptionPane.PLAIN_MESSAGE);

		}

	}
}
