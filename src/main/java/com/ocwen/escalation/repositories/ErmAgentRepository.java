package com.ocwen.escalation.repositories;

import java.util.List;

public interface ErmAgentRepository {
	int count();
	int save(ErmAgent agent);
	int update(ErmAgent agent,String ntId);
	int delete(String ntid);
	List<ErmAgent> findAll();
	List<ErmAgent> find(String ntid);
	String getNameById(String id);
	List<String> getSupervisorList();
}
