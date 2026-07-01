package com.github.silvertreekr.mcprefixachievement.customconfig;

import com.github.silvertreekr.mcprefixachievement.dto.PrefixDataTranferObject;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class PrefixConfigManager {
    private final Map<Integer, PrefixDataTranferObject> prefixMap = new java.util.HashMap<>();
    public void readConfig(PrefixConfigLoader loader) {
        // PrefixConfigLoader를 사용하여 prefix.yml 파일을 읽어옴.
        YamlConfiguration config = loader.getPrefixConfig();
        // prefix.yml 파일의 각 키(첫번째 properties)를 순회
        for (String key : config.getKeys(false)) {
            // 각 키에 해당하는 ConfigurationSection을 가져옴
            int index = Integer.parseInt(key);
            ConfigurationSection section = config.getConfigurationSection(key);
            // section이 null이 아닌 경우, PrefixDataTranferObject로 변환하여 Map에 저장
            if (section != null) {
                prefixMap.put(index, PrefixDataTranferObject.fromBukkitConfig(section));
            }
        }
    }

    public void reloadConfig(PrefixConfigLoader loader) {
        // PrefixConfigLoader를 사용하여 prefix.yml 파일을 다시 읽어옴.
        loader.loadPrefixConfig();
        // 기존의 prefixMap을 초기화
        prefixMap.clear();
        // 새로 읽어온 설정으로 prefixMap을 다시 채움
        readConfig(loader);
    }

    public Set<Component> getAllDisplayPrefixes() {
        // prefixMap의 모든 PrefixDataTranferObject에서 displayPrefix를 추출하여 Set으로 반환
        return prefixMap.entrySet().stream()
                .map(entry -> entry.getValue().getDisplayPrefix())
                .collect(Collectors.toSet());
    }
}
