package com.ocwen.escalation.repositories;

public class ErmAgent {
	
	private String ntId;
	private String agentName;
	private String supervisor;
	private String eRole;
	
	public ErmAgent(String ntId, String agentName, String supervisor, String eRole) {
		this.ntId = ntId;
		this.agentName = agentName;
		this.supervisor = supervisor;
		this.eRole = eRole;
		
	}
	public String getNtId() {
		return ntId;
	}
	public void setNtId(String ntId) {
		this.ntId = ntId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String geteRole() {
		return eRole;
	}
	public void seteRole(String eRole) {
		this.eRole = eRole;
	}
	
	public int getRoleInt()
	{
		return (eRole.equals("Supervisor"))?1:2;
	}
	
}
