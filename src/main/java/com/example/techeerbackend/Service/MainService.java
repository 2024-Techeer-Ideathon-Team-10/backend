package com.example.techeerbackend.Service;

import com.example.techeerbackend.DTO.ResponseDTO;
import com.example.techeerbackend.Entity.Question;
import com.example.techeerbackend.Repository.MainRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;

@Service
public class MainService {
    @Autowired
    MainRepository repository;

    public List<Question> getAllQuestions() {
        return repository.findAll();
    }

    public ResponseDTO RequestChatGpt(String base64) throws JSONException {
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
            imageUrl.put("url", "data:image/png;base64,"+base64); //todo: 주소 확정되면 넣을것.
            imageContent.put("image_url", imageUrl);
            JSONObject userMessage = new JSONObject();
            JSONObject assistantMessage = new JSONObject();
            userMessage.put("role", "user");
            assistantMessage.put("role","assistant");
            JSONArray userMessageArray = new JSONArray();
            userMessageArray.put(imageContent);
            userMessageArray.put(messageContent);
            JSONArray assistantMessageArray = new JSONArray();

            //userMessageArray.put(juseokContent);
            JSONObject assistantMessageContent = new JSONObject();
            assistantMessageContent.put("type","text");
            assistantMessageContent.put("text","");
            assistantMessageArray.put(assistantMessageContent);
            userMessage.put("content", userMessageArray);
            JSONArray array = new JSONArray();
            array.put(userMessage);
           // array.put(assistantMessage);
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
                if (!response.isSuccessful()) return new ResponseDTO(400,response);
                JSONObject obj =new JSONObject(response.body().string());
                String r = obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                //
                r = r.replace("`","");
                r = r.replace("json","");

                assert response.body() != null;
                String result = new JSONObject(r).toString();
                //String answer = new JSONObject(r).getJSONObject("정답도출").toString();
                String answer = "";
                for (Iterator it = new JSONObject(r).getJSONObject("정답도출").keys(); it.hasNext(); ) {
                    String n = (String) it.next();
                    String str = new JSONObject(r).getJSONObject("정답도출").get(n).toString();
                    answer += str+"\n";
                }
                Question question = new Question();
                question.setBase64Image(base64);
                question.setSolution(result);
                question.setAnswer(answer);
                repository.save(question);

                return new ResponseDTO(200, result);
            } catch (IOException e) {
                return new ResponseDTO(400,e.getMessage());
            }
        }
        public ResponseDTO likeQuest() throws JSONException {
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
                 return new ResponseDTO(200, result);
            } catch (IOException e) {
                return new ResponseDTO(400,e.getMessage());
            }
        }
    }

