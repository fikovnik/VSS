package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;

import ui.IDrawable;

import core.IActionable;

public class Car implements IActionable, IDrawable {

	private final Point2D orig;
	private final Point2D dest;
	private final Point2D pos;

	private final int size = 20;
	private Color color = Color.BLUE;

	private final int speed = 10;

	public Car(Point2D orig, Point2D dest) {
		this.orig = orig;
		this.dest = dest;
		this.pos = orig;
	}

	public void act() {
		if (equals(pos, dest)) {
			Random rnd = new Random();
			
			double nx = rnd.nextDouble() * 800;
			double ny = rnd.nextDouble() * 600;
			
			orig.setLocation(pos);
			dest.setLocation(nx, ny);
			
			color = new Color(rnd.nextInt());
			
			return;
		}

		if (pos.distance(dest) < speed) {
			pos.setLocation(dest);
		} else {
			double alpha = angle(pos, dest);
			double nx = pos.getX() + speed * Math.cos(alpha);
			double ny = pos.getY() + speed * Math.sin(alpha);
			pos.setLocation(nx, ny);
		}
	}

	private boolean equals(Point2D a, Point2D b) {
		return a.distance(b) < 1;
	}

	private double angle(Point2D o, Point2D d) {
		return Math.atan2(d.getY() - o.getY(), d.getX() - o.getX());
	}
	
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(color);
		g2.fillRect((int) pos.getX(), (int) pos.getY(), size, size);
	}

}
