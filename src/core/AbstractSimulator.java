package core;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

abstract public class AbstractSimulator {

	private final List<ISimulationListener> listeners = new CopyOnWriteArrayList<ISimulationListener>();
	private final Thread thread;
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
		thread.start();
	}

	public synchronized void start() {
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
		listeners.add(listener);
	}
	
	public void removeSimulationListener(ISimulationListener listener) {
		listeners.remove(listener);
	}
	
	public synchronized long getSimulationDelay() {
		return simulationDelay;
	}

	public synchronized void setSimulationDelay(long simulationDelay) {
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

	abstract protected void simulate();
}
