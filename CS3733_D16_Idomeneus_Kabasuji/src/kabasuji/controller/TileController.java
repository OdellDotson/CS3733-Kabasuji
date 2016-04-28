package kabasuji.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

import kabasuji.model.Board;
import kabasuji.model.Bullpen;
import kabasuji.model.Kabasuji;
import kabasuji.model.Piece;
import kabasuji.model.PuzzleLevel;
import kabasuji.model.ReleaseLevel;
import kabasuji.model.Tile;
import kabasuji.view.BoardView;
import kabasuji.view.BullpenView;
import kabasuji.view.JLabelIcon;
import kabasuji.view.PieceView;
import kabasuji.view.PlayLevelPanel;

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

	PlayLevelPanel panel;

	BoardView boardview;
	BullpenView bullpenview;

	String fn;
	Piece selectedPiece;
	Tile[][] tiles;
	Tile selfTile;
	Kabasuji kabasuji;
	JLabelIcon[] tileimgs;
	JLabelIcon zoompanel;

	public TileController(Kabasuji kabasuji, PlayLevelPanel panel, JLabelIcon tile, Tile selfTile) {
		this.panel = panel;
		this.zoompanel = panel.getZoomPiece();
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

	/**
	 * Whenever mouse is pressed (left button), attempt to select object. This
	 * is a GUI controller.
	 */
	public void mousePressed(MouseEvent me) {
		bullpen = kabasuji.getSelectedLevel().getBullpen();
		board = kabasuji.getSelectedLevel().getBoard();
		bullpenview = panel.getBullpenView();
		if (bullpen.getSelectedPiece() != null) {
			if (SwingUtilities.isLeftMouseButton(me)) {
				if (kabasuji.getSelectedLevel().canMoveBullpenToBoard(selfTile)) {
					board.addPiece(selectedPiece, selfTile);
					displayHoverPiece("bluenightbutton.png", true, false);
					bullpen.selectPiece(null);
					bullpen.removePiece(selectedPiece);
					bullpenview.setupBullpen();
					panel.getZoomPiece().removeAll();

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
						 * If this is the first star, change the next level
						 * button to be unlocked
						 */
						if (currNumStars == 1) {
							panel.updateNextLevel();
						}
					}
					panel.repaint();
					// update to show new number of moves if in puzzle mode
					if (kabasuji.getSelectedLevel() instanceof PuzzleLevel) {
						// decrement the number of moves left
						((PuzzleLevel) kabasuji.getSelectedLevel())
								.setMovesUsed(((PuzzleLevel) kabasuji.getSelectedLevel()).getMovesUsed() + 1);
						panel.setMovesLeftNum((Integer) ((PuzzleLevel) kabasuji.getSelectedLevel()).getMovesLeft());
					}
					// update to show new number of moves if in puzzle mode
					else if (kabasuji.getSelectedLevel() instanceof ReleaseLevel) {
						// decrement the number of moves left
						((ReleaseLevel) kabasuji.getSelectedLevel())
								.setMovesUsed(((ReleaseLevel) kabasuji.getSelectedLevel()).getMovesUsed() + 1);
						panel.setMovesLeftNum((Integer) ((ReleaseLevel) kabasuji.getSelectedLevel()).getMovesLeft());
					}
				}
			}
		} else if (board.getSelectedPiece() != null) {
			if (kabasuji.getSelectedLevel() instanceof PuzzleLevel) {
				if (kabasuji.getSelectedLevel().canMoveBoardToBoard(selfTile)) {
					((PuzzleLevel) kabasuji.getSelectedLevel()).moveBoardToBoard(selfTile);
					displayHoverPiece("bluenightbutton.png", true, false);
					board.selectPiece(null);
					boardview.setupBoard();
					panel.getZoomPiece().removeAll();

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
						 * If this is the first star, change the next level
						 * button to be unlocked
						 */
						if (currNumStars == 1) {
							panel.updateNextLevel();
						}
					}
					panel.repaint();
				}
			}
		}
		if (kabasuji.getSelectedLevel() instanceof PuzzleLevel) {
			if (SwingUtilities.isRightMouseButton(me)) {
				if (board.getSelectedPiece() == null) {
					//boardSelectPiece(selfTile.getPiece(), true);
					board.selectPiece(selfTile.getPiece());
					displayHoverPiece("boardtile.png", true, false);
					zoompanel.removeAll();
					PieceView pieceview = new PieceView(selfTile.getPiece());
					pieceview.setBounds(0, 0, (int) zoompanel.getSize().getWidth(),
							(int) zoompanel.getSize().getHeight());
					pieceview.setupPiece();
					zoompanel.add(pieceview);
					zoompanel.repaint();
				} else if (board.getSelectedPiece() != null) {
					boardSelectPiece(selfTile.getPiece(), true);
					board.selectPiece(null);
				}
			}
		}
	}

	public void mouseEntered(MouseEvent e) {
		selectedPiece = kabasuji.getSelectedLevel().getBullpen().getSelectedPiece();
		if (kabasuji.getSelectedLevel().getBullpen().getSelectedPiece() == null) {
			selectedPiece = kabasuji.getSelectedLevel().getBoard().getSelectedPiece();
		}

		if (kabasuji.getSelectedLevel().getBullpen().getSelectedPiece() != null) {
			System.out.println("Have piece....");
			if (kabasuji.getSelectedLevel().canMoveBullpenToBoard(selfTile)) {
				System.out.println("Can place.");
				displayHoverPiece("bluenightbutton.png", false, false);
			} else {
				System.out.println("Cannot place.");
				displayHoverPiece("rednightbutton.png", false, false);

			}
		} else if (kabasuji.getSelectedLevel().getBoard().getSelectedPiece() != null) {
			System.out.println("Have piece....");
			if (kabasuji.getSelectedLevel().canMoveBoardToBoard(selfTile)) {
				System.out.println("Can place.");
				displayHoverPiece("bluenightbutton.png", false, false);
			} else {
				System.out.println("Cannot place.");
				displayHoverPiece("rednightbutton.png", false, false);
			}
		}
	}

	public void mouseExited(MouseEvent e) {

		selectedPiece = kabasuji.getSelectedLevel().getBullpen().getSelectedPiece();
		if (kabasuji.getSelectedLevel().getBullpen().getSelectedPiece() == null) {
			selectedPiece = kabasuji.getSelectedLevel().getBoard().getSelectedPiece();
		}

		if (kabasuji.getSelectedLevel().getBullpen().getSelectedPiece() != null) {
			displayHoverPiece("general1button.png", false, true);
		} else if (kabasuji.getSelectedLevel().getBoard().getSelectedPiece() != null) {
			displayHoverPiece("general1button.png", false, true);
		}
	}

	public void displayHoverPiece(String hpfn, boolean setNewFilename, boolean setOriginalImg) {
		if (kabasuji.getSelectedLevel().getBullpen().getSelectedPiece() == null) {
			selectedPiece = kabasuji.getSelectedLevel().getBoard().getSelectedPiece();
		} else {
			selectedPiece = kabasuji.getSelectedLevel().getBullpen().getSelectedPiece();
		}
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

	public void boardSelectPiece(Piece p, boolean newfn) {
		selectedPiece = kabasuji.getSelectedLevel().getBullpen().getSelectedPiece();
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles.length; j++) {
				if (tiles[i][j].getPiece() == p) {
					if (newfn) {
						tileimgs[i * tiles.length + j].setImg("generalhoverbutton.png");
					} else {
						tileimgs[i * tiles.length + j].setImg(tileimgs[i * tiles.length + j].getFileName());
					}
				}
			}
		}
	}
}
