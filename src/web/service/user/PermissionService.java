package web.service.user;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lehecai.admin.web.bean.PageBean;
import com.lehecai.admin.web.domain.user.Menu;
import com.lehecai.admin.web.domain.user.Permission;
import com.lehecai.admin.web.domain.user.PermissionItem;
import com.lehecai.admin.web.domain.user.Role;
import com.lehecai.admin.web.domain.user.RolePermission;
import com.lehecai.admin.web.domain.user.User;
import com.lehecai.admin.web.domain.user.UserRole;

public interface PermissionService {

	/*menu*/
	void manage(Menu menu);
	List<Menu> listMenus(Menu menu); 
	Menu getMenu(Long ID);
	void del(Menu menu);
	
	
	void manage(Permission permission);
	List<Permission> listPermissions(Permission permission); 
	Permission getPermission(Long ID);
	void del(Permission permission);
	
	void manage(PermissionItem permissionItem);
	List<PermissionItem> listPermissionItems(PermissionItem permissionItem); 
	PermissionItem getPermissionItem(Long ID);
	void del(PermissionItem permissionItem);
	List<PermissionItem> listPermissionItems(Permission permission);
	
	void manage(RolePermission rolePremission);
	void manageBatch(List<RolePermission> rolePremissionList);
	void del(RolePermission rolePremission);
	void delBatch(List<RolePermission> rolePremissionList);
	List<RolePermission> getPermissionsByRole(Role role);
	
	void manage(Role role);
	void manage(Role role, List<RolePermission> rolePermissions);
	List<Role> listRoles(Role role); 
	Role getRole(Long ID);
	void del(Role role);
	
	void manage(UserRole userRole);
	void delUserRole(UserRole userRole);
	List<UserRole> getRolesByUser(User user);
	
	
	void manage(User user);
	List<User> list(String userName, String name, Long roleID, PageBean pageBean);
	PageBean getPageBean(String userName, String name, Long roleID, PageBean pageBean); 
	User getUser(Long ID);
	void delUser(User user);
	User getByUserName(String userName);
	User login(String userName, String password);
	Map<Long, User> userMapping();
	/**
	 * 封装多条件查询分页信息
	 * @param userName	用户名
	 * @param name	姓名
	 * @param beginDate	起始创建时间
	 * @param endDate 结束创建时间
	 * @param roleID 角色编号
	 * @param valid	是否有效
	 * @param pageBean
	 * @return
	 */
	PageBean getPageBean(String userName, String name, Date beginDate,
                         Date endDate, Long roleID, String valid, PageBean pageBean);
	/**
	 * 多条件分页查询用户
	 * @param userName	用户名
	 * @param name	姓名
	 * @param beginDate	起始创建时间
	 * @param endDate 结束创建时间
	 * @param roleID 角色编号
	 * @param valid	是否有效
	 * @param pageBean
	 * @return
	 */
	List<User> list(String userName, String name, Date beginDate,
                    Date endDate, Long roleID, String valid, PageBean pageBean);
	
	void manage(User user, UserRole userRole);
}