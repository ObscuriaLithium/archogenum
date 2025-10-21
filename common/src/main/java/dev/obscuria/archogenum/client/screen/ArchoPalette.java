package dev.obscuria.archogenum.client.screen;

import dev.obscuria.fragmentum.util.color.ARGB;
import dev.obscuria.fragmentum.util.color.Colors;

public interface ArchoPalette {

    ARGB WHITE = Colors.argbOf(0xFFFFFFFF);
    ARGB LIGHT = Colors.argbOf(0xffb9aeb8);
    ARGB MODERATE = Colors.argbOf(0xff80788a);
    ARGB DARK = Colors.argbOf(0xff3b2d3a);
    ARGB ACCENT = Colors.argbOf(0xffff8fdc);

    ARGB EMPHASIS = Colors.argbOf(0xFF8FE1FF);
    ARGB POSITIVE = Colors.argbOf(0xFF8FFFB6);
    ARGB NEGATIVE = Colors.argbOf(0xFFFF8F9C);
    ARGB IMPORTANT = Colors.argbOf(0xFFEC8FFF);
}
