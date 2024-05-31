package com.example.techeerbackend.Service;

import com.example.techeerbackend.DTO.ResponseDTO;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.time.Duration;

@Service
public class MainService {
    public ResponseDTO RequestChatGpt(String base64) throws JSONException {
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
            imageUrl.put("url", "data:image/png;base64,"+base64); //todo: 주소 확정되면 넣을것.
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
                if (!response.isSuccessful()) return new ResponseDTO(200,response);
                return new ResponseDTO(200,response.body().string());
            } catch (IOException e) {
                return new ResponseDTO(400,e.getMessage());
            }
        }
    }

