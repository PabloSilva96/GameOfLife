import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Random;

import javax.swing.*;

public class World extends JPanel
		implements ComponentListener, MouseListener, MouseMotionListener, Runnable, FormListener {
	private int cellSize = 10;
	private Dimension worldSize = new Dimension(getWidth() / cellSize - 2, getHeight() / cellSize - 2);
	private ArrayList<Point> point = new ArrayList<Point>(0);	
	private int fps = 20;
	private String insert = "point";

	public World() {
		addComponentListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	@Override
	public void formEventOccurred(FormEvent e) {
		fps = Integer.parseInt(e.getFps());
		insert = e.getInsert();
		cellSize = Integer.parseInt(e.getCellSize());
		worldSize = new Dimension(getWidth() / cellSize - 2, getHeight() / cellSize - 2);
		updateArraySize();
	}

	private void updateArraySize() {
		ArrayList<Point> removeList = new ArrayList<Point>(0);
		for (Point current : point) {
			if ((current.x > worldSize.width - 1) || (current.y > worldSize.height - 1)) {
				removeList.add(current);
			}
		}
		point.removeAll(removeList);
		repaint();
	}

	public void addPoint(int x, int y) {
		if (!point.contains(new Point(x, y))) {
			point.add(new Point(x, y));
		}
		repaint();
	}

	public void addPoint(MouseEvent me) {
		int x = me.getPoint().x / cellSize - 1;
		int y = me.getPoint().y / cellSize - 1;
		if ((x >= 0) && (x < worldSize.width) && (y >= 0) && (y < worldSize.height)) {
			if (SwingUtilities.isRightMouseButton(me)) { // tamen insertaria ca roda do raton ou oitros botons
				removePoint(x, y);
			} else if (insert.equals("point")) {
				addPoint(x, y);
			} else if (insert.equals("glider")) {
				glider(x, y);
			} else if (insert.equals("lightweightSpaceship")) {
				lightweightSpaceship(x, y);
			} else if (insert.equals("gospersGliderGun")) {
				gospersGliderGun(x, y);
			} else if (insert.equals("blinker")) {
				blinker(x, y);
			} else if (insert.equals("beehive")) {
				beehive(x, y);
			} else if (insert.equals("toad")) {
				toad(x, y);
			} else if (insert.equals("beacon")) {
				beacon(x, y);
			} else if (insert.equals("pulsar")) {
				pulsar(x, y);
			} else if (insert.equals("sans")) {
				sans(x, y);
			}
		}
	}

	public void removePoint(int x, int y) {
		point.remove(new Point(x, y));
		repaint();
	}

	public void resetBoard() {
		point.clear();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			for (Point newPoint : point) {
				// Draw new point
				g.setColor(Color.BLACK);
				g.fillRect(cellSize + (cellSize * newPoint.x), cellSize + (cellSize * newPoint.y), cellSize, cellSize);
			}
		} catch (ConcurrentModificationException cme) {
		}
		// Setup grid
		g.setColor(Color.BLACK);
		for (int i = 0; i <= worldSize.width; i++) {
			g.drawLine(((i * cellSize) + cellSize), cellSize, (i * cellSize) + cellSize,
					cellSize + (cellSize * worldSize.height));
		}
		for (int i = 0; i <= worldSize.height; i++) {
			g.drawLine(cellSize, ((i * cellSize) + cellSize), cellSize * (worldSize.width + 1),
					((i * cellSize) + cellSize));
		}
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// Si se amplia ou reduce a ventana se modifica o numero de celdas
		worldSize = new Dimension(getWidth() / cellSize - 2, getHeight() / cellSize - 2);
		updateArraySize();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Mouse was released (user clicked)
		addPoint(e);
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// Mouse is being dragged, user wants multiple selections
		addPoint(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void run() {
		boolean[][] gameBoard = new boolean[worldSize.width + 2][worldSize.height + 2];
		for (Point current : point) {
			gameBoard[current.x + 1][current.y + 1] = true;
		}
		ArrayList<Point> survivingCells = new ArrayList<Point>(0);
		// Iterate through the array, follow game of life rules
		for (int i = 1; i < gameBoard.length - 1; i++) {
			for (int j = 1; j < gameBoard[0].length - 1; j++) {
				int surrounding = 0;

				if (gameBoard[i - 1][j - 1]) {
					surrounding++;
				}
				if (gameBoard[i - 1][j]) {
					surrounding++;
				}
				if (gameBoard[i - 1][j + 1]) {
					surrounding++;
				}
				if (gameBoard[i][j - 1]) {
					surrounding++;
				}
				if (gameBoard[i][j + 1]) {
					surrounding++;
				}
				if (gameBoard[i + 1][j - 1]) {
					surrounding++;
				}
				if (gameBoard[i + 1][j]) {
					surrounding++;
				}
				if (gameBoard[i + 1][j + 1]) {
					surrounding++;
				}
				if (gameBoard[i][j]) {
					// Cell is alive, Can the cell live? (2-3)
					if ((surrounding == 2) || (surrounding == 3)) {
						survivingCells.add(new Point(i - 1, j - 1));
					}
				} else {
					// Cell is dead, will the cell be given birth? (3)
					if (surrounding == 3) {
						survivingCells.add(new Point(i - 1, j - 1));
					}
				}
			}
		}
		resetBoard();
		point.addAll(survivingCells);
		repaint();
		try {
			Thread.sleep(1000 / fps);
			run();
		} catch (InterruptedException ex) {
		}

	}

	// ---------------------------------------------------
	// Patrons
	// ---------------------------------------------------

	private void glider(int x, int y) {
		addPoint(x, y);
		addPoint(x + 2, y);
		addPoint(x + 1, y + 1);
		addPoint(x + 2, y + 1);
		addPoint(x + 1, y + 2);
		repaint();
	}

	private void lightweightSpaceship(int x, int y) {
		addPoint(x + 1, y);
		addPoint(x + 2, y);
		addPoint(x + 3, y);
		addPoint(x + 4, y);
		addPoint(x, y + 1);
		addPoint(x + 4, y + 1);
		addPoint(x + 4, y + 2);
		addPoint(x, y + 3);
		addPoint(x + 3, y + 3);
		repaint();
	}

	private void gospersGliderGun(int x, int y) {
		addPoint(x, y + 4);
		addPoint(x, y + 5);
		addPoint(x + 1, y + 4);
		addPoint(x + 1, y + 5);

		addPoint(x + 10, y + 4);
		addPoint(x + 10, y + 5);
		addPoint(x + 10, y + 6);
		addPoint(x + 11, y + 3);
		addPoint(x + 11, y + 7);
		addPoint(x + 12, y + 2);
		addPoint(x + 13, y + 2);
		addPoint(x + 12, y + 8);
		addPoint(x + 13, y + 8);

		addPoint(x + 14, y + 5);
		addPoint(x + 15, y + 3);
		addPoint(x + 15, y + 7);
		addPoint(x + 16, y + 4);
		addPoint(x + 16, y + 5);
		addPoint(x + 16, y + 6);
		addPoint(x + 17, y + 5);

		addPoint(x + 20, y + 2);
		addPoint(x + 20, y + 3);
		addPoint(x + 20, y + 4);
		addPoint(x + 21, y + 2);
		addPoint(x + 21, y + 3);
		addPoint(x + 21, y + 4);
		addPoint(x + 22, y + 1);
		addPoint(x + 22, y + 5);

		addPoint(x + 24, y);
		addPoint(x + 24, y + 1);

		addPoint(x + 24, y + 5);
		addPoint(x + 24, y + 6);

		addPoint(x + 34, y + 2);
		addPoint(x + 34, y + 3);
		addPoint(x + 35, y + 2);
		addPoint(x + 35, y + 3);

		repaint();
	}

	private void blinker(int x, int y) {
		addPoint(x, y);
		addPoint(x, y + 1);
		addPoint(x, y + 2);

		repaint();
	}

	private void beehive(int x, int y) {
		addPoint(x + 1, y);
		addPoint(x, y + 1);
		addPoint(x, y + 2);
		addPoint(x + 2, y + 1);
		addPoint(x + 2, y + 2);
		addPoint(x + 1, y + 3);

		repaint();
	}

	private void toad(int x, int y) {
		addPoint(x + 1, y);
		addPoint(x + 2, y);
		addPoint(x + 3, y);

		addPoint(x, y + 1);
		addPoint(x + 1, y + 1);
		addPoint(x + 2, y + 1);

		repaint();
	}

	private void beacon(int x, int y) {
		addPoint(x, y);
		addPoint(x + 1, y);
		addPoint(x, y + 1);

		addPoint(x + 3, y + 2);
		addPoint(x + 2, y + 3);
		addPoint(x + 3, y + 3);

		repaint();
	}

	private void pulsar(int x, int y) {
		addPoint(x + 2, y);
		addPoint(x + 3, y);
		addPoint(x + 4, y);

		addPoint(x, y + 2);
		addPoint(x, y + 3);
		addPoint(x, y + 4);

		addPoint(x + 5, y + 2);
		addPoint(x + 5, y + 3);
		addPoint(x + 5, y + 4);

		addPoint(x + 2, y + 5);
		addPoint(x + 3, y + 5);
		addPoint(x + 4, y + 5);

		addPoint(x + 8, y);
		addPoint(x + 9, y);
		addPoint(x + 10, y);

		addPoint(x + 7, y + 2);
		addPoint(x + 7, y + 3);
		addPoint(x + 7, y + 4);

		addPoint(x + 12, y + 2);
		addPoint(x + 12, y + 3);
		addPoint(x + 12, y + 4);

		addPoint(x + 8, y + 5);
		addPoint(x + 9, y + 5);
		addPoint(x + 10, y + 5);

		addPoint(x + 2, y + 7);
		addPoint(x + 3, y + 7);
		addPoint(x + 4, y + 7);

		addPoint(x, y + 8);
		addPoint(x, y + 9);
		addPoint(x, y + 10);

		addPoint(x + 2, y + 12);
		addPoint(x + 3, y + 12);
		addPoint(x + 4, y + 12);

		addPoint(x + 5, y + 8);
		addPoint(x + 5, y + 9);
		addPoint(x + 5, y + 10);

		addPoint(x + 7, y + 8);
		addPoint(x + 7, y + 9);
		addPoint(x + 7, y + 10);

		addPoint(x + 12, y + 8);
		addPoint(x + 12, y + 9);
		addPoint(x + 12, y + 10);

		addPoint(x + 8, y + 7);
		addPoint(x + 9, y + 7);
		addPoint(x + 10, y + 7);

		addPoint(x + 8, y + 12);
		addPoint(x + 9, y + 12);
		addPoint(x + 10, y + 12);

		repaint();
	}

	private void sans(int x, int y) {
		for (int i = 0; i<9; i++) {
			addPoint(x+i, y);
		}
		
		addPoint(x-1, y+1);
		addPoint(x-2, y+1);
		
		addPoint(x-3, y+2);
		addPoint(x-3, y+3);
		
		addPoint(x-4, y+4);
		addPoint(x-4, y+5);
		addPoint(x-4, y+6);
		addPoint(x-4, y+7);
		
		addPoint(x-3, y+8);	
		addPoint(x-3, y+9);
		addPoint(x-4, y+9);
		addPoint(x-4, y+10);
		addPoint(x-4, y+11);
		
		addPoint(x-3, y+12);
		addPoint(x-2, y+12);
		
		addPoint(x-1, y+13);
		addPoint(x, y+13);
		
		for (int i = 0; i<9; i++) {
			addPoint(x+i, y+14);
		}
		
		addPoint(x+8, y+13);
		addPoint(x+9, y+13);
		
		addPoint(x+10, y+12);
		addPoint(x+11, y+12);
		
		addPoint(x+12, y+11);
		addPoint(x+12, y+10);
		addPoint(x+12, y+9);
		addPoint(x+11, y+9);
		addPoint(x+11, y+8);
		
		addPoint(x+12, y+7);
		addPoint(x+12, y+6);
		addPoint(x+12, y+5);
		addPoint(x+12, y+4);
		
		addPoint(x+11, y+3);
		addPoint(x+11, y+2);
		
		addPoint(x+10, y+1);
		addPoint(x+9, y+1);
		
		// ollos
		
		addPoint(x-1, y+5);
		addPoint(x, y+5);
		addPoint(x+1, y+5);
		addPoint(x-1, y+6);
		addPoint(x, y+6);
		addPoint(x+1, y+6);
		addPoint(x-1, y+7);
		addPoint(x, y+7);
		addPoint(x+1, y+7);
		
		addPoint(x+7, y+5);
		addPoint(x+8, y+5);
		addPoint(x+9, y+5);
		addPoint(x+7, y+6);
		addPoint(x+8, y+6);
		addPoint(x+9, y+6);
		addPoint(x+7, y+7);
		addPoint(x+8, y+7);
		addPoint(x+9, y+7);
		
		// nariz
		addPoint(x+4, y+7);
		addPoint(x+3, y+8);
		addPoint(x+4, y+8);
		addPoint(x+5, y+8);
		
		// boca
		addPoint(x-1, y+9);
		addPoint(x-1, y+10);
		for (int i = 0; i<10; i++) {
			addPoint(x+i, y+10);
		}
		addPoint(x+9, y+9);
		
		addPoint(x, y+11);
		addPoint(x+2, y+11);
		addPoint(x+4, y+11);
		addPoint(x+6, y+11);
		addPoint(x+8, y+11);
		
		for (int i = 1; i<8; i++) {
			addPoint(x+i, y+12);
		}
		
		repaint();
	}

}
