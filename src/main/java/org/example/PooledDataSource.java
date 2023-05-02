package org.example;

import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PooledDataSource extends PGSimpleDataSource {
    public static final int POOL_SIZE = 10;
    // todo: create a collection that will hold the pooled connections
    // todo: initialize 10 connections
    // todo: ovverride the getConnection method so it return a connection from a pool

    private Queue<Connection> connectionPool;

    @SneakyThrows
    public PooledDataSource(String url, String username, String password) {
        super();
        setURL(url);
        setUser(username);
        setPassword(password);

        connectionPool = new ConcurrentLinkedDeque<>();

        for (int i = 0; i < POOL_SIZE; i++) {
            var realConnection = super.getConnection();
            var connectionProxy = new ConnectionProxy(realConnection, connectionPool);
            connectionPool.add(connectionProxy);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connectionPool.poll();
    }
}
