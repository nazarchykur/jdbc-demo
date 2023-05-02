package org.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * Hello world!
 */
public class App {
    public static final int PRODUCTS_NUMBER = 10_000;
    public static final int BATCH_SIZE = 100;
    private static DataSource dataSource;

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Hello World!");

        /*
            Якщо виконується запит, який містить параметри, то використання `PreparedStatement` є більш ефективним
            варіантом, ніж використання `Statement`. Це пов'язано з тим, що `PreparedStatement` дозволяє попередньо
            скомпілювати запит з параметрами, що дозволяє базі даних оптимізувати виконання запиту та зменшити кількість
            передач даних між додатком та базою даних.

            Крім того, використання `PreparedStatement` дозволяє запобігти SQL-ін'єкції, оскільки параметри запиту
            передаються окремо від запиту та не можуть бути інтерпретовані як частини SQL-запиту.


            У цьому прикладі ми використовуємо `PreparedStatement` для виконання запиту на вибірку продуктів за назвою.
            Ми передаємо назву продукту як параметр запиту, що дозволяє запобігти SQL-ін'єкції та оптимізувати виконання запиту.

                String sql = "SELECT * FROM products WHERE name = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, "apple");
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String name = rs.getString("name");
                            BigDecimal price = rs.getBigDecimal("price");
                            Product product = new Product(id, name, price);
                            System.out.println(product);
                        }
                    }
                }

            У цьому прикладі ми використовуємо `Statement` для виконання того ж запиту на вибірку продуктів за назвою.
            Ми передаємо назву продукту як частину SQL-запиту, що може призвести до SQL-ін'єкції та зменшення ефективності
            виконання запиту.

                String sql = "SELECT * FROM products WHERE name = 'apple'";
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        BigDecimal price = rs.getBigDecimal("price");
                        Product product = new Product(id, name, price);
                        System.out.println(product);
                    }
                }
         */


//        DataSource dataSource = new PGSimpleDataSource();
//        dataSource = initDatasource();

        // get product by id using PreparedStatement (we also can use Statement)
//        try(Connection connection = dataSource.getConnection()) {
//            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM products WHERE id = ?")) {
//                ps.setInt(1, 999);
//                try (ResultSet rs = ps.executeQuery()) {
//                    if (rs.next()) {
//                        Product product = new Product();
//                        product.setId(rs.getLong("id"));
//                        product.setName(rs.getString("name"));
//                        product.setPrice(rs.getBigDecimal("price"));
//
//                        System.out.println(product);
//                    } else {
//                        System.out.println("Product not found");
//                    }
//                }
//            }
//        }


        // select all products as a list using Statement (we also can use PreparedStatement)
//        try (Connection connection = dataSource.getConnection()) {
//            try (Statement statement = connection.createStatement()) {
//                ResultSet resultSet = statement.executeQuery("select * from products");
//                List<Product> list = new ArrayList<>();
//                while (resultSet.next()) {
//                    Product product = new Product();
//                    product.setId(resultSet.getLong("id"));
//                    product.setName(resultSet.getString("name"));
//                    product.setPrice(resultSet.getBigDecimal("price"));
//
//                    list.add(product);
//                }
//                System.out.println(list);
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }


        // for insert, update, delete using PreparedStatement (since we put some parameters, better to use PreparedStatement)
//        try(Connection con = dataSource.getConnection()) {
//            try (PreparedStatement ps = con.prepareStatement("insert into products(name, price) VALUES (?, ?)")) {
//                ps.setString(1, "apple");
//                ps.setBigDecimal(2, new java.math.BigDecimal(100));
//
//                ps.executeUpdate();
//            }
//        }


        // use a lot of inserts without batch => bad performance
//        var start = System.currentTimeMillis();
//        try (Connection con = dataSource.getConnection()) {
//            try (PreparedStatement ps = con.prepareStatement("insert into products(name, price) VALUES (?, ?)")) {
//                for (int i = 1; i <= 10_000; i++) {
//                    ps.setString(1, "product" + i);
//                    ps.setBigDecimal(2, new BigDecimal(100).add(new BigDecimal(i)));
//
//                    ps.executeUpdate();
//                }
//            }
//        }
//        var end = System.currentTimeMillis();
//        System.out.println("it took: " + (end - start) + "ms"); // it took: 3317ms  | it took: 3562ms


        // using batch
//        var start = System.currentTimeMillis();
//        try(Connection con = dataSource.getConnection()) {
//            try (PreparedStatement ps = con.prepareStatement("insert into products(name, price) VALUES (?, ?)")) {
//                for (int i = 1; i <= PRODUCTS_NUMBER; i++) {
//                    ps.setString(1, "product" + i);
//                    ps.setBigDecimal(2, new java.math.BigDecimal(100).add(new java.math.BigDecimal(i)));
//                    ps.addBatch();
//                    if (i % BATCH_SIZE == 0) {
//                        ps.executeBatch();
//                    }
//                }
//                // if there are more than BATCH_SIZE products
//                if(PRODUCTS_NUMBER % BATCH_SIZE != 0) {
//                    ps.executeBatch();
//                }
//            }
//        }
//        var end = System.currentTimeMillis();
//        System.out.println("using batch it took: " + (end - start) + "ms"); // 292ms | 293ms


//        // without connection pool
//        initDatasource();
//        var start = System.currentTimeMillis();
//        for (int i = 1; i <= 1000; i++) {
//            try (Connection con = dataSource.getConnection()) {
//                // just open connection for each iteration
//            }
//        }
//        var end = System.currentTimeMillis();
//        System.out.println("it took: " + (end - start) + "ms"); // it took: 4195ms | 4235ms


        // with our connection pool
//        initializePooledCustomDataSource();
//        var start = System.currentTimeMillis();
//        for (int i = 1; i <= 1000; i++) {
//            try (Connection con = dataSource.getConnection()) {
//                // just open connection for each iteration
//            }
//        }
//        var end = System.currentTimeMillis();
//        System.out.println("it took: " + (end - start) + "ms"); // it took: 3ms | 2ms


        // with our connection pool
        initializeHikariCpDataSource();
        var start = System.currentTimeMillis();
        for (int i = 1; i <= 1000; i++) {
            try (Connection con = dataSource.getConnection()) {
                // just open connection for each iteration
            }
        }
        var end = System.currentTimeMillis();
        System.out.println("it took: " + (end - start) + "ms"); // it took: 10ms


    }

    // after adding Postgres driver as dependency we can use this as implementation of DataSource
//    private static PGSimpleDataSource initDatasource() {
//        PGSimpleDataSource dataSource = new PGSimpleDataSource();
//        dataSource.setURL("jdbc:postgresql://localhost:5432/pg-db-demo");
//        dataSource.setUser("postgres");
//        dataSource.setPassword("pass");
//
//        return dataSource;
//    }

    private static void initDatasource() {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL("jdbc:postgresql://localhost:5432/pg-db-demo");
        pgSimpleDataSource.setUser("postgres");
        pgSimpleDataSource.setPassword("pass");

        dataSource = pgSimpleDataSource;
    }

//    private static void initializePooledCustomDataSource() {
//        PooledDataSource pooledDataSource = new PooledDataSource(
//                "jdbc:postgresql://localhost:5432/pg-db-demo",
//                "postgres",
//                "pass"
//        );
//
//        dataSource = pooledDataSource;
//    }

    private static void initializeHikariCpDataSource() {
        // add Hikari dependency
        // set props (or read props from file)
        // create pooled datasource

        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/pg-db-demo");
        config.setUsername("postgres");
        config.setPassword("pass");

        dataSource = new HikariDataSource(config);
    }
}
