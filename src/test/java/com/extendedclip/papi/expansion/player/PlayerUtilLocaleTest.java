package com.extendedclip.papi.expansion.player;

import org.bukkit.entity.Player;
import org.junit.Test;

import java.lang.reflect.Proxy;

import static org.junit.Assert.assertEquals;

public class PlayerUtilLocaleTest {
    private static Player playerWithLocale(String language) {
        return (Player) Proxy.newProxyInstance(
                Player.class.getClassLoader(),
                new Class<?>[]{Player.class},
                (proxy, method, args) -> {
                    if ("getLocale".equals(method.getName())) return language;
                    if ("toString".equals(method.getName())) return "MockPlayer";
                    if ("hashCode".equals(method.getName())) return System.identityHashCode(proxy);
                    if ("equals".equals(method.getName())) return proxy == args[0];
                    throw new UnsupportedOperationException(method.getName());
                });
    }

    @Test public void usesClientLocaleWithoutNmsReflection() {
        assertEquals("en_nz", PlayerUtil.getLocale(playerWithLocale("en_nz")));
        assertEquals("es_es", PlayerUtil.getLocale(playerWithLocale("es_es")));
    }

    @Test public void retainsLegacyFallback() {
        assertEquals("en_US", PlayerUtil.getLocale(playerWithLocale(null)));
        assertEquals("en_US", PlayerUtil.getLocale(playerWithLocale("")));
    }
}
