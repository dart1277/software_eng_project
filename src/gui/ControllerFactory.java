package gui;

public class ControllerFactory {
    private static Controller controller;

    public static void init(Controller controllerNew) {
        if (controller == null) controller = controllerNew;
    }

    public static Controller getController() {
        if (controller != null)
            return controller;
        else throw new NullPointerException("Invalid Controller State");
    }
}
