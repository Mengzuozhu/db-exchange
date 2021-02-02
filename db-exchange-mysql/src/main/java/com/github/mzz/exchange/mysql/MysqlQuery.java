package com.github.mzz.exchange.mysql;

import lombok.*;

import javax.sql.DataSource;
import java.sql.Statement;

/**
 * @author mengzz
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class MysqlQuery {
    @NonNull
    private DataSource dataSource;
    @NonNull
    private String sql;
    @Builder.Default
    private Integer batchSize = Integer.MAX_VALUE;
    private Statement statement;
    @Builder.Default()
    private boolean autoClose = true;

}
