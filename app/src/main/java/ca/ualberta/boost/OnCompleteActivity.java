package ca.ualberta.boost;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;

import java.math.BigDecimal;
import java.text.NumberFormat;

import ca.ualberta.boost.controllers.RideEventListener;
import ca.ualberta.boost.controllers.RideTracker;
import ca.ualberta.boost.controllers.ActiveUser;
import ca.ualberta.boost.models.Driver;
import ca.ualberta.boost.models.Ride;
import ca.ualberta.boost.models.RideStatus;
import ca.ualberta.boost.models.User;
import ca.ualberta.boost.models.UserType;
import ca.ualberta.boost.stores.UserStore;

public class OnCompleteActivity extends AppCompatActivity implements View.OnClickListener, RideEventListener {

    private ImageButton thumbsUpButton;
    private ImageButton thumbsDownButton;

    private EditText tipAmountView;

    private TextView totalAmountView;
    private BigDecimal totalAmount;

    private ImageButton subtractTipButton;
    private ImageButton addTipButton;
    private BigDecimal tipAmount = new BigDecimal(0).setScale(2, BigDecimal.ROUND_FLOOR);
    private static final BigDecimal TIP_RATE = new BigDecimal(1.00);

    private RideTracker tracker;

    private Button qrCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ride_complete_page);
        getSupportFragmentManager();

        tracker = new RideTracker(ActiveUser.getCurrentRide());
        tracker.addListener(this);

        thumbsUpButton = findViewById(R.id.thumbs_up_button);
        thumbsDownButton = findViewById(R.id.thumbs_down_button);

        thumbsUpButton.setOnClickListener(this);
        thumbsDownButton.setOnClickListener(this);

        subtractTipButton = findViewById(R.id.subtract_tip_button);
        addTipButton = findViewById(R.id.add_tip_button);
        tipAmountView = findViewById(R.id.tip_amount);
        tipAmountView.addTextChangedListener(new MoneyTextWatcher());

        subtractTipButton.setOnClickListener(this);
        addTipButton.setOnClickListener(this);

        totalAmountView = findViewById(R.id.total_fare_amount);
        changeTipText(tipAmount);

        qrCodeButton = findViewById(R.id.purchase_code_button);
        qrCodeButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.thumbs_up_button:
                thumbsUpButton.setSelected(true);
                thumbsDownButton.setSelected(false);
                break;
            case R.id.thumbs_down_button:
                thumbsDownButton.setSelected(true);
                thumbsUpButton.setSelected(false);
                break;
            case R.id.subtract_tip_button:
                if (tipAmount.compareTo(TIP_RATE) >= 0) {
                    // if tipamount > tip rate -> subtract tip rate from tipamount
                    changeTipText(tipAmount.subtract(TIP_RATE));
                } else if (tipAmount.compareTo(TIP_RATE) < 0 && tipAmount.compareTo(BigDecimal.ZERO) > 0){
                    // if tipamount < tip rate and tipamount > 0 -> make tipamount 0
                    changeTipText(BigDecimal.ZERO);
                }
                break;
            case R.id.add_tip_button:
                changeTipText(tipAmount.add(TIP_RATE));
                break;
            case R.id.purchase_code_button:
                launchQrCode();
                break;
            default:
                break;
        }
    }

    /**
     * Changes the total fare amount based in tip
     */
    private void changeTotal() {
        totalAmount = new BigDecimal(ActiveUser.getCurrentRide().baseFare()).add(tipAmount).setScale(2, BigDecimal.ROUND_FLOOR);
        Log.d("OnComplete fare amount", totalAmount.toString());
        String totalFormatted = String.format("$%s", totalAmount.toString());
        totalAmountView.setText(totalFormatted);
    }

    /**
     * Changes the tip view
     * @param tip
     *      BigDecimal of tip amount
     */
    private void changeTipText(BigDecimal tip) {
        tipAmountView.setText(tip.toString());
    }

    /**
     * Launches the QR code
     * @see QRCodeFragment
     */
    public void launchQrCode() {
        QRCodeFragment fragment = new QRCodeFragment(totalAmount.doubleValue());
        fragment.show(getSupportFragmentManager(),"QRCode");
    }

    /**
     * Checks if the status of the ride has changed and launches RiderMainPage
     * after the driver has accepted the payment
     * @param ride
     *      ride to check the status of
     */
    @Override
    public void onStatusChange(@NonNull Ride ride) {
        if (ride.getRideStatus() == RideStatus.PAID && ActiveUser.getUser().getType() == UserType.RIDER) {
            Log.d("OnCompleteActivity", "RideStatus == FINISHED");
            Intent intent = new Intent(this, RiderMainPage.class);
            startActivity(intent);
            changeDriver(ride);
        }
    }

    /**
     * Updates the driver in the database
     * @param ride
     *      the ride to check the driver of
     */
    private void changeDriver(Ride ride) {
        String driverUsername = ride.getDriverUsername();
        UserStore.getUser(driverUsername).addOnSuccessListener(new OnSuccessListener<User>() {
            @Override
            public void onSuccess(User user) {
                Driver driver = (Driver) user;
                if (thumbsUpButton.isSelected()) { driver.giveThumbsUp(); }
                else if (thumbsDownButton.isSelected()) { driver.giveThumbsDown(); }
                UserStore.saveUser(driver);
            }
        });
    }

    /**
     * Subclass that changes the tip view so it is always in a correct format
     * even while the user is typing in the text view
     */
    private class MoneyTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { }

        @Override
        public void afterTextChanged(Editable editable) {
            if (tipAmountView == null) return;
            String s = editable.toString();
            if (s.isEmpty()) {
                tipAmountView.setText("$0.00");
                return;
            }
            tipAmountView.removeTextChangedListener(this);
            String cleanString = s.replaceAll("[$,.]", "");
            BigDecimal parsed = new BigDecimal(cleanString).setScale(2, BigDecimal.ROUND_FLOOR)
                                                            .divide(new BigDecimal(100), BigDecimal.ROUND_FLOOR);
            String formatted = NumberFormat.getCurrencyInstance().format(parsed);
            tipAmountView.setText(formatted);
            tipAmountView.setSelection(formatted.length());
            tipAmountView.addTextChangedListener(this);

            tipAmount = parsed;
            changeTotal();
        }
    }
}
