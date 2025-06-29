package git.treacking.push.weshop.dto;

import java.util.Map;

public record MessageSendBody(

        String repoName,
        String authorName,
        String message,
        String branch,
        String dateTime
) {
}
