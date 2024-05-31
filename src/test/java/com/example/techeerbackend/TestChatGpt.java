package com.example.techeerbackend;

import com.example.techeerbackend.DTO.ResponseDTO;
import okhttp3.*;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;

public class TestChatGpt {
    public static void main(String[] args) throws JSONException, IOException {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(Duration.ofSeconds(60));
        OkHttpClient client = builder.build();
        String apiKey = System.getenv("OPENAI_API_KEY");
        JSONObject messageContent = new JSONObject();
        messageContent.put("type", "text");
        messageContent.put("text", "비슷한 영어 문법 문제를 만들어서 완벽한 json 형식으로만 출력해줘 필드이름은 problem과 answer로만 해줘");
        //JSONObject juseokContent = new JSONObject();
        //  juseokContent.put("type","text");
        //    juseokContent.put("text","추가로 각 영어문장을 한국어로 해석한 번역내용을 각 문장 하단에 주석을 달아서 json으로 출력해줘");
        //JSONObject imageContent = new JSONObject();
        //imageContent.put("type", "image_url");
        JSONObject imageUrl = new JSONObject();
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        JSONArray userMessageArray = new JSONArray();
        //userMessageArray.put(imageContent);
        userMessageArray.put(messageContent);
        //userMessageArray.put(juseokContent);
        userMessage.put("content", userMessageArray);
        JSONArray array = new JSONArray();
        array.put(userMessage);
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", array);
        requestBody.put("max_tokens", 1200);

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
            JSONObject obj =new JSONObject(response.body().string());
            String r = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
            r = r.replace("`","");
            r = r.replace("json","");
            assert response.body() != null;
            String result = new JSONObject(r).toString();
            System.out.println(result);
            // return new ResponseDTO(200, result);
        } catch (IOException e) {
            //return new ResponseDTO(400,e.getMessage());
        }
    }
}
