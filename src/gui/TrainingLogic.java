package gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by stoat on 11/18/16.
 */
class TrainingLogic {
    private ArrayList<WordsPair> arrayList;
    private HashMap<String, String> hashMap;
    private int currentIndex, correctAnswers, totalQuestions;
    public TrainingLogic(ArrayList<WordsPair> arrayList, HashMap<String, String> hashMap) {
        this.arrayList = new ArrayList<>(arrayList);
        this.hashMap = hashMap;
        currentIndex = 0;
        correctAnswers = 0;
        totalQuestions = 0;
    }
    public void shuffle() {
        Collections.shuffle(arrayList);
    }
    public WordsPair getCurrentWord() {
        return arrayList.get(currentIndex);
    }
    public boolean isCurrentLast() {
        return currentIndex == arrayList.size() - 1;
    }
    public void incrementCurrentIndex() {
        currentIndex++;
    }
    public void incrementCorrectAnswers() {
        correctAnswers++;
    }
    public void incrementTotalQuestions() {
        totalQuestions++;
    }
    public int getCorrectAnswers() {
        return correctAnswers;
    }
    public int getTotalQuestions() {
        return totalQuestions;
    }
    public String getRusTranslation() {
        return hashMap.get(arrayList.get(currentIndex).eng);
    }
}
