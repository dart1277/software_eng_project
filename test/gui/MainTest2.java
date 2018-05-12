package gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import org.loadui.testfx.GuiTest;
import org.testfx.api.FxAssert;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;


import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;


public class MainTest2 extends ApplicationTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent mainNode = null;
        mainNode = FXMLLoader.load(Main.class.getResource("sample.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();

    }

    @Test
    public void testEncryption() {
        String fakeFile = "/.file";
        String fakeDir = "/";
        clickOn("#passwordText");
        write("123456789");
        clickOn("#passwordTextRepeat");
        write("123456789");
        Controller controller = ControllerFactory.getController();
        Platform.runLater(() -> {
            controller.addPathChoice(fakeFile);
            controller.setChosenFolderPath(fakeDir);
            controller.getChosenFilesTree().setValue(fakeDir);
            controller.encryptOrDecryptFilesClick();
        });
    }

}