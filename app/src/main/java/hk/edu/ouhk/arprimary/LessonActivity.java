package hk.edu.ouhk.arprimary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.Locale;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

import hk.edu.ouhk.arprimary.model.Lesson;
import hk.edu.ouhk.arprimary.model.LessonFragment;
import hk.edu.ouhk.arprimary.viewmodel.armodel.practice.UnitSection;

public class LessonActivity extends ARVocabSectionBased<UnitSection> {

    Lesson lesson;

    ImageButton tipsBtn, speakerBtn, microphone, leave;
    TextView txt_name, txt_defin,wordDefin;

    ViewRenderable name_models, speaker, definition;
    TransformableNode modelNode, speakerNode, defNode, nameNode;

    private TextToSpeech mTTS;

    @Override
    protected void onCreateContent(Bundle bundle) {
        setContentView(R.layout.activity_lesson);

        lesson = (Lesson) getIntent().getSerializableExtra("lesson");

        tipsBtn = findViewById(R.id.tipsBtn);
        microphone = findViewById(R.id.microphone);
        leave = findViewById(R.id.leave);
        wordDefin = findViewById(R.id.wordDefin);

        wordDefin.setVisibility(View.INVISIBLE);
        findViewById(R.id.microphone).setEnabled(false);

        // Leave button handling for leaving the game
        leave.setOnClickListener(view -> {
            AlertDialog.Builder leave = new AlertDialog.Builder(LessonActivity.this);
            leave.setIcon(ContextCompat.getDrawable(LessonActivity.this, R.drawable.confirm_leave));
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
            AlertDialog.Builder tips = new AlertDialog.Builder(LessonActivity.this);
            tips.setIcon(ContextCompat.getDrawable(LessonActivity.this, R.drawable.tips));
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
        microphone.setOnClickListener(onIntentLaunch);
    }


    @Override
    protected void placeSceneModel(AnchorNode anchorNode, TransformationSystem transformationSystem) {
        // set model node
        modelNode = new TransformableNode(transformationSystem);
        modelNode.getScaleController().setMaxScale(0.16f);
        modelNode.getScaleController().setMinScale(0.1f);
        modelNode.setParent(anchorNode);
        modelNode.select();

        // set name node
        nameNode = new TransformableNode(transformationSystem);
        nameNode.setLocalPosition(new Vector3(0f, modelNode.getLocalPosition().y + 0.5f, 0));
        nameNode.getScaleController().setMaxScale(1f);
        nameNode.getScaleController().setMinScale(0.5f);
        nameNode.setParent(anchorNode);
        nameNode.setRenderable(name_models);
        nameNode.select();
        txt_name = (TextView) name_models.getView();

//        // set def node
//        defNode = new TransformableNode(transformationSystem);
//        defNode.setLocalPosition(new Vector3(0f, modelNode.getLocalPosition().y + 0.43f, 0));
//        defNode.getScaleController().setMaxScale(1f);
//        defNode.getScaleController().setMinScale(0.5f);
//        defNode.setParent(anchorNode);
//        defNode.setRenderable(definition);
//        defNode.select();
//        txt_defin = (TextView) definition.getView();

        // set speaker node
        speakerNode = new TransformableNode(transformationSystem);
        speakerNode.setLocalPosition(new Vector3(0.3f, nameNode.getLocalPosition().y + 0.025f, 0));
        speakerNode.getScaleController().setMaxScale(1f);
        speakerNode.getScaleController().setMinScale(0.5f);
        speakerNode.setParent(anchorNode);
        speakerNode.setRenderable(speaker);
        speakerNode.select();
        setupSpeaker();

        wordDefin.setVisibility(View.VISIBLE);
        findViewById(R.id.microphone).setEnabled(true);
    }

    @Override
    protected CompletableFuture<Void> prepareArModel(TreeSet<UnitSection> sections) {
        LessonFragment[] fragments = lesson.getFragments();
        int length = fragments.length;
        CompletableFuture<Void>[] futures = new CompletableFuture[length + 3];
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

        return CompletableFuture.allOf(futures);
    }

    @Override
    protected boolean buildDialogWithResult(AlertDialog.Builder answer, boolean result) {
        if (result) {
            answer.setIcon(ContextCompat.getDrawable(LessonActivity.this, R.drawable.happy));
            answer.setTitle("Congratulations!");
            answer.setMessage("You answered right!");
        } else {
            answer.setIcon(ContextCompat.getDrawable(LessonActivity.this, R.drawable.unhappy));
            answer.setTitle("Unfortunately!");
            answer.setMessage("You answered wrong, please try again!");
        }
        return result;
    }

    @Override
    protected void beforeFinish(Intent intent) {
        // clear all
        nameNode.setRenderable(null);
        modelNode.setRenderable(null);
        speakerNode.setRenderable(null);
        defNode.setRenderable(null);
    }

    @Override
    protected void onVocabularyUpdate(UnitSection updated) {
        txt_name.setText(updated.getVocab());
        wordDefin.setText(updated.getDefinition());
        modelNode.setRenderable(updated.getModelRenderable());
        speak(); // speak on each model updated
    }


    // Adding speaker button for model
    // TODO: i can't find the speaker....
    private void setupSpeaker() {
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
        speakerBtn.setOnClickListener(v -> speak());
    }

    private void speak() {
        String text = txt_name.getText().toString();
        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }


    // TODO: make better loading screen on AR screen
    protected void startLoading() {
        findViewById(R.id.view_loading).setVisibility(View.VISIBLE);
    }

    // TODO: make better loading screen on AR screen
    protected void stopLoading() {
        findViewById(R.id.view_loading).setVisibility(View.GONE);
    }


}