//package sg.edu.np.mad.assignment.Model;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.speech.RecognizerIntent;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
//import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
//import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
//
//import java.util.ArrayList;
//import java.util.Locale;
//
//import sg.edu.np.mad.assignment.R;
//import sg.edu.np.mad.assignment.Translate;
//
//public class Translation extends AppCompatActivity {
//    private Spinner fromSpinner, toSpinner;
//    private TextInputEditText sourceEdt;
//    private ImageView micIV;
//    private MaterialButton translateBtn;
//    private TextView translatedTV;
//    String[] fromLanguages = {"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
//    String[] toLanguages = {"To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
//
//    private static final int REQUEST_PERMISSIONCODE = 1;
//    int languageCode, fromLanguageCode, toLanguagecode = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_translate);
//        fromSpinner = findViewById(R.id.idFromSpinner);
//        toSpinner = findViewById(R.id.idtoSpinner);
//        sourceEdt = findViewById(R.id.idEdtSource);
//        micIV = findViewById(R.id.idIVMic);
//        translateBtn = findViewById(R.id.idBtnTranslate);
//        translatedTV = findViewById(R.id.idTVTranslatedTV);
//        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                fromLanguageCode = getLanguageCode(fromLanguages[position]);
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.translate_spinner_item,fromLanguages);
//        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        fromSpinner.setAdapter(fromAdapter);
//
//        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                toLanguagecode = getLanguageCode(toLanguages[position]);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.translate_spinner_item,toLanguages);
//        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        toSpinner.setAdapter(toAdapter);
//
//        translateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                translatedTV.setText("");
//                if(sourceEdt.getText().toString().isEmpty()) {
//                    Toast.makeText(Translate.this, "please enter Text to translate", Toast.LENGTH_SHORT).show();
//                }else if(fromLanguageCode==0){
//                    Toast.makeText(Translate.this,"please select choose language",Toast.LENGTH_SHORT).show();
//                }
//                else if(toLanguagecode==0){
//                    Toast.makeText(Translate.this,"please select the language to make translation",Toast.LENGTH_SHORT).show();
//                }else{
//                    translateText(fromLanguageCode,toLanguagecode, sourceEdt.getText().toString());
//
//                }
//            }
//        });
//        micIV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"speak to convert to text");
//                try {
//                    startActivityForResult(i, REQUEST_PERMISSIONCODE);
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Toast.makeText(Translate.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==REQUEST_PERMISSIONCODE){
//            if(resultCode==RESULT_OK&&data!=null){
//                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                sourceEdt.setText(result.get(0));
//            }
//        }
//    }
//
//    private void translateText(int fromLanguageCode, int toLanguagecode, String source){
//        translatedTV.setText("Downloading Model...");
//        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
//                .setSourceLanguage(fromLanguageCode)
//                .setTargetLanguage(toLanguagecode)
//                .build();
//
//        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
//
//        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
//
//        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                translatedTV.setText("Translating...");
//                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//                        translatedTV.setText(s);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(Translate.this,"fail to translate : "+e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(Translate.this,"Fail to download Model"+e.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }
//
//
//
//
//    //String[] toLanguages = {"To","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali","Catalan","Czech","Welsh","Hindi","Urdu"};
//    public int getLanguageCode(String language){
//        int languageCode = 0;
//        switch (language){
//            case "English":
//                languageCode = FirebaseTranslateLanguage.EN;
//                break;
//            case "Afrikaans":
//                languageCode = FirebaseTranslateLanguage.AF;
//                break;
//            case "Arabic":
//                languageCode = FirebaseTranslateLanguage.AR;
//                break;
//            case "Belarusian":
//                languageCode = FirebaseTranslateLanguage.BE;
//                break;
//            case "Bulgarian":
//                languageCode = FirebaseTranslateLanguage.BG;
//                break;
//            case "Bengali":
//                languageCode = FirebaseTranslateLanguage.BN;
//                break;
//            case "Catalan":
//                languageCode = FirebaseTranslateLanguage.CA;
//                break;
//            case "Czech":
//                languageCode = FirebaseTranslateLanguage.CS;
//                break;
//            case "Welsh":
//                languageCode = FirebaseTranslateLanguage.CY;
//                break;
//            case "Hindi":
//                languageCode = FirebaseTranslateLanguage.HI;
//                break;
//            case "Urdu":
//                languageCode = FirebaseTranslateLanguage.UR;
//                break;
//            default :
//                languageCode = 0;
//
//        }
//        return languageCode;
//
//
//    }
//
//
//
//
//
//
//}