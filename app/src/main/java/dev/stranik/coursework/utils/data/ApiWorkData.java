package dev.stranik.coursework.utils.data;

import androidx.core.util.Pair;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import dev.stranik.coursework.utils.SolutionInfo;
import dev.stranik.coursework.utils.SolutionList;
import dev.stranik.coursework.utils.TaskItem;
import dev.stranik.coursework.utils.User;
import dev.stranik.coursework.utils.UserInfo;
import dev.stranik.coursework.utils.UserSolution;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiWorkData extends WorkData {
    private final String apiUrl = "https://infoapi.stranik.dev/mobile/api/v1/";
    private final OkHttpClient client = new OkHttpClient();
    private final Gson gson = new Gson();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    public List<SolutionList> getTasks(int userId) throws IOException {
        String url = apiUrl + "tasks?userId=" + userId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Token", _token == null ? "" : _token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Type listType = new TypeToken<List<SolutionList>>() {}.getType();
                List<SolutionList> tasks = gson.fromJson(json, listType);
                return tasks;
            } else {
                throw new IOException("HTTP error code: " + response.code());
            }
        }
    }

    @Override
    public List<SolutionList> getSolutionTasks(int userId) throws IOException {
        String url = apiUrl + "solution-tasks?userId=" + userId;

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Token", _token.isBlank() ? "" : _token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String json = response.body().string();
                Type listType = new TypeToken<List<SolutionList>>() {}.getType();
                return gson.fromJson(json, listType);
            } else {
                throw new IOException("HTTP error code: " + response.code());
            }
        }
    }

    @Override
    public Pair<Boolean, String> loginUser(String username, String password) throws IOException {
        String loginUrl = apiUrl + "login";
        String json = gson.toJson(new LoginRequest(username, password));

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(loginUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);

                StaticWorkData.setAdminAndSave(loginResponse.isAdmin);
                StaticWorkData.setIdAndSave(loginResponse.id);

                _token = loginResponse.token;

                if (loginResponse.token != null) {
                    return new Pair<>(true, "");
                } else {
                    return new Pair<>(false, loginResponse.message != null ? loginResponse.message : "Unknown error");
                }
            } else {
                return new Pair<>(false, "HTTP error code: " + response.code());
            }
        }
    }

    @Override
    public Pair<Boolean, String> registerUser(String username, String password) throws IOException {
        String loginUrl = apiUrl + "register";
        String json = gson.toJson(new LoginRequest(username, password));

        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(loginUrl)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);

                StaticWorkData.setAdminAndSave(loginResponse.isAdmin);
                StaticWorkData.setIdAndSave(loginResponse.id);

                _token = loginResponse.token;

                if (loginResponse.token != null) {
                    return new Pair<>(true, "");
                } else {
                    return new Pair<>(false, loginResponse.message != null ? loginResponse.message : "Unknown error");
                }
            } else {
                return new Pair<>(false, "HTTP error code: " + response.code());
            }
        }
    }

    @Override
    public Pair<Boolean, UserInfo> getUserInfo() throws IOException {
        Request request = authorizedRequest("user/info").get().build();

        try (Response response = client.newCall(request).execute()) {
            Type type = new TypeToken<UserInfo>() {}.getType();
            return parseResponse(response, type);
        }
    }

    @Override
    public Pair<Boolean, SolutionInfo> getSolutionInfo(int solutionId) throws IOException {
        Request request = authorizedRequest("solutions/" + solutionId).get().build();

        try (Response response = client.newCall(request).execute()) {
            Type type = new TypeToken<SolutionInfo>() {}.getType();
            return parseResponse(response, type);
        }
    }

    @Override
    public Pair<Boolean, List<User>> getUsers() throws IOException {
        Request request = authorizedRequest("users").get().build();

        try (Response response = client.newCall(request).execute()) {
            Type type = new TypeToken<List<User>>() {}.getType();
            return parseResponse(response, type);
        }
    }

    @Override
    public Pair<Boolean, List<TaskItem>> getTaskItems(int solutionId) throws IOException {
        Request request = authorizedRequest("solutions/" + solutionId + "/tasks").get().build();

        try (Response response = client.newCall(request).execute()) {
            Type type = new TypeToken<List<TaskItem>>() {}.getType();
            Pair<Boolean, List<TaskItem>> result = parseResponse(response, type);
            return result;
        }
    }

    @Override
    public Pair<Boolean, List<UserSolution>> getUsersSolution(int solutionId) throws IOException {
        Request request = authorizedRequest("solutions/" + solutionId + "/users").get().build();

        try (Response response = client.newCall(request).execute()) {
            Type type = new TypeToken<List<UserSolution>>() {}.getType();
            Pair<Boolean, List<UserSolution>> result = parseResponse(response, type);
            return result;
        }
    }

    @Override
    public Boolean submitAnswers(int solutionId, List<Integer> answers) throws IOException {
        String endpoint = "solutions/" + solutionId + "/submit";
        String json = gson.toJson(new AnswersRequest(answers));
        RequestBody body = RequestBody.create(json, JSON);

        Request request = authorizedRequest(endpoint)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseBooleanResponse(response);
        }
    }

    @Override
    public Boolean deleteTask(int solutionId) throws IOException {
        String endpoint = "solutions/" + solutionId + "/delete";
        Request request = authorizedRequest(endpoint)
                .delete()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseBooleanResponse(response);
        }
    }

    @Override
    public Boolean createTask(SolutionList solutionInfo, List<TaskItem> tasks, List<User> userList) throws IOException {
        String endpoint = "tasks/create";

        CreateTaskRequest payload = new CreateTaskRequest(solutionInfo, tasks, userList);
        String json = gson.toJson(payload);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = authorizedRequest(endpoint)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return parseBooleanResponse(response);
        }
    }

    @Override
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Преобразуем байты в HEX
            StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private Request.Builder authorizedRequest(String endpoint) {
        return new Request.Builder()
            .url(apiUrl + endpoint)
            .addHeader("Token", _token)
            .addHeader("userId", String.valueOf(StaticWorkData.getId()));
    }

    private <T> Pair<Boolean, T> parseResponse(Response response, Type typeOfT) throws IOException {
        if (response.isSuccessful() && response.body() != null) {
            String json = response.body().string();
            T result = gson.fromJson(json, typeOfT);
            return new Pair<>(true, result);
        } else {
            return new Pair<>(false, null);
        }
    }

    private Boolean parseBooleanResponse(Response response) throws IOException {
        return response.isSuccessful();
    }

    private static class LoginRequest {
        String username;
        String password;

        LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    private static class LoginResponse {
        int id;
        boolean isAdmin;

        @SerializedName("token")
        String token;

        @SerializedName("message")
        String message;
    }

    private static class AnswersRequest {
        List<Integer> answers;

        AnswersRequest(List<Integer> answers) {
            this.answers = answers;
        }
    }

    private static class CreateTaskRequest {
        SolutionList solutionInfo;
        List<TaskItem> tasks;
        List<User> userList;

        CreateTaskRequest(SolutionList solutionInfo, List<TaskItem> tasks, List<User> userList) {
            this.solutionInfo = solutionInfo;
            this.tasks = tasks;
            this.userList = userList;
        }
    }
}
