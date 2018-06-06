package me.faustovaz.plugin.github.gists.view.provider;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class GistsErrorLabelProvider extends LabelProvider implements ITableLabelProvider{

    @Override
    public Image getColumnImage(Object obj, int column) {
        return null;
    }

    @Override
    public String getColumnText(Object obj, int column) {
        return getText(obj);
    }
    
}