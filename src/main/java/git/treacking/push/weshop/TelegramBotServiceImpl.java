package git.treacking.push.weshop;

import git.treacking.push.weshop.dto.MessageSendBody;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramBotServiceImpl implements TelegramBotService {

    @Value("${telegram.bot.token1}")
    private String botToken;

    private final RestTemplate restTemplate;
    private final RestClient restClient;

    @Override
    public void notification(String groupId, String topicId, Map<String, Object> payload) {

        processWebhookPayload(payload).forEach(message -> {
            send(groupId, topicId, message);
        });

    }

    private void send(String groupId, String topic, MessageSendBody messageSendBody) {
        // Format dateTime to dd/MM/yyyy HH:mm:ss
        String formattedDateTime = formatDateTime(messageSendBody.dateTime());

        // Prepare the Telegram message in Markdown
        String telegramMessage = String.format("""
                🎉 *NEW ACTION!* 🎉 
                ----------------------------------------
                
                • *Repository* : `%s` 
                • *Author*        : `%s` 
                • *Message*     : `%s` 
                • *Branch*        : `%s` 
                • *DateTime*   : `%s` 
                ----------------------------------------
                """,
                messageSendBody.repoName(),
                messageSendBody.authorName(),
                messageSendBody.message(),
                messageSendBody.dateTime(),
                formattedDateTime
        );

        // Construct JSON body
        var body = new java.util.LinkedHashMap<String, Object>();
        body.put("chat_id", groupId);
        body.put("text", telegramMessage);
        body.put("parse_mode", "Markdown");
        if (topic != null) {
            body.put("message_thread_id", Integer.parseInt(topic));
        }

        String url = String.format("https://api.telegram.org/bot%s/sendMessage", botToken);

        // Send POST using RestClient
        var response = restClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to send message to Telegram");
        }
    }


    private List<MessageSendBody> processWebhookPayload(Map<String, Object> payload) {

        String ref = (String) payload.get("ref");
        String branch = ref.replace("refs/heads/", "");

        Map<String, Object> repository = (Map<String, Object>) payload.get("repository");
        String repoName = (String) repository.get("name");

        List<Map<String, Object>> commits = (List<Map<String, Object>>) payload.get("commits");

        List<MessageSendBody> responses = new ArrayList<>();

        for (Map<String, Object> commit : commits) {
            Map<String, String> author = (Map<String, String>) commit.get("author");
            String authorName = author.get("name");
            String message = (String) commit.get("message");
            String dateTime = (String) commit.get("timestamp");

            responses.add(new MessageSendBody(repoName, authorName, message, branch, dateTime));
        }

        return responses;
    }


    private String formatDateTime(String dateTime) {
        try {
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(dateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            return offsetDateTime.format(formatter);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Failed to parse date time: " + dateTime, e);
        }
    }
}
