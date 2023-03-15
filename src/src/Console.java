import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class Console implements Runnable{

    private JFrame consoleFrame;
    private JTextArea outputArea;
    private JTextField inputField;
    PipedOutputStream outputFromField;
    PipedInputStream inputFromField;
    private Scanner fieldInput;
    private PrintStream fieldOutput;

    public void create() {
        //create components
        consoleFrame = new JFrame("Java console");
        outputArea = new JTextArea();
        inputField = new JTextField();

        //Make outputArea read-only
        outputArea.setEditable(false);

        //Set component backgrounds to BLACK and text color to WHITE to make it look more like a console
        outputArea.setBackground(Color.BLACK);
        inputField.setBackground(Color.BLACK);
        outputArea.setForeground(Color.WHITE);
        inputField.setForeground(Color.WHITE);

        //Add components
        consoleFrame.setLayout(new BorderLayout());
        consoleFrame.add(outputArea,BorderLayout.CENTER);
        consoleFrame.add(inputField,BorderLayout.SOUTH);

        //Setup Piped IO
        outputFromField = new PipedOutputStream();
        inputFromField = new PipedInputStream();
        try {
            outputFromField.connect(inputFromField);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        fieldInput = new Scanner(inputFromField);
        fieldOutput = new PrintStream(outputFromField);

        //Setup listeners

        //This listener listens for ENTER key on the inputField.
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = inputField.getText();
                fieldOutput.println(text);
                inputField.setText("");
                //Wake up the other thread for an immediate response.
                synchronized (inputFromField) {
                    inputFromField.notify();
                }
            }
        });

        //Setup Frame for display
        consoleFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        consoleFrame.setSize(400,300);
        consoleFrame.setVisible(true);

        //Start other thread that will run Console.run()
        Thread mainProgram = new Thread(this);
        mainProgram.start();
    }

    public void run() {
        while (fieldInput.hasNextLine()) {
            String line = fieldInput.nextLine();
            outputArea.append(line+"\n");
            System.out.println("Program recieved input: "+line);
        }
    }
    public void run(String input) {
        outputArea.append(input+"\n");
    }

    public static void main(String[] args) {
        //Run GUI Creation code on the AWT Event dispatching thread.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Console console = new Console();
                console.create();
            }
        });
    }
}