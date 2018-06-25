package com.mfanw.scoresort.util;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * 检测maven路径内的文件夹内容是否规范，如果该文件夹内没有jar文件则做删除处理
 * 
 * @author mengwei
 */
public class MavenCheck {

    private static final String MAVEN_REPOSITORY = "D:\\work\\maven_repository\\";

    private static int noJarCount = 0;
    private static int noPomCount = 0;
    private static int hasFileLastUpdatedCount = 0;

    private MavenCheck() {
    }

    public static void main(String[] args) throws Exception {
        File parent = new File(MAVEN_REPOSITORY);
        checkChildren(parent);
        System.out.println("noJarCount=" + noJarCount + "\tnoPomCount=" + noPomCount + "\thasFileLastUpdatedCount=" + hasFileLastUpdatedCount);
    }

    private static void checkChildren(File parent) throws Exception {
        if (parent == null || !parent.isDirectory()) {
            return;
        }
        File[] children = parent.listFiles();
        if (children == null) {
            return;
        }
        boolean hasFile = false;
        boolean hasFileJar = false;
        boolean hasFilePom = false;
        boolean hasFileLastUpdated = false;
        for (File child : children) {
            if (child.isDirectory()) {
                checkChildren(child);
            } else {
                hasFile = true;
                if (child.getName().endsWith(".jar")) {
                    hasFileJar = true;
                }
                if (child.getName().endsWith(".pom")) {
                    hasFilePom = true;
                }
                if (child.getName().endsWith(".lastUpdated")) {
                    hasFileLastUpdated = true;
                }
            }

        }
        if (hasFile && (!hasFileJar || !hasFilePom)) {
            if (!hasFileJar) {
                noJarCount++;
            }
            if (!hasFilePom) {
                noPomCount++;
            }
            if (hasFileLastUpdated) {
                hasFileLastUpdatedCount++;
                System.out.println("删除非法路径 " + parent.getPath());
                FileUtils.deleteDirectory(parent);
            } else {
                System.out.println("暂留空白路径  " + parent.getPath());
            }
        }
    }
}
