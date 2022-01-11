package geardesigner;

import javafx.geometry.Insets;

import java.util.HashMap;
import java.util.Map;

/**
 * 存储程序的常量设置
 *
 * @author SUPERTOP
 */
public final class Config {
    private volatile static Config config;
    private final Map<String, Object> configurations;

    private Config() {
        configurations = new HashMap<>();
        configurations.put("AccentDateCell.ACCENT_COLOR", "#7DEB56");
        configurations.put("Parameter.SPACING", 20d);
        configurations.put("InputParamTable.INIT_COL0_WIDTH", 100);
        configurations.put("InputParamTable.INIT_COL1_WIDTH", 60);
        configurations.put("InputParamTable.INIT_COL2_WIDTH", 100);
        configurations.put("OutputParamTable.INIT_COL0_WIDTH", 110);
        configurations.put("OutputParamTable.INIT_COL1_WIDTH", 80);
        configurations.put("OutputParamTable.INIT_COL2_WIDTH", 30);
        configurations.put("OutputParamTable.INIT_ROW_HEIGHT", 16);
        configurations.put("ParamTable.SPACING", 10d);
        configurations.put("ParamTable.PADDING", new Insets(6, 6, 12, 6));
        configurations.put("TexFormula.FONT_SIZE", 14f);
        /**
         * 0-“显示”样式（默认）；2-“文本”样式；4-“脚本”样式；6-更小的“脚本”样式
         */
        configurations.put("TexFormula.DEFAULT_STYLE", 0);
        configurations.put("SQLiteDatabase.COMPRESSION_BUFFER_SIZE", 1024);
        configurations.put("Version", "V1.2");
        configurations.put("Info", "本程序遵照国标方法计算");
    }

    public static Config getInstance() {
        if (config == null) {
            synchronized (Config.class) {
                if (config == null) {
                    config = new Config();
                }
            }
        }
        return config;
    }

    public static <T> T get(String key) {
        return (T) getInstance().configurations.get(key);
    }
}
