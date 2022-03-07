package com.lege.component;

import lombok.extern.slf4j.Slf4j;
import love.forte.simbot.bot.BotManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
public class DrinkNotify {

    @Resource
    private BotManager botManager;

    @Value("${bello.qq}")
    private Set<String> qqSet;

    /**
     * 喝水语录
     */
    static List<String> content;

    /**
     * 喝水图片
     */
    static List<String> images;

    static {
        content = new ArrayList<>();
        images = new ArrayList<>();
        log.info("开始加载喝水语录~~~");
        // 喝水语录
        content.add("俗话说\"女人是水造的\"，所以身为女生就要时刻喝水，这样就可以保持充足的水分，皮肤、头发就会更有光泽~");
        content.add("喝多点水还可以保持身材哦，因为水促进了我们身体的循环~");
        content.add("该喝水了哟，喝多点水整体上也会容光焕发~");
        content.add("该喝水了哟，要多爱护自己，多喝水、多吃新鲜水果蔬菜、尽量保证充足睡眠。加油！");
        content.add("多喝水很简单的话，多喝水对身体好！只有心中挂念着你们的人才会说你的家人也老说的话：你要多喝水呀！！~");
        content.add("天气寒冷干燥。多喝水，注意保暖。少抽烟喝酒吃辣。多想念我~");
        log.info("开始加载喝水图片~~~");
        // 喝水图片
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221637.jpeg");
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221739.jpeg");
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221758.jpeg");
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221815.jpeg");
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221834.jpeg");
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221913.jpeg");
        images.add("https://gitee.com/yinfeng-code/study-image/raw/master/image/20211224221925.jpeg");
    }

    /**
     * 每一分钟提醒一次: 0 0/1 * * * ?
     * 每一小时提醒一次: 0 0 0/1 * * ?
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void handler() {
        Calendar calendar = Calendar.getInstance();
        // 获取当前小时
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        // 只在早上9点到晚上8点发送消息提醒
        if (hour < 9 || hour > 20) {
            return;
        }
        qqSet.forEach(qq -> {
            try {
                final String msg = content.get(new Random().nextInt(content.size()));
                final String img = String.format("[CAT:image,url=%s,flash=false]", images.get(new Random().nextInt(content.size())));
                // 发送随机喝水语录
                botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(qq, msg);
                // 发送随机喝水图片
                botManager.getDefaultBot().getSender().SENDER.sendPrivateMsg(qq, img);
                log.info("正在发送喝水提醒，当前qq={}, 语录={}, img={}", qq, msg, img);
            } catch (Exception e) {
                log.error("发送喝水提醒异常, qq={}", qq, e);
            }

        });
    }

}