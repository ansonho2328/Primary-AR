package hk.edu.ouhk.arprimary.manager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hk.edu.ouhk.arprimary.model.LessonFragment;
import hk.edu.ouhk.arprimary.model.QuizFragment;

@Module
public class ServiceModule {

    @Provides
    @Singleton
    public SQLiteManager getSQLiteManager() {
        return new SQLiteManager();
    }


    @Provides
    @Singleton
    public LessonFragmentManager lessonFragmentManager() {
        LessonFragmentManager fragmentManager = new LessonFragmentManager();


        // TODO add data here
        LessonFragment[] fragments = {
                new LessonFragment("Apple", "apple",
                        " (Noun)\nA round fruit with firm, white flesh \nand a green, red, or yellow skin"),
                new LessonFragment("Banana", "banana",
                        " (Noun)\nA long, curved fruit with a yellow skin \nand soft, sweet, white flesh inside"),
                new LessonFragment("Grape", "grape",
                        " (Noun)\nA small, round, purple or pale green fruit \nthat you can eat or make into wine"),
                new LessonFragment("Lemon", "lemon",
                        " (Noun)\nAn oval fruit that has a thick,\n yellow skin and sour juice"),
//                            new LessonFragment("Book", "book",
//                                    " (Noun)\nA written text that can be \npublished in printed or electronic form")
        };

        fragmentManager.registerTopicUnit("Fruit", 1, fragments);
        return fragmentManager;
    }


    @Provides
    @Singleton
    public QuizFragmentManager quizFragmentManager() {
        QuizFragmentManager quizFragmentManager = new QuizFragmentManager();

        //TODO add data here
        QuizFragment[] fragments = {
                new QuizFragment("Apple", "apple"),
                new QuizFragment("Banana", "banana"),
                new QuizFragment("Grape", "grape")
        };

        quizFragmentManager.registerTopicUnit("Fruit", 1, fragments);
        return quizFragmentManager;
    }

}
