package Menu;

import Commands.Command;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    static class MockCommand implements Command {
        boolean wasExecuted = false;
        @Override
        public void execute() {
            wasExecuted = true;
            System.out.println("Command Executed");
        }
    }

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }


    @Test
    @DisplayName("MenuInvoker: Виклик існуючої команди")
    void testInvokerExecuteExisting() {
        MenuInvoker invoker = new MenuInvoker();
        MockCommand cmd = new MockCommand();

        invoker.setCommand(1, cmd);
        invoker.executeCommand(1);

        assertTrue(cmd.wasExecuted, "Команда мала бути виконана");
    }

    @Test
    @DisplayName("MenuInvoker: Виклик неіснуючої команди")
    void testInvokerExecuteNonExisting() {
        MenuInvoker invoker = new MenuInvoker();

        invoker.executeCommand(99);

        assertTrue(outContent.toString().contains("Такого пункту не існує"));
    }

    @Test
    @DisplayName("Menu: Одноразове виконання (is_constant = false)")
    void testMenuOnce() {
        String input = "1\n";
        Scanner sc = new Scanner(input);

        Menu menu = new Menu("TEST MENU", sc, false);
        MockCommand cmd = new MockCommand();
        menu.addItem(1, "Test Item", cmd);

        menu.show();

        String output = outContent.toString();
        assertTrue(output.contains("=== TEST MENU ==="));
        assertTrue(output.contains("1. Test Item"));

        assertTrue(cmd.wasExecuted);
    }

    @Test
    @DisplayName("Menu: Циклічне виконання (is_constant = true)")
    void testMenuLoop() {

        String input = "1\n0\n";
        Scanner sc = new Scanner(input);

        Menu menu = new Menu("LOOP MENU", sc, true);
        MockCommand cmd = new MockCommand();
        menu.addItem(1, "Run Me", cmd);

        menu.show();

        assertTrue(cmd.wasExecuted);
        assertTrue(outContent.toString().contains("Ваш вибір:"));
    }


    @Test
    @DisplayName("Menu: Вихід одразу (0)")
    void testMenuExitImmediately() {
        String input = "0\n";
        Scanner sc = new Scanner(input);

        Menu menu = new Menu("TEST", sc, true);
        MockCommand cmd = new MockCommand();
        menu.addItem(1, "Cmd", cmd);

        menu.show();

        assertFalse(cmd.wasExecuted, "Команда не мала виконуватися");
    }
}
