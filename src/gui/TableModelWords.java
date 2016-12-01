package gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 05.11.2016.
 */
class TableModelWords extends AbstractTableModel{
    private String[] columnNames = { "Английский", "Русский" };
    protected ArrayList<WordsPair> cells;
    TableModelWords(int capacity) {
        cells = new ArrayList<>(capacity);
    }
    TableModelWords() {
        cells = new ArrayList<>();
    }
    public void setCells(HashMap<String, String> entireSessionVocabulary) {
        cells.ensureCapacity(entireSessionVocabulary.size());
        for(Map.Entry<String, String> entry : entireSessionVocabulary.entrySet()) {
            WordsPair pair = new WordsPair(entry.getKey(), entry.getValue());
            cells.add(pair);
        }
    }
    @Override
    public int getRowCount() {
        return cells.size();
    }
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    @Override
    public int getColumnCount() {
        return 2;
    }
    @Override
    public String getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return cells.get(rowIndex).eng;
        }
        return cells.get(rowIndex).rus;
    }
    public void addRow(WordsPair pair) {
        cells.add(pair);
        fireTableDataChanged();
    }
    public WordsPair getRow(int index) {
        return cells.get(index);
    }
    public void removeRow(int index) {
        cells.remove(index);
        fireTableDataChanged();
    }
}
