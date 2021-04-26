package ru.mirea.my_di;

import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClasspathScanner {

    private static String removeExtension(String path) {
        if (path.endsWith(".class"))
            return path.substring(0, path.length() - 6);
        return null;
    }

    private static void scanClasspath(String fromPackage, Consumer<String> className) throws Exception {
        String rootFolder = fromPackage.replace('.', '/');
        if (!rootFolder.endsWith("/")) {
            rootFolder += "/";
        }
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Enumeration<URL> resources = cl.getResources(rootFolder);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            String protocol = url.getProtocol();
            if ("file".equals(protocol)) {
                Path dir = Paths.get(url.toURI());
                Path packPath = Paths.get(rootFolder);
                Path cpRoot = dir;
                for (int i = 0; i < packPath.getNameCount(); i++) {
                    cpRoot = cpRoot.getParent();
                }
                Path cpRoot0 = cpRoot;
                Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        String relPath = cpRoot0.relativize(file).toString();
                        String name = removeExtension(relPath);
                        if (name != null) {
                            className.accept(name.replace(File.separatorChar, '.'));
                        }
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else if ("jar".equals(protocol)) {
                JarURLConnection jc = (JarURLConnection) url.openConnection();
                JarFile jar = jc.getJarFile();
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory() && entry.getName().startsWith(rootFolder)) {
                        String name = removeExtension(entry.getName());
                        if (name != null) {
                            className.accept(name.replace('/', '.'));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        String rootPackage = "ru.mirea";
//        String rootPackage = "com.google";
        scanClasspath(rootPackage, className -> {
            try {
                Class<?> cls = Class.forName(className);
                if (cls.isAnnotationPresent(RestController.class)) {
                    System.out.println(cls.getName());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }
}
