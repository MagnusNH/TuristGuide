package com.example.turistguide.repository;


import com.example.turistguide.model.TouristAttraction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
import java.util.List;

@Repository
public class TouristRepository {

    private final JdbcTemplate jdbcTemplate;

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

        Integer attractionId = findCityIdByName(attraction.getName());

        for (String tag : attraction.getTags()){
            Integer tagId=findTagIdByDescription(tag);

            if (tagId!=null){
                jdbcTemplate.update( "INSERT INTO tourist_attraction_tags (tag_id, attraction_id) VALUES (?, ?)",
                        tagId,
                        attractionId
                );
            }
        }
    }


    public List<TouristAttraction> getAllAttractions() {
        String sql ="""
                SELECT ta.attraction_id, ta.name, ta.description, c.city_name
        FROM tourist_attraction ta
        LEFT JOIN cities c ON ta.city_id = c.city_id
        """;

        List<TouristAttraction> attractions = jdbcTemplate.query(sql, new rowMapperAttraction());

        for (TouristAttraction attraction : attractions) {
            attraction.setTags(getTagsByAttractionName(attraction.getName()));
        }

        return attractions;
    }


    public List<String> getTagsByAttractionName(String name) {
        String sql = """
            SELECT t.tag_description
            FROM tags t
            JOIN tourist_attraction_tags tat ON t.tag_id = tat.tag_id
            JOIN tourist_attraction ta ON tat.attraction_id = ta.attraction_id
            WHERE ta.name = ?
            """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> rs.getString("tag_description"),
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
        String getCities = "SELECT city_name FROM cities";

        return jdbcTemplate.query(
                getCities,
                (rs, rowNum) -> rs.getString("city_name")
        );
    }

    public List<String> getTags() {
        String getTags = "SELECT tag_description FROM tags";

        return jdbcTemplate.query(getTags,
                (rs, rowNum) -> rs.getString("tag_description"));
    }

    private Integer findCityIdByName(String cityName) {
        List<Integer> result = jdbcTemplate.query(
                "SELECT city_id FROM cities WHERE city_name = ?",
                (rs, rowNum) -> rs.getInt("city_id"),
                cityName
        );

        return result.isEmpty() ? null : result.get(0);
    }

    private Integer findTagIdByDescription(String tagDescription) {
        List<Integer> result = jdbcTemplate.query(
                "SELECT tag_id FROM tags WHERE tag_description = ?",
                (rs, rowNum) -> rs.getInt("tag_id"),
                tagDescription
        );

        return result.isEmpty() ? null : result.get(0);
    }
}