package core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

abstract public class AbstractSimulator {

	private final List<ISimulationListener> listeners = new CopyOnWriteArrayList<ISimulationListener>();
	private final Thread thread;
	private boolean started = false;
	private volatile boolean running = false;
	private volatile long simulationDelay = 25;

	public AbstractSimulator() {
		thread = new Thread("SimulatorThread") {
			@Override
			public void run() {
				try {
					AbstractSimulator.this.run();
				} catch (InterruptedException e) {
					throw new RuntimeException(
							"SimulationThread has been interrupted", e);
				}
			}
		};
	}

	public synchronized void start() {
		if (!started) {			
			thread.start();
			started = true;
		}
		running = true;
		notify();
		notifySimulationStarted();
	}

	public synchronized void stop() {
		running = false;
		notify();
		notifySimulationStopped();
	}

	protected void notifyCycleFinished() {
		for (ISimulationListener l : listeners) {
			l.cycleFinished();
		}
	}
	protected void notifySimulationStarted() {
		for (ISimulationListener l : listeners) {
			l.simulationStarted();
		}
	}
	protected void notifySimulationStopped() {
		for (ISimulationListener l : listeners) {
			l.simulationStopped();
		}
	}

	public void addSimulationListener(ISimulationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener must not be null");
		}

		listeners.add(listener);
	}
	
	public void removeSimulationListener(ISimulationListener listener) {
		if (listener == null) {
			throw new IllegalArgumentException("listener must not be null");
		}

		listeners.remove(listener);
	}
	
	public synchronized long getSimulationDelay() {
		return simulationDelay;
	}

	public synchronized void setSimulationDelay(long simulationDelay) {
		if (simulationDelay < 0) {
			throw new IllegalArgumentException("simulationDelay must not be < 0");
		}
		
		this.simulationDelay = simulationDelay;
	}

	protected void run() throws InterruptedException {
		while (true) {
			synchronized (this) {					
				if (!running) {
					wait();
				}
			}

			simulate();
			notifyCycleFinished();

			Thread.sleep(simulationDelay);
		}
	}

	protected void simulate() {
		for (IActionable actionable : getActionables()) {
			actionable.act();
		}
	}

	protected abstract Iterable<? extends IActionable> getActionables();
}
