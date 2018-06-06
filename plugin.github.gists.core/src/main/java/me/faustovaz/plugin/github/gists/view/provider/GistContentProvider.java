package me.faustovaz.plugin.github.gists.view.provider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

public class GistContentProvider implements IStructuredContentProvider {
    
    @Override
    public Object[] getElements(Object inputElement) {
        return ((List<?>) inputElement).toArray();
    }
    
}