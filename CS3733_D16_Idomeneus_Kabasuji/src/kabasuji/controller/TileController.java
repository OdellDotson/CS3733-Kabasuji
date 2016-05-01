package kabasuji.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import kabasuji.model.Board;
import kabasuji.model.Bullpen;
import kabasuji.model.Kabasuji;
import kabasuji.model.Level;
import kabasuji.model.LightningLevel;
import kabasuji.model.Piece;
import kabasuji.model.PuzzleLevel;
import kabasuji.model.ReleaseLevel;
import kabasuji.model.Tile;
import kabasuji.view.BoardView;
import kabasuji.view.BullpenView;
import kabasuji.view.JLabelIcon;
import kabasuji.view.PieceView;
import kabasuji.view.PlayLevelPanel;
import kabasuji.view.ZoomPanel;
import misc.MusicPlayer;

/**
 * Controller for Board gameplay; Modify BoardView;
 * 
 * When a desired tile is pressed, an action is attempted to progress game and
 * update board
 * 
 * @author jwu
 *
 */
public class TileController extends MouseAdapter {

	/** Entity and Boundaries Associated **/
	Board board;
	Bullpen bullpen;
	JLabelIcon tile;
	Level currentlevel;

	PlayLevelPanel panel;
	BoardView boardview;
	BullpenView bullpenview;

	String fn;
	Piece selectedPiece;
	Tile[][] tiles;
	Tile selfTile;
	Kabasuji kabasuji;
	JLabelIcon[] tileimgs;
	ZoomPanel zoompanel;

	public TileController(Kabasuji kabasuji, PlayLevelPanel panel, JLabelIcon tile, Tile selfTile) {
		this.panel = panel;
		this.zoompanel = panel.getZoomPanel();
		this.board = kabasuji.getSelectedLevel().getBoard();
		this.bullpen = kabasuji.getSelectedLevel().getBullpen();
		this.tiles = board.getTiles();
		this.tile = tile;
		this.fn = tile.getFileName();
		this.selfTile = selfTile;
		this.kabasuji = kabasuji;
		this.boardview = panel.getBoardView();
		this.bullpenview = panel.getBullpenView();
		this.tileimgs = boardview.getTileImages();
	}

	public void updateParameters() {
		currentlevel = kabasuji.getSelectedLevel();
		bullpen = currentlevel.getBullpen();
		board = currentlevel.getBoard();
		bullpenview = panel.getBullpenView();
		boardview = panel.getBoardView();
		selectedPiece = bullpen.getSelectedPiece();
		if (bullpen.getSelectedPiece() == null) {
			selectedPiece = board.getSelectedPiece();
		}
	}

