package ca.xera.pvpvelocity.file;

import com.moandjiezana.toml.Toml;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * @author SevJ6
 * READ-ONLY toml configuration
 */
@Getter
public class TomlConfig {

    private final File file;
    private final Path absolutePath;
    private final Toml toml;

    public TomlConfig(Path absolutePath, InputStream is) {
        this.absolutePath = absolutePath;
        file = new File(absolutePath.toUri());
        if (!file.exists()) {
            try {
                Files.copy(is, absolutePath, StandardCopyOption.REPLACE_EXISTING);
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        toml = new Toml().read(file);
    }

    public String getString(String key) {
        return toml.getString(key);
    }

    public int getInt(String key) {
        return Math.toIntExact(toml.getLong(key));
    }

    public long getLong(String key) {
        return toml.getLong(key);
    }

    public double getDouble(String key) {
        return toml.getDouble(key);
    }

    public List<String> getStringList(String key) {
        return toml.getList(key);
    }

    public List<?> getList(String key) {
        return toml.getList(key);
    }

    public boolean getBoolean(String key) {
        return toml.getBoolean(key);
    }
}
