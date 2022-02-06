package hk.edu.ouhk.arprimary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Iterator;
import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

import hk.edu.ouhk.arprimary.model.Lesson;
import hk.edu.ouhk.arprimary.model.LessonFragment;
import hk.edu.ouhk.arprimary.viewmodel.armodel.practice.SectionViewModel;
import hk.edu.ouhk.arprimary.viewmodel.armodel.practice.UnitSection;

@Deprecated
public class LessonActivityLegacy extends AppCompatActivity {

    private static final double MIN_OPENGL_VERSION = 3.0;
    private static final String TAG = LessonActivityLegacy.class.getSimpleName();


    String topic;
    int unitNo;
    Lesson lesson;

    ImageButton tipsBtn, speakerBtn, microphone, leave;
    ArFragment arFragment;
    TextView txt_name, txt_defin;
    EditText speechTextShow;

    ViewRenderable name_models, speaker, definition;
    TransformableNode modelNode, speakerNode, defNode, nameNode;
    SectionViewModel viewModel;
    ActivityResultLauncher<Intent> speechLauncher;

    Iterator<UnitSection> sectionIterator;

    private TextToSpeech mTTS;

    boolean lessonStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            Toast.makeText(this, "Your device is not support AR", Toast.LENGTH_LONG).show();
            return;
        }

        topic = getIntent().getExtras().getString("topic");
        unitNo = getIntent().getIntExtra("unit-no", 1);
        lesson = (Lesson) getIntent().getSerializableExtra("lesson");

        viewModel = new ViewModelProvider(this).get(SectionViewModel.class);

        tipsBtn = findViewById(R.id.tipsBtn);
        microphone = findViewById(R.id.microphone);
        leave = findViewById(R.id.leave);
        speechTextShow = findViewById(R.id.editText);

        findViewById(R.id.microphone).setEnabled(false);
        speechLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onSpeechResult);

        // Leave button handling for leaving the game
        leave.setOnClickListener(view -> {
            AlertDialog.Builder leave = new AlertDialog.Builder(LessonActivityLegacy.this);
            leave.setIcon(ContextCompat.getDrawable(LessonActivityLegacy.this, R.drawable.confirm_leave));
            leave.setTitle("Leave Game");
            leave.setMessage("Are you sure to leave the game?");

            leave.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    setResult(RESULT_CANCELED);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            });

            leave.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            leave.show();
        });

        // Tips button handling for game tips or rules
        tipsBtn.setOnClickListener(view -> {
            AlertDialog.Builder tips = new AlertDialog.Builder(LessonActivityLegacy.this);
            tips.setIcon(ContextCompat.getDrawable(LessonActivityLegacy.this, R.drawable.tips));
            tips.setTitle("Shadowing Game Tips");
            tips.setMessage("1. Please click the speaker button for word's pronunciation \n\n" +
                    "2. Please click the microphone button to pronounce the word");
            tips.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            tips.show();
        });

        // Microphone for pronouncing word by player
        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronounce the word");
                speechLauncher.launch(speachIntent);
            }
        });


        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.lesson_arfragment);

        if (arFragment == null) {
            Toast.makeText(this, "ArFragment is null", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // if anything is ready, it will call startLesson
        prepareArModel();

        viewModel.getUnitSectionMutableLiveData().observe(this, unitSection -> {
            txt_name.setText(unitSection.getVocab());
            txt_defin.setText(unitSection.getDefinition());
            modelNode.setRenderable(unitSection.getModelRenderable());
            stopLoading();
            speak(); // speak on each model updated
        });
    }

    private void startLesson() {
        if (lessonStarted) return;
        UnitSection section = sectionIterator.next(); // assume not empty
        viewModel.getUnitSectionMutableLiveData().postValue(section);
        lessonStarted = true;
        findViewById(R.id.microphone).setEnabled(true);
    }

    private int getResourcesFromModel(String id) {
        return getApplicationContext().getResources()
                .getIdentifier(id, "raw", getPackageName());
    }

    private void onSpeechResult(ActivityResult result) {
        if (result.getResultCode() != RESULT_OK || result.getData() == null) {
            Toast.makeText(this, "data is null or result is not ok", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = result.getData();
        String speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
        speechTextShow.setText(speechText);

        // "pass" is for testing only
        if (speechText.equalsIgnoreCase(txt_name.getText().toString()) || speechText.equalsIgnoreCase("pass")) {
            AlertDialog.Builder answer = new AlertDialog.Builder(LessonActivityLegacy.this);
            answer.setIcon(ContextCompat.getDrawable(LessonActivityLegacy.this, R.drawable.happy));
            answer.setTitle("Congratulations!");
            answer.setMessage("You have answered correctly!");
            answer.setCancelable(false);

            // has next section
            if (sectionIterator.hasNext()) {

                answer.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        UnitSection section = sectionIterator.next();
                        viewModel.getUnitSectionMutableLiveData().postValue(section);
                        startLoading();
                    }
                });

            } else { // no next

                // clear all
                nameNode.setRenderable(null);
                modelNode.setRenderable(null);
                speakerNode.setRenderable(null);
                defNode.setRenderable(null);

                answer.setPositiveButton("Finish the lesson", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // do saving progress before finish ?
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });

            }

            answer.show();
        } else {
            AlertDialog.Builder answer = new AlertDialog.Builder(LessonActivityLegacy.this);
            answer.setIcon(ContextCompat.getDrawable(LessonActivityLegacy.this, R.drawable.unhappy));
            answer.setTitle("Unfortunately!");
            answer.setMessage("You have answered wrongly, please try again!");
            answer.setPositiveButton("Okay", (arg0, arg1) -> {
            });
            answer.show();
        }
    }


    private void prepareArModel() {

        startLoading();

        LessonFragment[] fragments = lesson.getFragments();
        int length = fragments.length;
        CompletableFuture<Void>[] futures = new CompletableFuture[length + 3];
        TreeSet<UnitSection> sections = new TreeSet<>();
        for (int i = 0; i < length; i++) {
            LessonFragment fragment = fragments[i];
            final int order = i;
            futures[i] = ModelRenderable.builder()
                    .setSource(this, getResourcesFromModel(fragment.getModelResourceName()))
                    .build()
                    .thenAccept(renderable -> {
                        UnitSection section = new UnitSection(order, renderable, fragment.getDefinition(), fragment.getModelName());
                        sections.add(section);
                    });
        }

        // name model
        futures[length] = ViewRenderable.builder()
                .setView(this, R.layout.name_models)
                .build()
                .thenAccept(renderable -> name_models = renderable);

        // def model
        futures[length + 1] = ViewRenderable.builder()
                .setView(this, R.layout.definition)
                .build()
                .thenAccept(renderable -> definition = renderable);

        // speaker model
        futures[length + 2] = ViewRenderable.builder()
                .setView(this, R.layout.speaker)
                .build()
                .thenAccept(renderable -> speaker = renderable);


        CompletableFuture.allOf(futures).whenComplete((v, ex) -> {

            if (ex != null) {
                Toast.makeText(this, "Render Model Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
                return;
            }

            // finished loading
            stopLoading();

            sectionIterator = sections.iterator();

            Toast.makeText(this, "All models are rendered, you can now tap the screen", Toast.LENGTH_LONG).show();

            arFragment.setOnTapArPlaneListener(
                    (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                        if (lessonStarted) return; // once only

                        startLoading();
                        Anchor anchor = hitResult.createAnchor();
                        AnchorNode anchorNode = new AnchorNode(anchor);
                        anchorNode.setParent(arFragment.getArSceneView().getScene());

                        // set model node
                        modelNode = new TransformableNode(arFragment.getTransformationSystem());
                        modelNode.getScaleController().setMaxScale(0.16f);
                        modelNode.getScaleController().setMinScale(0.1f);
                        modelNode.setParent(anchorNode);
                        modelNode.select();

                        // set name node
                        nameNode = new TransformableNode(arFragment.getTransformationSystem());
                        nameNode.setLocalPosition(new Vector3(0f, modelNode.getLocalPosition().y + 0.7f, 0));
                        nameNode.getScaleController().setMaxScale(1f);
                        nameNode.getScaleController().setMinScale(0.5f);
                        nameNode.setParent(anchorNode);
                        nameNode.setRenderable(name_models);
                        nameNode.select();
                        txt_name = (TextView) name_models.getView();

                        // set def node
                        defNode = new TransformableNode(arFragment.getTransformationSystem());
                        defNode.setLocalPosition(new Vector3(0f, modelNode.getLocalPosition().y + 0.43f, 0));
                        defNode.getScaleController().setMaxScale(1f);
                        defNode.getScaleController().setMinScale(0.5f);
                        defNode.setParent(anchorNode);
                        defNode.setRenderable(definition);
                        defNode.select();
                        txt_defin = (TextView) definition.getView();

                        // set speaker node
                        speakerNode = new TransformableNode(arFragment.getTransformationSystem());
                        speakerNode.setLocalPosition(new Vector3(0.35f, nameNode.getLocalPosition().y + 0.025f, 0));
                        speakerNode.getScaleController().setMaxScale(1f);
                        speakerNode.getScaleController().setMinScale(0.5f);
                        speakerNode.setParent(anchorNode);
                        speakerNode.setRenderable(speaker);
                        speakerNode.select();
                        setupSpeaker();


                        stopLoading();
                        Toast.makeText(this, "Screen Tapped, lesson is now starting...", Toast.LENGTH_LONG).show();
                        this.startLesson();
                    });
        });
    }

    // Adding speaker button for model
    // TODO: i can't find the speaker....
    private void setupSpeaker() {
        mTTS = new TextToSpeech(LessonActivityLegacy.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "Language not supported");
                    } else {
                        speakerBtn.setEnabled(true);
                    }
                } else {
                    Log.e("TTS", "Initialization failed");
                }
            }
        });

        speakerBtn = (ImageButton) speaker.getView();
        speakerBtn.setOnClickListener(v -> speak());
    }

    private void speak(){
        String text = txt_name.getText().toString();
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    // TODO: make better loading screen on AR screen
    private void startLoading(){
        findViewById(R.id.view_loading).setVisibility(View.VISIBLE);
    }

    // TODO: make better loading screen on AR screen
    private void stopLoading(){
        findViewById(R.id.view_loading).setVisibility(View.GONE);
    }


    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo().getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }
}