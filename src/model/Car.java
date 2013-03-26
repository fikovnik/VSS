package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.Random;

import ui.IDrawable;

import core.IActionable;

public class Car implements IActionable, IDrawable {

	private static final int MAX_SPEED = 50;
	private static final int SIZE = 20;

	private final Point2D orig;
	private final Point2D dest;
	private final Point2D pos;

	private Color color = Color.BLUE;
	private int speed = 10;

	public Car(Point2D orig, Point2D dest) {
		if (orig == null) {
			throw new IllegalArgumentException("orig must not be null");
		}
		if (dest == null) {
			throw new IllegalArgumentException("dest must not be null");
		}
		
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
			do {
				speed = rnd.nextInt(MAX_SPEED);
			} while (speed < 1);

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
		assert a != null;
		assert b != null;
		
		return a.distance(b) < 1;
	}

	private double angle(Point2D o, Point2D d) {
		assert o != null;
		assert d != null;
		
		return Math.atan2(d.getY() - o.getY(), d.getX() - o.getX());
	}

	@Override
	public void draw(Graphics2D g2) {
		if (g2 == null) {
			throw new IllegalArgumentException("g2 must not be null");
		}
		
		g2.setColor(color);
		g2.fillRect((int) pos.getX(), (int) pos.getY(), SIZE, SIZE);
	}

}
