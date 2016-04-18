package kabasuji.view;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;

import kabasuji.controller.BoardController;
import kabasuji.controller.GoToLevelSelectController;
import kabasuji.controller.GoToMainMenuController;
import kabasuji.model.Board;
import kabasuji.model.Bullpen;
import kabasuji.model.Kabasuji;
import kabasuji.model.Piece;
import kabasuji.model.Screen;
import kabasuji.model.Tile;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;

import javax.imageio.ImageIO;
import java.awt.SystemColor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;

import javax.swing.SwingConstants;
import javax.swing.ImageIcon;

public class BullpenView extends JPanel {
	// the associated board
	Bullpen bullpen;
	// the JPanel that contains it
	Container panel;

	ArrayList<Piece> pieces;

	JLabelIcon[] imgpieces;

	int row;
	int col;

	int sqmatrixlength;

	int piecesidelength;
	int piecesidescaled;

	int offset;

	/**
	 * Create the Main Menu Panel.
	 */

	public BullpenView(Bullpen bullpen, Container panel, int row, int col) {
		this.bullpen = bullpen;
		this.panel = panel;
		this.pieces = bullpen.getPieces();

		this.row = row;
		this.col = col;

		imgpieces = new JLabelIcon[pieces.size()];

		// this is the largest length of the tile matrix
		// finds the smallest tile length
		if((panel.getSize().getWidth() / row) < (panel.getSize().getHeight() / col)){
			this.piecesidelength = (int) (panel.getSize().getWidth() / row);
			sqmatrixlength = row;
		} else {
			this.piecesidelength = (int) (panel.getSize().getHeight() / col);
			sqmatrixlength = col;
		}

		// scaling + offset to fit the container panel;
		piecesidescaled = (int) (piecesidelength * 0.8);
		offset = (int) ((piecesidelength - piecesidescaled) / 2);

		// sets the bounds of the bullpenview within the panel container
		setBounds(0, 0, (int) panel.getSize().getWidth(), (int) panel.getSize().getHeight());
		updateBullpen();

	}

	public void updateBullpen() {
		// display the tiles on the container panel and scales them to fit
		// row/col
		// includes centering as well
		for (int i = 0; i < col; i++) {
			for (int j = 0; j < row; j++) {
				// create a button image with specified dimension
				// only display tile if it's valid
				displayPiece(i, j, "generalbutton.png");
				// tile[i*row+j].addMouseListener(new BoardController(board,
				// panel, tile[i*row+j]));
			}
		}
		repaint();
	}

	public void displayPiece(int i, int j, String pic) {
		imgpieces[i * row + j] = new JLabelIcon(pic, piecesidescaled, piecesidescaled);
		imgpieces[i * row + j].setLocation((int) (piecesidelength * (j) + offset),
				(int) (piecesidelength * (i) + offset));
		// JLabel numlbl = new JLabel(""+ti[j][i].getNumber(),
		// SwingConstants.CENTER);
		// numlbl.setBounds(0, 0, tilesidescaled, tilesidescaled);
		// numlbl.setFont(new Font("Arial", Font.BOLD,
		// (int)(tilesidescaled*0.5)));
		// imgpieces[i*row+j].add(numlbl);
		panel.add(imgpieces[i * row + j]);
	}
}