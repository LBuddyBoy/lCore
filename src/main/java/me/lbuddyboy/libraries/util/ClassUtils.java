package me.lbuddyboy.libraries.util;

import com.google.common.collect.ImmutableSet;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public final class ClassUtils {

    public static Collection<Class<?>> getClassesInPackage(Plugin plugin, String packageName) {
        Collection<Class<?>> classes = new ArrayList();
        CodeSource codeSource = plugin.getClass().getProtectionDomain().getCodeSource();
        URL resource = codeSource.getLocation();
        String relPath = packageName.replace('.', '/');
        String resPath = resource.getPath().replace("%20", " ");
        String jarPath = resPath.replaceFirst("[.]jar[!].*", ".jar").replaceFirst("file:", "");

        JarFile jarFile;
        try {
            jarFile = new JarFile(jarPath);
        } catch (IOException var17) {
            throw new RuntimeException("Unexpected IOException reading JAR File '" + jarPath + "'", var17);
        }

        Enumeration entries = jarFile.entries();

        while(entries.hasMoreElements()) {
            JarEntry entry = (JarEntry)entries.nextElement();
            String entryName = entry.getName();
            String className = null;
            if (entryName.endsWith(".class") && entryName.startsWith(relPath) && entryName.length() > relPath.length() + "/".length()) {
                className = entryName.replace('/', '.').replace('\\', '.').replace(".class", "");
            }

            if (className != null) {
                Class clazz = null;

                try {
                    clazz = Class.forName(className);
                } catch (ClassNotFoundException var16) {
                    var16.printStackTrace();
                }

                if (clazz != null) {
                    classes.add(clazz);
                }
            }
        }

        try {
            jarFile.close();
        } catch (IOException var15) {
            var15.printStackTrace();
        }

        return ImmutableSet.copyOf(classes);
    }

    private ClassUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

