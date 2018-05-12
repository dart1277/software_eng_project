package gui;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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


public class MainTest extends ApplicationTest {

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
    public void testPasswordInput() {
        //Label label = GuiTest.find("#label");
        PasswordField passwordField = GuiTest.find("#passwordText");
        clickOn("#passwordText");
        write("123456789");
        //clickOn("#applyButton");
        assertThat(passwordField.getText(), is("123456789"));
    }

    @Test
    public void testPasswordAgainInput() {
        PasswordField passwordField = GuiTest.find("#passwordTextRepeat");
        clickOn("#passwordTextRepeat");
        write("123456789");
        assertThat(passwordField.getText(), is("123456789"));
        clickOn("#encryptOrDecryptFilesBtn");
    }

    @Test
    public void testDecryptRadioSwitch() {
        clickOn("#chooseLanguage");
        clickOn("#polish");

        Button button = GuiTest.find("#encryptOrDecryptFilesBtn");
        clickOn("#encryptFiles");
        assertThat(button.getText(), is("Zaszyfruj pliki"));
        clickOn("#decryptFiles");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        button = GuiTest.find("#encryptOrDecryptFilesBtn");
        assertThat(button.getText(), is("Zaszyfruj pliki"));

    }

    @Test
    public void testLanguageMenuSwitch() {
        Button button = GuiTest.find("#encryptOrDecryptFilesBtn");
        clickOn("#chooseLanguage");
        clickOn("#polish");
        assertThat(button.getText(), is("Zaszyfruj pliki"));


    }

    @Test
    public void testCallBacks() {
        clickOn("#program");
        clickOn("#help");
        clickOn("#chooseDestinationFolder");
        clickOn("#slowEncSpeed");
        clickOn("#undoSelection");
        clickOn("#clearSelection");


    }


}