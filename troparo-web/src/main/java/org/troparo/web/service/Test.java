package org.troparo.web.service;

import org.troparo.business.contract.MemberManager;
import org.troparo.business.impl.MemberManagerImpl;
import org.troparo.model.Member;

import java.util.Date;

public class Test {

    public static void main(String[] args) {
        MemberManager memberManager = new MemberManagerImpl();

        Member m = new Member();
        m.setLogin("test");
        m.setPassword("pepper");
        m.setFirstName("test");
        m.setLastName("test");
        m.setEmail("test@test.test");
        m.setDateJoin(new Date());
        System.out.println("member: "+m);
        memberManager.addMember(m);
        System.out.println("inserting member: "+memberManager.addMember(m));
    }
}
