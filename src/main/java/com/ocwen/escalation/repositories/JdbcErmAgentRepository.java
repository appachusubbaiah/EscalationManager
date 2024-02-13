package com.ocwen.escalation.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcErmAgentRepository implements ErmAgentRepository {

	 @Autowired
	    private JdbcTemplate jdbcTemplate;
	 
	@Override
	public int count() {
		return jdbcTemplate
                .queryForObject("select count(*) from appachu_ermagents", Integer.class);
	}

	@Override
	public String getNameById(String id) {
		return jdbcTemplate.queryForObject(
                "select ntid from appachu_ermagents where ntid = ?",
                new Object[]{id},
                String.class
        );
	}

	@Override
	public int save(ErmAgent agent) {
		return jdbcTemplate.update(
                "insert into appachu_ermagents (ntid,agentname, supervisor,erole) values(?,?,?,?)",
                agent.getNtId(), agent.getAgentName(),agent.getSupervisor(),agent.getRoleInt());
	}

	@Override
	public int update(ErmAgent agent,String ntId) {
		return jdbcTemplate.update(
                "update appachu_ermagents set ntid=?, agentname = ?,supervisor = ?, erole = ? where ntid = ?",
                agent.getNtId(),agent.getAgentName(), agent.getSupervisor(),agent.getRoleInt(),ntId);
	}

	@Override
	public int delete(String ntid) {
		return jdbcTemplate.update(
                "delete appachu_ermagents where ntid = ?",
                ntid);
	}

	@Override
	public List<ErmAgent> findAll() {
		return  jdbcTemplate.query(
                "select a.ntid,a.agentname,a.supervisor,b.erole from appachu_ermagents a join appachu_ermroles b on a.erole=b.id",
                (rs, rowNum) ->
                        new ErmAgent(
                                rs.getString("NTID"),
                                rs.getString("AGENTNAME"),
                                rs.getString("SUPERVISOR"),
                                rs.getString("EROLE")
                         )
        );
	}

	@Override
	public List<ErmAgent> find(String ntid) {
		return  jdbcTemplate.query(
                "select a.ntid,a.agentname,a.supervisor,b.erole from appachu_ermagents a join appachu_ermroles b on a.erole=b.id where ntid = ?",
                new Object[]{ntid},
                (rs, rowNum) ->
                        new ErmAgent(
                                rs.getString("NTID"),
                                rs.getString("AGENTNAME"),
                                rs.getString("SUPERVISOR"),
                                rs.getString("EROLE")
                         )
        );
	}

	@Override
	public List<String> getSupervisorList() {
		return jdbcTemplate.queryForList(
                "select agentname from appachu_ermagents where erole=1",
                String.class);
	}

	
}
