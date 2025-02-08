package com.yedu.backend.domain.teacher.domain.entity.constant;

public enum Day {
    월,화,수,목,금,토,일;

    public static Day byInt(int day) {
        if (day == 0)
            return 월;
        if (day == 1)
            return 화;
        if (day == 2)
            return 수;
        if (day == 3)
            return 목;
        if (day == 4)
            return 금;
        if (day == 5)
            return 토;
        return 일;
    }
}
