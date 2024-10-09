package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * MainActivity serves as the entry point of the calculator app. It handles the initialization
 * of UI components, sets up the button click listeners, and performs basic arithmetic operations.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // TextView to display the result and the equation
    TextView resultTV, solutionTV;

    // Declaration of all the calculator buttons
    MaterialButton buttonC, buttonBracketOpen, buttonBracketClose;
    MaterialButton button0, button1, button2, button3, button4, button5, button6, button7, button8, button9;
    MaterialButton buttonMul, buttonPlus, buttonMinus, buttonDivide, buttonEquals;
    MaterialButton buttonAC, buttonDot;

    /**
     * Initializes the activity. Sets up the UI components, applies window insets, and assigns
     * IDs to all the buttons in the layout.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable edge-to-edge display mode
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Adjust the padding of the main view to fit the system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the TextViews for displaying results and equations
        resultTV = findViewById(R.id.result_tv);
        solutionTV = findViewById(R.id.solution_tv);

        // Assign IDs to all buttons
        assignID(buttonC, R.id.button_c);
        assignID(button0, R.id.button_zero);
        assignID(button1, R.id.button_one);
        assignID(button2, R.id.button_two);
        assignID(button3, R.id.button_three);
        assignID(button4, R.id.button_four);
        assignID(button5, R.id.button_five);
        assignID(button6, R.id.button_six);
        assignID(button7, R.id.button_seven);
        assignID(button8, R.id.button_eight);
        assignID(button9, R.id.button_nine);

        assignID(buttonMul, R.id.button_multiply);
        assignID(buttonPlus, R.id.button_plus);
        assignID(buttonMinus, R.id.button_minus);
        assignID(buttonDivide, R.id.button_divide);

        assignID(buttonBracketOpen, R.id.button_open_bracket);
        assignID(buttonBracketClose, R.id.button_close_bracket);

        assignID(buttonEquals, R.id.button_equal);
        assignID(buttonAC, R.id.button_ac);
        assignID(buttonDot, R.id.button_dot);
    }

    /**
     * Assigns an ID to a MaterialButton and sets the OnClickListener.
     *
     * @param btn The MaterialButton to assign.
     * @param id  The resource ID of the button from the layout.
     */
    void assignID(MaterialButton btn, int id) {
        // Link the button ID to the actual button object
        btn = findViewById(id);
        // Set the click listener for the button
        btn.setOnClickListener(this);
    }

    /**
     * Handles click events for all the calculator buttons. Determines the button pressed,
     * updates the display, and performs calculations accordingly.
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        // Get the clicked button and its text
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataTocalculate = solutionTV.getText().toString();

        // Handle the 'AC' button to clear the screen
        if (buttonText.equals("AC")) {
            solutionTV.setText("");
            resultTV.setText("0");
            return;
        }

        // Handle the '=' button to evaluate the current expression
        if (buttonText.equals("=")) {
            // Get the final result of the expression
            String finalResult = getResults(dataTocalculate);
            // Display the result in the result TextView
            if (!finalResult.equals("Error")) {
                resultTV.setText(finalResult);
            } else {
                resultTV.setText("Error");
            }
            return;
        }


        // Handle the 'C' button to remove the last character
        if (buttonText.equals("C")) {
            // Check if there's something to delete before removing the last character
            if (!dataTocalculate.isEmpty()) {
                dataTocalculate = dataTocalculate.substring(0, dataTocalculate.length() - 1);
            }
        } else {
            // Append the button text to the current calculation
            dataTocalculate = dataTocalculate + buttonText;
        }

        // Update the equation display
        solutionTV.setText(dataTocalculate);

        // Calculate the result and display it
        String finalResult = getResults(dataTocalculate);
        if (!finalResult.equals("Error")) {
            resultTV.setText(finalResult);
        }
    }

    /**
     * Evaluates the mathematical expression passed to it using the Rhino JavaScript engine.
     *
     * @param data The mathematical expression in string format.
     * @return The result of the evaluated expression as a string, or "Error" if the evaluation fails.
     */
    String getResults(String data) {
        try {
            // Enter the JavaScript context for expression evaluation
            Context context = Context.enter();
            context.setOptimizationLevel(-1); // Disable optimization for compatibility
            Scriptable scriptable = context.initSafeStandardObjects();
            // Evaluate the mathematical expression and return the result
            return context.evaluateString(scriptable, data, "javascript", 1, null).toString();
        } catch (Exception e) {
            // Return "Error" if evaluation fails
            return "Error";
        }
    }
}
