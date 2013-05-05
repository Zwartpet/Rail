/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simulatedDevices.main;

/**
 *
 * @author Administrator
 */
class SimulationController {

    private final SimulationView simulationView;
    private SimulationHandler simulationHandler;

    public SimulationController() {
        simulationView = new SimulationView(this);
        simulationView.setVisible(true);

        simulationHandler = new SimulationHandler(this);

    }

    void startSimulation() {

        simulationHandler.setRunning(true);
        Thread thread = new Thread(simulationHandler);

        thread.start();


    }

    public SimulationView getSimulationView() {
        return simulationView;
    }
    
    

    void stopSimulation() {
        simulationHandler.setRunning(false);
    }
}
