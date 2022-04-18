package hk.edu.ouhk.arprimary.manager;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = ServiceModule.class)
@Singleton
public interface ApplicationComponent {

    LessonFragmentManager lessonFragmentManager();

    SentenceFragmentManager sentenceFragmentManager();

    QuizFragmentManager quizFragmentManager();

}
