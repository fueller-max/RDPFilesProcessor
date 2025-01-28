package Utils;

import DataStorage.DBStorage.POJO.RDPEntry;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;


public class PostgresSQLDriver {

    StandardServiceRegistry registry;
    Metadata metadata;
    SessionFactory sessionFactory;


    public PostgresSQLDriver(String cfgFile){
        registry =  new StandardServiceRegistryBuilder().configure(cfgFile).build();
        metadata =  new MetadataSources(registry).getMetadataBuilder().build();
        sessionFactory = metadata.getSessionFactoryBuilder().build();

    }

    public void insertEntry(RDPEntry entry ){
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        session.persist(entry);
        t.commit();
       // sessionFactory.close();
        session.close();
    }

    public void insertBatchOfEntries( List<RDPEntry> entries ){
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();

        for (RDPEntry entry : entries){
            session.persist(entry);
        }

        t.commit();
        // sessionFactory.close();
        session.close();
    }

}
