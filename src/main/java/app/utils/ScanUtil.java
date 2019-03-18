package app.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ScanUtil {
    private static final Logger logger = LogManager.getLogger(ScanUtil.class);

    public static Set<String> scanClasses(String basePackage) {
        Set<String> clsList = new HashSet<>();
        for (String clsName : scanClasses()) {
           if (clsName.startsWith(basePackage)) {
               logger.debug("checkout clsName: {}", clsName);
               clsList.add(clsName);
           }
        }
        return clsList;
    }

    private static Set<String> scanClasses() {
        Set<String> clsList = new HashSet<>();
        try {
            String modulePath = System.getProperty(JDK_MODULE_PATH);
            if (StringUtils.isNotBlank(modulePath)) {
                String[] paths = modulePath.split(System.getProperty(PATH_SEPARATOR));
                for (int i = 0; i < paths.length; i++) {
                    File file = new File(paths[i]);
                    if (file.exists()) {
                        logger.debug("scan file {}", paths[i]);
                        if (file.isDirectory()) {
                            clsList.addAll(scanClassesFromDirectory(paths[i], file));
                        } else {
                            JarFile jarFile = new JarFile(file);
                            clsList.addAll(scanClassesFromJarFile(jarFile));
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return clsList;
    }

    private static List<String> scanClassesFromDirectory(String rootPath, File directory) {
        List<String> clsList = new ArrayList<>();
        File[] childFiles = directory.listFiles();
        for (File childFile : childFiles) {
            if (childFile.isDirectory()) {
                clsList.addAll(scanClassesFromDirectory(rootPath, childFile));
            } else {
                String fileName = childFile.getName();
                if (fileName.endsWith(".class") && !fileName.contains("$")) {
                    String clsName = childFile.getPath()
                            .substring(rootPath.length())
                            .replaceAll("\\\\", ".")
                            .replaceAll("/", ".");
                    if (clsName.startsWith(".")) clsName = clsName.substring(1);
                    if (clsName.endsWith(".class")) clsName = clsName.substring(0, clsName.length() - 6);
                    logger.debug("{} => {}", childFile.getPath(), clsName);
                    clsList.add(clsName);
                }
            }
        }
        return clsList;
    }

    private static List<String> scanClassesFromJarFile(JarFile jarFile) throws Exception{
        List<String> clsList = new ArrayList<>();
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry jarEntry = entries.nextElement();
            String entryName = jarEntry.getName();
            if (entryName.endsWith(".class") && !entryName.contains("$")) {
                String clsName = entryName.replaceAll("\\\\", ".")
                        .replaceAll("/", ".");
                if (clsName.startsWith(".")) clsName = clsName.substring(1);
                if (clsName.endsWith(".class")) clsName = clsName.substring(0, clsName.length() - 6);
                logger.debug("{} => {}", entryName, clsName);
                clsList.add(clsName);
            }
        }
        return clsList;
    }

    static final String JDK_MODULE_PATH = "jdk.module.path";
    static final String PATH_SEPARATOR = "path.separator";
}
