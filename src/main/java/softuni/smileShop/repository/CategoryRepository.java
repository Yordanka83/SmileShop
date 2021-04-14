package softuni.smileShop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.smileShop.model.entities.Category;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    @Query("SELECT c.name FROM Category c")
    List<String> allCategoryName();

}
