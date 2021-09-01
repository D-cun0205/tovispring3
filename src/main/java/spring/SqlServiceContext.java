package spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SqlServiceContext {

    @Bean
    public SqlService sqlService() {
        SimpleSqlService simpleSqlService = new SimpleSqlService();
        Map<String, String> map = new HashMap<>();
        map.put("userAdd", "INSERT INTO auser (id, name, password, level, login, recommend) VALUES (?, ?, ?, ?, ?, ?)");
        map.put("userGet", "SELECT * FROM auser WHERE id = ?");
        map.put("userDel", "DELETE FROM auser");
        map.put("userCnt", "SELECT COUNT(*) FROM auser");
        map.put("userGetAll", "SELECT * FROM auser ORDER BY id");
        map.put("userUpd", "UPDATE auser SET name = ?, password = ?, level = ?, login = ?, recommend = ? WHERE id = ?");
        simpleSqlService.setSqlMap(map);
        return simpleSqlService;
    }
}
