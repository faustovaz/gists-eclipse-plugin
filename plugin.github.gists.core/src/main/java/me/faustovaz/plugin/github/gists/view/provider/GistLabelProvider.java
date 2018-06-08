package me.faustovaz.plugin.github.gists.view.provider;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
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
        if (obj instanceof Gist) {
            Gist gist = (Gist) obj;
            
            if(column == 0)
                return gist.getDescription();
            if(column == 1)
                return gist.getHtmlUrl();
            if(column == 2)
                return gist.getCreatedAt().toString();
            if(column == 3)
                return gist.getUpdatedAt().toString();
            
        } else {
            GistFile gistFile = (GistFile) obj;
            
            if(column == 0)
                return gistFile.getFilename();
            if(column == 1)
                return gistFile.getRawUrl();
            
        }
        
        return null;
    }
    
}