package com.bjgl.web.action;


import com.bjgl.web.bean.PageBean;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class BaseAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 2489105406760450598L;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private String action = "handle";	
	private String forwardUrl = "";
	private String errorMessage;
	private String successMessage;
	@SuppressWarnings("unchecked")
	private Map session;
	
	private PageBean pageBean;
	private String pageString;
	private String simplePageString; //不带form的pageString,为支持多个分页标签使用
	
	protected String executeMethod(String method) throws Exception {
		logger.info("1 Enter Class : "+this.getClass());
		logger.info("2 Execute Method : "+method);
		Class<?>[] c = null;
		Method m = this.getClass().getMethod(method, c);
		Object[] o = null;
		String result = (String) m.invoke(this, o);
		logger.info("3 Return Result:"+result);
		logger.info("4 Exit Class : "+this.getClass());
		return result;
	}
	
	public String execute() {
		try {
			return this.executeMethod(this.getAction());
		} catch (Throwable e) {
			logger.error("ActionError",e);
			this.addActionError(this.getText("error.message"));
			return ERROR;
		}
	}
	
	protected String getContextPath() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return request.getContextPath();
	}
	
	protected String getContextURI() {
		HttpServletRequest request = ServletActionContext.getRequest();
		return StringUtils.substringAfter(request.getRequestURI(), this.getContextPath());
	}
	
	/**
	 * 默认的查询开始时间，加速查询，减少数据库占用
	 * @return
	 */
	protected Date getDefaultQueryBeginDate() {
		return this.getDefaultQueryBeginDate(-3);
	}

    protected Date getDefaultQueryBeginDate(int offsetDays) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, offsetDays);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    protected Date getLastDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
	
	public void writeRs(HttpServletResponse response, JSONObject rs){
		writeRs(response, rs.toString());
	}
	public void writeRs(HttpServletResponse response, JSONArray rs){
		writeRs(response, rs.toString());
	}
	public void writeRs(HttpServletResponse response, String rs){
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("text/html;charset=utf-8");
		try{
			response.getWriter().print(rs.toString());
			response.getWriter().close();
		}catch(IOException e){
			logger.error(e.getMessage(),e);
		}	
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getForwardUrl() {
		return forwardUrl;
	}
	
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}

	@SuppressWarnings("unchecked")
	public Map getSession() {
		return session;
	}

	@SuppressWarnings("unchecked")
	public void setSession(Map session) {
		this.session = session;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getSuccessMessage() {
		return successMessage;
	}

	public void setSuccessMessage(String successMessage) {
		this.successMessage = successMessage;
	}

	public PageBean getPageBean() {
		if(pageBean == null){
			pageBean = new PageBean();
		}
		return pageBean;
	}

	public void setPageBean(PageBean pageBean) {
		this.pageBean = pageBean;
	}

	public String getPageString() {
		return pageString;
	}

	public void setPageString(String pageString) {
		this.pageString = pageString;
	}

	public String getSimplePageString() {
		return simplePageString;
	}

	public void setSimplePageString(String simplePageString) {
		this.simplePageString = simplePageString;
	}
	
}