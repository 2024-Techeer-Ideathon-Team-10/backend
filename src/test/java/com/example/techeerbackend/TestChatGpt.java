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

public class TestChatGpt {
    public static void main(String[] args) throws JSONException, IOException {
        BufferedImage image = ImageIO.read(new URL("https://i.postimg.cc/NFstN489/2024-05-31-9-47-11.png"));
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image,"png",os);
        String base64 = "data:image/png;base64,"+Base64.getEncoder().encodeToString(os.toByteArray());
        //System.out.println(base64);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(Duration.ofSeconds(60));
        OkHttpClient client = builder.build();
        String apiKey = System.getenv("OPENAI_API_KEY");
        JSONObject messageContent = new JSONObject();
        messageContent.put("type", "text");
        messageContent.put("text", "해당 문제를 해당문제를\n 1. 문제 해설 \n2. 풀이 과정 \n 3. 정답 도출\n 과정으로 분리하고 \n초등학생도 이해할 수 있게 해설과 풀이를 적어서 단계별로 설명한 결과를 완벽한 json 형식으로 출력해줘 json 필드의 이름은 각각 문제해설, 풀이과정, 정답도출로 하고 문제가 여러개인 경우 각각 하위필드의 필드 모두를 번호로만 해줘");
        //JSONObject juseokContent = new JSONObject();
      //  juseokContent.put("type","text");
    //    juseokContent.put("text","추가로 각 영어문장을 한국어로 해석한 번역내용을 각 문장 하단에 주석을 달아서 json으로 출력해줘");
        JSONObject imageContent = new JSONObject();
        imageContent.put("type", "image_url");
        JSONObject imageUrl = new JSONObject();
        imageUrl.put("url", base64); //todo: 주소 확정되면 넣을것.
        imageContent.put("image_url", imageUrl);
        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        JSONArray userMessageArray = new JSONArray();
        userMessageArray.put(imageContent);
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
          //
            r = r.replace("`","");
            r = r.replace("json","");
            System.out.println(new JSONObject(r).toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
