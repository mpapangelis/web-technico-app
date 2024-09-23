package com.example.webtechnico.repositories;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {
    T create(T t);

    void update(T t);

    void delete(T t);

    <V> Optional<T>  findById(V v);

    List<T> findAll();
}
