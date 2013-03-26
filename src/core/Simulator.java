package core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Simulator extends AbstractSimulator {

	private final Set<? extends IActionable> actionables;

	public Simulator(Collection<? extends IActionable> actionables) {
		this.actionables = new HashSet<IActionable>(actionables);
	}

	@Override
	protected void simulate() {
		for (IActionable actionable : actionables) {
			actionable.act();
		}
	}

}
