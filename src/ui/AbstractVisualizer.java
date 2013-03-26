package ui;

import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class AbstractVisualizer extends JPanel {

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		for (IDrawable d : getDrawables()) {
			d.draw(g2);
		}
	}
	
	protected abstract Iterable<? extends IDrawable> getDrawables();
	
}
