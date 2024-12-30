package com.example.springsecuritydemo;

public class Singleton {
    private static volatile Singleton instance; // 确保多线程环境下的安全
    private String data;

    // 私有构造函数，禁止外部直接实例化
    private Singleton(String data) {
        this.data = data;
    }

    // 静态方法获取单例实例
    public static Singleton getInstance(String data) {
        Singleton result = instance; // 本地变量提高性能
        if (result == null) {
            synchronized (Singleton.class) { // 加锁确保线程安全
                result = instance;
                if (result == null) {
                    instance = result = new Singleton(data);
                }
            }
        }
        return result;
    }

}
