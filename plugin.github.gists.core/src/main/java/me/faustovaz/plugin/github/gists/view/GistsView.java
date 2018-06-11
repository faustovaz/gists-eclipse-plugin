package me.faustovaz.plugin.github.gists.view;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import me.faustovaz.plugin.github.gists.core.GistsPlugin;
import me.faustovaz.plugin.github.gists.view.provider.GistContentProvider;
import me.faustovaz.plugin.github.gists.view.provider.GistLabelProvider;
import me.faustovaz.plugin.github.gists.view.provider.GistsErrorLabelProvider;

public class GistsView extends ViewPart {

    public static final String ID = "me.faustovaz.plugin.github.gists.view.GistsView";

    @Inject
    IWorkbench workbench;

    private ColumnViewer viewer;
    private Action action1;
    private Action action2;
    private Action doubleClickAction;

    @Override
    public void createPartControl(Composite parent) {
        buildTable(parent);
        workbench.getHelpSystem().setHelp(viewer.getControl(), "plugin.github.gists.core.viewer");
        getSite().setSelectionProvider(viewer);
        hookCtrlCAction();
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }

    public Viewer buildTable(Composite parent) {
        if(GistsPlugin.isGitHubCredentialsSaved()) {
            viewer = buildGistsTable(parent);
        }
        else {
            viewer = buildSingleLineTable(
                        parent, 
                        "Please inform your GitHub Credentials in the Gists Preferences Page",
                        false);
        }
        return viewer;
    }
    
    public TableViewer buildSingleLineTable(Composite parent, String message, boolean error) {
        TableViewer viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        viewer.setContentProvider(ArrayContentProvider.getInstance());
        viewer.setLabelProvider(new GistsErrorLabelProvider());
        if (error)
            viewer.setInput(new String[] { "Error: " + message });
        else
            viewer.setInput(new String[] { message });
        return viewer;
    }
    
    public ColumnViewer buildGistsTable(Composite parent) {
        ColumnViewer columnViewer = null;
        TreeViewer viewer = null;
        
        GistService gistService = new GistService(GistsPlugin.getGitHubClient());
        UserService userService = new UserService(GistsPlugin.getGitHubClient());
        
        try {
            List<Gist> gists = gistService.getGists(userService.getUser().getLogin());
            viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
            
            TreeViewerColumn descriptionColumn = new TreeViewerColumn(viewer, SWT.NONE);
            descriptionColumn.getColumn().setWidth(300);
            descriptionColumn.getColumn().setText("Description");
                        
            TreeViewerColumn linkColumn = new TreeViewerColumn(viewer, SWT.NONE);
            linkColumn.getColumn().setWidth(300);
            linkColumn.getColumn().setText("Link");

            TreeViewerColumn createdAtColumn = new TreeViewerColumn(viewer, SWT.NONE);
            createdAtColumn.getColumn().setWidth(100);
            createdAtColumn.getColumn().setText("Created At");
            
            TreeViewerColumn updatedAtColumn = new TreeViewerColumn(viewer, SWT.NONE);
            updatedAtColumn.getColumn().setWidth(100);
            updatedAtColumn.getColumn().setText("Updated At");

            viewer.setContentProvider(new GistContentProvider());
            viewer.setLabelProvider(new GistLabelProvider());
            viewer.setInput(gists);
            viewer.getTree().setHeaderVisible(true);
            viewer.getTree().setLinesVisible(true);
            columnViewer = viewer;
        } catch (IOException e) {
            columnViewer = buildSingleLineTable(parent, e.getMessage(), true);
        }        
        
        return columnViewer;
    }
    
    private void hookCtrlCAction() {
        viewer.getControl().addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent evt) { }
            
            @Override
            public void keyPressed(KeyEvent evt) {
                if(((evt.stateMask & SWT.CTRL) != 0) && (evt.keyCode == 99)) { //If Ctrl C;
                    Tree tree = (Tree) evt.getSource();
                    TreeItem[] selection = tree.getSelection();
                    if(selection.length > 0) {
                        Gist gist = (Gist) selection[0].getData();
                        copyToClipboard(evt.display, gist);
                    }
                }
            }
        });
    }

    private void copyToClipboard(Display display, Gist gist) {
        Clipboard clipboard = new Clipboard(display);
        clipboard.setContents(new String[] {gist.getHtmlUrl()}, 
                              new Transfer[] {TextTransfer.getInstance()});
        clipboard.dispose();
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                GistsView.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(viewer.getControl());
        viewer.getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, viewer);
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        manager.add(action2);
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(action2);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(action2);
    }
    
    private void makeActions() {
        action1 = new Action() {
            public void run() {
                showMessage("Action 1 executed");
            }
        };
        action1.setText("Action 1");
        action1.setToolTipText("Action 1 tooltip");
        action1.setImageDescriptor(
                PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

        action2 = new Action() {
            public void run() {
                showMessage("Action 2 executed");
            }
        };
        action2.setText("Action 2");
        action2.setToolTipText("Action 2 tooltip");
        action2.setImageDescriptor(workbench.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
        doubleClickAction = new Action() {
            public void run() {
                IStructuredSelection selection = viewer.getStructuredSelection();
                Object obj = selection.getFirstElement();
                showMessage("Double-click detected on " + obj.toString());
            }
        };
    }

    private void hookDoubleClickAction() {
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                doubleClickAction.run();
            }
        });
    }

    private void showMessage(String message) {
        MessageDialog.openInformation(viewer.getControl().getShell(), "Gists View", message);
    }

    @Override
    public void setFocus() {
        viewer.getControl().setFocus();
    }
}
