package hk.edu.ouhk.arprimary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.ar.sceneform.ux.TransformationSystem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import hk.edu.ouhk.arprimary.model.Lesson;
import hk.edu.ouhk.arprimary.model.Sentence;
import hk.edu.ouhk.arprimary.model.SentenceFragment;
import hk.edu.ouhk.arprimary.viewmodel.armodel.sentence.SentenceSection;

public class SentenceActivity extends ARVocabSectionBased<SentenceSection> {

    Sentence sentence;

    ImageButton tipsBtn, leave;

    TextView answerDisplay;

    Button confirmAnswer,reset_button;
    TransformableNode wordNode;

    @Override
    protected void onCreateContent(Bundle bundle) {
        setContentView(R.layout.activity_sentence);

        sentence = (Sentence) getIntent().getSerializableExtra("sentence");

        tipsBtn = findViewById(R.id.tipsBtn);
        leave = findViewById(R.id.leave);

        answerDisplay = findViewById(R.id.sentence);
        confirmAnswer = findViewById(R.id.confirm_button);
        reset_button = findViewById(R.id.reset_button);


        // Leave button handling for leaving the game
        leave.setOnClickListener(view -> {
            AlertDialog.Builder leave = new AlertDialog.Builder(SentenceActivity.this);
            leave.setIcon(ContextCompat.getDrawable(SentenceActivity.this, R.drawable.confirm_leave));
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
            AlertDialog.Builder tips = new AlertDialog.Builder(SentenceActivity.this);
            tips.setIcon(ContextCompat.getDrawable(SentenceActivity.this, R.drawable.tips));
            tips.setTitle("Sentence-Making Game Tips");
            tips.setMessage("Present Tense - Subject(S) + Verb(V)/be e.g I am happy. which 'I' is subject and 'am' is verb.\n\n" +
                    "1. Please select the word by order to make sentence in present tense.\n" +
                    "2. Please check the grammar for the sentence.");
            tips.setPositiveButton("Got It", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                }
            });
            tips.show();
        });

        confirmAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt = (String) answerDisplay.getText();
                onAnswerResult(txt);
            }
        });
    }


    private final List<String> answering = new ArrayList<>();

    @Override
    protected void placeSceneModel(AnchorNode anchorNode, TransformationSystem transformationSystem) {
        for (ViewRenderable viewRenderable : viewRenderables) {
            wordNode = new TransformableNode(transformationSystem);
            float place = new Random().nextFloat(); // TODO random spread plcaement
            wordNode.setLocalPosition(new Vector3(0f, place, 0));
            wordNode.getScaleController().setMaxScale(1f);
            wordNode.getScaleController().setMinScale(0.5f);
            wordNode.setParent(anchorNode);
            wordNode.setRenderable(viewRenderable);
            wordNode.select();
            TextView tv = (TextView) viewRenderable.getView();
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String word = (String) tv.getText();
                    if (word.isEmpty()) return;
                    if (answering.contains(word)) {
                        answering.remove(word);
                    } else {
                        answering.add(word);
                    }
                    // after edit, update the display
                    String answer = String.join(" ", answering);
                    answerDisplay.setText(answer);
                }
            });
        }

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerDisplay.setText("");
                answering.clear();
            }
        });

    }



    private ViewRenderable[] viewRenderables;

    @Override
    protected CompletableFuture<Void> prepareArModel(TreeSet<SentenceSection> treeSet) {
        SentenceFragment[] fragments = sentence.getFragments();
        int maxWordsSize = Arrays.stream(fragments).mapToInt(s -> s.getFragments().size()).max().orElse(0);
        viewRenderables = new ViewRenderable[maxWordsSize];

        CompletableFuture[] futures = new CompletableFuture[maxWordsSize];
        for (int i = 0; i < maxWordsSize; i++) {
            int index = i;
            futures[i] = ViewRenderable.builder()
                    .setView(this, R.layout.name_models)
                    .build()
                    .thenAccept(render -> viewRenderables[index] = render);
        }

        for (int i = 0; i < fragments.length; i++) {
            SentenceFragment fragment = fragments[i];
            SentenceSection section = new SentenceSection(i, fragment.getFragments(),
                    fragment.getCorrectAnswer());
            treeSet.add(section);
        }

        return CompletableFuture.allOf(futures);
    }

    @Override
    protected boolean buildDialogWithResult(AlertDialog.Builder answer, boolean result) {
        if (result) {
            answer.setIcon(ContextCompat.getDrawable(SentenceActivity.this, R.drawable.happy));
            answer.setTitle("Congratulations!");
            answer.setMessage("You answered right!");
        } else {
            answer.setIcon(ContextCompat.getDrawable(SentenceActivity.this, R.drawable.unhappy));
            answer.setTitle("Unfortunately!");
            answer.setMessage("You answered wrong, please try again!");
        }
        return result;
    }

    @Override
    protected void beforeFinish(Intent intent) {
        //clear all
        wordNode.setRenderable(null);
        setResult(RESULT_OK);
    }

    @Override
    protected void onVocabularyUpdate(SentenceSection updated) {
        // clear all text first
        for (ViewRenderable viewRenderable : viewRenderables) {
            TextView tv = ((TextView)viewRenderable.getView());
            tv.setText("");
            tv.setVisibility(View.INVISIBLE);
            answerDisplay.setText("");
            answering.clear();
        }
        //assign
        for (int i = 0; i < updated.getFragments().size(); i++) {
            String fragment = updated.getFragments().get(i);
            TextView tv = (TextView)viewRenderables[i].getView();
            tv.setText(fragment);
            tv.setVisibility(View.VISIBLE);
        }
    }
}