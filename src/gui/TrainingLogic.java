package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by stoat on 11/18/16.
 */
class TrainingLogic {
    private ArrayList<WordsPair> arrayList;
    private HashMap<String, String> hashMap;
    private static final int lastStage = 3;
    private int currentIndex = 0;
    private int correctAnswers = 0;
    private int totalQuestions = 0;
    private int stage = 1;
    private void shuffle() {
        Collections.shuffle(arrayList);
        currentIndex = 0;
    }
    public TrainingLogic(ArrayList<WordsPair> arrayList, HashMap<String, String> hashMap) {
        this.arrayList = new ArrayList<>(arrayList);
        this.hashMap = hashMap;
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
        if (stage % 2 == 1) {
            result = translation.equals(hashMap.get(arrayList.get(currentIndex).eng));
        } else {
            for (Map.Entry<String, String> entry : hashMap.entrySet()) {
                if(translation.equals(entry.getKey())) {
                    result = true;
                    break;
                }
            }
        }
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
}
