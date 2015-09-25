package com.tipcalculator.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Scott on 9/23/2015.
 */
public class TipFragment extends Fragment {

    @Bind(R.id.amountEditText)
    EditText amountEditText;
    @Bind(R.id.myseekbar)
    SeekBar percentageSeekBar;
    @Bind(R.id.peopleSeekBar)
    SeekBar peopleSeekBar;
    @Bind(R.id.snackbarPosition)
    View coordinatorLayoutView;
    @Bind(R.id.tipButton)
    Button tipButton;
    @Bind(R.id.enterBillAmountTitleTextView)
    TextView billAmountTitle;
    @Bind(R.id.tipAmountTextView)
    TextView tipAmount;
    @Bind(R.id.peopleTitleTextView)
    TextView peopleTitle;
    @Bind(R.id.PercentageTitleTextView)
    TextView percentageTitle;
    @Bind(R.id.selectedPercentageTextView)
    TextView selectedPercentage;
    @Bind(R.id.selectedPeopleTextView)
    TextView selectedPeople;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tip_calculator, container, false);

        ButterKnife.bind(this, view);

        //sets custom textwatcher to only allow currency values
        amountEditText.addTextChangedListener(new CurrencyTextWatcher(amountEditText));


        setDefaults();
        setFonts();

        //set and attach listeners to view elements
        tipButton.setOnClickListener(new CalcTip());
        peopleSeekBar.setOnSeekBarChangeListener(new PeopleSeekBarListener());
        percentageSeekBar.setOnSeekBarChangeListener(new PercentageSeekListener());

        return view;
    }


    private void setFonts() {
        new Fonts().setFontLollipopLightRoboto(peopleTitle);
        new Fonts().setFontLollipopLightRoboto(percentageTitle);
        new Fonts().setFontLollipopLightRoboto(billAmountTitle);
        new Fonts().setFontLollipopLightRoboto(tipAmount);
        new Fonts().setFontLollipopLightRoboto(selectedPeople);
        new Fonts().setFontLollipopLightRoboto(selectedPercentage);
    }

    private void setDefaults() {
        percentageSeekBar.setMax(99); //0-99 = 100
        percentageSeekBar.setProgress(29); //default tip percentage 30% 29 + 1 starts at 0
        selectedPercentage.setText((percentageSeekBar.getProgress() + 1) + "%");

        amountEditText.setText("$0.00"); //default bill amount
        peopleSeekBar.setProgress(0);
        selectedPeople.setText(peopleSeekBar.getProgress() + 1 + "");

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void showErrorSnackBar(String message) {
        Snackbar
                .make(coordinatorLayoutView, message, Snackbar.LENGTH_LONG)
                .setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        amountEditText.requestFocus();
                    }
                })
                .show();
    }

    private String removeSpecialCharacters(String string) {
        return string.replace("$", "").replace(",", "");
    }

    private String buildLineSpacer(String tipAmount) {
        int numberOfSpaces = 12; //10 for "TIP AMOUNT" + 2 spaces for centering
        String divider = "____________";

        for (int i = 0; i < tipAmount.length(); i++) {
            numberOfSpaces++;
            divider = divider + "_";
        }

        return divider;


    }

    private String findTipSeperation(CalculateTip calcTipObject, int numPeople) {

        if (numPeople == 1) {
            return (calcTipObject.getGrandTotal() + ""); //one person
        } else {
            //more than one person
            //get grand total and divide by number of people
            return calcTipObject.formatCurrencyAmount(calcTipObject.getGrandTotalDouble() / numPeople) + " Per Person";
        }
    }

    class CalcTip implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            if (amountEditText.getEditableText().toString().equals("") ||
                    amountEditText.getEditableText().toString().equals("$0.00")) {
                showErrorSnackBar("No Amount Entered, Please Enter a Bill Amount");

            } else {
                //calculate tip and enter tip amount into textview

                double amount = 0.0;
                int percent, people;

                percent = percentageSeekBar.getProgress() + 1;
                people = peopleSeekBar.getProgress() + 1;

                try {
                    //attempt to parse editable text into double
                    amount = Double.parseDouble(removeSpecialCharacters(amountEditText.getEditableText().toString()));
                } catch (NumberFormatException nfe) {
                    System.out.println("could not parse" + amountEditText.getEditableText().toString());
                }
                CalculateTip calcTipObject = new CalculateTip(percent, amount, people);
                tipAmount.setText(BulletTextUtil.makeBulletList(1,
                        "Bill Amount: " + calcTipObject.getBillAmount(),
                        "Tip %: " + selectedPercentage.getText().toString(),
                        "Number Of Guest(s): " + selectedPeople.getText().toString(),
                        "Tip Amount: " + calcTipObject.getTipAmount(),
                        buildLineSpacer(calcTipObject.getTipAmount()),
                        "Grand Total: " + findTipSeperation(calcTipObject, Integer.parseInt(selectedPeople.getText().toString()))));

            }
        }


    }

    class PercentageSeekListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            selectedPercentage.setText((progress + 1) + "%");
        }

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    class PeopleSeekBarListener implements SeekBar.OnSeekBarChangeListener {

        /**
         * Notification that the progress level has changed. Clients can use the fromUser parameter
         * to distinguish user-initiated changes from those that occurred programmatically.
         *
         * @param seekBar  The SeekBar whose progress has changed
         * @param progress The current progress level. This will be in the range 0..max where max
         *                 was set by {@link ProgressBar#setMax(int)}. (The default value for max is 100.)
         * @param fromUser True if the progress change was initiated by the user.
         */
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            selectedPeople.setText(progress + 1 + "");
        }

        /**
         * Notification that the user has started a touch gesture. Clients may want to use this
         * to disable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        /**
         * Notification that the user has finished a touch gesture. Clients may want to use this
         * to re-enable advancing the seekbar.
         *
         * @param seekBar The SeekBar in which the touch gesture began
         */
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }


}
