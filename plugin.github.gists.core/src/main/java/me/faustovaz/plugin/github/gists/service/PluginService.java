package me.faustovaz.plugin.github.gists.service;

import java.io.IOException;
import java.util.List;

import org.eclipse.egit.github.core.Gist;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.GistService;
import org.eclipse.egit.github.core.service.UserService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PluginService {

    private GitHubClient client;
    
    public PluginService(GitHubClient client) {
        this.client = client;
    }
    
    public String getUserLogin() throws IOException {
        UserService userService = new UserService(client);
        return userService.getUser().getLogin();
    }
    
    public List<Gist> getGists(boolean downloadContent) throws IOException{
        GistService gistService = new GistService(client);
        List<Gist> gists = gistService.getGists(getUserLogin());
        if(downloadContent) {
            gists.forEach(gist -> loadContent(gist));
        }
        return gists;
    }
    
    public Gist loadContent(Gist gist) {
        OkHttpClient httpClient = new OkHttpClient();
        gist.getFiles().forEach((fileName, gistFile) -> {
            try {
                Response response = httpClient
                        .newCall(new Request.Builder().url(gistFile.getRawUrl()).build())
                        .execute();            
                gistFile.setContent(response.body().string());
            }
            catch(IOException ioException) {
                gistFile.setContent("Error: " + ioException.getMessage());
            }
        });
        return gist;
    }
}
