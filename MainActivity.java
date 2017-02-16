package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * Diese App bestellt Kaffee
 */
public class MainActivity extends AppCompatActivity {

    //Globale Variable
    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    // Diese Methode wird aufgerufen wenn Knopf "Order" gedrückt wird
    public void submitOrder(View view) {
        // get input of EditText
        EditText nameEditText = (EditText) findViewById(R.id.name_editText);
        String customerName = nameEditText.getText().toString();

        // get status of CheckBoxes
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();
        boolean hasChocolate = chocolateCheckBox.isChecked();

        // Create OrderSummary
        int price = calculatePrice(5, hasWhippedCream, hasChocolate);
        String text = createOrderSummary(price, hasWhippedCream, hasChocolate, customerName);

        // Send OrderSummary via Intent to Email-App
        Intent eMailIntent = new Intent(Intent.ACTION_SENDTO);
        eMailIntent.setData(Uri.parse("mailto:")); // only email apps should handle this
        eMailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject, customerName));
        eMailIntent.putExtra(Intent.EXTRA_TEXT, text);
        if (eMailIntent.resolveActivity(getPackageManager()) != null){
            startActivity(eMailIntent);
        }
    }

    /*
     * berechnet den Preis der Becher
     *
     * @param basePrice; will be mltiplied with the quantity
     * @param withWhippedCream; increase the base price by one if true
     * @param withChocolate; increase the base price by two if true
     * @return total Price
     */
    private int calculatePrice(int basePrice, boolean withWhippedCream, boolean withChocolate) {
        if (withWhippedCream) basePrice++; // add 1€ if user wants whipped cream
        if (withChocolate) basePrice += 2; // add 2€ if user wants chocolate
        return quantity * basePrice;
    }

    /*
     * erstellt den Bestellungs-Text
     *
     * @param price; of order
     * @param addWhippedCream; -> with or without; is it checked? Want whipped Cream?
     * @param addChocolate; -> with or without; is it checked? Want Chocolate?
     * @param name; of the customer
     *
     * @return order message
     */
    private String createOrderSummary(int price, boolean addWhippedCream,
                                      boolean addChocolate, String name) {
        String text = getString(R.string.order_summary_name, name);
        text += "\n" + getString(R.string.order_summary_whippedCream) + " " + addWhippedCream;
        text += "\n" + getString(R.string.order_summary_chocolate) + " " + addChocolate;
        text += "\n" + getString(R.string.order_summary_quantity) + " " + quantity;
        text += "\n" + getString(R.string.order_summary_total) + " " +
                NumberFormat.getCurrencyInstance().format(price);
        text += "\n"+ getString(R.string.thank_you);
        return text;
    }

    // Diese Methode erhöht die Quantität um 1
    public void increment(View view) {
        if (quantity < 100)
            displayQuantity(++quantity);
        else
            Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show();
    }

    // Diese Methode erniedrigt die Quantität um 1
    public void decrement(View view) {
        if (quantity > 1)
            displayQuantity(--quantity);
        else
            Toast.makeText(this, "Nope", Toast.LENGTH_SHORT).show();
    }

    // Diese Methode schreibt die Anzahl der Kaffees
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }
}