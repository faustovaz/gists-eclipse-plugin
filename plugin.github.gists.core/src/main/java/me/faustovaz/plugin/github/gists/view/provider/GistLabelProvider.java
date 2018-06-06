package me.faustovaz.plugin.github.gists.view.provider;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class GistLabelProvider extends LabelProvider implements ITableLabelProvider{

    @Override
    public Image getColumnImage(Object obj, int column) {
        return null;
    }

    @Override
    public String getColumnText(Object obj, int column) {
        Gist gist = (Gist) obj;
        if(column == 0)
            return gist.getDescription();
        
        return null;
    }
    
}