package ru.job4j;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.model.Task;

import java.util.List;

public class HibernateRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            var tasks = new Task();
            tasks.setDescription("Learn Hibernate");
            create(tasks, sf);
            System.out.println(tasks);
            tasks.setDescription("Learn Hibernate 5.");
            update(tasks, sf);
            System.out.println(tasks);
            Task rsl = findById(tasks.getId(), sf);
            System.out.println(rsl);
            delete(rsl.getId(), sf);
            List<Task> list = findAll(sf);
            for (Task it : list) {
                System.out.println(it);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static Task create(Task tasks, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(tasks);
        session.getTransaction().commit();
        session.close();
        return tasks;
    }

    public static void update(Task tasks, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(tasks);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Task tasks = new Task();
        tasks.setId(id);
        session.delete(tasks);
        session.getTransaction().commit();
        session.close();
    }

    public static List<Task> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<Task> result = session.createQuery("from Tasks", Task.class).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static Task findById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        Task result = session.get(Task.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}