	/**
	 * Whenever mouse is pressed (left button), attempt to select object. This
	 * is a GUI controller.
	 */
	public void mousePressed(MouseEvent me) {
		updateParameters();
		if (bullpen.getSelectedPiece() != null) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				if (currentlevel.canMoveBullpenToBoard(selfTile)) {
					board.addPiece(selectedPiece, selfTile);
					new MusicPlayer("success.wav");
					displayHoverPiece("bluenightbutton.png", true, false);
					bullpen.selectPiece(null);
					bullpen.removePiece(selectedPiece);
					bullpenview.setupBullpen();
					panel.getZoomPanel().removeAll();

					// update display of stars
					updateStarDisplay();

					// update to show new number of moves if in puzzle mode
					if (currentlevel instanceof PuzzleLevel) {
						// decrement the number of moves left
						((PuzzleLevel) currentlevel)
								.setMovesUsed(((PuzzleLevel) currentlevel).getMovesUsed() + 1);
						panel.setMovesLeftNum((Integer) ((PuzzleLevel) currentlevel).getMovesLeft());
					}
					// update to show new number of moves if in puzzle mode
					else if (currentlevel instanceof ReleaseLevel) {
						// decrement the number of moves left
						((ReleaseLevel) currentlevel)
								.setMovesUsed(((ReleaseLevel) currentlevel).getMovesUsed() + 1);
						panel.setMovesLeftNum((Integer) ((ReleaseLevel) currentlevel).getMovesLeft());
					}
				} else {
					new MusicPlayer("fail.wav");
				}
			}
		} else if (board.getSelectedPiece() != null) {
			if (currentlevel instanceof PuzzleLevel) {
				if (currentlevel.canMoveBoardToBoard(selfTile)) {
					((PuzzleLevel) currentlevel).moveBoardToBoard(selfTile);
					displayHoverPiece("bluenightbutton.png", true, false);
					new MusicPlayer("success.wav");
					board.selectPiece(null);
					boardview.setupBoard();
					panel.getZoomPanel().removeAll();
					// updates the stars display
					updateStarDisplay();
				} else {
					new MusicPlayer("fail.wav");
				}
			}
		}
		if (kabasuji.getSelectedLevel() instanceof PuzzleLevel) {
			if (SwingUtilities.isRightMouseButton(me)) {
				if (board.getSelectedPiece() == null && bullpen.getSelectedPiece() == null) {
					board.selectPiece(selfTile.getPiece());
					boardSelectPiece(selfTile.getPiece());
					zoompanel.removeAll();
					zoompanel.displayPieceView(selfTile.getPiece());
				}
			}
		}

		// If the number of stars are 3, display the winning screen
		int currNumStars1 = currentlevel.getStars();
		if (currNumStars1 == 3) {
			try {
				Thread.sleep(100); // 1000 milliseconds is one second.
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			panel.winningScreen();
		}
		panel.repaint();

		// If the number of moves become 0, display a lose screen in Puzzle
		// Level
		if ((currentlevel instanceof PuzzleLevel)) {
			int getMoves = ((PuzzleLevel) currentlevel).getMovesUsed();
			int maxMoves = ((PuzzleLevel) currentlevel).getMaxMoves();
			if ((getMoves == maxMoves) && (currNumStars1 == 0)) {
				try {
					Thread.sleep(100); // 1000 milliseconds is one second.
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				panel.losingScreen();
			}
			panel.repaint();
		}

		// If the number of moves become 0, display a lose screen in Release
		// Level
		if ((currentlevel instanceof ReleaseLevel)) {
			int getMoves = ((ReleaseLevel) currentlevel).getMovesUsed();
			int maxMoves = ((ReleaseLevel) currentlevel).getMaxMoves();
			if ((getMoves == maxMoves) && (currNumStars1 == 0)) {
				try {
					Thread.sleep(100); // 1000 milliseconds is one second.
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				panel.losingScreen();
			}
			panel.repaint();
		}
	}

	public void mouseEntered(MouseEvent e) {
		updateParameters();
		String hoverfn = null;
		if (bullpen.getSelectedPiece() != null) {
			if (currentlevel.canMoveBullpenToBoard(selfTile)) {
				hoverfn = "bluenightbutton.png";
			} else {
				hoverfn = "rednightbutton.png";
			}
		} else if (board.getSelectedPiece() != null) {
			if (currentlevel.canMoveBoardToBoard(selfTile)) {
				hoverfn = "bluenightbutton.png";
			} else {
				hoverfn = "rednightbutton.png";
			}
		} else if (hoverfn == null) {
			// no piece is selected
			return;
		}
		// set image to hover
		displayHoverPiece(hoverfn, false, false);
	}

	public void mouseExited(MouseEvent e) {
		updateParameters();
		if (selectedPiece == null){
			// if selectedPiece doesn't exist
			return;
		}
		// set to original image filename
		displayHoverPiece("general1button.png", false, true);
	}

	public void displayHoverPiece(String hpfn, boolean setNewFilename, boolean setOriginalImg) {
		updateParameters();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[i][j] == selfTile) {
					int xoffset = (int) selectedPiece.findReferencePoint().getX();
					int yoffset = (int) selectedPiece.findReferencePoint().getY();
					for (int y = -yoffset; y < selectedPiece.getDim() - yoffset; y++) {
						for (int x = -xoffset; x < selectedPiece.getDim() - xoffset; x++) {
							try {
								if ((selectedPiece.getTile(y + yoffset, x + xoffset).isValid())
										&& tiles[i + y][j + x].isValid()) {
									if (setNewFilename) {
										tileimgs[(i + y) * tiles.length + (j + x)].setFileName(hpfn);
									}
									if (setOriginalImg) {
										String origfn = tileimgs[(i + y) * tiles.length + (j + x)].getFileName();
										tileimgs[(i + y) * tiles.length + (j + x)].setImg(origfn);
									} else {
										tileimgs[(i + y) * tiles.length + (j + x)].setImg(hpfn);
									}
								}
							} catch (IndexOutOfBoundsException e) {
								System.out.println("Out of bounds!!");
							}
						}
					}
				}
			}
		}
	}

	public void boardSelectPiece(Piece p) {
		selectedPiece = kabasuji.getSelectedLevel().getBullpen().getSelectedPiece();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[i][j].getPiece() == p) {
					tileimgs[i * tiles.length + j].setImg("boardtile.png");
					tileimgs[i * tiles.length + j].setFileName("boardtile.png");

				}
			}
		}
	}

	/**
	 * Updates the display of stars ( 1-3 ).
	 */
	public void updateStarDisplay() {
		/* Check if the number of stars has been updated */
		int currNumStars = kabasuji.getSelectedLevel().getStars();
		System.out.print("Calculated Number of Stars: ");
		System.out.println(currNumStars);
		System.out.print("Stored Number of Stars: ");
		System.out.println(kabasuji.getSelectedLevel().getBoard().getNumStars());
		if (currNumStars != kabasuji.getSelectedLevel().getBoard().getNumStars()) {
			System.out.println("Entered star condition!");
			// update the stored number of stars
			kabasuji.getSelectedLevel().getBoard().setNumStars(currNumStars);

			panel.updateStars(); // draw the correct number of stars

			/*
			 * If this is the first star, change the next level button to be
			 * unlocked
			 */
			if (currNumStars == 1) {
				panel.updateNextLevel();
			}
		}
		panel.repaint();
	}
}
