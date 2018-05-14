package gui;

/**
 * This class provides a factory for the controller object as a singleton
 * */

public class ControllerFactory {
    private static Controller controller;

    /**
     * A setter method for the controller object
     * */
    public static void init(Controller controllerNew) {
        if (controller == null) controller = controllerNew;
    }

    /**
     * Getter method for the controller object
     *
     * @return controller singleton object
     */
    public static Controller getController() {
        if (controller != null)
            return controller;
        else throw new NullPointerException("Invalid Controller State");
    }
}
