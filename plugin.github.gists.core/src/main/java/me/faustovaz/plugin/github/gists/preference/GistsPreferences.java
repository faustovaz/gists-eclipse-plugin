package me.faustovaz.plugin.github.gists.preference;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import me.faustovaz.plugin.github.gists.core.GistsPlugin;
import me.faustovaz.plugin.github.gists.custom.MaskedStringFieldEditor;

public class GistsPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

    public static final String id = "me.faustovaz.plugin.github.gists.preference.GistsPreferences";
    private StringFieldEditor login = null;
    private StringFieldEditor password = null;
    
    public GistsPreferences() {
        super(GRID);
        setPreferenceStore(GistsPlugin.getDefault().getPreferenceStore());
        setTitle("Github Credentials");
        setDescription("Github Credentials for accessing Gists");
    }

    public void createFieldEditors() {
        this.login = new StringFieldEditor(
                    PreferenceConstants.P_GITHUB_LOGIN, "Your Github &Login", getFieldEditorParent());
        addField(this.login);
        
        this.password = new MaskedStringFieldEditor(
                    PreferenceConstants.P_GITHUB_PASSWD, "Your Github &Password", getFieldEditorParent());
        addField(this.password);
    }
    
    @Override
    protected void performApply() {
        refreshCredentials();
        super.performApply();
    }
    
    @Override
    public boolean performOk() {
        refreshCredentials();
        return super.performOk();
    }

    public void refreshCredentials() {
        this.login.store();
        this.password.store();
        
        GistsPlugin.getDefault().newGitHubClient();
    }
    
    public void init(IWorkbench workbench) { }

}