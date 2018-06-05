package me.faustovaz.plugin.github.gists.preference;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import me.faustovaz.plugin.github.gists.core.Activator;
import me.faustovaz.plugin.github.gists.custom.MaskedStringFieldEditor;

public class GistsPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String id = "me.faustovaz.plugin.github.gists.preference.GistsPreferences";
    
    public GistsPreferences() {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setTitle("Github Credentials");
        setDescription("Github Credentials for accessing Gists");
    }

    public void createFieldEditors() {
        addField(new StringFieldEditor(
                    PreferenceConstants.P_GITHUB_LOGIN, "Your Github &Login", getFieldEditorParent()));
        addField(new MaskedStringFieldEditor(
                    PreferenceConstants.P_GITHUB_PASSWD, "Your Github &Password", getFieldEditorParent()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }

}