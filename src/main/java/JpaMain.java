import jpql.entity.Member;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            for (int i = 0; i < 100; i++){
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

//            TypedQuery<Member> innerJoinQuery = em.createQuery("select m from Member m inner join m.team t", Member.class);
//            List<Member> resultList = innerJoinQuery.getResultList();

            TypedQuery<Member> setaJoin = em.createQuery("select m from Member m ,m.team t where m.username = t.name", Member.class);
            List<Member> resultList = setaJoin.getResultList();


//            TypedQuery<Member> query = em.createQuery("select m from Member m where m.age = :memberage", Member.class);
//            query.setParameter("memberage", 10);
//            List<Member> resultList = query.getResultList();
//
//            for(Member one : resultList){
//                System.out.println(one.getClass().getMethod("getUsername").invoke(one));
//            }

            tx.commit();
        }catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();
    }
}
