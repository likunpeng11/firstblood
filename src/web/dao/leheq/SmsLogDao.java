package web.dao.leheq;

import java.util.Date;
import java.util.List;

import com.lehecai.admin.web.bean.PageBean;
import com.lehecai.admin.web.domain.leheq.SmsLog;
import com.lehecai.core.YesNoStatus;

/**
 * 短信日志数据访问层
 * @author yanweijie
 *
 */
public interface SmsLogDao {

	/**
	 * 条件并分页查询短信日志
	 */
	List<SmsLog> findSmsLogList(String smsto, Date beginSendTime, Date endSendTime,
                                YesNoStatus result, PageBean pageBean);
	
	/**
	 * 查询短信日志详细信息
	 */
	SmsLog get(Integer id);
	
	/**
	 * 条件并分页查询短信日志分页
	 */
	PageBean getPageBean(String smsto, Date beginSendTime, Date endSendTime,
                         YesNoStatus result, PageBean pageBean);
}
