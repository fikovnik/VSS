package app;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Car;
import ui.Visualizer;
import core.ISimulationListener;
import core.Simulator;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private final Visualizer visualizer;
	private final Simulator simulator;

	private final JButton btnStart;
	private final JButton btnStop;
	private final JButton btnExit;
	private final JLabel lblStatus;

	public Main(Collection<Car> cars) {
		setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		add(controls, BorderLayout.NORTH);

		btnStart = new JButton("Start");
		controls.add(btnStart);
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleStartClick();
			}
		});

		btnStop = new JButton("Stop");
		controls.add(btnStop);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleStopClick();
			}
		});
		btnExit = new JButton("Exit");
		controls.add(btnExit);
		btnExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handleExitClick();
			}
		});
		
		lblStatus = new JLabel();
		add(lblStatus, BorderLayout.SOUTH);

		simulator = new Simulator(cars);
		simulator.addSimulationListener(new ISimulationListener() {

			@Override
			public void cycleFinished() {
				visualizer.repaint();
			}

			@Override
			public void simulationStarted() {
				handleSimulationStateChange(true);
			}

			@Override
			public void simulationStopped() {
				handleSimulationStateChange(false);
			}
		});

		visualizer = new Visualizer(cars);
		add(visualizer, BorderLayout.CENTER);

		handleSimulationStateChange(false);
	}

	protected void handleSimulationStateChange(boolean running) {
		lblStatus.setText(running ? "Running" : "Ready");
		btnStart.setEnabled(!running);
		btnStop.setEnabled(running);		
	}

	protected void handleExitClick() {
		System.exit(0);
	}

	protected void handleStopClick() {
		simulator.stop();
	}

	protected void handleStartClick() {
		simulator.start();
	}

	public static void main(String[] args) {
		Set<Car> cars = new HashSet<Car>();
		Random rnd = new Random();
		for (int i=0; i<20; i++) {
			double x = rnd.nextDouble() * 800;
			double y = rnd.nextDouble() * 600;
			double dx = rnd.nextDouble() * 800;
			double dy = rnd.nextDouble() * 600;
			cars.add(new Car(new Point2D.Double(x, y), new Point2D.Double(dx, dy)));
		}

		Main app = new Main(cars);
		app.setSize(800, 600);
		app.setVisible(true);

	}

}
