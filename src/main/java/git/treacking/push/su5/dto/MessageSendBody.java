package git.treacking.push.su5.dto;

public record MessageSendBody(

        String organization,
        String url,
        String repoName,
        String authorName,
        String message,
        String branch,
        String dateTime
) {
}
