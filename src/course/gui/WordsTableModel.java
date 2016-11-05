package course.gui;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Created by user on 05.11.2016.
 */
public class WordsTableModel extends AbstractTableModel{
    private String[] columnNames = { "Английский", "Русский" };
    ArrayList<WordsPair> entireSessionVocabularity;
    WordsTableModel(ArrayList<WordsPair> entireSessionVocabularity) {
        super();
        this.entireSessionVocabularity = entireSessionVocabularity;
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
}
