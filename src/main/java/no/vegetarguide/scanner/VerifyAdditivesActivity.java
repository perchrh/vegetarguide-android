package no.vegetarguide.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import no.vegetarguide.scanner.model.AdditivesDetails;

public class VerifyAdditivesActivity extends Activity {
    static final String VERIFICATION_DATA_KEY = "data";

    private CheckBox maybeAnimalDerivedENumbers;
    private CheckBox unspecifiedAromas;
    private CheckBox unspecifiedOils;
    private CheckBox maybeOtherAnimalDerivedAdditives;
    private EditText additivesVerificationDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_additives);

        initCheckBoxes();
        initSubmitButton();
    }

    private void initSubmitButton() {
        View submitButton = findViewById(R.id.submitConfirmedAdditives);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdditivesDetails data = new AdditivesDetails(unspecifiedAromas.isChecked(), unspecifiedOils.isChecked(),
                        maybeAnimalDerivedENumbers.isChecked(), maybeOtherAnimalDerivedAdditives.isChecked(),
                        additivesVerificationDetail.getText().toString());
                Intent dataWrapper = new Intent();
                dataWrapper.putExtra(VERIFICATION_DATA_KEY, data);
                setResult(Activity.RESULT_OK, dataWrapper);
                finish();
            }
        });
    }

    private void initCheckBoxes() {
        maybeAnimalDerivedENumbers = (CheckBox) findViewById(R.id.contains_maybe_animal_derived_e_numbers);
        maybeOtherAnimalDerivedAdditives = (CheckBox) findViewById(R.id.contains_maybe_animal_derived_additives);
        unspecifiedAromas = (CheckBox) findViewById(R.id.contains_unspecified_aromas);
        unspecifiedOils = (CheckBox) findViewById(R.id.contains_unspecified_oils);
        additivesVerificationDetail = (EditText) findViewById(R.id.additives_verification_user_detail);
    }

}
