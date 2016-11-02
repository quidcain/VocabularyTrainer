package course.gui;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Created by user on 02.11.2016.
 */
public class JTextFieldLimit extends PlainDocument{
    private int limit;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }
    @Override
    public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
        if (str == null) return;

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
