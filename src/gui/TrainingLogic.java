package gui;

import java.util.*;

/**
 * Created by stoat on 11/18/16.
 */
class TrainingLogic {
    private ArrayList<WordsPair> arrayList;
    private static final int lastStage = 3;
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private int stage = 1;
    private void shuffle() {
        Collections.shuffle(arrayList);
        currentIndex = 0;
    }
    TrainingLogic(ArrayList<WordsPair> arrayList) {
        this.arrayList = new ArrayList<>(arrayList);
        shuffle();
    }
    public String getCurrentAskedWord() {
        if (stage % 2 == 1)
            return arrayList.get(currentIndex).eng;
        else
            return arrayList.get(currentIndex).rus;
    }
    public boolean isCorrectTranslation(String translation) {
        boolean result = false;
        if (stage % 2 == 1)
            result = translation.equals(arrayList.get(currentIndex).rus);
        else
            result = translation.equals(arrayList.get(currentIndex).eng);
        if(result)
            correctAnswers++;
        totalQuestions++;
        currentIndex++;
        return result;
    }
    private boolean isStageComplete() {
        if (currentIndex == arrayList.size()) {
            shuffle();
            stage++;
            return true;
        } else {
            return false;
        }
    }
    public boolean isIntermediateStage() {
        if (isStageComplete() && stage == lastStage)
            return true;
        else
            return false;
    }
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    public int getTotalQuestions() {
        return totalQuestions;
    }
    public String[] getOptions() {
        String[] result = new String[4];
        int correctOption = new Random().nextInt(4) + 1;
        HashSet<String> setOptions = new HashSet<>();
        if (stage % 2 == 1) {
            setOptions.add(arrayList.get(correctOption).rus);
            for (int i = 0; i < 4; ++i)
                if (i == correctOption)
                    result[i] = arrayList.get(currentIndex).rus;
                else {
                    int confusingOption;
                    do {
                        confusingOption = new Random().nextInt(5);
                    } while (setOptions.contains(arrayList.get(confusingOption).rus));
                    setOptions.add(arrayList.get(confusingOption).rus);
                    result[i] = arrayList.get(confusingOption).rus;
                }
        }
        else {
            setOptions.add(arrayList.get(correctOption).eng);
            for (int i = 0; i < 4; ++i)
                if (i == correctOption)
                    result[i] = arrayList.get(currentIndex).eng;
                else {
                    int confusingOption;
                    do {
                        confusingOption = new Random().nextInt(5);
                    } while (setOptions.contains(arrayList.get(confusingOption).eng));
                    setOptions.add(arrayList.get(confusingOption).eng);
                    result[i] = arrayList.get(confusingOption).eng;
                }
        }
        return result;
    }
}
