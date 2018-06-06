package me.faustovaz.plugin.github.gists.view.provider;

import java.util.ArrayList;
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
        Gist gist = (Gist) obj;
        List<GistFile> files = new ArrayList<>();
        gist.getFiles().forEach((key, gistFile) -> files.add(gistFile));
        return files.toArray();
    }

    @Override
    public Object getParent(Object arg0) {
        return null;
    }

    @Override
    public boolean hasChildren(Object obj) {
        if (obj instanceof Gist) {
            Map<String, GistFile> files = ((Gist) obj).getFiles();
            return !files.isEmpty();
        }
        return false;
    }
    
}