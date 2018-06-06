package me.faustovaz.plugin.github.gists.view.provider;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.GistFile;
import org.eclipse.jface.viewers.ITreeContentProvider;

public class GistContentProvider implements ITreeContentProvider {
    
    @Override
    public Object[] getElements(Object inputElement) {
        return ((List<?>) inputElement).toArray();
    }

    @Override
    public Object[] getChildren(Object obj) {
        Gist g = new Gist();
        g.setDescription("UHEEET");
        g.setFiles(new HashMap<>());
        return Arrays.asList(g).toArray();
    }

    @Override
    public Object getParent(Object arg0) {
        return null;
    }

    @Override
    public boolean hasChildren(Object obj) {
        Map<String, GistFile> files = ((Gist) obj).getFiles();
        return files.size() > 1;
    }
    
}