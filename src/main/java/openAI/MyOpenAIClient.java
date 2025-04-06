package openAI;

import com.openai.models.ChatModel;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;

public class MyOpenAIClient {
    private static final String OPENAI_API_KEY = "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";

    public String analyzeCode(String code) {
        try {
            return generateText(createAnalysisPrompt(code), 1.f, 150);
        } catch (Exception e) {
            return "Could not generate: " + e.getMessage();
        }
    }

    public String generateText(String prompt, float temp, int maxTokens) throws Exception {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(OPENAI_API_KEY)
                .build();

        ResponseCreateParams params = ResponseCreateParams.builder()
                .input(prompt)
                .model(ChatModel.GPT_3_5_TURBO)
                .temperature(temp)
                .maxOutputTokens(maxTokens)
                .build();

        Response response = client.responses().create(params);

        if (response.output().isEmpty() || response.output().get(0).message().isEmpty()
            || response.output().get(0).message().get().content().isEmpty()
            || response.output().get(0).message().get().content().get(0).outputText().isEmpty()) {
            return "Could not generate!";
        }

        return response.output().get(0).message().get().content().get(0).outputText().get().text();
    }

    private String createAnalysisPrompt(String code) {
        return "Analyze the following piece of code and explain its purpose, potential quirks, and "
                + "what it does in a human-readable way. Make it short, no more than 100 words! "
                + "If there are any issues, please point them out and suggest improvements if "
                + "possible. If intent cannot be inferred because too little code was given, say "
                + " 'Too little code was selected!'\n\n"
                + "Code:\n" + code;
    }

    public boolean isCodeRelated(String needle, String hay) {
        try {
            String res = generateText("Compare these two pieces of code. Answer only \"yes\" if they are similar enough " +
                    "and both are clarifying something about the code. Answer only \"no\" if you do " +
                    "not consider that. Again, do not say anything else other than 'yes' and " +
                    "'no'\n Piece of code one: " + needle + "\nSecond piece of code: " + hay +
                    "\n", 0, 20);
            return res.equalsIgnoreCase("yes");
        } catch (Exception e) {
            return false;
        }
    }
}
