package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.Optional;
@Component
public class BookDAOImpl implements BookDAO{
    @Autowired
    private final EntityManagerFactory entityManagerFactory;
    public BookDAOImpl( EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public Optional<Book> getById(Long id) {
        Book book = (this.getEntityManager().find(Book.class,id));
        if(book==null){
            return Optional.empty();
        }
        return Optional.of(book);
    }


    @Override
    public Optional<Book> findBookByTitle(String title) {
        TypedQuery<Book> bookTypedQuery = this.getEntityManager().createQuery("select a from Book a where a.title=:title",Book.class);

        bookTypedQuery.setParameter("title",title);

        Book book=bookTypedQuery.getSingleResult();
        if(book==null){
            return Optional.empty();
        }
        return Optional.of(book);
    }

    @Override
    public Optional<Book> saveNewBook(Book book) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(book);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return Optional.of(book);
    }

    @Override
    public Optional<Book> updateBook(Book book) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(book);
        entityManager.flush();
        entityManager.getTransaction().commit();
        return Optional.of(book);
    }

    @Override
    public void deleteBookById(Long id) {
        EntityManager entityManager = this.getEntityManager();
        entityManager.getTransaction().begin();
        Book book = entityManager.find(Book.class,id);
        entityManager.remove(book);
        entityManager.flush();
        entityManager.getTransaction().commit();


    }
    private EntityManager getEntityManager() {
        return  entityManagerFactory.createEntityManager();
    }

}
