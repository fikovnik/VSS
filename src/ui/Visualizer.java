package ui;

@SuppressWarnings("serial")
public class Visualizer extends AbstractVisualizer {

	private Iterable<? extends IDrawable> drawables;

	public Visualizer(Iterable<? extends IDrawable> drawables) {
		this.drawables = drawables;
	}

	@Override
	protected Iterable<? extends IDrawable> getDrawables() {
		return drawables;
	}

}
