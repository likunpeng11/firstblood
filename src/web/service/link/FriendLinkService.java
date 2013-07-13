package web.service.link;

import java.util.List;

import com.lehecai.admin.web.bean.PageBean;
import com.lehecai.admin.web.domain.link.FriendLink;


public interface FriendLinkService {

	/**
	 * 查询所有友情链接
	 */
	List<FriendLink> findFriendLinkList(FriendLink friendLink, PageBean pageBean);
	
	/**
	 * 根据友情链接编号查询友情链接
	 * @param id 友情链接编号
	 */
	FriendLink get(Long id);
	
	/**
	 * 添加/修改友情链接
	 * @param friendlink 友情链接对象
	 */
	void merge(FriendLink friendLink);
	
	/**
	 * 删除友情链接
	 */
	void del(Long id);
}
