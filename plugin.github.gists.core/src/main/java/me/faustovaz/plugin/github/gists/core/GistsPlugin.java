package me.faustovaz.plugin.github.gists.core;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import me.faustovaz.plugin.github.gists.preference.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle
 */
public class GistsPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "plugin.github.gists.core";

    // The shared instance
    private static GistsPlugin plugin;

    boolean started;
    
    private static GitHubClient gitHubClient;

    /**
     * The constructor
     */
    public GistsPlugin() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
     * BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        started = true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        started = false;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static GistsPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative
     * path
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
    
    public static String getStoredValue(String key) {
        return getDefault().getPreferenceStore().getString(key);
    }
    
    public static GitHubClient getGitHubClient() {
        if(gitHubClient == null) {
            gitHubClient = new GitHubClient();
            gitHubClient.setCredentials(
                        getStoredValue(PreferenceConstants.P_GITHUB_LOGIN), 
                        getStoredValue(PreferenceConstants.P_GITHUB_PASSWD));
        }
        return gitHubClient;
    }
    
    public GitHubClient newGitHubClient() {
        gitHubClient = null;
        return getGitHubClient();
    }

    public static boolean isGitHubCredentialsSaved() {
        String login = getStoredValue(PreferenceConstants.P_GITHUB_LOGIN);
        String passwd = getStoredValue(PreferenceConstants.P_GITHUB_PASSWD);
        return !(login.trim().isEmpty() || passwd.trim().isEmpty());
    }
}
