package com.normalian.skjavasamples;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;

public class ChatCompletionServiceTest {
    @Test
    public void simpleQA() {
        String endpoint = System.getenv("AZURE_OPENAI_ENDPOINT");
        String secretKey = System.getenv("AZURE_OPENAI_API_KEY");
        String modelID = System.getenv("AZURE_OPENAI_DEPLOYMENT_NAME");

        if (endpoint == null)
            throw new NullPointerException("AZURE_OPENAI_ENDPOINT is not defined on env variable");
        if (secretKey == null)
            throw new NullPointerException("AZURE_OPENAI_API_KEY is not defined on env variable");
        if (modelID == null)
            throw new NullPointerException("AZURE_OPENAI_DEPLOYMENT_NAME is not defined on env variable");

        // Create OpenAIClient instance
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(secretKey))
                .buildAsyncClient();

        Kernel kernel = Kernel.builder()
                .withAIService(ChatCompletionService.class,
                        OpenAIChatCompletion.builder()
                                .withModelId(modelID)
                                .withOpenAIAsyncClient(client)
                                .build())
                .build();

        String request = "List the benefits of Java Semantic Kernel compared with Langchain.";
        String prompt = """
                Give me answer of following questions.

                =================== Questions ===================
                %s
                """.formatted(request);

        var result = kernel.invokePromptAsync(prompt).block().getResult();
        System.out.println(result);
        var str = result.toString();
        assertTrue(str != null && !str.trim().isEmpty());
    }

}
