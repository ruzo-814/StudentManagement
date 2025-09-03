package raisetech.Student.Management;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface StudentRepository {

  @Select("SELECT * FROM student")
  List<Student> findAll();

  @Insert("INSERT student values(#{name}, #{age}, #{gender})")
  void resisterStudent(String name, int age, String gender);

  @Update("UPDATE student SET age = COALESCE(#{age}, age), gender = COALESCE(#{gender}, gender) WHERE name = #{name}")
  void updateStudent(String name, Integer age, String gender);

  @Delete("DELETE FROM student WHERE name = #{name}")
  void deleteStudent(String name);

}