package no.vegetarguide.scanner.wizard;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import no.vegetarguide.scanner.AlertDialogFragment;
import no.vegetarguide.scanner.BaseActivity;
import no.vegetarguide.scanner.R;
import no.vegetarguide.scanner.SingleClickListener;
import no.vegetarguide.scanner.integration.ModifyProductRequest;
import no.vegetarguide.scanner.model.Category;
import no.vegetarguide.scanner.model.Product;

import static org.apache.commons.lang3.StringUtils.trimToNull;

public class RequestMetaInformation extends BaseActivity {

    public static final String CATEGORIES_KEY = "categories";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metainformation);

        Bundle b = getIntent().getExtras();
        Parcelable obj = b.getParcelable(ModifyProductRequest.class.getSimpleName());
        ModifyProductRequest modifyRequest = (ModifyProductRequest) obj;
        ArrayList<Category> categories = b.getParcelableArrayList(CATEGORIES_KEY);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, MetaInformationFragment.newInstance(modifyRequest, categories))
                    .commit();
        }

    }

    public static class MetaInformationFragment extends Fragment {

        private static Category NO_CATEGORY;
        private EditText brand_edit;
        private TextView gtin_value;
        private EditText title_edit;
        private EditText subtitle_edit;
        private EditText comment;
        private ModifyProductRequest modifyRequest;
        private ArrayList<Category> categories;


        public MetaInformationFragment() {
        }

        public static MetaInformationFragment newInstance(ModifyProductRequest modifyRequest, ArrayList<Category> categories) {
            MetaInformationFragment frag = new MetaInformationFragment();
            Bundle args = new Bundle();
            args.putParcelable(ModifyProductRequest.class.getSimpleName(), modifyRequest);
            args.putParcelableArrayList(CATEGORIES_KEY, categories);
            frag.setArguments(args);

            return frag;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_metainformation, container, false);
            Bundle arguments = getArguments();
            if (arguments == null) {
                throw new IllegalStateException("Missing required state arguments bundle");
            }
            modifyRequest = arguments.getParcelable(ModifyProductRequest.class.getSimpleName());
            categories = arguments.getParcelableArrayList(CATEGORIES_KEY);
            NO_CATEGORY = new Category(getString(R.string.category_not_selected), null);

            createNextButton(rootView, modifyRequest.getProduct());
            createCancelButton(rootView);
            createTextEdits(rootView, modifyRequest.getProduct());
            createCategorySelector(rootView, categories, modifyRequest.getProduct());

            return rootView;
        }

        private void createCategorySelector(View rootView, final List<Category> categories, final Product product) {
            Spinner categorySelector = (Spinner) rootView.findViewById(R.id.category_selector);
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySelector.setAdapter(adapter);

            String previouslySelectedCategory = product.getCategory();

            if (!categories.contains(NO_CATEGORY)) {
                categories.add(0, NO_CATEGORY);
            }
            if (previouslySelectedCategory != null) {
                for (int i = 1; i < categories.size(); i++) {
                    Category candidate = categories.get(i);
                    if (candidate.getCode().equals(previouslySelectedCategory)) {
                        categorySelector.setSelection(i);
                        break;
                    }
                }
            }

            categorySelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Category selection = (Category) parent.getItemAtPosition(position);
                    product.setCategory(selection.getCode());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    product.setCategory(null);
                }
            });

        }

        private void createTextEdits(View rootView, final Product product) {
            gtin_value = (TextView) rootView.findViewById(R.id.gtin_value);
            gtin_value.setText(product.getGtin());

            title_edit = (EditText) rootView.findViewById(R.id.title_edit);
            title_edit.setText(product.getTitle());
            subtitle_edit = (EditText) rootView.findViewById(R.id.subtitle_edit);
            subtitle_edit.setText(product.getSubtitle());
            brand_edit = (EditText) rootView.findViewById(R.id.brand_edit);
            brand_edit.setText(product.getBrand());

            comment = (EditText) rootView.findViewById(R.id.commentary_edit);
            comment.setText(product.getGeneral_comment());
        }

        private void createCancelButton(View rootView) {
            View cancelButton = rootView.findViewById(R.id.cancel_wizard_button);
            cancelButton.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                }
            });
        }

        private void createNextButton(View rootView, final Product product) {
            View nextButton = rootView.findViewById(R.id.next_wizard_button);
            nextButton.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    Intent launchNext = new Intent(getActivity(), CheckIfNotVegetarianAtAll.class);

                    mergeProductValues(product);
                    if (missingValues(product)) {
                        DialogFragment dialog = AlertDialogFragment.newInstance(
                                R.string.meta_information_input_error_title, R.string.meta_information_input_error);
                        if (dialog != null)
                            dialog.show(getFragmentManager(), "inputError");
                    } else {
                        launchNext.putExtra(ModifyProductRequest.class.getSimpleName(), modifyRequest);
                        startActivity(launchNext);
                    }
                }
            });
        }

        private boolean missingValues(Product product) {
            return product.getTitle() == null; // only title is mandatory
        }

        private void mergeProductValues(Product product) {
            String trimmedTitle = trimToNull(title_edit.getText().toString());
            product.setTitle(trimmedTitle == null ? product.getTitle() : trimmedTitle); // keep previous value if missing

            String trimmedSubtitle = trimToNull(subtitle_edit.getText().toString());
            product.setSubtitle(trimmedSubtitle); // allow deleting field, i.e. new subtitle can be null

            String trimmedBrand = trimToNull(brand_edit.getText().toString());
            product.setBrand(trimmedBrand == null ? product.getBrand() : trimmedBrand); // keep previous value if missing

            String trimmedComment = trimToNull(comment.getText().toString());
            product.setGeneral_comment(trimmedComment); // overwrite previous value always
        }
    }
}
