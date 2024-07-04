import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private JList<String> historyList;
    private DefaultListModel<String> historyModel;
    private String operator;
    private double num1, num2, result;

    public Calculator() {
        createUI();
    }

    private void createUI() {
        display = new JTextField();
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.PLAIN, 84));
        display.setBackground(Color.CYAN);
        display.setForeground(Color.BLACK);
        display.setHorizontalAlignment(JTextField.RIGHT);

        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        historyList.setFont(new Font("Arial", Font.PLAIN, 20));
        historyList.setBackground(Color.LIGHT_GRAY);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));
        panel.setBackground(Color.RED);

        String[] buttons = {
                "7", "8", "9", "Del",
                "4", "5", "6", "C",
                "1", "2", "3", "-",
                "0", ".", "√", "+",
                "*", "/", "x^n", "="
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.BOLD, 28));
            button.setFocusPainted(false);

            // Set colors based on button type
            if (text.matches("[0-9]") || text.equals(".")) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK);
            } else if (text.equals("=")) {
                button.setBackground(Color.GREEN);
                button.setForeground(Color.BLACK);
            } else {
                button.setBackground(Color.ORANGE);
                button.setForeground(Color.BLACK);
            }

            button.addActionListener(this);
            panel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setPreferredSize(new Dimension(150, 400));

        setLayout(new BorderLayout(10, 10));
        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.EAST);

        setTitle("Calculator");
        setSize(600, 600); // Adjusted to accommodate the history display
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.charAt(0) >= '0' && command.charAt(0) <= '9' || command.equals(".")) {
            display.setText(display.getText() + command);
        } else if (command.equals("C")) {
            display.setText("");
            num1 = num2 = result = 0;
            operator = "";
        } else if (command.equals("Del")) { // Changed "←" to "Del" to match button label
            String text = display.getText();
            if (!text.isEmpty()) {
                display.setText(text.substring(0, text.length() - 1));
            }
        } else if (command.equals("√")) {
            num1 = Double.parseDouble(display.getText());
            result = Math.sqrt(num1);
            display.setText(String.valueOf(result));
            addCalculationToHistory("√" + num1 + " = " + result);
        } else if (command.equals("=")) {
            num2 = Double.parseDouble(display.getText());
            switch (operator) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/": result = num1 / num2; break;
                case "x^n": result = Math.pow(num1, num2); break;
            }
            display.setText(String.valueOf(result));
            addCalculationToHistory(num1 + " " + operator + " " + num2 + " = " + result);
        } else {
            if (!display.getText().isEmpty()) {
                num1 = Double.parseDouble(display.getText());
                operator = command;
                display.setText("");
            }
        }
    }

    private void addCalculationToHistory(String calculation) {
        if (historyModel.size() == 10) {
            historyModel.remove(0); // Remove the oldest calculation if the list has 10 items
        }
        historyModel.addElement(calculation);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}
