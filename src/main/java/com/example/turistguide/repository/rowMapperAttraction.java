package com.example.turistguide.repository;

import com.example.turistguide.model.TouristAttraction;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class rowMapperAttraction implements RowMapper {

    @Override
    public TouristAttraction mapRow(ResultSet rs, int rowNum) throws SQLException{
        return new TouristAttraction(
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("city"),
                List.of()
        );
    }
}
