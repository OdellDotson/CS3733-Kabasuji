/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package kabasuji.view;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * SplashWindow for Opening Screen.
 * @author jwu
 *
 */
public class SplashWindow extends Frame{
	/**
	 * Constructor SplashWindow.
	 */
	public SplashWindow() {
		super("SplashScreen demo");
		try {
			final SplashScreen splash = SplashScreen.getSplashScreen();
			if (splash == null) {
				System.out.println("SplashScreen.getSplashScreen() returned null");
				return;
			}
			Graphics2D g = splash.createGraphics();

			if (g == null) {
				System.out.println("g is null");
				return;
			}
			for (int i = 0; i < 6; i++) {
				renderSplashFrame(g, i);
				splash.update();
				try {
					if (i > 4) {
						Thread.sleep(1700);
					} else {
						Thread.sleep(550);
					}
				} catch (InterruptedException e) {
				}
			}
			splash.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Displays the frame of interest in the splash screen.
	 * @param g
	 * @param frame
	 */
	static void renderSplashFrame(Graphics2D g, int frame) {
		final String[] names = { "THE COOLEST KIDS", "Odell Dotson", "Breanne Happell", "Ethan Prihar", "Vishal Rathi",
				"Yu-sen Wu" };
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(20, 20, 200, 200);
		g.setPaintMode();
		g.setColor(Color.BLACK);
		g.setFont(Font.decode("Arial-BOLD-18"));
		// need exception catching
		BufferedImage img = null;
		try {
			// maps path to the image file
			String path = System.getProperty("user.dir") + File.separator + "src\\images\\8bitbunny.jpg";
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
		}
		for (int i = 0; i < frame + 1; i++) {
			int plotX = 20;
			int plotY = 30 + 43 * i;
			if (i > 4) {
				plotX = 150;
				g.drawImage(img, 255, 10 + 43 * i, null);
			}
			g.drawString(" " + names[i], plotX, plotY);
		}
	}
}