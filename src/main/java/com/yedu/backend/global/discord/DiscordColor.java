package com.yedu.backend.global.discord;

public enum DiscordColor {
    RED(16711680), // 🔴 빨간색 (오류)
    BLUE(255), // 🔵 파란색 (일반 알림)
    LIGHT_BLUE(11393254); // 💙 연한 파란색

    private final int colorCode;

    DiscordColor(int colorCode) {
        this.colorCode = colorCode;
    }

    public int getCode() {
        return colorCode;
    }
}