package com.dabel.oculusbank.app;

import java.util.Random;

public final class Generator {

    public static String generateAccountNumber() {
        Random random = new Random();
        int part1 = random.nextInt(1000);
        int part2 = random.nextInt(1,10000);
        int part3 = random.nextInt(1,10000);

        return String.format("%03d%04d%04d", part1, part2, part3);
    }
}
