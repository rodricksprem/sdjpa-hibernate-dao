package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Optional;

/**
 * Created by jt on 8/28/21.
 */
@Component
public class AuthorDAOImpl implements AuthorDAO {
    @Autowired
    private final EntityManagerFactory entityManagerFactory;

    public AuthorDAOImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<Author> getById(Long id) {
        Author author = (this.getEntityManager().find(Author.class,id));
        if(author==null){
            return Optional.empty();
        }
        return Optional.of(author);
    }

    @Override
    public Optional<Author> getByName(String firstName, String lastName) {

        TypedQuery<Author> authorTypedQuery = this.getEntityManager().createQuery("select a from Author a where a.firstName=:firstName and a.lastName=:lastName",Author.class);

        authorTypedQuery.setParameter("firstName",firstName);
        authorTypedQuery.setParameter("lastName",lastName);

        Author author=authorTypedQuery.getSingleResult();
        if(author==null){
            return Optional.empty();
        }
        return Optional.of(author);
    }

    @Override
    public Optional<Author> saveAutor(Author author) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(author);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return Optional.of(author);
    }

    @Override
    public Optional<Author> updateAutor(Author author) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(author);
        entityManager.flush();

        entityManager.getTransaction().commit();
        return Optional.of(author);
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin();
        Author author=entityManager.find(Author.class,id);
        entityManager.remove(author);
        entityManager.flush();

        entityManager.getTransaction().commit();
    }

    private EntityManager getEntityManager(){
        return  entityManagerFactory.createEntityManager();
    }
}
