package gui;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by stoat on 12/1/16.
 */
class TableModelWordsChecked extends TableModelWords {
    private HashMap<String, String> map = new HashMap<>();
    TableModelWordsChecked(int capacity) {
        super(capacity);
    }
    public boolean containsKey(WordsPair wordsPair) {
        return map.containsKey(wordsPair.eng);
    }
    public  boolean containsValue(WordsPair wordsPair) {
        return map.containsValue(wordsPair.rus);
    }
    @Override
    public void addRow(WordsPair wordsPair) {
        super.addRow(wordsPair);
        map.put(wordsPair.eng, wordsPair.rus);
    }
    public void removeRow(WordsPair wordsPair, int index) {
        removeRow(index);
        map.remove(wordsPair.eng);
    }
    public ArrayList<WordsPair> getCells() {
        return cells;
    }
}
