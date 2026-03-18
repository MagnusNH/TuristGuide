package com.example.turistguide.repository;


import com.example.turistguide.model.TouristAttraction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class TouristRepository {

    private final JdbcTemplate jdbcTemplate;

    public TouristRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addAttraction(TouristAttraction attraction) {
        Integer cityId = findCityIdByName(attraction.getCity());

        if (cityId == null) {
            throw new IllegalArgumentException("Byen er ikke fundet: " + attraction.getCity());
        }

        String sqlAttraction = "INSERT INTO tourist_attraction (name, description, city_id) VALUES (?,?,?)";
        jdbcTemplate.update(sqlAttraction,
                attraction.getName(),
                attraction.getDescription(),
                cityId
        );

        Integer attractionId = findAttractionIdByName(attraction.getName());

        if (attractionId == null) {
            throw new IllegalArgumentException("Attraktionen er oprettet, men attraction_id kunne ikke findes");
        }

        for (String tag : attraction.getTags()) {
            Integer tagId = findTagIdByDescription(tag);

            if (tagId != null) {
                jdbcTemplate.update("INSERT INTO tourist_attraction_tags (tag_id, attraction_id) VALUES (?, ?)",
                        tagId,
                        attractionId
                );
            }
        }
    }


    public List<TouristAttraction> getAllAttractions() {
        String sql = """
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
        Integer attractionId = findAttractionIdByName(name);

        if (attractionId == null) {
            throw new IllegalArgumentException("Der kunne ikke findes en attraktion med det id.");
        }

        Integer cityId = findCityIdByName(updatedAttraction.getCity());

        if (cityId == null) {
            throw new IllegalArgumentException("Byen blev ikke fundet: " + updatedAttraction.getCity());
        }

        String updateSql = """
                UPDATE tourist_attraction
                SET NAME = ?, description = ?, city_id = ?
                WHERE attraction_id = ?
                """;

        int rows = jdbcTemplate.update(
                updateSql,
                updatedAttraction.getName(),
                updatedAttraction.getDescription(),
                cityId,
                attractionId
        );

        if (rows == 0) {
            return false;
        }

        String deleteSql = """
                DELETE FROM tourist_attraction_tags 
                WHERE attraction_id = ?
                """;
        jdbcTemplate.update(deleteSql, attractionId);

        String insertTagSql = "INSERT INTO tourist_attraction_tags (tag_id, attraction_id) VALUES (?, ?)";
        for (String tag : updatedAttraction.getTags()) {
            Integer tagId = findTagIdByDescription(tag);
            if (tagId != null) {
                jdbcTemplate.update(insertTagSql, tagId, attractionId);
            }
        }

        return true;
    }

    public boolean deleteAttraction(String name) {
        Integer attractionId = findAttractionIdByName(name);

        if (attractionId == null) {
            return false;
        }

        String deleteTagsSql = """
                DELETE FROM tourist_attraction_tags
                        WHERE attraction_id = ?
                """;
        jdbcTemplate.update(deleteTagsSql, attractionId);

        String deleteAttractionSql = """
                DELETE FROM tourist_attraction 
                       WHERE attraction_id = ?
                """;
        int rows = jdbcTemplate.update(deleteAttractionSql, attractionId);

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

    private Integer findAttractionIdByName(String attractionName) {
        List<Integer> result = jdbcTemplate.query(
                "SELECT attraction_id FROM tourist_attraction WHERE name = ?",
                (rs, rowNum) -> rs.getInt("attraction_id"),
                attractionName
        );

        return result.isEmpty() ? null : result.get(0);
    }
}