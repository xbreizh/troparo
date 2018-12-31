package org.troparo.business.contract;

import org.troparo.model.Member;

import java.util.HashMap;
import java.util.List;


public interface MemberManager {

  String addMember(Member member);
  List<Member> getMembers();
  Member getMemberById(int id);
  List<Member> getMembersByCriterias(HashMap<String, String> map);
  String updateMember(Member member);

  String remove(int id);


}