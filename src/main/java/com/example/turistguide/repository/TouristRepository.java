package com.example.turistguide.repository;


import com.example.turistguide.model.TouristAttraction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class TouristRepository {

    private final JdbcTemplate jdbcTemplate;
    private final List <String> ALL_TAGS = List.of("forlystelsespark", "familievenlig","kultur", "historie", "havet", "slot");
    private final List<String> ALL_CITIES =
            List.of("København", "Aarhus", "Odense", "Aalborg", "Esbjerg");

    public TouristRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate=jdbcTemplate;
    }

    public void addAttraction(TouristAttraction attraction) {
        String sqlAttraction = "INSERT INTO tourist_attraction (name, description, city) VALUES (?,?,?)";
        jdbcTemplate.update(sqlAttraction,
                attraction.getName(),
                attraction.getDescription(),
                attraction.getCity()
        );
    }


    public List<TouristAttraction> getAllAttractions() {
        String sql = "SELECT name, description, city FROM tourist_attraction";

        List<TouristAttraction> attractions = jdbcTemplate.query(sql, new rowMapperAttraction());

        for (TouristAttraction attraction : attractions){
            attraction.setTags(getTagsByAttractionName(attraction.getName()));
        }

        return attractions;
    }

 public TouristAttraction getAttractionByName(String name) {
        String sql = "SELECT name, description, city FROM tourist_attraction WHERE name = ?";

        List<TouristAttraction> result = jdbcTemplate.query(sql, new rowMapperAttraction(), name);

        if(result.isEmpty()){
            return null;
        }

        TouristAttraction attraction = result.get(0);
        attraction.setTags(getTagsByAttractionName(name));
        return attraction;
  }

    public List<String> getTagsByAttractionName(String name) {
        String sql = "SELECT tag FROM attraction_tag WHERE attraction_name = ?";

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("tag"),
                name
        );
    }

    public boolean updateAttraction(String name, TouristAttraction updatedAttraction) {
        String updateSql = "UPDATE tourist_attraction SET NAME = ?, description = ?, city = ? WHERE name = ?";

        int rows = jdbcTemplate.update(
                updateSql,
                updatedAttraction.getName(),
                updatedAttraction.getDescription(),
                updatedAttraction.getCity(),
                name
        );

        if (rows==0){
            return false;
        }

        String deleteSql = "DELETE FROM attraction_tag WHERE attraction_name = ?";
        jdbcTemplate.update(deleteSql,name);

        String insertTagSql = "INSERT INTO attraction_tag (attraction_name, tag) VALUES (?, ?)";
        for (String tag : updatedAttraction.getTags()){
            jdbcTemplate.update(insertTagSql, updatedAttraction.getName(), tag);
        }

        return true;
    }

    public boolean deleteAttraction(String name) {
        String deleteTagsSql = "DELETE FROM attraction_tag WHERE attraction_name = ?";
        String deleteAttractionSql = "DELETE FROM tourist_attraction WHERE name = ?";

        jdbcTemplate.update(deleteTagsSql, name);
        int rows = jdbcTemplate.update(deleteAttractionSql, name);

        return rows > 0;

    }


    public List<String> getCities() {
        return new ArrayList<>(ALL_CITIES);
    }

    public List<String> getTags() {
        return new ArrayList<>(ALL_TAGS);
    }
}