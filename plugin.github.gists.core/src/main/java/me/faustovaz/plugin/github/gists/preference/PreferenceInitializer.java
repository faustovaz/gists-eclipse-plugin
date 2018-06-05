package me.faustovaz.plugin.github.gists.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import me.faustovaz.plugin.github.gists.core.GistsPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = GistsPlugin.getDefault().getPreferenceStore();
        store.setDefault(PreferenceConstants.P_GITHUB_LOGIN, "");
        store.setDefault(PreferenceConstants.P_GITHUB_PASSWD, "");
    }

}
