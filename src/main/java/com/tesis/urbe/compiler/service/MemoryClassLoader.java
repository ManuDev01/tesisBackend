package com.tesis.urbe.compiler.service;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;

public class MemoryClassLoader extends ClassLoader {
    private final ByteArrayOutputStream byteCode = new ByteArrayOutputStream();

    public SimpleJavaFileObject getJavaFileObject(String name) {
        return new SimpleJavaFileObject(
                java.net.URI.create("bytes:///" + name.replace('.', '/') + SimpleJavaFileObject.Kind.CLASS.extension),
                SimpleJavaFileObject.Kind.CLASS) {
            @Override
            public java.io.OutputStream openOutputStream() {
                return byteCode;
            }
        };
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] bytes = byteCode.toByteArray();
        if (bytes.length == 0) {
            return super.findClass(name);
        }
        return defineClass(name, bytes, 0, bytes.length);
    }
}