package com.ctrlaltdefeat.utils;

import com.NotificationSocketService;
import com.google.gson.*;
import com.ctraltdefeat.settings.MyAppSettings;
import com.intellij.openapi.project.Project;
import groovy.lang.Tuple2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class GitUtils {
    public static String GITHUB_API_BASE = "https://api.github.com";

    public static String runGitCommand(File repoDir, String... args) throws IOException {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(args);
        builder.directory(repoDir);
        builder.redirectErrorStream(true);

        Process process = builder.start();
        InputStream inputStream = process.getInputStream();
        String result = new String(inputStream.readAllBytes());
        return result.trim();
    }

    public static boolean isBranchUpToDate(File repoDir) throws IOException {
        String branch = getCurrentBranch(repoDir);
        String status = runGitCommand(repoDir,
                "git", "rev-list", "--left-right", "--count", "origin/" + branch + "...HEAD");
        return status.equals("0\t0");
    }

    public static String getCurrentBranch(File repoDir) throws IOException {
        return runGitCommand(repoDir, "git", "rev-parse", "--abbrev-ref", "HEAD");
    }

    public static String getRemoteUrl(File repoDir) throws IOException {
        return runGitCommand(repoDir, "git", "remote", "get-url", "origin");
    }

    public static String[] extractOwnerAndRepo(File repoDir) throws IOException {
        String gitUrl = getRemoteUrl(repoDir);

        String ownerRepo = null;

        if (gitUrl.startsWith("git@github.com:")) {
            ownerRepo = gitUrl.substring("git@github.com:".length());
        } else if (gitUrl.startsWith("https://github.com/")) {
            ownerRepo = gitUrl.substring("https://github.com/".length());
        } else {
            throw new IllegalArgumentException("Unsupported git URL format: " + gitUrl);
        }

        // Remove trailing .git if present
        if (ownerRepo.endsWith(".git")) {
            ownerRepo = ownerRepo.substring(0, ownerRepo.length() - 4);
        }

        String[] parts = ownerRepo.split("/");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid owner/repo format in URL: " + gitUrl);
        }

        return parts;
    }

    public static void createGitIssue(String repoOwner, String repoName, String title, String body,
                                      String token, int prio, Project project) throws Exception {
        String apiUrl = GITHUB_API_BASE + "/repos/" + repoOwner + "/" + repoName + "/issues";

        JsonObject issue = new JsonObject();
        issue.addProperty("title", title);
        issue.addProperty("body", body);
        issue.add("labels", new Gson().toJsonTree(List.of("question")));

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .POST(HttpRequest.BodyPublishers.ofString(issue.toString())).build();

        HttpResponse<String> response;
//        response = client.send(req, HttpResponse.BodyHandlers.ofString());
//        if (response.statusCode() != 201) {
//            throw new RuntimeException("Failed to create issue. HTTP error code: "
//                    + response.statusCode());
//        }

        try {
//            JsonObject notification = new JsonObject();
//            notification.addProperty("teamID",
//                    MyAppSettings.Companion.getInstance().getState().getTeamID());
//            notification.addProperty("urgency", prio);
//            req = HttpRequest
//                    .newBuilder()
//                    .uri(URI.create("http://"
//                            + MyAppSettings.Companion.getInstance().getState().getServerIP()
//                            + ":"
//                            + MyAppSettings
//                            .Companion
//                            .getInstance()
//                            .getState().getServerPort() + "/broadcast"))
//                    .header("Content-Type", "application/json")
//                    .POST(HttpRequest.BodyPublishers.ofString(notification.toString())).build();
//            response = client.send(req, HttpResponse.BodyHandlers.ofString());
            NotificationSocketService.Companion.getInstance(project).emitNotification(prio);
        } catch (Exception ignored) {}
    }

    public static ArrayList<Tuple2<String, String>> listGitIssues(String repoOwner,
                                                                  String repoName,
                                                                  String token)
            throws Exception {
        String apiUrl = GITHUB_API_BASE + "/repos/" + repoOwner + "/" + repoName + "/issues";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Authorization", "Bearer " + token)
                .header("X-GitHub-Api-Version", "2022-11-28")
                .GET().build();

        HttpResponse<String> response = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException("Failed to fetch issues. HTTP error code: "
                    + response.statusCode());
        }

        JsonArray issuesArray = JsonParser.parseString(response.body()).getAsJsonArray();
        ArrayList<Tuple2<String, String>> res = new ArrayList<>();
        for (JsonElement issueElement : issuesArray) {
            JsonObject issue = issueElement.getAsJsonObject();
            res.add(new Tuple2<>(issue.get("html_url").getAsString(),
                    issue.get("body").getAsString()));
        }
        return res;
    }

    private static String escapeJson(String text) {
        return text.replace("\"", "\\\"");
    }
}
