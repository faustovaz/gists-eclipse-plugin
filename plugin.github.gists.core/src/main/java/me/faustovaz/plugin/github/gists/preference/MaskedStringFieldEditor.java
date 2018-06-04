package me.faustovaz.plugin.github.gists.preference;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class MaskedStringFieldEditor extends StringFieldEditor {
    
    public MaskedStringFieldEditor(String name, String label, Composite parent) {
        super(name, label, parent);
    }

    public MaskedStringFieldEditor(String name, String label, int width, Composite parent) {
        super(name, label, width, parent);
    }
    
    public MaskedStringFieldEditor(String name, String label, int width, int strategy, Composite parent) {
        super(name, label, width, strategy, parent);
    }

    @Override
    public Text getTextControl(Composite parent) {
        Text textControl = super.getTextControl(parent);
        textControl.setEchoChar('*');
        return textControl;
    }

    //TODO Add encription to store github passwords
}
