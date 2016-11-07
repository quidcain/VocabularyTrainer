package course.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 05.11.2016.
 */
public class WordsTableModel extends AbstractTableModel{
    private String[] columnNames = { "Английский", "Русский" };
    private ArrayList<WordsPair> entireSessionVocabularity;
    WordsTableModel(HashMap<String, String> entireSessionVocabularity) {
        super();
        this.entireSessionVocabularity =  new ArrayList<>();
        for( Map.Entry<String, String> entry : entireSessionVocabularity.entrySet() ) {
            WordsPair pair  = new WordsPair(entry.getKey(), entry.getValue());
            this.entireSessionVocabularity.add(pair);
        }
    }
    @Override
    public int getRowCount() {
        return entireSessionVocabularity.size();
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
            return entireSessionVocabularity.get(rowIndex).eng;
        }
        return entireSessionVocabularity.get(rowIndex).rus;
    }
    public void addRow(WordsPair pair) {
        entireSessionVocabularity.add(pair);
        fireTableDataChanged();
    }
}
