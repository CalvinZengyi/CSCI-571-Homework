package com.zengyicalvin.homework9;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab1Search extends Fragment{
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab1search, container, false);

        final AppCompatAutoCompleteTextView autoCompleteTextView = rootView.findViewById(R.id.editText);
        final TextView selectedText = rootView.findViewById(R.id.selected_item);

        //Setting up the adapter for AutoSuggest
        autoSuggestAdapter = new AutoSuggestAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(2);
        autoCompleteTextView.setAdapter(autoSuggestAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        selectedText.setText(autoSuggestAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });



        //****************Category spinner***************
        final Spinner spinner1 = (Spinner)rootView.findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.category_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner1.setAdapter(adapter1);

        //****************Unit spinner***************
        final Spinner spinner2 = (Spinner)rootView.findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.unit_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner2.setAdapter(adapter2);

        EditText edt = (EditText) rootView.findViewById(R.id.editText3);
        edt.setEnabled(false);
        edt.setInputType(InputType.TYPE_NULL);
        edt.setFocusable(false);

        RadioGroup radioGroup2 = (RadioGroup) rootView.findViewById(R.id.radioGroup2);
        radioGroup2
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        EditText edt = (EditText) rootView.findViewById(R.id.editText3);
                        if (checkedId == R.id.here) {
                            edt.setEnabled(false);
                            edt.setInputType(InputType.TYPE_NULL);
                            edt.setFocusable(false);
                        } else if (checkedId == R.id.there) {
                            edt.setEnabled(true);
                            edt.setInputType(InputType.TYPE_CLASS_TEXT);
                            edt.setFocusableInTouchMode(true);
                            edt.setFocusable(true);
                        }
                    }
                });

        Button clear = (Button) rootView.findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)rootView.findViewById(R.id.warning_keyword)).setVisibility(View.GONE);
                ((TextView)rootView.findViewById(R.id.warning_address)).setVisibility(View.GONE);
                EditText editText = (EditText) rootView.findViewById(R.id.editText);
                editText.setText("");
                EditText editText2 = (EditText) rootView.findViewById(R.id.editText2);
                editText2.setText("");
                EditText editText3 = (EditText) rootView.findViewById(R.id.editText3);
                editText3.setText("");
                RadioButton here = (RadioButton) rootView.findViewById(R.id.here);
                here.setChecked(true);
                spinner1.setSelection(0);
            }
        });

        Button button = (Button) rootView.findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = ((TextView) (rootView.findViewById(R.id.editText))).getText().toString();

                RadioGroup radioGroup = (RadioGroup)(rootView.findViewById(R.id.radioGroup2));
                int checkedId = radioGroup.getCheckedRadioButtonId();
                RadioButton checkedRadioButton = (RadioButton) (rootView.findViewById(checkedId));
                String location = checkedRadioButton.getText().toString().substring(0,1);

                String adress = ((TextView) (rootView.findViewById(R.id.editText3))).getText().toString();



                if (keyword.trim().equalsIgnoreCase("") || (location.equals("O") && adress.trim().equalsIgnoreCase(""))) {
                    Toast.makeText(getActivity(), "Please fix all fields with errors",
                            Toast.LENGTH_LONG).show();
                    if ((keyword.trim().equalsIgnoreCase("")))
                        ((TextView)rootView.findViewById(R.id.warning_keyword)).setVisibility(View.VISIBLE);
                    if ((location.equals("O") && adress.trim().equalsIgnoreCase("")))
                        ((TextView)rootView.findViewById(R.id.warning_address)).setVisibility(View.VISIBLE);
                    return;
                }
                ((TextView)rootView.findViewById(R.id.warning_keyword)).setVisibility(View.GONE);
                ((TextView)rootView.findViewById(R.id.warning_address)).setVisibility(View.GONE);

                Intent intent = new Intent(getActivity(), displayEventsActivity.class);
                //keyword

                intent.putExtra("EXTRA_SESSION_KEYWORD", keyword);
                //category
                String category = ((Spinner) (rootView.findViewById(R.id.spinner))).getSelectedItem().toString();
                intent.putExtra("EXTRA_SESSION_CATEGORY", category);
                //distance
                String distance = ((TextView) (rootView.findViewById(R.id.editText2))).getText().toString().equals("")? "10": ((TextView) (rootView.findViewById(R.id.editText2))).getText().toString();
                Log.i("distanceTest", distance);
                intent.putExtra("EXTRA_SESSION_DISTANCE", distance);
                //unit
                String unit = ((Spinner) (rootView.findViewById(R.id.spinner2))).getSelectedItem().toString();
                intent.putExtra("EXTRA_SESSION_UNIT", unit);
                //location

                intent.putExtra("EXTRA_SESSION_LOCATION", location);
                //address

                intent.putExtra("EXTRA_SESSION_ADDRESS", adress);
                //latitude
                intent.putExtra("EXTRA_SESSION_LATITUDE", "34.0266");
                //longitude
                intent.putExtra("EXTRA_SESSION_LONGITUDE", "-118.283");

                startActivity(intent);
            }
        });
        return rootView;
    }

    private void makeApiCall(String text) {
        ApiCall.make(getActivity(), text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray array = responseObject.getJSONArray("results");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject row = array.getJSONObject(i);
                        stringList.add(row.getString("trackName"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }
}
