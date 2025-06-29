package git.treacking.push.weshop;

import git.treacking.push.weshop.dto.MessageSendBody;

import java.util.List;
import java.util.Map;

public interface TelegramBotService {


    void  notification(String groupId,String topicId,Map<String, Object> payload);


}
