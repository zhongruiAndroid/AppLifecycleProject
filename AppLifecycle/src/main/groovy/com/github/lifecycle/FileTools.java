package com.github.lifecycle;

import java.io.File;

public class FileTools {
    public static boolean deleteDir(File self) {
        if (!self.exists()) {
            return true;
        } else if (!self.isDirectory()) {
            return false;
        } else {
            File[] files = self.listFiles();
            if (files == null) {
                return false;
            } else {
                boolean result = true;
                File[] var3 = files;
                int var4 = files.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    File file = var3[var5];
                    if (file.isDirectory()) {
                        if (!deleteDir(file)) {
                            result = false;
                        }
                    } else if (!file.delete()) {
                        result = false;
                    }
                }

                if (!self.delete()) {
                    result = false;
                }

                return result;
            }
        }
    }
}
