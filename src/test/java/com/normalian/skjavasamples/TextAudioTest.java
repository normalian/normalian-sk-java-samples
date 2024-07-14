package com.normalian.skjavasamples;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.junit.Test;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.util.BinaryData;
import com.microsoft.semantickernel.services.audio.AudioContent;
import com.microsoft.semantickernel.services.audio.AudioToTextExecutionSettings;
import com.microsoft.semantickernel.services.audio.AudioToTextService;
import com.microsoft.semantickernel.services.audio.TextToAudioExecutionSettings;
import com.microsoft.semantickernel.services.audio.TextToAudioService;

public class TextAudioTest {
    // @Test
    public void textToAudio01() throws IOException {

        File audioFile = new File("test-" + UUID.randomUUID() + ".mp3");

        String endpoint = System.getenv("AZURE_OPENAI_ENDPOINT");
        String secretKey = System.getenv("AZURE_OPENAI_API_KEY");
        String modelID = System.getenv("AZURE_OPENAI_TTS_DEPLOYMENT_NAME");

        if (endpoint == null)
            throw new NullPointerException("AZURE_OPENAI_ENDPOINT is not defined on env variable");
        if (secretKey == null)
            throw new NullPointerException("AZURE_OPENAI_API_KEY is not defined on env variable");
        if (modelID == null)
            throw new NullPointerException("AZURE_OPENAI_TTS_DEPLOYMENT_NAME is not defined on env variable");

        // Create OpenAIClient instance
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(secretKey))
                .buildAsyncClient();

        // create TextToSpeech model
        var textToAudioService = TextToAudioService.builder()
                .withModelId(modelID)
                .withOpenAIAsyncClient(client)
                .build();

        String text = "Hi, I'm Daichi. Java is one of the most popular programming languages. Let's enjoy the world of AI with this historic language.";

        TextToAudioExecutionSettings executionSettings = TextToAudioExecutionSettings.builder()
                .withVoice("alloy")
                .withResponseFormat("mp3")
                .withSpeed(1.0)
                .build();

        AudioContent audioContent = textToAudioService.getAudioContentAsync(
                text,
                executionSettings)
                .block();

        // Save audio content to a file
        Files.write(audioFile.toPath(), audioContent.getData());

        // check the file exists
        assertTrue(audioFile.exists());
    }

    @Test
    public void audioTotext01() throws IOException {

        File audioFile = new File("test01.mp3");

        String endpoint = System.getenv("AZURE_OPENAI_ENDPOINT");
        String secretKey = System.getenv("AZURE_OPENAI_API_KEY");
        String modelID = System.getenv("AZURE_OPENAI_WHISPER_DEPLOYMENT_NAME");

        if (endpoint == null)
            throw new NullPointerException("AZURE_OPENAI_ENDPOINT is not defined on env variable");
        if (secretKey == null)
            throw new NullPointerException("AZURE_OPENAI_API_KEY is not defined on env variable");
        if (modelID == null)
            throw new NullPointerException("AZURE_OPENAI_WHISPER_DEPLOYMENT_NAME is not defined on env variable");

        // Create OpenAIClient instance
        OpenAIAsyncClient client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(secretKey))
                .buildAsyncClient();

        // create TextToSpeech model
        var textToAudioService = AudioToTextService.builder()
                .withModelId(modelID)
                .withOpenAIAsyncClient(client)
                .build();

        byte[] data = BinaryData.fromFile(audioFile.toPath()).toBytes();

        AudioToTextExecutionSettings executionSettings = AudioToTextExecutionSettings.builder()
                .withLanguage("en")
                .withPrompt("sample prompt")
                .withResponseFormat("json")
                .withTemperature(0.3)
                .withFilename(audioFile.getName())
                .build();

        String text = textToAudioService.getTextContentsAsync(
                AudioContent.builder()
                        .withModelId(modelID) // this setting is mandatory even though this looks duplicated
                        .withData(data)
                        .build(),
                executionSettings).block();

        System.out.println(text);

        // Save audio content to a file
        assertTrue(text != null & text.length() != 0);
    }
}
