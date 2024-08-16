package br.com.alura.screenmatch.service;

public interface iConvertData {
    <T> T getData(String json, Class<T> classe);
}
