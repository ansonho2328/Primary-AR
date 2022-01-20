package hk.edu.ouhk.arprimary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.Locale;

public class LessonActivity extends AppCompatActivity {

    private static final double MIN_OPENGL_VERSION = 3.0;
    private static final String TAG = LessonActivity.class.getSimpleName();


    String topic;
    String modelName;
    int modelid;
    int unitNo;
    ImageButton tipsBtn,speakerBtn,microphone;
    ArFragment arFragment;
    TextView txt_name;
    EditText editText;
    ViewRenderable name_models,speaker;
    ModelRenderable apple;

    private TextToSpeech mTTS;
    private static final int RECOGNIZER_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        topic = getIntent().getExtras().getString("topic");
        unitNo = getIntent().getIntExtra("unit-no",1);
        tipsBtn = findViewById(R.id.tipsBtn);
        microphone = findViewById(R.id.microphone);
        editText = findViewById(R.id.editText);

        Toast.makeText(LessonActivity.this, String.valueOf(unitNo), Toast.LENGTH_LONG).show();

        if(topic.equals("Fruit")){

            switch (unitNo){
                case 1: modelName = "Apple";
                    modelid = getApplicationContext().getResources()
                            .getIdentifier("apple"
                                    ,"raw"
                                    ,getPackageName());

                    break;
                case 2: modelName = "Eggplant";
                    modelid = getApplicationContext().getResources()
                            .getIdentifier("eggplant"
                                    ,"raw"
                                    ,getPackageName());
                    break;
                case 3: modelName = "Grape";
                    modelid = getApplicationContext().getResources()
                            .getIdentifier("grape_purlple"
                                    ,"raw"
                                    ,getPackageName());
                    break;

            }
        }

        if(topic.equals("Stationary")){

            switch (unitNo){
                case 1: modelName = "Book";
                    modelid = getApplicationContext().getResources()
                            .getIdentifier("book"
                                    ,"raw"
                                    ,getPackageName());

                    break;

            }
        }
        tipsBtn.setOnClickListener(view -> {
            AlertDialog.Builder tips = new AlertDialog.Builder(LessonActivity.this);
            tips.setIcon(ContextCompat.getDrawable(LessonActivity.this,R.drawable.tips));
            tips.setTitle("Shadowing Game Tips");
            tips.setMessage("1. Click the speaker button for word's pronunciation \n\n" +
                    "2. Click the microphone button to pronounce the word ");
            tips.setPositiveButton("Got It",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {}
            });
            tips.show();
        });

        microphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speach to text");
                startActivityForResult(speachIntent,RECOGNIZER_RESULT);
            }
        });

        if (!checkIsSupportedDeviceOrFinish(this)) return;

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.lesson_arfragment);

        if (arFragment == null) {
            Toast.makeText(this, "ArFragment is null", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        createModel();


    }

    private void createModel(){
        ViewRenderable.builder()
                .setView(this, R.layout.name_models)
                .build()
                .thenAccept(renderable -> name_models = renderable);

        ViewRenderable.builder()
                .setView(this, R.layout.speaker)
                .build()
                .thenAccept(renderable -> speaker = renderable);

        ModelRenderable.builder()
                .setSource(this, modelid)
                .build()
                .thenAccept(model -> {

                    arFragment.setOnTapArPlaneListener(
                            (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                                Anchor anchor = hitResult.createAnchor();
                                addNodeToScene(arFragment, anchor, model);
                            });

                    Toast.makeText(LessonActivity.this, "you are ready to tap", Toast.LENGTH_LONG).show();

                })
                .exceptionally(ex -> {
                    Toast.makeText(arFragment.getContext(), "Error:" + ex.getMessage(), Toast.LENGTH_LONG).show();
                    return null;
                });


    }

    private void addNodeToScene(ArFragment arFragment, Anchor anchor, Renderable renderable) {
        AnchorNode anchorNode = new AnchorNode(anchor);
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        TransformableNode node = new TransformableNode(arFragment.getTransformationSystem());
        node.getScaleController().setMaxScale(0.16f);
        node.getScaleController().setMinScale(0.06f);
        node.setParent(anchorNode);
        node.setRenderable(renderable);
        node.select();

        addName(anchorNode, node, modelName);
        addSpeaker(anchorNode, node);
    }

    private void addName(AnchorNode anchorNode,TransformableNode model,String name ){
        TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
        nameView.setLocalPosition(new Vector3(0f, model.getLocalPosition().y+0.5f,0));
        nameView.getScaleController().setMaxScale(1f);
        nameView.getScaleController().setMinScale(0.5f);
        nameView.setParent(anchorNode);
        nameView.setRenderable(name_models);
        nameView.select();

        txt_name = (TextView) name_models.getView();
        txt_name.setText(name);
    }

    private void addSpeaker(AnchorNode anchorNode,TransformableNode model){
        TransformableNode speakerView = new TransformableNode(arFragment.getTransformationSystem());
        speakerView.setLocalPosition(new Vector3(0.2f, model.getLocalPosition().y+0.5f,0));
        speakerView.getScaleController().setMaxScale(1f);
        speakerView.getScaleController().setMinScale(0.5f);
        speakerView.setParent(anchorNode);
        speakerView.setRenderable(speaker);
        speakerView.select();

        mTTS = new TextToSpeech(LessonActivity.this, new TextToSpeech.OnInitListener() {
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
        speakerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { speak(); }
        });
    }

    private void speak() {
        String text = txt_name.getText().toString();
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            editText.setText(matches.get(0).toString());
            if (matches.get(0).toString().equals(txt_name.getText().toString())){
                AlertDialog.Builder tips = new AlertDialog.Builder(LessonActivity.this);
                tips.setIcon(ContextCompat.getDrawable(LessonActivity.this,R.drawable.happy));
                tips.setTitle("Congratulations!");
                tips.setMessage("You have answer correctly!");
                tips.setPositiveButton("Got It",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {}
                });
                tips.show();
            } else {
                AlertDialog.Builder tips = new AlertDialog.Builder(LessonActivity.this);
                tips.setIcon(ContextCompat.getDrawable(LessonActivity.this,R.drawable.unhappy));
                tips.setTitle("Unfortunately!");
                tips.setMessage("You have answer wrongly, please try again!");
                tips.setPositiveButton("Got It",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {}
                });
                tips.show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
        {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo().getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION)
        {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }
}