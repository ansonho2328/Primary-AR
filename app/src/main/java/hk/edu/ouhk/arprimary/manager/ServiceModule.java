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
        LessonFragment[] fruitFragments = {
                new LessonFragment("Apple", "apple",
                        " (Noun)\nA round fruit with firm, white flesh \nand a green, red, or yellow skin"),
                new LessonFragment("Banana", "banana",
                        " (Noun)\nA long, curved fruit with a yellow skin \nand soft, sweet, white flesh inside"),
                new LessonFragment("Grape", "grape",
                        " (Noun)\nA small, round, purple or pale green fruit \nthat you can eat or make into wine"),
                new LessonFragment("Lemon", "lemon",
                        " (Noun)\nAn oval fruit that has a thick,\n yellow skin and sour juice")
        };

        LessonFragment[] stationaryFragments = {
                new LessonFragment("Book", "book",
                        " (Noun)\nA written text that can be \npublished in printed or electronic form"),
                new LessonFragment("Pencil", "pencil",
                        " (Noun)\nA long, thin object, usually made of wood, for writing or drawing, " +
                                "with a sharp black or coloured point at one end")
        };

        fragmentManager.registerTopicUnit("Fruit", 1, fruitFragments);
        fragmentManager.registerTopicUnit("Stationary", 1, stationaryFragments);
        return fragmentManager;
    }


    @Provides
    @Singleton
    public QuizFragmentManager quizFragmentManager() {
        QuizFragmentManager quizFragmentManager = new QuizFragmentManager();

        //TODO add data here
        QuizFragment[] fruitQuiz = {
                new QuizFragment("Apple", "apple"),
                new QuizFragment("Banana", "banana"),
                new QuizFragment("Grape", "grape")
        };

        QuizFragment[] stationaryQuiz = {
                new QuizFragment("Book", "book"),
                new QuizFragment("Pencil", "pencil")
        };

        quizFragmentManager.registerTopicUnit("Fruit", 1, fruitQuiz);
        quizFragmentManager.registerTopicUnit("Stationary", 1, stationaryQuiz);
        return quizFragmentManager;
    }

}
