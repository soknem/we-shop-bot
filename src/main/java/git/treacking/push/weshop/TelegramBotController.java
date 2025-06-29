package git.treacking.push.weshop;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bot")
public class TelegramBotController {

    private final TelegramBotService telegramBotService;

    @PostMapping(value = "/{groupId}", consumes = "application/json")
    public void weShopNotification(

            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Gitlab-Token",required = false) String token,
            @PathVariable String groupId,
            @RequestParam(required = false) String threadId) {

         telegramBotService.notification(groupId,threadId,payload);
    }

}
