package org.dragon.channel;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.dragon.channel.adapter.ChannelAdapter;
import org.dragon.channel.entity.NormalizedMessage;
import org.dragon.gateway.Gateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: zhz
 * Version: 1.0
 * Create Date Time: 2026/3/13 23:13
 * Update Date Time:
 *
 */
@Service
@Slf4j
public class ChannelManager {

    // 渠道注册表：ChannelName -> ChannelAdapter 实例
    private final Map<String, ChannelAdapter> registry = new ConcurrentHashMap<>();

    private final Gateway gateway;

    // 核心魔法：Spring 会自动把所有实现了 ChannelAdapter 的 Bean 塞进这个 List 里
    @Autowired
    public ChannelManager(List<ChannelAdapter> adapters, Gateway gateway) {
        this.gateway = gateway;
        for (ChannelAdapter adapter : adapters) {
            registry.put(adapter.getChannelName(), adapter);
            log.info("[Manager] 成功注册渠道插件: " + adapter.getChannelName());
        }
    }

    // Spring Boot 启动完成后自动执行
    @PostConstruct
    public void startAllChannels() {
        log.info("[Manager] 正在启动所有渠道监听服务...");
        for (ChannelAdapter adapter : registry.values()) {
            // 必须异步启动！因为 Netty 的 bind().sync() 会阻塞线程
            // 如果不异步，Spring Boot 将永远卡在这里无法完成启动
            CompletableFuture.runAsync(() -> {
                try {
                    adapter.startListening(gateway);
                } catch (Exception e) {
                    log.error("[Manager] 渠道 " + adapter.getChannelName() + " 启动失败: " + e.getMessage());
                }
            });
        }
    }

    @Scheduled(fixedRate = 30000)
    public void healthCheckProbes() {
        log.info("[Watchdog] 开始执行全渠道健康巡检...");

        for (ChannelAdapter adapter : registry.values()) {
            try {
                if (!adapter.isHealthy()) {
                    log.error("[Watchdog] 发现渠道异常宕机: " + adapter.getChannelName() + "，准备触发自愈流程！");

                    // 必须异步执行重启，千万不能卡死巡检线程！
                    CompletableFuture.runAsync(() -> {
                        try {
                            adapter.restart();
                            log.info("[Watchdog] 渠道 " + adapter.getChannelName() + " 重启自愈成功！");
                        } catch (Exception e) {
                            log.error("[Watchdog] 渠道自愈失败，可能需要人工介入: " + e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                // 防止由于某个 Channel 的 isHealthy 抛错，导致整个巡检挂掉
                e.printStackTrace();
            }
        }
    }

    // 提供给 Gateway 或大模型使用的统一发送入口 (下行路由分发)
    public CompletableFuture<Void> routeMessageOutbound(String targetChannel, String targetUser, NormalizedMessage msg) {
        ChannelAdapter adapter = registry.get(targetChannel);
        if (adapter == null) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("未找到对应的渠道: " + targetChannel));
        }
        return adapter.sendMessage(targetUser, msg);
    }

    // Spring Boot 关闭时自动执行，优雅释放 Netty 资源
    @PreDestroy
    public void stopAllChannels() {
        log.info("[Manager] 收到停机指令，正在关闭所有渠道...");
        registry.values().forEach(ChannelAdapter::stop);
    }
}
