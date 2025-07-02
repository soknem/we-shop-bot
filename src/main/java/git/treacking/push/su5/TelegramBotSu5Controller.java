package git.treacking.push.su5;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/su5/bot")
public class TelegramBotSu5Controller {

    private final TelegramBotSu5Service telegramBotService;

    @PostMapping(value = "/{groupId}", consumes = "application/json")
    public void su5Notification(

            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Gitlab-Token",required = false) String token,
            @PathVariable String groupId,
            @RequestParam(required = false) String threadId) {

         telegramBotService.notification(groupId,threadId,payload);
    }

}
