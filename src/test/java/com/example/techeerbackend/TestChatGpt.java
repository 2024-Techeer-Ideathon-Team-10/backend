package com.example.techeerbackend;

import com.example.techeerbackend.DTO.ResponseDTO;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Duration;
import java.io.IOException;

public class TestChatGpt {
    public static void main(String[] args) throws JSONException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(Duration.ofSeconds(60));
        OkHttpClient client = builder.build();
        String apiKey = System.getenv("OPENAI_API_KEY");
        JSONObject messageContent = new JSONObject();
        messageContent.put("type", "text");
        messageContent.put("text", "해당 문제를 초등학생도 이해할 수 있게 해설과 풀이를 적어서 단계별로 설명해줘");
        JSONObject juseokContent = new JSONObject();
        juseokContent.put("type","text");
        juseokContent.put("text","추가로 각 영어문장을 한국어로 해석한 번역내용을 각 문장 하단에 주석을 달아줘");
        JSONObject imageContent = new JSONObject();
        imageContent.put("type", "image_url");
        JSONObject imageUrl = new JSONObject();
        imageUrl.put("url", "https://i.postimg.cc/3wZ7H129/2024-05-31-10-09-26.png"); //todo: 주소 확정되면 넣을것.
        imageContent.put("image_url", imageUrl);

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        JSONArray userMessageArray = new JSONArray();
        userMessageArray.put(imageContent);
        userMessageArray.put(messageContent);
        userMessageArray.put(juseokContent);
        userMessage.put("content", userMessageArray);
        JSONArray array = new JSONArray();
        array.put(userMessage);
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", array);
        requestBody.put("max_tokens", 1000);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                requestBody.toString()
        );

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) System.out.println(response);
            System.out.println(response.body().string());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}