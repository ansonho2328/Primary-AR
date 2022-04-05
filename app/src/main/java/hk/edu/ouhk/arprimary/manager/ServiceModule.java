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
        LessonFragment[] animalFragments = {
                new LessonFragment("Cow", "cow",
                        "(Noun)\nA large female farm animal kept to produce meat and milk"),
                new LessonFragment("Shark", "shark",
                        "(Noun)\nA large fish that has sharp teeth and a pointed fin on its back"),
                new LessonFragment("Wolf", "wolf",
                        "(Noun)\nA wild animal of the dog family")

        };

        LessonFragment[] fruitFragments = {
                new LessonFragment("Apple", "apple",
                        "(Noun)\nA round fruit with firm, white flesh and a green, red, or yellow skin"),
                new LessonFragment("Banana", "banana",
                        "(Noun)\nA long, curved fruit with a yellow skin and soft, sweet, white flesh inside"),
                new LessonFragment("Grape", "grape",
                        "(Noun)\nA small, round, purple or pale green fruit that you can eat or make into wine"),
                new LessonFragment("Lemon", "lemon",
                        "(Noun)\nAn oval fruit that has a thick, yellow skin and sour juice")
        };

        LessonFragment[] stationeryFragments = {
                new LessonFragment("Book", "book",
                        "(Noun)\nA written text that can be published in printed or electronic form"),
                new LessonFragment("Pencil", "pencil",
                        "(Noun)\nA long, thin object, usually made of wood, for writing or drawing, " +
                                "with a sharp black or coloured point at one end"),
                new LessonFragment("Eraser", "eraser",
                        "(Noun)\nA small piece of rubber used to remove the marks made by a pencil")
        };

        fragmentManager.registerTopicUnit("Animal", 1, animalFragments);
        fragmentManager.registerTopicUnit("Fruit", 1, fruitFragments);
        fragmentManager.registerTopicUnit("Stationery", 1, stationeryFragments);
        return fragmentManager;
    }


    @Provides
    @Singleton
    public QuizFragmentManager quizFragmentManager() {
        QuizFragmentManager quizFragmentManager = new QuizFragmentManager();

        //TODO add data here
        QuizFragment[] animalQuiz = {
                new QuizFragment("Cow", "cow"),
                new QuizFragment("Shark", "shark"),
                new QuizFragment("Wolf", "wolf")
        };

        QuizFragment[] fruitQuiz = {
                new QuizFragment("Apple", "apple"),
                new QuizFragment("Banana", "banana"),
                new QuizFragment("Grape", "grape")
        };

        QuizFragment[] stationeryQuiz = {
                new QuizFragment("Book", "book"),
                new QuizFragment("Pencil", "pencil"),
                new QuizFragment("Eraser", "eraser")
        };

        quizFragmentManager.registerTopicUnit("Animal", 1, animalQuiz);
        quizFragmentManager.registerTopicUnit("Fruit", 1, fruitQuiz);
        quizFragmentManager.registerTopicUnit("Stationery", 1, stationeryQuiz);
        return quizFragmentManager;
    }

}
