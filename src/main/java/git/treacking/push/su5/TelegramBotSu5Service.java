package git.treacking.push.su5;

import java.util.Map;

public interface TelegramBotSu5Service {


    void  notification(String groupId,String topicId,Map<String, Object> payload);

    void scheduleNotification();


}
