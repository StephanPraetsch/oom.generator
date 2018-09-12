package com.mercateo.oom.generator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class MemoryConsumer {

    private final List<byte[]> bytes = new ArrayList<>();

    public void add(int amountOfBytes) {
        byte[] newBytes = new byte[amountOfBytes];
        for (int i = 0; i < amountOfBytes; i++) {
            newBytes[i] = (byte) i;
        }
        bytes.add(newBytes);
    }

    public void clear() {
        bytes.clear();
    }

    public void generateOom() {
        while (true) {
            add(Integer.MAX_VALUE);
        }
    }

}
