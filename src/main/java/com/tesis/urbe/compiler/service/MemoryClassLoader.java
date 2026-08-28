package com.tesis.urbe.compiler.service;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MemoryClassLoader extends ClassLoader {
    private final Map<String, ByteArrayOutputStream> classBytesMap = new HashMap<>();

    public SimpleJavaFileObject getJavaFileObject(String className) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        classBytesMap.put(className, baos);

        return new SimpleJavaFileObject(
                URI.create("bytes:///" + className.replace('.', '/') + SimpleJavaFileObject.Kind.CLASS.extension),
                SimpleJavaFileObject.Kind.CLASS) {
            @Override
            public OutputStream openOutputStream() {
                return baos;
            }
        };
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        ByteArrayOutputStream baos = classBytesMap.get(name);
        if (baos == null) {
            return super.findClass(name);
        }
        byte[] bytes = baos.toByteArray();
        return defineClass(name, bytes, 0, bytes.length);
    }
}