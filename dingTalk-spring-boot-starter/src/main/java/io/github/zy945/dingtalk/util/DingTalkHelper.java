package io.github.zy945.dingtalk.util;

import com.aliyun.credentials.utils.StringUtils;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.request.OapiRobotSendRequest.Markdown;
import com.dingtalk.api.request.OapiRobotSendRequest.Text;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import io.github.zy945.dingtalk.empty.DingDingConnect;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * @author 伍六七
 * @date 2023/6/22 22:38
 */
@Slf4j
@Component
public class DingTalkHelper {

    /**
     * 消息类型
     */
    private static final String MSG_TYPE_TEXT = "text";

    /**
     * 客户端实例
     */
    @Resource(name = "DingTalkClient")
    public DingTalkClient textClient;
    private static volatile DingTalkHelper dingTalkHelper;

    public static DingTalkHelper getSingleton() {
        if (dingTalkHelper == null) {
            synchronized (DingTalkHelper.class) {
                if (dingTalkHelper == null) {
                    dingTalkHelper = new DingTalkHelper();
                }
            }
        }
        return dingTalkHelper;
    }

    /**
     * @param content    文本消息
     * @param mobileList 指定@ 联系人
     * @param isAtAll    是否@ 全部联系人
     * @description: 发送普通文本消息
     * @return: com.dingtalk.api.response.OapiRobotSendResponse
     * @author: niaonao
     * @date: 2019/7/6
     */
    public OapiRobotSendResponse sendMessageByText(String content, List<String> mobileList, boolean isAtAll) {
        if (StringUtils.isEmpty(content)) {
            return null;
        }

        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：text
        //content	String	是	消息内容
        //atMobiles	Array	否	被@人的手机号(在content里添加@人的手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        Text text = new Text();
        text.setContent(content);
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        if (!CollectionUtils.isEmpty(mobileList)) {
            // 发送消息并@ 以下手机号联系人
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(mobileList);
            at.setIsAtAll(isAtAll);
            request.setAt(at);
        }
        request.setMsgtype(DingTalkHelper.MSG_TYPE_TEXT);
        request.setText(text);

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = textClient.execute(request);
        } catch (ApiException e) {
            log.error("[发送普通文本消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }

    public OapiRobotSendResponse sendAlarmByMarkdown(Markdown markdown, List<String> mobileList, boolean isAtAll) {
        if (ObjectUtils.isEmpty(markdown)) {
            return null;
        }

        //参数	参数类型	必须	说明
        //msgtype	String	是	消息类型，此时固定为：text
        //content	String	是	消息内容
        //atMobiles	Array	否	被@人的手机号(在content里添加@人的手机号)
        //isAtAll	bool	否	@所有人时：true，否则为：false
        OapiRobotSendRequest request = new OapiRobotSendRequest();
        if (!CollectionUtils.isEmpty(mobileList)) {
            // 发送消息并@ 以下手机号联系人
            OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
            at.setAtMobiles(mobileList);
            at.setIsAtAll(isAtAll);
            request.setAt(at);
        }
        request.setMsgtype("markdown");
        request.setMarkdown(markdown);

        OapiRobotSendResponse response = new OapiRobotSendResponse();
        try {
            response = textClient.execute(request);
        } catch (ApiException e) {
            log.error("[发送普通文本消息]: 发送消息失败, 异常捕获{}", e.getMessage());
        }
        return response;
    }
}
 