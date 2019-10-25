package app.mapper;

import eel.annotation.orm.*;

/**
 * @author monetto
 */
@SqlMapper
public class SampleMapper {

    /** Common Select */
    @Sql(type = "SELECT", sql = "select * from eel_user;")
    private String sampleSelectAnnotationMapper;

    /** Common Update Omit Sql Value in Annotation */
    @Sql(type = "Update")
    private String sampleUpdateAnnotationMapper = "update eel_user set password = ${password} where name = ${name};";

    /** You Can Also Just Provide Sql */
    @Sql(sql = "delete from eel_user where name = ${name} and password = ${password};")
    private String sampleDeleteAnnotationMapper = "This value is overwritten;";

    /** Omit Annotation Value */
    @Sql
    private String sampleInsertAnnotationMapper = "INSERT INTO eel_user(name, password) VALUES (${name}, ${password})";

}
