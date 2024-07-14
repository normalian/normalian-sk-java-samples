# normalian-sk-java-samples
Here are sample codes for Semantic Kernel Java. You have to setup following env variables to run this sample.
- AZURE_OPENAI_ENDPOINT
- AZURE_OPENAI_API_KEY: API key of Azure OpenAI
- AZURE_OPENAI_DEPLOYMENT_NAME: ChatCompletion model deployment
- AZURE_OPENAI_TTS_DEPLOYMENT_NAME: Text To Speech model deployment
- AZURE_OPENAI_WHISPER_DEPLOYMENT_NAME: Speech to Text model deployment

Note that you have to create TTS model by following an article because you can deploy TTS model on very few regions. 
https://learn.microsoft.com/en-us/azure/ai-services/openai/concepts/models#whisper-models

# Reference
- https://github.com/microsoft/semantic-kernel/blob/main/java/README.md
- https://devblogs.microsoft.com/semantic-kernel/announcing-the-general-availability-of-semantic-kernel-java/