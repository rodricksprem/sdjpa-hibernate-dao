package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDAO;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
class AuthorDAOIntegrationTest {
    @Autowired
    AuthorDAO authorDAO;
    @BeforeEach
    void setUp() {
    }

    @Test
    void getById() {
        Optional<Author> authorOptional=authorDAO.getById(1L);
        assertTrue(authorOptional.isPresent());
            assertEquals(authorOptional.get().getId(),1);
    }

    @Test
    void getByName() {
        Optional<Author> authorOptional=authorDAO.getByName("Eric","Evans");
        assertTrue(authorOptional.isPresent());
            assertEquals(authorOptional.get().getId(),2);
    }

    @Test
    void saveAuthor() {
        Author author = new Author("rodricks","premkumar");
        Optional<Author> authorOptional=authorDAO.saveAutor(author);
        assertTrue(authorOptional.isPresent());
        assertNotNull(author);
        assertNotNull(authorOptional.get().getId());

    }
    @Test
    void testUpdateAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");

        Optional<Author> saved = authorDAO.saveAutor(author);
        assertTrue(saved.isPresent());
        saved.get().setLastName("Thompson");
        Optional<Author> updated = authorDAO.updateAutor(saved.get());
        assertTrue(updated.isPresent());
        assertThat(updated.get().getLastName()).isEqualTo("Thompson");



    }

    @Test
    void testDeleteAuthor() {
        Author author = new Author();
        author.setFirstName("john");
        author.setLastName("t");
        Optional<Author> saved = authorDAO.saveAutor(author);
        assertTrue(saved.isPresent());
        authorDAO.deleteAuthorById(saved.get().getId());
        Optional<Author>  deleted = authorDAO.getById(saved.get().getId());
         assertThat(deleted).isEmpty();


    }
}