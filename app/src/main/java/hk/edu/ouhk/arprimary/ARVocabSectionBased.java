package hk.edu.ouhk.arprimary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformationSystem;

import java.util.Iterator;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;

import hk.edu.ouhk.arprimary.viewmodel.armodel.Sectionable;

public abstract class ARVocabSectionBased<E extends Sectionable> extends AppCompatActivity {

    private static final double MIN_OPENGL_VERSION = 3.0;
    private static final String TAG = ARVocabSectionBased.class.getSimpleName();


    protected View.OnClickListener onIntentLaunch;

    protected ArFragment arFragment;

    private Iterator<E> iterator;
    protected E current = null;

    private final MutableLiveData<E> liveData = new MutableLiveData<>();


    private ActivityResultLauncher<Intent> intentLauncher;
    protected boolean sessionStarted;


    protected abstract void onCreateContent(Bundle bundle);

    // overridable
    public Intent createIntent() {
        Intent speachIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speachIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speachIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Pronounce the word");
        return speachIntent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            Toast.makeText(this, "Your device is not support AR", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        intentLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), this::onIntentResult);

        onIntentLaunch = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentLauncher.launch(createIntent());
            }
        };

        this.onCreateContent(savedInstanceState);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.lesson_arfragment);

        if (arFragment == null) {
            Toast.makeText(this, "ArFragment is null", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        liveData.observe(this, this::onVocabularyUpdate);

        startLoading();
        TreeSet<E> sections = new TreeSet<>(); // for order
        prepareArModel(sections).whenComplete((v, ex) -> {

            if (ex != null) {
                Toast.makeText(this, "Render Model Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                ex.printStackTrace();
                return;
            }

            stopLoading();

            iterator = sections.iterator();

            Toast.makeText(this, "All models are rendered, you can now tap the screen", Toast.LENGTH_LONG).show();

            arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                if (sessionStarted) return;

                startLoading();

                Anchor anchor = hitResult.createAnchor();
                AnchorNode anchorNode = new AnchorNode(anchor);
                anchorNode.setParent(arFragment.getArSceneView().getScene());

                placeSceneModel(anchorNode, arFragment.getTransformationSystem());

                stopLoading();
                Toast.makeText(this, "Screen Tapped, lesson is now starting...", Toast.LENGTH_LONG).show();
                startLesson();
            });
        });

    }


    protected void startLesson() {
        if (sessionStarted) return;
        if (!iterator.hasNext()) {
            Toast.makeText(this, "The Section IS Empty!", Toast.LENGTH_LONG).show();
            return;
        }
        doNext();
        sessionStarted = true;
    }


    protected abstract void placeSceneModel(AnchorNode anchorNode, TransformationSystem transformationSystem);

    protected abstract CompletableFuture<Void> prepareArModel(TreeSet<E> treeSet);

    // overridable, default is tts
    public String resultToAnswer(ActivityResult result) {
        Intent data = result.getData();
        return data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
    }

    // overridable
    protected boolean isPassResult(String answer) {
        return current != null && current.getAnswer().equalsIgnoreCase(answer);
    }

    protected void onAnswerResult(String answer) {
        boolean passed = isPassResult(answer);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        boolean shouldNext = buildDialogWithResult(builder, passed);
        if (shouldNext) {
            if (iterator.hasNext()) {
                builder.setPositiveButton("Next", (arg0, arg1) -> doNext());
            } else {
                builder.setPositiveButton("Finish", (arg0, arg1) -> {
                    beforeFinish(getIntent());
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                });
            }
        } else {
            builder.setPositiveButton("Okay", (arg0, arg1) -> {
            });
        }
        builder.show();
    }

    protected void onIntentResult(ActivityResult result) {
        if (result.getResultCode() != RESULT_OK || result.getData() == null) {
            Toast.makeText(this, "data is null or result is not ok", Toast.LENGTH_LONG).show();
            return;
        }
        String answer = resultToAnswer(result);
        onAnswerResult(answer);
    }


    protected void doNext() {
        E next = iterator.next();
        current = next;
        liveData.postValue(next);
    }

    protected abstract boolean buildDialogWithResult(AlertDialog.Builder builder, boolean result);

    protected abstract void beforeFinish(Intent intent);

    protected abstract void onVocabularyUpdate(E updated);

    // optional implementation
    protected void startLoading() {
    }

    protected void stopLoading() {
    }


    protected int getResourcesFromModel(String id) {
        return getApplicationContext().getResources()
                .getIdentifier(id, "raw", getPackageName());
    }

    protected boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            log("Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo().getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            log("Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        return true;
    }


    protected void log(String msg) {
        Log.e(TAG, msg);
    }
}
