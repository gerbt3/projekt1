package design;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class CustomScrollBarUI extends BasicScrollBarUI {

	private Color darkgrey = new Color(100,100,100);
	private Color darkblue = new Color(122,138,153);
	
	protected void configureScrollBarColors() {
		
		thumbColor = darkgrey;
		thumbDarkShadowColor = darkblue;
		thumbHighlightColor = darkblue;
		thumbLightShadowColor = darkblue;
		trackColor = darkgrey;
		trackHighlightColor = darkgrey;
	}

	protected JButton createDecreaseButton(int orientation) {
		
		JButton button = new BasicArrowButton(orientation);
		button.setBackground(darkgrey);
		button.setForeground(darkgrey);
		
		return button;
	}

	protected JButton createIncreaseButton(int orientation) {
		
		JButton button = new BasicArrowButton(orientation);
		button.setBackground(darkgrey);
		button.setForeground(darkgrey);
		
		return button;
	}
}
