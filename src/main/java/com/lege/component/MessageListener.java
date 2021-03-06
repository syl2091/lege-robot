package com.lege.component;

import cn.hutool.core.text.StrBuilder;
import com.alibaba.fastjson.JSONObject;
import com.lege.entity.LoveChatDTO;
import com.lege.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.annotation.OnGroup;
import love.forte.simbot.annotation.OnPrivate;
import love.forte.simbot.api.message.Reply;
import love.forte.simbot.api.message.ReplyAble;
import love.forte.simbot.api.message.events.GroupMsg;
import love.forte.simbot.api.message.events.MessageGet;
import love.forte.simbot.api.message.events.PrivateMsg;
import love.forte.simbot.api.sender.MsgSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author yinfeng
 * @description 机器人监听
 * @since 2021/11/6 20:51
 */
@Component
@Slf4j
public class MessageListener {

    static final String URL = "http://api.qingyunke.com/api.php";

    /**
     * 监听私聊消息
     */
    @OnPrivate
    public void privateMsg(PrivateMsg privateMsg, MsgSender sender) {
        // 智能聊天
//        sendMsg(privateMsg, sender, false);
        // 恋爱话术获取
        loveChat(privateMsg, sender);
    }


    /**
     * 监听群消息
     */
    @OnGroup
    public ReplyAble groupMsg(GroupMsg groupMsg, MsgSender sender) {
        // 默认关闭群聊模式，需要的话把注释去掉
        return sendMsg(groupMsg, sender, true);
//        return null;
    }

    /**
     * 恋爱话术功能
     *
     * @param commonMsg commonMsg
     * @param sender    sender
     */
    private void loveChat(MessageGet commonMsg, MsgSender sender) {
        log.info("恋爱话术~~~,接收消息：qq={}, msg={}", commonMsg.getAccountInfo().getAccountCode(),
                commonMsg.getMsgContent().getMsg());
        // 获取话术
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("page", 1);
        jsonObject.put("count", 1);
        jsonObject.put("key", commonMsg.getMsgContent().getMsg());
            final String resultStr = HttpUtil.post("https://yin-feng.top/open/getLoveChat", jsonObject.toJSONString());
        if (StringUtils.isEmpty(resultStr)) {
            return;
        }
        final JSONObject resultObj = JSONObject.parseObject(resultStr);
        if (!Objects.equals("200", resultObj.getString("code"))) {
            return;
        }
        final List<LoveChatDTO> loveChatList = JSONObject.parseArray(resultObj.getString("data"), LoveChatDTO.class);
        //发送私聊话术消息
        loveChatList.forEach(item -> {
            final StrBuilder msgBuilder = new StrBuilder("女生说: ");
            msgBuilder.append(item.getFemale()).append("\n");
            int i = 0;
            for (String msg : item.getMale()) {
                msgBuilder.append("话术").append(++i).append(": ").append(msg).append("\n");
            }
            sender.SENDER.sendPrivateMsg(commonMsg, msgBuilder.toString());
        });
    }

    /**
     * 通过青客云封装智能聊天
     *
     * @param commonMsg commonMsg
     * @param sender    sender
     */
    private ReplyAble sendMsg(MessageGet commonMsg, MsgSender sender, boolean group) {
        log.info("智能聊天中~~~,接收消息：qq={}, msg={}", commonMsg.getAccountInfo().getAccountCode(),
                commonMsg.getMsgContent().getMsg());
        // MsgSender中存在三大送信器，以及非常多的重载方法。
        // 通过get请求调用聊天接口
        final String result = HttpUtil.get(URL.concat("?key=free&appid=0&msg=").concat(commonMsg.getMsgContent().getMsg()));
        if (!StringUtils.isEmpty(result)) {
            final JSONObject json = JSONObject.parseObject(result);
            if (json.getInteger("result") == 0 && !StringUtils.isEmpty(json.getString("content"))) {
                final String msg = json.getString("content").replace("{br}", "\n");
                log.info("智能聊天中~~~,发送消息：qq={}, msg={}", commonMsg.getAccountInfo().getAccountCode(), msg);
                //发送群消息
                if (group) {
                    // 参数1：回复的消息 参数2：是否at当事人
                    return Reply.reply(msg, true);
                }
                //发送私聊消息
                sender.SENDER.sendPrivateMsg(commonMsg, msg);
            }
        }
        return null;
    }

}
