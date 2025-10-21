package dev.obscuria.archogenum.server;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import net.minecraft.FileUtil;
import org.jetbrains.annotations.Nullable;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public interface FileHelper {

    static @Nullable JsonElement loadJson(Path path) {
        try (var reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return Streams.parse(new JsonReader(reader));
        } catch (Exception ignored) {
            return null;
        }
    }

    static void saveJson(Path path, JsonElement element) {
        try {
            FileUtil.createDirectoriesSafe(path.getParent());
            try (var writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
                new GsonBuilder().setPrettyPrinting().create().toJson(element, writer);
            } catch (Exception ignored) {}
        } catch (Exception ignored) {}
    }
}
