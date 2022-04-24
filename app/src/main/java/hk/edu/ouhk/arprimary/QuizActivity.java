package hk.edu.ouhk.arprimary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.core.content.ContextCompat;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

import hk.edu.ouhk.arprimary.firestore.User;
import hk.edu.ouhk.arprimary.model.Quiz;
import hk.edu.ouhk.arprimary.model.QuizFragment;
import hk.edu.ouhk.arprimary.viewmodel.armodel.quiz.QuizSection;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class QuizActivity extends ARVocabSectionBased<QuizSection> {


    Quiz quiz;

    ImageButton tipsBtn, microphone, leave;

    TransformableNode modelNode;

    int score = 0;

    @Override
    protected void onCreateContent(Bundle bundle) {

        setContentView(R.layout.activity_quiz);
        quiz = (Quiz) getIntent().getSerializableExtra("quiz");

        tipsBtn = findViewById(R.id.tipsBtn);
        microphone = findViewById(R.id.microphone);
        leave = findViewById(R.id.leave);

        findViewById(R.id.microphone).setEnabled(false);

        // Leave button handling for leaving the game
        leave.setOnClickListener(view -> {
            AlertDialog.Builder leave = new AlertDialog.Builder(this);
            leave.setIcon(ContextCompat.getDrawable(this, R.drawable.confirm_leave));
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
            AlertDialog.Builder tips = new AlertDialog.Builder(this);
            tips.setIcon(ContextCompat.getDrawable(this, R.drawable.tips));
            tips.setTitle("Quiz Tips");
            tips.setMessage("1. Please try to pronounce the word which you have learnt \n\n" +
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

        findViewById(R.id.microphone).setEnabled(true);
    }

    @Override
    protected CompletableFuture<Void> prepareArModel(TreeSet<QuizSection> treeSet) {
        QuizFragment[] fragments = quiz.getQuizFragments();
        int length = fragments.length;
        CompletableFuture<Void>[] futures = new CompletableFuture[length];
        for (int i = 0; i < length; i++) {
            QuizFragment fragment = fragments[i];
            final int order = i;
            futures[i] = ModelRenderable.builder()
                    .setSource(this, getResourcesFromModel(fragment.getModelResourceName()))
                    .build()
                    .thenAccept(renderable -> {
                        QuizSection section = new QuizSection(order, renderable, fragment.getVocab());
                        treeSet.add(section);
                    });
        }
        return CompletableFuture.allOf(futures);
    }

    @Override
    protected boolean buildDialogWithResult(AlertDialog.Builder builder, boolean result) {

        // TODO build alert dialog without button

        if (result && current != null) {
            score += current.getScore();
            builder.setIcon(ContextCompat.getDrawable(QuizActivity.this, R.drawable.happy));
            builder.setTitle("Congratulations!");
            builder.setMessage("You answered right and get 10 marks!");
            //insert to firebase data
            ArrayList<User> user = new ArrayList<>();
        } else {
            builder.setIcon(ContextCompat.getDrawable(QuizActivity.this, R.drawable.unhappy));
            builder.setTitle("Unfortunately!");
            builder.setMessage("You answered wrong and get no marks!");
        }
        return true; // no matter correct or incorrect, still need to move into next section
    }

    @Override
    protected void beforeFinish(Intent intent) {
        intent.putExtra("scores", score);
        setResult(RESULT_OK,intent.putExtra("scores", score));
    }

    @Override
    protected void onVocabularyUpdate(QuizSection updated) {
        modelNode.setRenderable(updated.getModelRenderable());
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