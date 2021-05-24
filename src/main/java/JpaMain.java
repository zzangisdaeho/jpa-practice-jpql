import jpql.entity.Member;
import jpql.entity.Team;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{

            Team teamA = new Team();
            teamA.setName("팀A");
            Team teamB = new Team();
            teamB.setName("팀B");

            em.persist(teamA);
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            member1.setAge(1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            member2.setAge(2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            member3.setAge(3);

            em.persist(member1);
            em.persist(member2);
            em.persist(member3);

            em.flush();
            em.clear();

//            String query = "select m from Member m";
            // 회원1, 팀A(SQL)
            // 회원2, 팀A(1차 캐시)
            // 회원3, 팀B(SQL)

            // 회원100명 -> N + 1

//            String query = "select m from Member m join fetch m.team";
//
//            List<Member> result = em.createQuery(query, Member.class).getResultList();
//
//            System.out.println("---------------------------------");
//
//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
//
//            }


            // 여러 타입을 가져오는 방법
            Query queryObject = em.createQuery("select m.username, m.age from Member m");

            List resultList = queryObject.getResultList();

            for (Object o : resultList) {
                Object[] arrayO = (Object[]) o;
                System.out.println("username : " + arrayO[0]);
                System.out.println("age : " + arrayO[1]);
            }


            List<Object[]> resultList2 = queryObject.getResultList();

            for (Object[] objects : resultList2) {
                System.out.println("username : " + objects[0]);
                System.out.println("age : " + objects[1]);
            }
            // ---------



//            TypedQuery<Member> innerJoinQuery = em.createQuery("select m from Member m inner join m.team t", Member.class);
//            List<Member> resultList = innerJoinQuery.getResultList();

//            TypedQuery<Member> setaJoin = em.createQuery("select m from Member m ,m.team t where m.username = t.name", Member.class);
//            List<Member> resultList = setaJoin.getResultList();


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
