package no.kristiania.db;

import no.kristiania.http.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DepartmentDao extends AbstractDao <Department> {
    public static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    public DepartmentDao(DataSource dataSource) {
        super(dataSource);
    }

    public void insert(Department department) throws SQLException {
        boolean insertOk = insert(department, "insert into department (name) values (?)");
        if(insertOk) logger.info("Department({}) successfully inserted into database", department.getName());
    }

    public List <Department> list() throws SQLException {
        return list("select * from department");
    }

    public Department retrieve(long id) throws SQLException {
        Department department = retrieve(id, "select * from department where id = ?");
        if(department == null){
            System.out.println("Department not found");
            return null;
        }
        return department;
    }

    public void delete(long id) throws SQLException {
        alter(id, null, "UPDATE member SET department_id = ? WHERE department_id = ?");
        delete(id, "DELETE FROM department WHERE id = ?");
    }

    @Override
    protected void setDataOnStatement(PreparedStatement statement, Department department) throws SQLException {
        statement.setString(1, department.getName());
    }

    @Override
    protected Department mapRow(ResultSet rs) throws SQLException {
        return new Department(
                rs.getLong("id"),
                rs.getString("name"));
    }
}
