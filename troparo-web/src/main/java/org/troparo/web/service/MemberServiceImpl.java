package org.troparo.web.service;

import org.apache.log4j.Logger;
import org.troparo.business.contract.MemberManager;
import org.troparo.entities.member.*;
import org.troparo.model.Member;
import org.troparo.services.memberservice.BusinessException;
import org.troparo.services.memberservice.IMemberService;

import javax.inject.Inject;
import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.*;

@WebService(serviceName = "MemberService", endpointInterface = "org.troparo.services.memberservice.IMemberService",
        targetNamespace = "http://troparo.org/services/MemberService/", portName = "MemberServicePort", name = "MemberServiceImpl")
public class MemberServiceImpl implements IMemberService {
    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Inject
    private MemberManager memberManager;

    private String exception = "";
    private List<Member> memberList = new ArrayList<>();
    private MemberTypeOut memberTypeOut = null;
    private MemberTypeIn memberTypeIn = null;
    private MemberListType memberListType = new MemberListType();
    private Member member = null;

    // Create
    @Override
    public AddMemberResponseType addMember(AddMemberRequestType parameters) throws BusinessException {
        AddMemberResponseType ar = new AddMemberResponseType();
        ar.setReturn(true);
        memberTypeIn = parameters.getMemberTypeIn();
        convertMemberTypeInIntoMember();
        logger.info("memberManager: " + memberManager);
        exception = memberManager.addMember(member);
        if (!exception.equals("")) {
            logger.info(exception);
            throw new BusinessException(exception);
        }

        return ar;
    }
    // Converts Input into Member for business
    private void convertMemberTypeInIntoMember() {
        member = new Member();
        member.setLogin(memberTypeIn.getLogin().toUpperCase());
        member.setFirstName(memberTypeIn.getFirstName().toUpperCase());
        member.setLastName(memberTypeIn.getLastName().toUpperCase());
        member.setPassword(memberTypeIn.getPassword().toUpperCase());
        member.setEmail(memberTypeIn.getEmail().toUpperCase());
        logger.info("conversion memberType into member done");
    }

    // Converts Input into Member for business
    private void convertMemberTypeUpdateIntoMember(MemberTypeUpdate memberTypeUpdate) {
        member = new Member();
        member.setFirstName(memberTypeUpdate.getFirstName().toUpperCase());
        member.setPassword(memberTypeUpdate.getPassword().toUpperCase());
        member.setLastName(memberTypeUpdate.getLastName().toUpperCase());
        member.setEmail(memberTypeUpdate.getEmail().toUpperCase());
        logger.info("conversion memberTypeUpdate into member done");
    }
    // Update
    @Override
    public UpdateMemberResponseType updateMember(UpdateMemberRequestType parameters) throws BusinessException {
        UpdateMemberResponseType ar = new UpdateMemberResponseType();
        ar.setReturn(true);
        MemberTypeUpdate memberTypeUpdate = parameters.getMemberTypeUpdate();
        // update
        convertMemberTypeUpdateIntoMember(memberTypeUpdate);
        logger.info("memberManager: " + memberManager);
        exception = memberManager.updateMember(member);
        if (!exception.equals("")) {
            throw new BusinessException(exception);
        }

        return ar;
    }


    // Get One
    @Override
    public GetMemberByIdResponseType getMemberById(GetMemberByIdRequestType parameters) throws BusinessException {

        logger.info("new method added");
        GetMemberByIdResponseType rep = new GetMemberByIdResponseType();
        MemberTypeOut bt = new MemberTypeOut();
        Member member = memberManager.getMemberById(parameters.getReturn());
        if (member == null) {
            throw new BusinessException("no member found with that id");
        } else {
            bt.setId(member.getId());
            bt.setLogin(member.getLogin());
            bt.setFirstName(member.getFirstName());
            bt.setLastName(member.getLastName());
            bt.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());
            bt.setDateJoin(xmlCalendar);
            rep.setMemberTypeOut(bt);
        }
        return rep;
    }

    // Get All
    @Override
    public MemberListResponseType getAllMembers() throws BusinessException {
        memberList = memberManager.getMembers();
        logger.info("size list: " + memberList.size());

        MemberListResponseType memberListResponseType = new MemberListResponseType();

        convertMemberIntoMemberTypeOut();
        // add memberType to the movieListType

        memberListResponseType.setMemberListType(memberListType);
        return memberListResponseType;
    }


    // Get List By Criterias
    @Override
    public GetMemberByCriteriasResponseType getMemberByCriterias(GetMemberByCriteriasRequestType parameters) throws BusinessException {
        HashMap<String, String> map = new HashMap<>();
        MemberCriterias criterias = parameters.getMemberCriterias();
        map.put("Login", criterias.getLogin().toUpperCase());
        map.put("FirstName", criterias.getFirstName().toUpperCase());
        map.put("LastName", criterias.getLastName().toUpperCase());
        map.put("Email", criterias.getEmail().toUpperCase());
        logger.info("map: " + map);
        
        memberList = memberManager.getMembersByCriterias(map);
        GetMemberByCriteriasResponseType brt = new GetMemberByCriteriasResponseType();
        logger.info("memberListType beg: " + memberListType.getMemberTypeOut().size());

        convertMemberIntoMemberTypeOut();
        
        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
        brt.setMemberListType(memberListType);
        return brt;
    }



    // Delete
    @Override
    public RemoveMemberResponseType removeMember(RemoveMemberRequestType parameters) throws BusinessException {
        RemoveMemberResponseType ar = new RemoveMemberResponseType();
        ar.setReturn(true);

        logger.info("memberManager: " + memberManager);
        exception = memberManager.remove(parameters.getId());
        if (!exception.equals("")) {
            throw new BusinessException(exception);
        }

        return ar;

    }



    // Converts Member from Business into output
    private void convertMemberIntoMemberTypeOut() {
        memberListType.getMemberTypeOut().clear();
        for (Member member : memberList) {

            // set values retrieved from DAO class
            memberTypeOut = new MemberTypeOut();
            memberTypeOut.setId(member.getId());
            memberTypeOut.setLogin(member.getLogin());
            memberTypeOut.setFirstName(member.getFirstName());
            memberTypeOut.setLastName(member.getLastName());
            memberTypeOut.setEmail(member.getEmail());
            XMLGregorianCalendar xmlCalendar = convertDateIntoXmlDate(member.getDateJoin());

            logger.info("new date: "+xmlCalendar);

            // converting xml into Date

          /*  XMLGregorianCalendar xcal = xmlCalendar;
            java.util.Date dt = xcal.toGregorianCalendar().getTime();*/


            memberTypeOut.setDateJoin(convertDateIntoXmlDate(member.getDateJoin()));

            memberListType.getMemberTypeOut().add(memberTypeOut);
        }
        logger.info("memberListType end: " + memberListType.getMemberTypeOut().size());
    }

    private XMLGregorianCalendar convertDateIntoXmlDate(Date date) {
        // converting Date into XML date

        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return xmlCalendar;
    }


}
