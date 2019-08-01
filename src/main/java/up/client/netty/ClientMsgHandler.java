package up.client.netty;
 
  
 
import java.text.DateFormat;
import java.text.SimpleDateFormat; 
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate; 
import org.springframework.stereotype.Component;

import com.application.CustomRedisSerializer;
import com.github.pagehelper.Page;
import com.google.protobuf.InvalidProtocolBufferException;
import com.networkcollect.NetworkCollect;
import com.networkcollect.NetworkCollect.MsgHeadType; 
  
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter; 
import up.common.entity.AssetsDeviceEntity;
import up.common.entity.BackupconfDateEntity;
import up.common.entity.BackupdataDateEntity;
import up.common.entity.BaselineEntity;
import up.common.entity.CertificateEntity;
import up.common.entity.ClientIP;
import up.common.entity.CollectEventAndDescribe;
import up.common.entity.DangerList;
import up.common.entity.LogsEntity;
import up.common.entity.PageEntity;
import up.common.entity.RoleEntity;
import up.common.entity.UploadEventAndDescribe;
import up.common.entity.UserEntity;
import up.common.entity.UserGroup;
import up.common.nettypojo.Message;
import up.common.tool.request.MessageHead;
import up.common.xmlentity.Platform;

/**
 * @ClassName: ClientMsgHandler
 * @Description: 客户端信息返回处理
 * @Author: chenjd
 * @Date: 2019-02-15 16:22
 **/ 
@Component
@Scope("prototype")
public class ClientMsgHandler extends ChannelInboundHandlerAdapter {
 
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired(required = false) 
	public void setRedisTemplate(RedisTemplate redisTemplate) {
		CustomRedisSerializer stringSerializer = new CustomRedisSerializer();
	    redisTemplate.setKeySerializer(stringSerializer);
	    redisTemplate.setValueSerializer(stringSerializer);
	    redisTemplate.setHashKeySerializer(stringSerializer);
	    redisTemplate.setHashValueSerializer(stringSerializer);
	    this.redisTemplate = redisTemplate;
	}
	
	
	
	public NetworkCollect.UserEntity.Builder getUserEntityBuilder(UserEntity userEntity){
		NetworkCollect.UserEntity.Builder userEntityBuilder = NetworkCollect.UserEntity.newBuilder(); 
		
		userEntityBuilder.setId(userEntity.getId());
		userEntityBuilder.setUsername(userEntity.getUsername());
		userEntityBuilder.setRealname(userEntity.getRealname());
		userEntityBuilder.setRoleid(userEntity.getRoleid());
		userEntityBuilder.setGroupid(userEntity.getGroupid());
		userEntityBuilder.setPassword(userEntity.getPassword());    
		
		String telnumber = userEntity.getTelnumber() == null ? "" : userEntity.getTelnumber();
		userEntityBuilder.setTelnumber(telnumber);
		String phonenumber = userEntity.getPhonenumber() == null ? "" : userEntity.getPhonenumber();
		userEntityBuilder.setPhonenumber(phonenumber);
		String email = userEntity.getEmail() == null ? "" : userEntity.getEmail();
		userEntityBuilder.setEmail(email);
		String description =  userEntity.getDescription() == null ? "" : userEntity.getDescription();
		userEntityBuilder.setDescription(description); 
		String ukey = userEntity.getUkey() == null ? "" : userEntity.getUkey();
		
		userEntityBuilder.setUkey(ukey);
		userEntityBuilder.setLocktime(userEntity.getLocktime());
		userEntityBuilder.setPasswordmodifytime(userEntity.getPasswordmodifytime());
		userEntityBuilder.setIfinner(userEntity.isIfinner());
		userEntityBuilder.setNotfirstlogin(userEntity.isNotfirstlogin());
		userEntityBuilder.setDelflag(userEntity.isDelflag()); 
		
		return userEntityBuilder;
	}
	
	public NetworkCollect.RoleEntity.Builder getRoleEntityBuilder(RoleEntity roleEntity) {
		NetworkCollect.RoleEntity.Builder roleEntityBuilder = NetworkCollect.RoleEntity.newBuilder();
		if (roleEntity == null) {
			return roleEntityBuilder;
		}
		roleEntityBuilder.setId(roleEntity.getId());
		roleEntityBuilder.setName(roleEntity.getName());
		roleEntityBuilder.setRoletype(roleEntity.getRoletype());
		roleEntityBuilder.setDescription(roleEntity.getDescription());
		roleEntityBuilder.setIfinner(roleEntity.isIfinner());
		roleEntityBuilder.setRunstatus(roleEntity.getRunstatus());
		roleEntityBuilder.setAssetsmanage(roleEntity.getAssetsmanage()); 
		roleEntityBuilder.setColinfo(roleEntity.getColinfo());
		roleEntityBuilder.setEvent(roleEntity.getEvent());
		roleEntityBuilder.setPrivilege(roleEntity.getPrivilege()); 
		roleEntityBuilder.setSyslog(roleEntity.getSyslog()); 
		roleEntityBuilder.setConfiguration(roleEntity.getConfiguration()); 
		roleEntityBuilder.setSystem(roleEntity.getSystem()); 
		roleEntityBuilder.setParadigm(roleEntity.getParadigm());
		roleEntityBuilder.setComm(roleEntity.getComm());  
		roleEntityBuilder.setTopo(roleEntity.getTopo()); 
		roleEntityBuilder.setNetflow(roleEntity.getNetflow());
		roleEntityBuilder.setTest(roleEntity.getTest()); 
		roleEntityBuilder.setBackup(roleEntity.getBackup()); 
		roleEntityBuilder.setBaselinkcheck(roleEntity.getBaselinkcheck()); 
		roleEntityBuilder.setSystemupdate(roleEntity.getSystemupdate());
		
		return roleEntityBuilder;
	}
	 
	public NetworkCollect.UserGroup.Builder getUserGroupBuilder(UserGroup userGroup){ 
		NetworkCollect.UserGroup.Builder userGroupBuilder = NetworkCollect.UserGroup.newBuilder(); 
		if(userGroup == null)
			return userGroupBuilder;
		
		userGroupBuilder.setId(userGroup.getId());
		userGroupBuilder.setGroupname(userGroup.getGroupname());
		userGroupBuilder.setGroupdescription(userGroup.getGroupdescription());
		userGroupBuilder.setIfinner(userGroup.getIfinner());  
		
		return userGroupBuilder;
	}
	public NetworkCollect.PageEntity.Builder getPageEntityBuilder(Page<?> page) { 
		NetworkCollect.PageEntity.Builder pageEntityBuilder = NetworkCollect.PageEntity.newBuilder(); 
		pageEntityBuilder.setPageNum(page.getPageNum());
		pageEntityBuilder.setTotal((int)page.getTotal());
		pageEntityBuilder.setPages(page.getPages());
		pageEntityBuilder.setPageSize(page.getPageSize()); 
		
		return pageEntityBuilder;
	}
	
	public NetworkCollect.PageEntity.Builder getPageEntityBuilder(PageEntity page) { 
		NetworkCollect.PageEntity.Builder pageEntityBuilder = NetworkCollect.PageEntity.newBuilder(); 
		pageEntityBuilder.setPageNum(page.getPageNum());
		pageEntityBuilder.setTotal(page.getTotal());
		pageEntityBuilder.setPages(page.getPages());
		pageEntityBuilder.setPageSize(page.getPageSize()); 
		
		return pageEntityBuilder;
	}
	
	public NetworkCollect.CollectEventAndDescribe.Builder getCollectEventAndDescribeBuilder(CollectEventAndDescribe collectEventAndDescribe) {
		NetworkCollect.CollectEventAndDescribe.Builder collectEventAndDescribeBuilder = NetworkCollect.CollectEventAndDescribe.newBuilder(); 
		collectEventAndDescribeBuilder.setId(collectEventAndDescribe.getId());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		collectEventAndDescribeBuilder.setCollecTime(dateFormat.format(collectEventAndDescribe.getCollecTime()));
		collectEventAndDescribeBuilder.setLevel(collectEventAndDescribe.getLevel());
		collectEventAndDescribeBuilder.setDeviceName(collectEventAndDescribe.getDeviceName());
		collectEventAndDescribeBuilder.setDeviceType(collectEventAndDescribe.getDeviceType());
		collectEventAndDescribeBuilder.setIp(collectEventAndDescribe.getIp());
		collectEventAndDescribeBuilder.setEventType(collectEventAndDescribe.getEventType());
		collectEventAndDescribeBuilder.setSubType(collectEventAndDescribe.getSubType()); 
		collectEventAndDescribeBuilder.setDesmap(collectEventAndDescribe.getDesmap()); 
		collectEventAndDescribeBuilder.setEventId(collectEventAndDescribe.getEventId());
		String identify = collectEventAndDescribe.getIdentify() == null ? "" : collectEventAndDescribe.getIdentify();
		collectEventAndDescribeBuilder.setIdentify(identify);
		collectEventAndDescribeBuilder.setContent(collectEventAndDescribe.getContent());
		collectEventAndDescribeBuilder.setEventdescribe(collectEventAndDescribe.getEventdescribe());
		collectEventAndDescribeBuilder.setContentdescribe(collectEventAndDescribe.getContentdescribe());  
		
		return collectEventAndDescribeBuilder;
	}
	
	public NetworkCollect.UploadEventAndDescribe.Builder getUploadEventAndDescribeBuilder(UploadEventAndDescribe uploadEventAndDescribe) {
		NetworkCollect.UploadEventAndDescribe.Builder uploadEventAndDescribeBuilder = NetworkCollect.UploadEventAndDescribe.newBuilder(); 
		
		uploadEventAndDescribeBuilder.setId(uploadEventAndDescribe.getId());
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		uploadEventAndDescribeBuilder.setUploadTime(dateFormat.format(uploadEventAndDescribe.getUploadTime()));
		uploadEventAndDescribeBuilder.setLevel(uploadEventAndDescribe.getLevel());
		uploadEventAndDescribeBuilder.setDeviceName(uploadEventAndDescribe.getDeviceName());
		uploadEventAndDescribeBuilder.setDeviceType(uploadEventAndDescribe.getDeviceType());
		uploadEventAndDescribeBuilder.setIp(uploadEventAndDescribe.getIp());
		uploadEventAndDescribeBuilder.setEventType(uploadEventAndDescribe.getEventType());
		uploadEventAndDescribeBuilder.setSubType(uploadEventAndDescribe.getSubType()); 
		uploadEventAndDescribeBuilder.setDesmap(uploadEventAndDescribe.getDesmap()); 
		uploadEventAndDescribeBuilder.setEventId(uploadEventAndDescribe.getEventId()); 
		
		String identify = uploadEventAndDescribe.getIdentify() == null ? "" : uploadEventAndDescribe.getIdentify();
		uploadEventAndDescribeBuilder.setIdentify(identify);
		
		uploadEventAndDescribeBuilder.setContent(uploadEventAndDescribe.getContent());
		
		int eventNumber = uploadEventAndDescribe.getEventNumber() == null ? 0 : uploadEventAndDescribe.getEventNumber();  
		uploadEventAndDescribeBuilder.setEventNumber(eventNumber);
		
		uploadEventAndDescribeBuilder.setRepeatNum(uploadEventAndDescribe.getRepeatNum());
		
		String eventdescribe = uploadEventAndDescribe.getEventdescribe() == null ? "" : uploadEventAndDescribe.getEventdescribe();
		uploadEventAndDescribeBuilder.setEventdescribe(eventdescribe);
		
		String contentdescribe = uploadEventAndDescribe.getContentdescribe() == null ? "" : uploadEventAndDescribe.getContentdescribe();
		uploadEventAndDescribeBuilder.setContentdescribe(contentdescribe);  
		
		return uploadEventAndDescribeBuilder;
	}
	public NetworkCollect.PlatformInfo.Builder getPlatformInfoBuilder(Platform platform) {
		
		NetworkCollect.PlatformInfo.Builder platformInfoBuilder = NetworkCollect.PlatformInfo.newBuilder();
		platformInfoBuilder.setId(platform.getId()); 
		platformInfoBuilder.setIpaddress(platform.getIpaddress());
		platformInfoBuilder.setEventport(platform.getEventport());
		platformInfoBuilder.setPermission(platform.getPermission()); 
		String publicKey = platform.getPublicKey() == null ? "" : platform.getPublicKey();
		platformInfoBuilder.setPublicKey(publicKey);
		String group = platform.getGroup() == null ? "" : platform.getGroup();
		platformInfoBuilder.setGroup(group);
		
		return platformInfoBuilder;
	}

	public NetworkCollect.CertificateEntity.Builder getCertificateEntityBuilder(CertificateEntity certificateEntity) { 
		NetworkCollect.CertificateEntity.Builder certificateEntityBuilder = NetworkCollect.CertificateEntity.newBuilder();
		
		certificateEntityBuilder.setId(certificateEntity.getId());
		certificateEntityBuilder.setIp(certificateEntity.getIp());
		certificateEntityBuilder.setType(certificateEntity.getType());
		certificateEntityBuilder.setName(certificateEntity.getName());
		certificateEntityBuilder.setFormat(certificateEntity.getFormat());
		certificateEntityBuilder.setValidityPeriod(certificateEntity.getValidityPeriod());
		certificateEntityBuilder.setSubject(certificateEntity.getSubject());
		certificateEntityBuilder.setIssue(certificateEntity.getIssue());
		certificateEntityBuilder.setContent(certificateEntity.getContent());
		certificateEntityBuilder.setOriginalContent(certificateEntity.getOriginalContent()); 
		
		return certificateEntityBuilder;
	}
	
	public NetworkCollect.LogsEntity.Builder getLogsEntityBuilder(LogsEntity logsEntity) {
		
		NetworkCollect.LogsEntity.Builder logsEntityBuilder = NetworkCollect.LogsEntity.newBuilder();
		logsEntityBuilder.setId(logsEntity.getId()); 
		logsEntityBuilder.setType(logsEntity.getType());  
		Integer UserId = logsEntity.getUserId() == null? -1 : logsEntity.getUserId();
		logsEntityBuilder.setUserId(UserId);
		logsEntityBuilder.setLevel(logsEntity.getLevel());  
		logsEntityBuilder.setTargetObj(logsEntity.getTargetObj());
		logsEntityBuilder.setActionDesc(logsEntity.getActionDesc()); 
		logsEntityBuilder.setActionTime(logsEntity.getActionTime());
		logsEntityBuilder.setIp(logsEntity.getIp());   
		logsEntityBuilder.setLastmodifytime(logsEntity.getLastmodifytime());
		
		return logsEntityBuilder;
	}
	
	
	public NetworkCollect.AssetsDeviceEntity.Builder getAssetsDeviceEntityBuilder(AssetsDeviceEntity assetsDeviceEntity){

		NetworkCollect.AssetsDeviceEntity.Builder assetsDeviceEntityBuilder = NetworkCollect.AssetsDeviceEntity.newBuilder(); 
		assetsDeviceEntityBuilder.setId(assetsDeviceEntity.getId());
		assetsDeviceEntityBuilder.setName(assetsDeviceEntity.getName());
		assetsDeviceEntityBuilder.setIp(assetsDeviceEntity.getIp());
		assetsDeviceEntityBuilder.setMac(assetsDeviceEntity.getMac());
		assetsDeviceEntityBuilder.setSerialnumber(assetsDeviceEntity.getSerialnumber());
		
		String factorydec = assetsDeviceEntity.getFactorydec() == null ? "" : assetsDeviceEntity.getFactorydec();
		assetsDeviceEntityBuilder.setFactorydec(factorydec); 
		
		String Factoryname = assetsDeviceEntity.getFactoryname() == null ? "" : assetsDeviceEntity.getFactoryname();
		assetsDeviceEntityBuilder.setFactoryname(Factoryname);
		
		String Iptwo = assetsDeviceEntity.getIptwo() == null ? "" : assetsDeviceEntity.getIptwo();
		assetsDeviceEntityBuilder.setIptwo(Iptwo);
		
		String Mactwo = assetsDeviceEntity.getMactwo() == null ? "" : assetsDeviceEntity.getMactwo(); 
		assetsDeviceEntityBuilder.setMactwo(Mactwo);
		
		assetsDeviceEntityBuilder.setDevicetype(assetsDeviceEntity.getDevicetype()); 
		
		String Publicname = assetsDeviceEntity.getPublicname() == null ? "" :assetsDeviceEntity.getPublicname();
		assetsDeviceEntityBuilder.setPublicname(Publicname);
		
		assetsDeviceEntityBuilder.setSystemVersion(assetsDeviceEntity.getSystemVersion());
		
		int getHold = assetsDeviceEntity.getHold() == null ? 0 : assetsDeviceEntity.getHold();
		assetsDeviceEntityBuilder.setHold(getHold);
		
		assetsDeviceEntityBuilder.setSnmpVersion(assetsDeviceEntity.getSnmpVersion());
		assetsDeviceEntityBuilder.setSnmpv3Username(assetsDeviceEntity.getSnmpv3Username());
		assetsDeviceEntityBuilder.setSnmpv3EncryptAlgo(assetsDeviceEntity.getSnmpv3EncryptAlgo());
		assetsDeviceEntityBuilder.setSnmpv3AuthAlgo(assetsDeviceEntity.getSnmpv3AuthAlgo());
		assetsDeviceEntityBuilder.setSnmpRead(assetsDeviceEntity.getSnmpRead());
		assetsDeviceEntityBuilder.setSnmpWrite(assetsDeviceEntity.getSnmpWrite());
		
		int Isonline =assetsDeviceEntity.getIsonline() == null ? 0 :assetsDeviceEntity.getIsonline();
		assetsDeviceEntityBuilder.setIsonline(Isonline);
		
		return assetsDeviceEntityBuilder;
	}
	
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws InvalidProtocolBufferException {
 
		// 处理数据返回
		Message message = (Message) msg;
		String head = message.getHead(); 
		Object object = message.getBody(); 
		
		if (head == null) {
			return;
		}
		System.out.println("channelRead:" + (Message) msg);
		NetworkCollect.MainMessage.Builder mainMessageBuilder = NetworkCollect.MainMessage.newBuilder();
		switch (head) {
		case MessageHead.userLogin: 
			Map<String, Object> mapUserLogin = (Map<String, Object>) object; 
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UserLogin); 
			NetworkCollect.UserLoginInfo.Builder userLoginInfoBuilder = NetworkCollect.UserLoginInfo.newBuilder(); 
			for (String key : mapUserLogin.keySet()) { 
				switch (key) {
				case "returnValue":
					String returnValue = (String) mapUserLogin.get(key);
					userLoginInfoBuilder.setReturnValue(returnValue);  
					System.out.println("用户登录返回:" + returnValue);
					break;
				case "user":
					UserEntity userEntity = (UserEntity) mapUserLogin.get(key);
					
					NetworkCollect.UserEntity.Builder userEntityBuilder = getUserEntityBuilder(userEntity);
					
					RoleEntity roleEntity = userEntity.getRoleEntity();
					 
					NetworkCollect.RoleEntity.Builder roleEntityBuilder = getRoleEntityBuilder(roleEntity);
					
					userEntityBuilder.setRoleEntity(roleEntityBuilder);
					
					userLoginInfoBuilder.setUserInfo(userEntityBuilder); 
					break;
				case "role":
					//RoleEntity roleEntity1 = (RoleEntity) mapUserLogin.get(key);  
					break;
				case "userPrivMap":
					Map<Integer,Integer> userPrivMap= (Map<Integer,Integer>) mapUserLogin.get(key);

					for(Entry<Integer, Integer> entry : userPrivMap.entrySet()){  
						NetworkCollect.UserPrivMap.Builder userPrivMapBuilder = NetworkCollect.UserPrivMap.newBuilder(); 
						userPrivMapBuilder.setFunc(entry.getKey());
						userPrivMapBuilder.setFlag(entry.getValue());
						
						userLoginInfoBuilder.addPrivInfo(userPrivMapBuilder);
					} 
					break;
				default:
					break;
				} 
			} 
			mainMessageBuilder.setUserLogin(userLoginInfoBuilder);
			break;
		case MessageHead.getUserByUserName:
			break;
		case MessageHead.getUserRolePriv:
			break;
		case MessageHead.getRoleById:
			break;
		case MessageHead.getAllUserGroup: 
			Map<String, Object> mapUserGroupEntity = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetAllUserGroup);
			NetworkCollect.GetAllUserGroup.Builder getAllUserGroupBuilder = NetworkCollect.GetAllUserGroup.newBuilder();
		  
			List<UserGroup> listAllUserGroup = (List<UserGroup>)mapUserGroupEntity.get("list");  
			
			for (int i = 0; i < listAllUserGroup.size(); i++) {
				UserGroup userGroup = listAllUserGroup.get(i);  
				
				NetworkCollect.UserGroup.Builder userGroupBuilder = getUserGroupBuilder(userGroup);
				
				List<UserEntity> listUserEntity = userGroup.getUserEntities();
				for (int j = 0; j < listUserEntity.size(); j++) {
					UserEntity userEntity = listUserEntity.get(j);  
					
					NetworkCollect.UserEntity.Builder userEntityBuilder = getUserEntityBuilder(userEntity);
					userGroupBuilder.addUserEntities(userEntityBuilder);
				}
				 
				getAllUserGroupBuilder.addAllUserGroup(userGroupBuilder); 
			} 
			System.out.println("获取所有用户组-完成");
			mainMessageBuilder.setGetAllUserGroup(getAllUserGroupBuilder);
			break;
		case MessageHead.getAllUser:
			Map<String, Object> mapUserEntity = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetAllUser); 
			NetworkCollect.GetAllUser.Builder getAllUserBuilder = NetworkCollect.GetAllUser.newBuilder();
	 
			for (Entry<String, Object> entry : mapUserEntity.entrySet()) { 
				
				if(entry.getKey().compareTo("page") == 0){
					Page<?> page = (Page<?>)entry.getValue();
					
					NetworkCollect.PageEntity.Builder pageEntityBuilder = getPageEntityBuilder(page);
					
					getAllUserBuilder.setPageInfo(pageEntityBuilder);
				}else if(entry.getKey().compareTo("list") == 0){
					List<UserEntity> listUserEntity = (List<UserEntity>)entry.getValue();
					
					for (int i = 0; i < listUserEntity.size(); i++) {
						UserEntity userEntity = listUserEntity.get(i);
						
						NetworkCollect.UserEntity.Builder userEntityBuilder = getUserEntityBuilder(userEntity);  
						 
						RoleEntity roleEntity = userEntity.getRoleEntity(); 
						NetworkCollect.RoleEntity.Builder roleEntityBuilder = getRoleEntityBuilder(roleEntity); 
						userEntityBuilder.setRoleEntity(roleEntityBuilder);

						UserGroup userGroup = userEntity.getUserGroup(); 
						NetworkCollect.UserGroup.Builder userGroupBuilder = getUserGroupBuilder(userGroup);
						userEntityBuilder.setUserGroup(userGroupBuilder);
						
						getAllUserBuilder.addUserEntities(userEntityBuilder);
					} 
				} 
			} 
			
			System.out.println("获取所有用户-完成");
			mainMessageBuilder.setGetAllUser(getAllUserBuilder);
			break;
		case MessageHead.deleteUser:
			Map<String,Object> mapDeleteRole = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteUser);
			NetworkCollect.DeleteUser.Builder deleteUserBuilder = NetworkCollect.DeleteUser.newBuilder();
			 
			deleteUserBuilder.setSize((int)mapDeleteRole.get("size"));
			System.out.println("删除用户个数:" + mapDeleteRole.get("size")); 
			mainMessageBuilder.setDeleteUser(deleteUserBuilder);
			break;
		case MessageHead.createUser:
			Map<String, Object> mapCreateUserResult = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_CreateUser);
			NetworkCollect.CreateUser.Builder createUserBuilder = NetworkCollect.CreateUser.newBuilder();
			int createUserResult = (int)mapCreateUserResult.get("num");
			createUserBuilder.setResult(createUserResult);
			
			System.out.println("新增用户结果:" + createUserResult);
			mainMessageBuilder.setCreateUser(createUserBuilder);
			break;
		case MessageHead.updateUser:
			Map<String, Object> mapUpdateUserResult = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UpdateUser);
			NetworkCollect.UpdateUser.Builder updateUserBuilder = NetworkCollect.UpdateUser.newBuilder();
			int updateUserResult = (int)mapUpdateUserResult.get("num");
			updateUserBuilder.setResult(updateUserResult);
			
			System.out.println("更新用户结果:" + updateUserResult);
			mainMessageBuilder.setUpdateUser(updateUserBuilder); 
			break; 
		case MessageHead.queryUser:
			break;
		case MessageHead.deleteUserGroup:
			int deleteUserGroupFlag = (int) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteUserGroup);
			NetworkCollect.DeleteUserGroup.Builder deleteUserGroupBuilder = NetworkCollect.DeleteUserGroup.newBuilder();
			deleteUserGroupBuilder.setFlag(deleteUserGroupFlag); 
			
			System.out.println("删除用户组结果:" + deleteUserGroupFlag);
			mainMessageBuilder.setDeleteUserGroup(deleteUserGroupBuilder); 
			break;
		case MessageHead.createUserGroup:
			Map<String, Object> mapCreateUserGroup = (Map<String,Object>) object;
			 
			mainMessageBuilder.setMsgType(MsgHeadType.HT_CreateUserGroup);
			NetworkCollect.CreateUserGroup.Builder createUserGroupBuilder = NetworkCollect.CreateUserGroup.newBuilder();
			createUserGroupBuilder.setFlag((int)mapCreateUserGroup.get("flag"));
			
			UserGroup createUserGroup = (UserGroup)mapCreateUserGroup.get("entity");
			NetworkCollect.UserGroup.Builder userGroupBuilder = getUserGroupBuilder(createUserGroup);
			
			createUserGroupBuilder.setGroupInfo(userGroupBuilder);
			
			System.out.println("新增用户组名:" + userGroupBuilder.getGroupname() +  "新增用户组结果:" + mapCreateUserGroup.get("flag")); 
			mainMessageBuilder.setCreateUserGroup(createUserGroupBuilder); 
			break;
		case MessageHead.updateUserGroup:
			Map<String,Object> mapUpdateUserGroup = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UpdateUserGroup);
			NetworkCollect.UpdateUserGroup.Builder updateUserGroupBuilder = NetworkCollect.UpdateUserGroup.newBuilder();
			updateUserGroupBuilder.setFlag((int)mapUpdateUserGroup.get("flag"));
			
			System.out.println("更新用户组结果:" + mapUpdateUserGroup.get("flag"));
			mainMessageBuilder.setUpdateUserGroup(updateUserGroupBuilder); 
			break;
		case MessageHead.getUserGroupByName:
			break;
		case MessageHead.confirmAuthen:
			break;
		case MessageHead.unlockUser:
			Map<String, Object> mapUnlockUser = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UnlockUser);
			
			NetworkCollect.UnlockUser.Builder unlockUserBuilder = NetworkCollect.UnlockUser.newBuilder();

			Boolean unlockUserFlag = (Boolean) mapUnlockUser.get("flag");
			unlockUserBuilder.setFlag(unlockUserFlag);
			
			mainMessageBuilder.setUnlockUser(unlockUserBuilder);
			
			System.out.println("解锁用户结果:" + unlockUserFlag);
			break;
		case MessageHead.initCreateUser:
			Map<String, Object> mapInitCreateUser = (Map<String, Object>) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_InitCreateUser);
			
			NetworkCollect.InitCreateUser.Builder initCreateUserBuilder = NetworkCollect.InitCreateUser.newBuilder();
			  
			List<RoleEntity> listRoleEntity = (List<RoleEntity>)mapInitCreateUser.get("role");
			for (int i = 0; i < listRoleEntity.size(); i++) {
				RoleEntity roleEntity = listRoleEntity.get(i); 
				NetworkCollect.RoleEntity.Builder roleEntityBuilder = getRoleEntityBuilder(roleEntity);
				initCreateUserBuilder.addRoleInfo(roleEntityBuilder);
			}
			
			List<UserGroup> listUserGroup = (List<UserGroup>)mapInitCreateUser.get("userGroup");
			for (int i = 0; i < listUserGroup.size(); i++) {
				UserGroup userGroup = listUserGroup.get(i);
				NetworkCollect.UserGroup.Builder initCreateUserGroupBuilder = getUserGroupBuilder(userGroup);
				initCreateUserBuilder.addGroupInfo(initCreateUserGroupBuilder);
			}
			System.out.println("初始化用户新增/编辑界面-完成");
			mainMessageBuilder.setInitCreateUser(initCreateUserBuilder);
			break;
		case MessageHead.initCreateRole:
			Map<String,Object> mapInitCreateRole = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_InitCreateRole);
			NetworkCollect.InitCreateRole.Builder initCreateRoleBuilder = NetworkCollect.InitCreateRole.newBuilder(); 
			
			List<UserGroup> listInitCreateRoleUserGroup = (List<UserGroup>)mapInitCreateRole.get("userGroup"); 
			for (int i = 0; i < listInitCreateRoleUserGroup.size(); i++) {
				UserGroup userGroup = listInitCreateRoleUserGroup.get(i);
				
				NetworkCollect.UserGroup.Builder initCreateRoleUserGroupBuilder =  getUserGroupBuilder(userGroup);
				List<UserEntity> listUserEntity =  userGroup.getUserEntities();
				for (int j = 0; j < listUserEntity.size(); j++) {
					UserEntity userEntity = listUserEntity.get(j);
					
					NetworkCollect.UserEntity.Builder userEntityBuilder = NetworkCollect.UserEntity.newBuilder();
					userEntityBuilder = getUserEntityBuilder(userEntity);
					initCreateRoleUserGroupBuilder.addUserEntities(userEntityBuilder);
				}

				initCreateRoleBuilder.addAllUserGroup(initCreateRoleUserGroupBuilder);  
			}
			
			System.out.println("初始化角色创建/编辑界面-完成");
			mainMessageBuilder.setInitCreateRole(initCreateRoleBuilder); 
			break;
		case MessageHead.initAssociateUsers:
			break;
		case MessageHead.updateUsersRoleId:
			boolean updateUsersRoleIdResult = (boolean) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UpdateUsersRoleId);
			NetworkCollect.UpdateUsersRoleId.Builder updateUsersRoleIdBuilder = NetworkCollect.UpdateUsersRoleId.newBuilder();
			updateUsersRoleIdBuilder.setResult(updateUsersRoleIdResult);

			System.out.println("更新用户与角色的关系结果:" + updateUsersRoleIdResult);
			mainMessageBuilder.setUpdateUsersRoleId(updateUsersRoleIdBuilder);
			break;
		case MessageHead.modifyPasswordByAdmin:
			Map<String, Object> mapModifyPasswordByAdminResult = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyPasswordByAdmin);
			NetworkCollect.ModifyPasswordByAdmin.Builder modifyPasswordByAdminBuilder = NetworkCollect.ModifyPasswordByAdmin.newBuilder();
			
			int modifyPasswordByAdminResult = (int)mapModifyPasswordByAdminResult.get("flag");
			modifyPasswordByAdminBuilder.setResult(modifyPasswordByAdminResult);
			System.out.println("管理员修改密码结果:" + modifyPasswordByAdminResult);
			mainMessageBuilder.setModifyPasswordByAdmin(modifyPasswordByAdminBuilder);
			break;
		case MessageHead.modifyPassword: 
			Map<String, Object> modifyPasswordResult = (Map<String, Object>) object;
			
			System.out.println(modifyPasswordResult);
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyPassword);

			NetworkCollect.ModifyPassword.Builder modifyPasswordBuilder = NetworkCollect.ModifyPassword.newBuilder();
			
			modifyPasswordBuilder.setResult((int)modifyPasswordResult.get("flag"));
			System.out.println("用户修改密码结果:" + (int)modifyPasswordResult.get("flag"));
			mainMessageBuilder.setModifyPassword(modifyPasswordBuilder);
			break; 
		case MessageHead.getLogs:
			Map<String, Object> mapLogsEntity = (Map<String, Object>) object; 
 
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetLogs);
			NetworkCollect.GetLogs.Builder getLogsBuilder = NetworkCollect.GetLogs.newBuilder(); 
			  
			Page<?> getLogsPage = (Page<?>)mapLogsEntity.get("logs");  
			NetworkCollect.PageEntity.Builder getLogsPageEntityBuilder = getPageEntityBuilder(getLogsPage); 
			getLogsBuilder.setPageInfo(getLogsPageEntityBuilder); 
			
			List<LogsEntity> listLogsEntity = (List<LogsEntity>)mapLogsEntity.get("logs");  
			for (int i = 0; i < listLogsEntity.size(); i++) { 
				LogsEntity logsEntity = listLogsEntity.get(i);

				NetworkCollect.LogsEntity.Builder logsEntityBuilder = getLogsEntityBuilder(logsEntity);
				
				getLogsBuilder.addLogInfoList(logsEntityBuilder);  
			}
			System.out.println("获取审计日志列表-完成");
			mainMessageBuilder.setGetLogs(getLogsBuilder);
			break;
		case MessageHead.exportLogs:
			Map<String, Object> mapExportLogs = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ExportLogs);
			NetworkCollect.ExportLogs.Builder exportLogsBuilder = NetworkCollect.ExportLogs.newBuilder();
			
			List<LogsEntity> listExportLogsEntity = (List<LogsEntity>)mapExportLogs.get("logs"); 
			System.out.println("listExportLogsEntity:" + listExportLogsEntity.size());
			for (int i = 0; i < listExportLogsEntity.size(); i++) {
				LogsEntity logsEntity = listExportLogsEntity.get(i);
				
				NetworkCollect.LogsEntity.Builder logsEntityBuilder = getLogsEntityBuilder(logsEntity);
				
				exportLogsBuilder.addLogInfoList(logsEntityBuilder);
			} 
			
			System.out.println("导出日志-完成");
			mainMessageBuilder.setExportLogs(exportLogsBuilder);
			break;
		case MessageHead.cleanOrRecoverLogs:
			break;
		case MessageHead.insertLog:
			int logNum = (int) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_InsertLog); 
			NetworkCollect.InsertLog.Builder insertLogBuilder = NetworkCollect.InsertLog.newBuilder();
			insertLogBuilder.setLogsNum(logNum);
			
			System.out.println("插入日志结果:" + logNum);
			mainMessageBuilder.setInsertLog(insertLogBuilder);
			break;
		case MessageHead.getIsCover:
			boolean getIsCover = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetIsCover);
			
			boolean isCover = getIsCover == true ? true : false;
			NetworkCollect.GetIsCover.Builder getIsCoverBuilder = NetworkCollect.GetIsCover.newBuilder();
			getIsCoverBuilder.setFlag(isCover);
			
			mainMessageBuilder.setGetIsCover(getIsCoverBuilder); 
			System.out.println("获取当前是否覆盖旧的审计记录:" + isCover);
			break;
		case MessageHead.setIsCoverLogs:
			Map<String, Object> mapSetIsCoverLogs = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_SetIsCoverLogs);
			NetworkCollect.SetIsCoverLogs.Builder setIsCoverLogsBuilder = NetworkCollect.SetIsCoverLogs.newBuilder();
			
			boolean covered = (boolean)mapSetIsCoverLogs.get("covered");
			setIsCoverLogsBuilder.setCovered(covered);
			
			boolean coveredResult =  mapSetIsCoverLogs.get("result") == null ? true : false;
			 
			setIsCoverLogsBuilder.setResult(coveredResult);
			System.out.println("设置当前是否覆盖旧的审计记录:" + covered);
			break;
		case MessageHead.checkLogsAccount:
			break;
		case MessageHead.backupLogsTable:
			break;
		case MessageHead.exportLogsChart:
			Map<String, Object> mapExportLogsChart = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ExportLogsChart);
			
			NetworkCollect.ExportLogsChart.Builder exportLogsChartBuilder = NetworkCollect.ExportLogsChart.newBuilder();
			
			Map<String, Integer> mapReportByType = (Map<String, Integer>)mapExportLogsChart.get("reportByType");
			for(Map.Entry<String, Integer> entry : mapReportByType.entrySet()){
				String type = entry.getKey();
				int num = entry.getValue(); 
				
				NetworkCollect.ReportTypeMatchNum.Builder reportTypeMatchNumBuilder = NetworkCollect.ReportTypeMatchNum.newBuilder();
				reportTypeMatchNumBuilder.setLogType(type);
				reportTypeMatchNumBuilder.setLogNum(num);
			
				exportLogsChartBuilder.addReportByType(reportTypeMatchNumBuilder);
			}
			
			Map<String, Map<String, Integer>> mapReportByLevelAndType = ( Map<String, Map<String, Integer>>)mapExportLogsChart.get("reportByLevelAndType");
			for(Map.Entry<String, Map<String, Integer>> entry : mapReportByLevelAndType.entrySet()){
				String Level = entry.getKey();
				Map<String, Integer> mapReportTypeMatchNum = entry.getValue();   
				
				NetworkCollect.ReportLevelAndTypeMatchNum.Builder reportLevelAndTypeMatchNumBuilder = NetworkCollect.ReportLevelAndTypeMatchNum.newBuilder();
				reportLevelAndTypeMatchNumBuilder.setLevel(Level);
				for(Map.Entry<String, Integer> entryReportByType : mapReportTypeMatchNum.entrySet()){
					String type = entryReportByType.getKey();
					int num = entryReportByType.getValue(); 
					
					NetworkCollect.ReportTypeMatchNum.Builder reportTypeMatchNumBuilder = NetworkCollect.ReportTypeMatchNum.newBuilder();
					reportTypeMatchNumBuilder.setLogType(type);
					reportTypeMatchNumBuilder.setLogNum(num);
				
					reportLevelAndTypeMatchNumBuilder.addLogNumList(reportTypeMatchNumBuilder);
				}
				
				exportLogsChartBuilder.addReportByLevelAndType(reportLevelAndTypeMatchNumBuilder);
			}
			mainMessageBuilder.setExportLogsChart(exportLogsChartBuilder);
			
			System.out.println("导出日志报表-完成");
			break;
		case MessageHead.getCollectNumber:
			break;
		case MessageHead.getCollectList:
			Map<String, Object> mapCollectList = (Map<String, Object>) object;
			System.out.println(mapCollectList);
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetCollectList);
			NetworkCollect.GetCollectList.Builder getCollectListBuilder = NetworkCollect.GetCollectList.newBuilder();
			
			Integer collectListCount = (Integer)mapCollectList.get("count"); 
			getCollectListBuilder.setCount(collectListCount); 
			
			List<CollectEventAndDescribe> listCollectEventAndDescribe = (List<CollectEventAndDescribe>)mapCollectList.get("collectEventList");
			System.out.println("listCollectEventAndDescribeSize:" + listCollectEventAndDescribe.size());
			
			for (int i = 0; i < listCollectEventAndDescribe.size(); i++) {
				CollectEventAndDescribe collectEventAndDescribe = listCollectEventAndDescribe.get(i);
				
				NetworkCollect.CollectEventAndDescribe.Builder collectEventAndDescribeBuilder = getCollectEventAndDescribeBuilder(collectEventAndDescribe);
				
				getCollectListBuilder.addCollectEventList(collectEventAndDescribeBuilder);
			} 

			System.out.println("获取采集信息列表-完成");
			mainMessageBuilder.setGetCollectList(getCollectListBuilder);
			break; 
		case MessageHead.getAllRole:
			Map<String, Object> mapAllRole = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetAllRole);
			NetworkCollect.GetAllRole.Builder getAllRoleBuilder = NetworkCollect.GetAllRole.newBuilder();  
			 
			Page<?> page = (Page<?>)mapAllRole.get("page");
			NetworkCollect.PageEntity.Builder getAllRolePageEntityBuilder = getPageEntityBuilder(page);
			
			getAllRoleBuilder.setPageInfo(getAllRolePageEntityBuilder);
	 
			List<RoleEntity> listAllRoleEntity = (List<RoleEntity>) mapAllRole.get("list");
			for (int i = 0; i < listAllRoleEntity.size(); i++) {
				RoleEntity roleEntity = listAllRoleEntity.get(i);
	 
				NetworkCollect.RoleEntity.Builder roleEntityBuilder = getRoleEntityBuilder(roleEntity);
		 
				List<UserEntity> listUserEntity = roleEntity.getUserEntities();
			 
				for (int j = 0; j < listUserEntity.size(); j++) {
					UserEntity userEntity = listUserEntity.get(j); 
					
					NetworkCollect.UserEntity.Builder userEntityBuilder = getUserEntityBuilder(userEntity);
					roleEntityBuilder.addUserEntities(userEntityBuilder);
				}
				 
				getAllRoleBuilder.addRoleEntities(roleEntityBuilder); 
			}  
			
			System.out.println("获取所有角色-完成");
			mainMessageBuilder.setGetAllRole(getAllRoleBuilder); 
			break;
		case MessageHead.createRole:
			int createRoleResult = (int) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_CreateRole);
			NetworkCollect.CreateRole.Builder createRoleBuilder = NetworkCollect.CreateRole.newBuilder();
			createRoleBuilder.setResult(createRoleResult);
			
			System.out.println("新增角色结果:" + createRoleResult);
			mainMessageBuilder.setCreateRole(createRoleBuilder);
			break;
		case MessageHead.updateRole:
			int updateRoleResult = (int) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UpdateRole);
			NetworkCollect.UpdateRole.Builder updateRoleBuilder = NetworkCollect.UpdateRole.newBuilder();
			updateRoleBuilder.setResult(updateRoleResult);
			
			System.out.println("更新角色结果:" + updateRoleResult);
			mainMessageBuilder.setUpdateRole(updateRoleBuilder);
			break;
		case MessageHead.deleteRole: 
			boolean deleteRoleResult = (boolean) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteRole);
			NetworkCollect.DeleteRole.Builder deleteRoleBuilder = NetworkCollect.DeleteRole.newBuilder();
			deleteRoleBuilder.setResult(deleteRoleResult);
			
			System.out.println("删除角色结果:" + deleteRoleResult);
			mainMessageBuilder.setDeleteRole(deleteRoleBuilder);
			break;
		case MessageHead.getRoleByName:
			break; 
		case MessageHead.systemUpdate:
			int systemUpdateResult = (int) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_SystemUpdate);
			
			NetworkCollect.SystemUpdate.Builder systemUpdateBuilder = NetworkCollect.SystemUpdate.newBuilder();
			systemUpdateBuilder.setCmdResultRet(systemUpdateResult);
			
			System.out.println("系统升级结果:" + systemUpdateResult);
			mainMessageBuilder.setSystemUpdate(systemUpdateBuilder);
			break;
		case MessageHead.restart:
			break; 
		case MessageHead.backupDatabase:
			boolean backupDatabaseResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_BackupDatabase);
			
			NetworkCollect.SystemDataBackup.Builder backupDatabaseBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			backupDatabaseBuilder.setResultRet(backupDatabaseResult);
			
			System.out.println("备份数据库结果:" + backupDatabaseResult);
			mainMessageBuilder.setSystemDataBackup(backupDatabaseBuilder); 
			break;
		case MessageHead.recoverDatabase:
			boolean recoverDatabaseResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_RecoverConfigure);
			
			NetworkCollect.SystemDataBackup.Builder recoverDatabaseBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			recoverDatabaseBuilder.setResultRet(recoverDatabaseResult);
			
			System.out.println("恢复数据库结果:" + recoverDatabaseResult);
			mainMessageBuilder.setSystemDataBackup(recoverDatabaseBuilder); 
			break;
		case MessageHead.backupConfigure:
			boolean backupConfigureResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_BackupConfigure);
			
			NetworkCollect.SystemDataBackup.Builder backupConfigureBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			backupConfigureBuilder.setResultRet(backupConfigureResult);
			
			System.out.println("备份配置文件结果:" + backupConfigureResult);
			mainMessageBuilder.setSystemDataBackup(backupConfigureBuilder);
			break;
		case MessageHead.recoverConfigure:
			boolean recoverConfigureResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_RecoverConfigure);
			
			NetworkCollect.SystemDataBackup.Builder recoverConfigureBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			recoverConfigureBuilder.setResultRet(recoverConfigureResult);
			
			System.out.println("恢复配置文件结果:" + recoverConfigureResult);
			mainMessageBuilder.setSystemDataBackup(recoverConfigureBuilder); 
			break;
		case MessageHead.deleteDataBackup:
			boolean deleteDataBackupResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteDataBackup);
			
			NetworkCollect.SystemDataBackup.Builder deleteDataBackupBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			deleteDataBackupBuilder.setResultRet(deleteDataBackupResult);
			
			System.out.println("删除数据库备份结果:" + deleteDataBackupResult);
			mainMessageBuilder.setSystemDataBackup(deleteDataBackupBuilder);
			break;
		case MessageHead.deleteConfBackup:
			boolean deleteConfBackupResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteConfBackup);
			
			NetworkCollect.SystemDataBackup.Builder deleteConfBackupBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			deleteConfBackupBuilder.setResultRet(deleteConfBackupResult);
			
			System.out.println("删除配置文件备份结果:" + deleteConfBackupResult);
			mainMessageBuilder.setSystemDataBackup(deleteConfBackupBuilder); 
			break;
		case MessageHead.getBackupDataDate:
			Map<String, Object> mapGetBackupDataDate = (Map<String, Object>)object;
			List<BackupdataDateEntity> listBackupdataDateEntity = (List<BackupdataDateEntity>) mapGetBackupDataDate.get("list");
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetBackupDataDate);
			NetworkCollect.SystemDataBackup.Builder systemDataBackupBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			
			for (int i = 0; i < listBackupdataDateEntity.size(); i++) {
				BackupdataDateEntity backupdataDateEntity = listBackupdataDateEntity.get(i);
				 
				NetworkCollect.BackupdataDateEntity.Builder backupdataDateEntityBuilder = NetworkCollect.BackupdataDateEntity.newBuilder(); 
				backupdataDateEntityBuilder.setId(backupdataDateEntity.getId());
				backupdataDateEntityBuilder.setCreateTime(backupdataDateEntity.getCreatetime());
				
				String date = backupdataDateEntity.getDate() == null ? "" : backupdataDateEntity.getDate();
				backupdataDateEntityBuilder.setDate(date); 
				
				systemDataBackupBuilder.addBackupdataDateEntitys(backupdataDateEntityBuilder);
			}
			
			mainMessageBuilder.setSystemDataBackup(systemDataBackupBuilder);
			break;
		case MessageHead.getBackupConfDate:
			Map<String, Object> mapGetBackupConfDate = (Map<String, Object>) object;
			
			List<BackupconfDateEntity> listBackupconfDateEntity = (List<BackupconfDateEntity>) mapGetBackupConfDate.get("list");
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetBackupConfDate);
			NetworkCollect.SystemDataBackup.Builder systemConfBackupBuilder = NetworkCollect.SystemDataBackup.newBuilder();
			
			for (int i = 0; i < listBackupconfDateEntity.size(); i++) {	
				BackupconfDateEntity backupconfDateEntity = listBackupconfDateEntity.get(i);
				 
				NetworkCollect.BackupconfDateEntity.Builder backupconfDateEntityBuilder = NetworkCollect.BackupconfDateEntity.newBuilder(); 
				backupconfDateEntityBuilder.setId(backupconfDateEntity.getId());
				backupconfDateEntityBuilder.setCreateTime(backupconfDateEntity.getCreateTime());
				backupconfDateEntityBuilder.setDate(backupconfDateEntity.getDate()); 
				
				systemConfBackupBuilder.addBackupconfDateEntitys(backupconfDateEntityBuilder);
			}
			System.out.println("获取配置文件备份信息-完成");
			mainMessageBuilder.setSystemDataBackup(systemConfBackupBuilder); 
			break;
		case MessageHead.initBackupPanel:
			break;

		case MessageHead.getSystemConfigs:
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetSystemConfigs);
			
			NetworkCollect.GetSystemConfigs.Builder getSystemConfigsBuilder = NetworkCollect.GetSystemConfigs.newBuilder();
			NetworkCollect.SystemConfigInfo.Builder systemConfigInfoBuilder = NetworkCollect.SystemConfigInfo.newBuilder();
			
			Map<String, String> mapSystemConfigs = (Map<String, String>) object;
			 
			systemConfigInfoBuilder.setLoginfailedwithin(mapSystemConfigs.get("loginfailedwithin")); 
			systemConfigInfoBuilder.setLoginfailedtime(mapSystemConfigs.get("loginfailedtime")); 
			systemConfigInfoBuilder.setPasswordvalidwarntime(mapSystemConfigs.get("passwordvalidwarntime")); 
			systemConfigInfoBuilder.setAccountlockedtime(mapSystemConfigs.get("accountlockedtime")); 
			systemConfigInfoBuilder.setPasswordvalidtime(mapSystemConfigs.get("passwordvalidtime")); 
			systemConfigInfoBuilder.setHistoricalrepetition(mapSystemConfigs.get("historicalrepetition")); 
			systemConfigInfoBuilder.setOperationloghold(mapSystemConfigs.get("operationloghold")); 
			systemConfigInfoBuilder.setMonitorloghold(mapSystemConfigs.get("monitorloghold")); 
			systemConfigInfoBuilder.setUploadloghold(mapSystemConfigs.get("uploadloghold")); 
			systemConfigInfoBuilder.setSnmptrapip(mapSystemConfigs.get("snmptrapip")); 
			 
			getSystemConfigsBuilder.setConfigInfo(systemConfigInfoBuilder);
			
			System.out.println("获取系统配置参数-完成");
			mainMessageBuilder.setGetSystemConfigs(getSystemConfigsBuilder); 
			break;
		case MessageHead.modifySystemConfigs:
			Map<String, Object> mapModifySystemConfigs = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifySystemConfigs);
			boolean modifySystemConfigsResult = (boolean)mapModifySystemConfigs.get("result");
			int modifySystemConfigsIdx = (int)mapModifySystemConfigs.get("idx");
			
			NetworkCollect.ModifySystemConfigs.Builder modifySystemConfigsBuilder = NetworkCollect.ModifySystemConfigs.newBuilder();
			modifySystemConfigsBuilder.setResult(modifySystemConfigsResult);
			modifySystemConfigsBuilder.setIdx(modifySystemConfigsIdx);
			
			System.out.println("修改系统配置参数结果:"+ modifySystemConfigsResult);
			mainMessageBuilder.setModifySystemConfigs(modifySystemConfigsBuilder);
			break;
		case MessageHead.getNetworkInterfaces: 
			Map<String, List<Map<String, String>>> mapNetworkInterfaces = (Map<String, List<Map<String, String>>>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetNetworkInterfaces);  
			NetworkCollect.GetNetworkInterfaces.Builder getNetworkInterfacesBuilder = NetworkCollect.GetNetworkInterfaces.newBuilder();
			
			System.out.println(mapNetworkInterfaces);
			
			for (Map.Entry<String, List<Map<String, String>>> entry : mapNetworkInterfaces.entrySet()) {  
			 
				if(entry.getKey().compareTo("list") == 0){
					List<Map<String, String>> listNetworkInterfaces = entry.getValue();
					 
					for (int i = 0; i < listNetworkInterfaces.size(); i++) {
						Map<String, String> mapNetworkInterfacesParas = listNetworkInterfaces.get(i);
			 
						NetworkCollect.NetworkConfigInfo.Builder networkConfigInfoBuilder = NetworkCollect.NetworkConfigInfo.newBuilder(); 
						networkConfigInfoBuilder.setName(mapNetworkInterfacesParas.get("interfacename")); 
						networkConfigInfoBuilder.setHostaddr(mapNetworkInterfacesParas.get("hostaddr"));  
						networkConfigInfoBuilder.setSubnetmask(mapNetworkInterfacesParas.get("subnetmask"));    
					
						getNetworkInterfacesBuilder.addConfigInfo(networkConfigInfoBuilder);
					} 
				}
			}
			
			System.out.println("获取网卡配置参数-完成");
			mainMessageBuilder.setGetNetworkInterfaces(getNetworkInterfacesBuilder);
			break;
		case MessageHead.modifyNetworkInterfaces:
			Map<String, Object> mapModifyNetworkInterfaces = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyNetworkInterfaces);
			NetworkCollect.ModifyNetworkInterfaces.Builder modifyNetworkInterfacesBuilder= NetworkCollect.ModifyNetworkInterfaces.newBuilder();
			modifyNetworkInterfacesBuilder.setResult((boolean)mapModifyNetworkInterfaces.get("result"));
			modifyNetworkInterfacesBuilder.setIdx((int)mapModifyNetworkInterfaces.get("idx"));
			
			System.out.println("修改网卡配置参数结果:" + mapModifyNetworkInterfaces.get("result"));
			mainMessageBuilder.setModifyNetworkInterfaces(modifyNetworkInterfacesBuilder);
			break;
		case MessageHead.getGateways:
			Map<String, List<Map<String, String>>> mapGateways = (Map<String, List<Map<String, String>>>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetGateways);
			NetworkCollect.GetGateways.Builder getGatewaysBuilder = NetworkCollect.GetGateways.newBuilder();
			 
			List<Map<String, String>> listGateways = mapGateways.get("list"); 
			for(int i = 0; i < listGateways.size(); ++i){
				Map<String, String> mapGatewaysParas = listGateways.get(i); 
				
				NetworkCollect.GatewaysInfo.Builder gatewaysInfoBuilder = NetworkCollect.GatewaysInfo.newBuilder();
				gatewaysInfoBuilder.setId(mapGatewaysParas.get("id")); 
				gatewaysInfoBuilder.setDestination(mapGatewaysParas.get("destination")); 
				gatewaysInfoBuilder.setMask(mapGatewaysParas.get("mask")); 
				gatewaysInfoBuilder.setGateway(mapGatewaysParas.get("gateway")); 
				gatewaysInfoBuilder.setNetwork(mapGatewaysParas.get("interfacename")); 
				gatewaysInfoBuilder.setFlags(mapGatewaysParas.get("flags")); 
				
				getGatewaysBuilder.addGatewaysInfoList(gatewaysInfoBuilder);
			} 

			System.out.println("获取路由配置参数-完成");
			mainMessageBuilder.setGetGateways(getGatewaysBuilder);
			break;
		case MessageHead.modifyGateways:
			Map<String, Object> mapModifyGateways = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyGateways);
			NetworkCollect.ModifyGateways.Builder modifyModifyGatewaysBuilder = NetworkCollect.ModifyGateways.newBuilder();
			modifyModifyGatewaysBuilder.setResult((boolean)mapModifyGateways.get("result"));
			modifyModifyGatewaysBuilder.setIdx((int)mapModifyGateways.get("idx"));
			
			System.out.println("修改路由配置参数结果:" + mapModifyGateways.get("result"));
			mainMessageBuilder.setModifyGateways(modifyModifyGatewaysBuilder);
			break;
		case MessageHead.getCommuConfigs:
			Map<String, Object> mapCommuConfigs = (Map<String, Object>) object;
	 
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetCommuConfigs);
			NetworkCollect.GetCommuConfigs.Builder getCommuConfigsBuilder = NetworkCollect.GetCommuConfigs.newBuilder();
			
			NetworkCollect.CommuConfigInfo.Builder commuConfigInfoBuilder = NetworkCollect.CommuConfigInfo.newBuilder();
			commuConfigInfoBuilder.setWorkstation((String)mapCommuConfigs.get("workstation")); 
			commuConfigInfoBuilder.setSecurity((String)mapCommuConfigs.get("security")); 
			commuConfigInfoBuilder.setAgent((String)mapCommuConfigs.get("agent"));
			commuConfigInfoBuilder.setNetwork((String)mapCommuConfigs.get("network"));
		 
			List<Platform> listPlatform = (List<Platform>)mapCommuConfigs.get("platforms");
			for(int i = 0; i < listPlatform.size(); ++i){
				Platform platform = listPlatform.get(i);
				
				NetworkCollect.PlatformInfo.Builder platformInfoBuilder = getPlatformInfoBuilder(platform);
				
				getCommuConfigsBuilder.addPlatInfo(platformInfoBuilder);
			}
			
			getCommuConfigsBuilder.setConfigInfo(commuConfigInfoBuilder); 

			System.out.println("获取通信配置参数-完成");
			mainMessageBuilder.setGetCommuConfigs(getCommuConfigsBuilder);
			break;
		case MessageHead.modifyCommuConfigs:
			Map<String, Object> mapModifyCommuConfigs = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyCommuConfigs);
			NetworkCollect.ModifyCommuConfigs.Builder modifyCommuConfigsBuilder = NetworkCollect.ModifyCommuConfigs.newBuilder();
			boolean result = (boolean)mapModifyCommuConfigs.get("result");
			int idx = (int) mapModifyCommuConfigs.get("idx");
			modifyCommuConfigsBuilder.setResult(result);
			modifyCommuConfigsBuilder.setIdx(idx);
			
			System.out.println("修改通信配置参数结果:" + result);
			mainMessageBuilder.setModifyCommuConfigs(modifyCommuConfigsBuilder);
			break;
		case MessageHead.getEventConfigs:
			Map<String, String> mapEventConfigs = (Map<String, String>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetEventConfigs);
			NetworkCollect.GetEventConfigs.Builder getEventConfigsBuilder = NetworkCollect.GetEventConfigs.newBuilder(); 
			
			NetworkCollect.EventConfigInfo.Builder eventConfigInfoBuilder = NetworkCollect.EventConfigInfo.newBuilder();
			eventConfigInfoBuilder.setCputhreshold(mapEventConfigs.get("cputhreshold"));
			eventConfigInfoBuilder.setMemorythreshold(mapEventConfigs.get("memorythreshold"));
			eventConfigInfoBuilder.setFlowthreshold(mapEventConfigs.get("flowthreshold"));
			eventConfigInfoBuilder.setMergeperiod(mapEventConfigs.get("mergeperiod"));
			eventConfigInfoBuilder.setLoginfailedtime(mapEventConfigs.get("loginfailedtime"));
			eventConfigInfoBuilder.setDiskthreshold(mapEventConfigs.get("diskthreshold"));
			eventConfigInfoBuilder.setUploaddividing(mapEventConfigs.get("uploaddividing"));
			String logsnumhold = mapEventConfigs.get("logsnumhold") == null? "" : mapEventConfigs.get("logsnumhold");
			eventConfigInfoBuilder.setLogsnumhold(logsnumhold);
			
			getEventConfigsBuilder.setConfigInfo(eventConfigInfoBuilder);
			mainMessageBuilder.setGetEventConfigs(getEventConfigsBuilder);
			System.out.println("获取事件配置参数-完成");
			break;
		case MessageHead.modifyEventConfigs:
			Map<String, Object> mapModifyEventConfigs = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyEventConfigs);
			NetworkCollect.ModifyEventConfigs.Builder modifyEventConfigsBuilder = NetworkCollect.ModifyEventConfigs.newBuilder();
			modifyEventConfigsBuilder.setResult((boolean)mapModifyEventConfigs.get("result"));
			modifyEventConfigsBuilder.setIdx((int)mapModifyEventConfigs.get("idx"));
			
			System.out.println("修改事件配置参数结果:" + mapModifyEventConfigs.get("result"));
			mainMessageBuilder.setModifyEventConfigs(modifyEventConfigsBuilder);
			break;
		case MessageHead.getNtpConfigs:
			Map<String, String> mapNtpConfigs = (Map<String, String>) object;

			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetNtpConfigs);
			NetworkCollect.GetNtpConfigs.Builder  getNtpConfigsBuilder = NetworkCollect.GetNtpConfigs.newBuilder();
	 
			NetworkCollect.NtpConfigInfo.Builder ntpConfigInfoBuilder = NetworkCollect.NtpConfigInfo.newBuilder(); 
			ntpConfigInfoBuilder.setBroadcast(mapNtpConfigs.get("broadcast"));
			ntpConfigInfoBuilder.setPort(mapNtpConfigs.get("port"));
			ntpConfigInfoBuilder.setAlternateNeta(mapNtpConfigs.get("alternate_neta"));
			ntpConfigInfoBuilder.setAlternateNetb(mapNtpConfigs.get("alternate_netb"));
			ntpConfigInfoBuilder.setMainNeta(mapNtpConfigs.get("main_neta"));
			ntpConfigInfoBuilder.setMainNetb(mapNtpConfigs.get("main_netb")); 
			ntpConfigInfoBuilder.setPeroid(mapNtpConfigs.get("peroid"));  
	 
			getNtpConfigsBuilder.setConfigInfo(ntpConfigInfoBuilder);
			
			System.out.println("获取NTP配置参数-完成");
			mainMessageBuilder.setGetNtpConfigs(getNtpConfigsBuilder);
			break;
		case MessageHead.modifyNtpConfigs:
			Map<String, Object> mapModifyNtpConfigs = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyNtpConfigs);
			NetworkCollect.ModifyNtpConfigs.Builder modifyNtpConfigsBuilder = NetworkCollect.ModifyNtpConfigs.newBuilder();
			modifyNtpConfigsBuilder.setResult((boolean)mapModifyNtpConfigs.get("result"));
			modifyNtpConfigsBuilder.setIdx((int)mapModifyNtpConfigs.get("idx"));
			
			System.out.println("修改NTP配置参数结果:" + mapModifyNtpConfigs.get("result"));
			mainMessageBuilder.setModifyNtpConfigs(modifyNtpConfigsBuilder);
			break;
		case MessageHead.getBackupConfigs:
			break;
		case MessageHead.modifyBackupConfigs:
			break;
		case MessageHead.modifyWhiteList:
			break;
		case MessageHead.addGateways:
			boolean addGatewaysResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_AddGateways);
			
			NetworkCollect.AddGateways.Builder addGatewaysBuilder = NetworkCollect.AddGateways.newBuilder();
			addGatewaysBuilder.setResult(addGatewaysResult);
			
			System.out.println("新增路由配置参数结果:" + addGatewaysResult);
			mainMessageBuilder.setAddGateways(addGatewaysBuilder); 
			break;
		case MessageHead.delGateways:
			boolean delGatewaysResult = (boolean) object;
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DelGateways);
			
			NetworkCollect.DelGateways.Builder delGatewaysBuilder = NetworkCollect.DelGateways.newBuilder();
			delGatewaysBuilder.setResult(delGatewaysResult);
			
			System.out.println("删除路由配置参数结果:" + delGatewaysResult);
			mainMessageBuilder.setDelGateways(delGatewaysBuilder); 
			break;
		case MessageHead.importCertificate:
			Map<String, Object> mapImportCertificate = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ImportCertificate);
			
			NetworkCollect.ImportCertificate.Builder importCertificateBuilder = NetworkCollect.ImportCertificate.newBuilder();
			int flag = (int)mapImportCertificate.get("flag");
			importCertificateBuilder.setFlag(flag);
			
			System.out.println("导入证书结果:" + flag);
			mainMessageBuilder.setImportCertificate(importCertificateBuilder);
			break;
		case MessageHead.exportCertificate:
			Map<String, Object> mapExportCertificate = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ExportCertificate);
			
			NetworkCollect.ExportCertificate.Builder exportCertificateBuilder = NetworkCollect.ExportCertificate.newBuilder();
			
			
			int exportCertificateFlag = (int)mapExportCertificate.get("flag");
			exportCertificateBuilder.setFlag(exportCertificateFlag);
			
			CertificateEntity exportCertificateEntity = (CertificateEntity)mapExportCertificate.get("entity");
			NetworkCollect.CertificateEntity.Builder exportCertificateEntityBuilder = getCertificateEntityBuilder(exportCertificateEntity);
			exportCertificateBuilder.setEntity(exportCertificateEntityBuilder);
			
			String exportCertificateFilePath = (String)mapExportCertificate.get("filePath");
			exportCertificateBuilder.setFilePath(exportCertificateFilePath);
			
			String exportCertificateFileName = (String)mapExportCertificate.get("fileName"); 
			exportCertificateBuilder.setFileName(exportCertificateFileName);
			
			System.out.println("导出证书结果:" + exportCertificateFlag);
			mainMessageBuilder.setExportCertificate(exportCertificateBuilder);
			break;
		case MessageHead.updateCertificate:
			Map<String, Object> mapUpdateCertificate = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UpdateCertificate);
			 
			NetworkCollect.UpdateCertificate.Builder updateCertificateBuilder = NetworkCollect.UpdateCertificate.newBuilder();
			int updateCertificateResult = (int)mapUpdateCertificate.get("flag");
			updateCertificateBuilder.setFlag(updateCertificateResult);
			
			System.out.println("更新证书结果:" + updateCertificateResult);
			mainMessageBuilder.setUpdateCertificate(updateCertificateBuilder);
			break;
		case MessageHead.deleteCertificate:
			Map<String, Object> mapDeleteCertificate = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteCertificate);
			
			NetworkCollect.DeleteCertificate.Builder deleteCertificateBuilder = NetworkCollect.DeleteCertificate.newBuilder();
			deleteCertificateBuilder.setFlag((int)mapDeleteCertificate.get("flag"));
			
			System.out.println("删除证书结果:" + (int)mapDeleteCertificate.get("flag"));
			mainMessageBuilder.setDeleteCertificate(deleteCertificateBuilder); 
			break;
		case MessageHead.initCertificatePanel:
			Map<String, Object> mapCertificatePanel = (Map<String, Object>) object; 

			mainMessageBuilder.setMsgType(MsgHeadType.HT_InitCertificatePanel);
			NetworkCollect.InitCertificatePanel.Builder initCertificatePanelBuilder = NetworkCollect.InitCertificatePanel.newBuilder(); 
			
			List<CertificateEntity> listCertificateEntity = (List<CertificateEntity>) mapCertificatePanel.get("certificates");
			for (int i = 0; i < listCertificateEntity.size(); i++) {
				CertificateEntity certificateEntity = listCertificateEntity.get(i);
 
				NetworkCollect.CertificateEntity.Builder certificateEntityBuilder = getCertificateEntityBuilder(certificateEntity);
				 
				initCertificatePanelBuilder.addCertificateInfo(certificateEntityBuilder);
			}
			
			NetworkCollect.CommuConfigInfo.Builder commuConfigInfoBuilder1 = NetworkCollect.CommuConfigInfo.newBuilder();
			Map<String, Object> mapCommuConfigs1 = (Map<String, Object>) mapCertificatePanel.get("communiConfig"); 
			List<Platform> listPlatform1 = (List<Platform>)mapCommuConfigs1.get("platforms");
			for (int i = 0; i < listPlatform1.size(); i++) {
				Platform platform = listPlatform1.get(i);
				NetworkCollect.PlatformInfo.Builder platformInfoBuilder = getPlatformInfoBuilder(platform);
				
				initCertificatePanelBuilder.addPlatInfo(platformInfoBuilder);
			}
			
			System.out.println("初始化证书页面-完成");
			mainMessageBuilder.setInitCertificatePanel(initCertificatePanelBuilder); 
			break;
		case MessageHead.initClientIPPanel:
			Map<String, List<ClientIP>> mapClientIP = (Map<String, List<ClientIP>>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_InitClientIPPanel);
			NetworkCollect.InitClientIPPanel.Builder initClientIPPanelBuilder = NetworkCollect.InitClientIPPanel.newBuilder();
			 
			Page<?> initClientIPPanelPage = (Page<?>)mapClientIP.get("page"); 
			NetworkCollect.PageEntity.Builder initClientIPPanelPageBuilder = NetworkCollect.PageEntity.newBuilder(); 
			initClientIPPanelPageBuilder.setPageNum(initClientIPPanelPage.getPageNum());
			initClientIPPanelPageBuilder.setTotal((int)initClientIPPanelPage.getTotal());
			initClientIPPanelPageBuilder.setPages(initClientIPPanelPage.getPages());
			initClientIPPanelPageBuilder.setPageSize(initClientIPPanelPage.getPageSize()); 
			
			initClientIPPanelBuilder.setPageInfo(initClientIPPanelPageBuilder);
			
			List<ClientIP> listClientIP = (List<ClientIP>)mapClientIP.get("list");
			for (int i = 0; i < listClientIP.size(); ++i) {
				ClientIP clientIP = listClientIP.get(i);
				
				NetworkCollect.ClientIP.Builder clientIPBuilder = NetworkCollect.ClientIP.newBuilder(); 
				clientIPBuilder.setId(clientIP.getId());
				clientIPBuilder.setIp(clientIP.getIp());
				clientIPBuilder.setCreatetime(clientIP.getCreatetime());  
				
				initClientIPPanelBuilder.addClientIpList(clientIPBuilder);
			} 
			System.out.println("客户端IP管理界面-完成");
			mainMessageBuilder.setInitClientIPPanel(initClientIPPanelBuilder);
			break;
		case MessageHead.addClientIP:
			Map<String, Object> mapAddClientIP = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_AddClientIP);
			NetworkCollect.AddClientIP.Builder addClientIPBuilder = NetworkCollect.AddClientIP.newBuilder();
			addClientIPBuilder.setIp((String)mapAddClientIP.get("ip"));
			addClientIPBuilder.setFlag((int)mapAddClientIP.get("flag")); 
			
			System.out.println("新增客户端IP结果:" + mapAddClientIP.get("flag"));
			mainMessageBuilder.setAddClientIP(addClientIPBuilder); 
			break;
		case MessageHead.delClientIP:
			Map<String, Object> mapDelClientIP = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DelClientIP);
			NetworkCollect.DelClientIP.Builder delClientIPBuilder = NetworkCollect.DelClientIP.newBuilder(); 
			delClientIPBuilder.setFlag((int)mapDelClientIP.get("flag"));  
			
			System.out.println("删除客户端IP结果:" + mapDelClientIP.get("flag"));
			mainMessageBuilder.setDelClientIP(delClientIPBuilder); 
			break;
		case MessageHead.getAllEventWarning:
			break;
		case MessageHead.deleteEventWarning:
			break;
		case MessageHead.insertEventWarning:
			break;
		case MessageHead.updateEventWarning:
			break;
		case MessageHead.queryEventWarning:
			break;
		case MessageHead.getDeviceInfoConfigs:
			Map<String, String> mapDeviceInfoConfigs = (Map<String, String>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetDeviceInfoConfigs); 
			NetworkCollect.GetDeviceInfoConfigs.Builder getDeviceInfoConfigsBuilder = NetworkCollect.GetDeviceInfoConfigs.newBuilder();
			
			NetworkCollect.DeviceInfoConfigInfo.Builder deviceInfoConfigInfoBuilder = NetworkCollect.DeviceInfoConfigInfo.newBuilder();
			deviceInfoConfigInfoBuilder.setNameAndVersion(mapDeviceInfoConfigs.get("devicename"));
			deviceInfoConfigInfoBuilder.setKeyWord(mapDeviceInfoConfigs.get("privateKey"));
			getDeviceInfoConfigsBuilder.setConfigInfo(deviceInfoConfigInfoBuilder);
			
			System.out.println("获取装置信息-完成");
			mainMessageBuilder.setGetDeviceInfoConfigs(getDeviceInfoConfigsBuilder);
			break;
		case MessageHead.modifyDeviceInfoConfigs:
			Map<String, Object> mapModifyDeviceInfoConfigs = (Map<String, Object>) object;
		
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyDeviceInfoConfigs);
			NetworkCollect.ModifyDeviceInfoConfigs.Builder modifyDeviceInfoConfigsBuilder = NetworkCollect.ModifyDeviceInfoConfigs.newBuilder();
			modifyDeviceInfoConfigsBuilder.setResult((boolean)mapModifyDeviceInfoConfigs.get("result"));
			modifyDeviceInfoConfigsBuilder.setIdx((int)mapModifyDeviceInfoConfigs.get("idx"));
			
			System.out.println("修改装置信息结果:" + mapModifyDeviceInfoConfigs.get("result"));
			mainMessageBuilder.setModifyDeviceInfoConfigs(modifyDeviceInfoConfigsBuilder);
			break; 
		case MessageHead.getDevices:
			Map<String, Object> mapAssetsDeviceEntity = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetDevices);
			NetworkCollect.GetDevices.Builder getDevicesBuilder = NetworkCollect.GetDevices.newBuilder();
			 
			Page<?> getDevicesPage = (Page<?>)mapAssetsDeviceEntity.get("page");
			NetworkCollect.PageEntity.Builder getDevicesPageEntity = getPageEntityBuilder(getDevicesPage);
			getDevicesBuilder.setPageInfo(getDevicesPageEntity);
			
			List<AssetsDeviceEntity> listAssetsDeviceEntity = (List<AssetsDeviceEntity>)mapAssetsDeviceEntity.get("devices");
			for (int i = 0; i < listAssetsDeviceEntity.size(); i++) {
				AssetsDeviceEntity assetsDeviceEntity = listAssetsDeviceEntity.get(i);
				
				NetworkCollect.AssetsDeviceEntity.Builder assetsDeviceEntityBuilder = getAssetsDeviceEntityBuilder(assetsDeviceEntity);
				
				getDevicesBuilder.addAssetInfo(assetsDeviceEntityBuilder);
			} 
			System.out.println("获取资产设备列表-完成");
			mainMessageBuilder.setGetDevices(getDevicesBuilder);
			break;
		case MessageHead.insertDevice:
			AssetsDeviceEntity assetsDeviceEntity = (AssetsDeviceEntity) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_InsertDevice); 
			NetworkCollect.InsertDevice.Builder insertDeviceBuilder = NetworkCollect.InsertDevice.newBuilder();
			NetworkCollect.AssetsDeviceEntity.Builder assetsDeviceEntityBuilder = getAssetsDeviceEntityBuilder(assetsDeviceEntity);
			insertDeviceBuilder.setAssetInfo(assetsDeviceEntityBuilder);
			
			System.out.println("添加资产设备-完成");
			mainMessageBuilder.setInsertDevice(insertDeviceBuilder);
			break;
		case MessageHead.deleteDevice:
			Map<String, String> MapdeleteDevice = (Map<String, String>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteDevice);
			
			NetworkCollect.DeleteDevice.Builder deleteDeviceBuilder = NetworkCollect.DeleteDevice.newBuilder();
			 
			deleteDeviceBuilder.setResult(MapdeleteDevice.get("flag"));
			
			System.out.println("删除资产设备结果:" + MapdeleteDevice.get("flag"));
			mainMessageBuilder.setDeleteDevice(deleteDeviceBuilder);
			break;
		case MessageHead.modifyDevice:
			Map<String, String> mapModifyDeviceResult = (Map<String, String>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_ModifyDevice);
			
			NetworkCollect.ModifyDevice.Builder modifyDeviceBuilder = NetworkCollect.ModifyDevice.newBuilder();
			modifyDeviceBuilder.setResult(mapModifyDeviceResult.get("flag"));
			
			System.out.println("更新资产设备结果:" + mapModifyDeviceResult.get("flag"));
			mainMessageBuilder.setModifyDevice(modifyDeviceBuilder);
			break;
		case MessageHead.selectDeviceById:
			break;
		case MessageHead.setIsWhite:
			break;
		case MessageHead.getIsWhite:
			break;
		case MessageHead.queryDeviceByIp:
			break;
		case MessageHead.getDevicesChartData:
			Map<String, Integer> mapGetDevicesChartData = (Map<String, Integer>) object;

			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetDevicesChartData);
			
			NetworkCollect.GetDevicesChartData.Builder getDevicesChartDataBuilder = NetworkCollect.GetDevicesChartData.newBuilder();
			for(Entry<String, Integer> entry : mapGetDevicesChartData.entrySet()){
				NetworkCollect.TypeMatchInfoNum.Builder typeMatchInfoNumBuilder = NetworkCollect.TypeMatchInfoNum.newBuilder();
				typeMatchInfoNumBuilder.setDeviceType(entry.getKey());
				typeMatchInfoNumBuilder.setInfoNum(entry.getValue());
				
				getDevicesChartDataBuilder.addAllAssets(typeMatchInfoNumBuilder);
			}
			
			System.out.println("按设备类型统计所有资产设备结果:" + mapGetDevicesChartData);
			mainMessageBuilder.setGetDevicesChartData(getDevicesChartDataBuilder); 
			break;
		case MessageHead.getOnlineDevicesChartData:
			Map<String, Integer> mapOnlineDevicesChartData = (Map<String, Integer>) object;

			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetOnlineDevicesChartData);
			
			NetworkCollect.GetDevicesChartData.Builder getOnlineDevicesChartDataBuilder = NetworkCollect.GetDevicesChartData.newBuilder();
			for(Entry<String, Integer> entry : mapOnlineDevicesChartData.entrySet()){
				NetworkCollect.TypeMatchInfoNum.Builder typeMatchInfoNumBuilder = NetworkCollect.TypeMatchInfoNum.newBuilder();
				typeMatchInfoNumBuilder.setDeviceType(entry.getKey());
				typeMatchInfoNumBuilder.setInfoNum(entry.getValue());
				
				getOnlineDevicesChartDataBuilder.addOnlineAssets(typeMatchInfoNumBuilder);
			}
			
			System.out.println("按设备类型统计在线资产设备:" + mapOnlineDevicesChartData);
			mainMessageBuilder.setGetDevicesChartData(getOnlineDevicesChartDataBuilder); 
			break;
		case MessageHead.getcategoryChartData:
			break;
		case MessageHead.setIsOnline:
			break;

		case MessageHead.getCertificateById:
			break;
		case MessageHead.delCertificateById:
			break;
		case MessageHead.importCertificateWithIp:
			break;
		case MessageHead.getCertificates:
			break;
		case MessageHead.getUploadList: 
			Map<String, Object> mapUploadList = (Map<String, Object>) object;  
			 
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetUploadList);
			NetworkCollect.GetUploadList.Builder getUploadListBuilder = NetworkCollect.GetUploadList.newBuilder();
			 
			Integer uploadCount = (Integer)mapUploadList.get("count"); 
			getUploadListBuilder.setCount(uploadCount); 
			
			List<UploadEventAndDescribe> listUploadEventAndDescribe = (List<UploadEventAndDescribe>)mapUploadList.get("uploadEventList");
			for (int i = 0; i < listUploadEventAndDescribe.size(); i++) {
				UploadEventAndDescribe uploadEventAndDescribe = listUploadEventAndDescribe.get(i);
				NetworkCollect.UploadEventAndDescribe.Builder uploadEventAndDescribeBuilder = getUploadEventAndDescribeBuilder(uploadEventAndDescribe);
				
				getUploadListBuilder.addUploadEventList(uploadEventAndDescribeBuilder);
			}
	 
			System.out.println("获取上传事件列表-完成");
			mainMessageBuilder.setGetUploadList(getUploadListBuilder); 
			break;
		case MessageHead.getSVRDevice: 
			Map<String, Object> mapGetSVRDevice = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetSVRDevice);
			NetworkCollect.GetSvrDevices.Builder getSVRDevicesBuilder = NetworkCollect.GetSvrDevices.newBuilder();
			 
			Page<?> getSVRDevicePage = (Page<?>)mapGetSVRDevice.get("page");
			NetworkCollect.PageEntity.Builder getSVRDevicePageEntity = getPageEntityBuilder(getSVRDevicePage);
			getSVRDevicesBuilder.setPageInfo(getSVRDevicePageEntity);
			
			List<AssetsDeviceEntity> listSVRDeviceEntity = (List<AssetsDeviceEntity>)mapGetSVRDevice.get("list");
	 
			for (int i = 0; i < listSVRDeviceEntity.size(); i++) {
				AssetsDeviceEntity SVRDeviceEntity = listSVRDeviceEntity.get(i);
				
				NetworkCollect.AssetsDeviceEntity.Builder SVRDeviceEntityBuilder = getAssetsDeviceEntityBuilder(SVRDeviceEntity);
				
				getSVRDevicesBuilder.addAssetInfo(SVRDeviceEntityBuilder);
			} 
			System.out.println("获取服务器类的资产-完成");
			mainMessageBuilder.setGetSvrDevices(getSVRDevicesBuilder);
			break;
		case MessageHead.startBaseLineCheck:
			Map<String,Object> mapStartBaseLineCheck = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_StartBaseLineCheck);
			NetworkCollect.BaselineCheck.Builder startBaseLineCheckBuilder = NetworkCollect.BaselineCheck.newBuilder();
			startBaseLineCheckBuilder.setTaskUuid((String)mapStartBaseLineCheck.get("uuid"));
			
			System.out.println("开启基线核查结果:" + mapStartBaseLineCheck.get("uuid"));
			mainMessageBuilder.setBaselineCheck(startBaseLineCheckBuilder);
			break;
		case MessageHead.stopBaseLineCheck:
			Map<String,Object> mapStopBaseLineCheck = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_StopBaseLineCheck);
			NetworkCollect.BaselineCheck.Builder stopBaseLineCheckBuilder = NetworkCollect.BaselineCheck.newBuilder();
			stopBaseLineCheckBuilder.setCmdRet((int)mapStopBaseLineCheck.get("result"));
			
			System.out.println("停止基线核查结果:" + mapStopBaseLineCheck.get("result"));
			mainMessageBuilder.setBaselineCheck(stopBaseLineCheckBuilder);
			break;
		case MessageHead.getBaseLineCheckResult:
			Map<String,Object> mapGetBaseLineCheckResult = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetBaseLineCheckResult);
			NetworkCollect.BaselineCheck.Builder getBaseLineCheckResultBuilder = NetworkCollect.BaselineCheck.newBuilder();
			getBaseLineCheckResultBuilder.setStatus((int)mapGetBaseLineCheckResult.get("status"));
			
			System.out.println("获取基线核查状态:" + mapGetBaseLineCheckResult.get("status"));
			mainMessageBuilder.setBaselineCheck(getBaseLineCheckResultBuilder);
			break;
		case MessageHead.getBaseLineCheckContent:
			Map<String,Object> mapGetBaseLineCheckContent = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetBaseLineCheckContent);
			NetworkCollect.BaselineCheck.Builder getBaseLineCheckContentBuilder = NetworkCollect.BaselineCheck.newBuilder();
			getBaseLineCheckContentBuilder.setResultContent((String)mapGetBaseLineCheckContent.get("result"));
			
			System.out.println("获取基线核查结果内容:" + mapGetBaseLineCheckContent.get("result"));
			mainMessageBuilder.setBaselineCheck(getBaseLineCheckContentBuilder);
			break;
		case MessageHead.getBaseLineCheckHisContent:
			Map<String, Object> mapBaselineEntity = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetBaseLineCheckHisContent);
			NetworkCollect.BaselineCheck.Builder getBaseLineCheckHisContentBuilder = NetworkCollect.BaselineCheck.newBuilder(); 
			
		 
			List<BaselineEntity> listBaselineEntity = (List<BaselineEntity>)mapBaselineEntity.get("list"); 
			for (int i = 0; i < listBaselineEntity.size(); i++) { 
				BaselineEntity baselineEntity = listBaselineEntity.get(i);
				
				NetworkCollect.BaselineEntity.Builder baselineEntityBuilder = NetworkCollect.BaselineEntity.newBuilder();
				baselineEntityBuilder.setIp(baselineEntity.getIp());
				baselineEntityBuilder.setContent(baselineEntity.getContent());
				baselineEntityBuilder.setReturnValue(baselineEntity.getReturnValue()); 
				
				getBaseLineCheckHisContentBuilder.addBaselineEntitys(baselineEntityBuilder); 
			}
		 
			System.out.println("获取基线核查历史记录-完成");
			mainMessageBuilder.setBaselineCheck(getBaseLineCheckHisContentBuilder);
			break;
		case MessageHead.deleteBaseLinkCheckResult:
			Map<String,Object> mapDeleteBaseLinkCheckResult = (Map<String,Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteBaseLinkCheckResult);
			NetworkCollect.BaselineCheck.Builder getDeleteBaseLinkCheckResultBuilder = NetworkCollect.BaselineCheck.newBuilder();
			getDeleteBaseLinkCheckResultBuilder.setCmdRet((int)mapDeleteBaseLinkCheckResult.get("num"));
			
			System.out.println("删除基线核查记录结果:" + mapDeleteBaseLinkCheckResult.get("num"));
			mainMessageBuilder.setBaselineCheck(getDeleteBaseLinkCheckResultBuilder);
			break; 
		case MessageHead.generateConfigurationChangeInfo:
			break;
		case MessageHead.getVersionInfo:
			String versionInfo = (String) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetVersionInfo);
			NetworkCollect.GetVersionInfo.Builder getVersionInfoBuilder = NetworkCollect.GetVersionInfo.newBuilder();
			getVersionInfoBuilder.setVersion(versionInfo);
			
			System.out.println("版本信息:" + versionInfo);
			mainMessageBuilder.setGetVersionInfo(getVersionInfoBuilder);
			break; 
		case MessageHead.initDangerCommand:
			Map<String, Object> mapDangerCommand = (Map<String, Object>) object; 
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_InitDangerCommand);
			NetworkCollect.InitDangerCommand.Builder initDangerCommandBuilder = NetworkCollect.InitDangerCommand.newBuilder(); 
	 
			Page<?> initDangerCommandPage = (Page<?>)mapDangerCommand.get("page");
			NetworkCollect.PageEntity.Builder initDangerCommandPageEntity = getPageEntityBuilder(initDangerCommandPage);
			
			initDangerCommandBuilder.setPageInfo(initDangerCommandPageEntity);
			
			List<DangerList> listDangerList = (List<DangerList>)mapDangerCommand.get("list");
			for (int i = 0; i < listDangerList.size(); i++) {  
				DangerList dangerList = listDangerList.get(i);
				
				NetworkCollect.DangerList.Builder dangerListBuilder = NetworkCollect.DangerList.newBuilder();
				dangerListBuilder.setId(dangerList.getId());
				dangerListBuilder.setIp(dangerList.getIp());
				dangerListBuilder.setContent(dangerList.getContent());
				
				initDangerCommandBuilder.addInfoList(dangerListBuilder); 
			} 
			
			System.out.println("获取危险操作命令列表-完成");
			mainMessageBuilder.setInitDangerCommand(initDangerCommandBuilder);
			break;
		case MessageHead.addDangerCommand:
			Map<String, Object> mapAddDangerCommandResult = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_AddDangerCommand);
			NetworkCollect.AddDangerCommand.Builder addDangerCommandBuilder = NetworkCollect.AddDangerCommand.newBuilder();
			addDangerCommandBuilder.setFlag((int)mapAddDangerCommandResult.get("flag"));
			
			System.out.println("添加危险操作命令结果:" + mapAddDangerCommandResult.get("flag"));
			mainMessageBuilder.setAddDangerCommand(addDangerCommandBuilder);
			break;
		case MessageHead.updateDangerCommand:
			Map<String, Object> mapUpdateDangerCommandResult = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_UpdateDangerCommand);
			NetworkCollect.UpdateDangerCommand.Builder updateDangerCommandBuilder = NetworkCollect.UpdateDangerCommand.newBuilder();
			updateDangerCommandBuilder.setFlag((int)mapUpdateDangerCommandResult.get("flag"));
			
			System.out.println("添加修改操作命令结果:" + mapUpdateDangerCommandResult.get("flag"));
			mainMessageBuilder.setUpdateDangerCommand(updateDangerCommandBuilder);
			break;
		case MessageHead.delDangerCommand:
			Map<String, Object> mapDelDangerCommandResult = (Map<String, Object>) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DelDangerCommand);
			NetworkCollect.DelDangerCommand.Builder delDangerCommandBuilder = NetworkCollect.DelDangerCommand.newBuilder();
			delDangerCommandBuilder.setIp((String) mapDelDangerCommandResult.get("ip"));
			delDangerCommandBuilder.setFlag((int)mapDelDangerCommandResult.get("flag"));
			
			System.out.println("删除危险操作命令结果:" + mapDelDangerCommandResult.get("flag"));
			mainMessageBuilder.setDelDangerCommand(delDangerCommandBuilder);
			break;
		case MessageHead.deleteCommunication:
			boolean deleteCommunicationResult = (boolean) object;
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_DeleteCommunication);
			NetworkCollect.DeleteCommunication.Builder deleteCommunicationBuilder = NetworkCollect.DeleteCommunication.newBuilder();
			deleteCommunicationBuilder.setResult(deleteCommunicationResult);
			
			System.out.println("删除通信配置参数结果:" + deleteCommunicationResult);
			mainMessageBuilder.setDeleteCommunication(deleteCommunicationBuilder);
			break;
		case MessageHead.Chart.getSysData: 
			Double[] SysData = (Double[]) object; 
			
			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetSysData);
			NetworkCollect.GetSystemData.Builder getSystemDataBuilder = NetworkCollect.GetSystemData.newBuilder();
			getSystemDataBuilder.setCpuUsed(SysData[0]);
			getSystemDataBuilder.setDiskUsed(SysData[1]);
			getSystemDataBuilder.setMemUsed(SysData[2]);
			getSystemDataBuilder.setRunProNum(SysData[3]);
			
			System.out.println("获取当前装置系统资源利用情况-完成");
			mainMessageBuilder.setGetSystemDatas(getSystemDataBuilder);
			break;
		case MessageHead.Chart.getChartData:
			Map<String, Map<String, Object>> mapGetChartData = (Map<String, Map<String, Object>>) object;

			mainMessageBuilder.setMsgType(MsgHeadType.HT_GetChartData);  
			NetworkCollect.GetRunStateOverview.Builder getRunStateOverview = NetworkCollect.GetRunStateOverview.newBuilder(); 
			
			Map<String, Object> mapUpTypeLevelNum = (Map<String,Object>) mapGetChartData.get("upTypeLevelNum");
			for (Entry<String, Object> entryUpTypeLevelNum : mapUpTypeLevelNum.entrySet()) {
				String upTypeLevelDeviceType = entryUpTypeLevelNum.getKey();
				Map<Integer, Integer> mapUpTypeLevelNumParas = (Map<Integer, Integer>)(entryUpTypeLevelNum.getValue());
				
				NetworkCollect.LevelMatchInfoNum.Builder levelMatchInfoNumBuilder = NetworkCollect.LevelMatchInfoNum.newBuilder();
				levelMatchInfoNumBuilder.setDeviceType(upTypeLevelDeviceType);
				 
				for (Entry<Integer, Integer> entryLevelNum : mapUpTypeLevelNumParas.entrySet() ) {
					NetworkCollect.LevelNum.Builder levelNumBuilder = NetworkCollect.LevelNum.newBuilder();
					
					levelNumBuilder.setLevel(entryLevelNum.getKey());
					levelNumBuilder.setInfoNum(entryLevelNum.getValue());
					
					levelMatchInfoNumBuilder.addLevelNums(levelNumBuilder);
				}
				
				getRunStateOverview.addUpTypeLevelNum(levelMatchInfoNumBuilder);
			}
			
			Map<String, Object> mapTypeLevelNum = (Map<String, Object>) mapGetChartData.get("typeLevelNum");
			for (Entry<String, Object> entryTypeLevelNum : mapTypeLevelNum.entrySet()) {
				String typeLevelDeviceType = entryTypeLevelNum.getKey(); 
				Map<Integer, Integer> mapTypeLevelNumParas = (Map<Integer, Integer>)(entryTypeLevelNum.getValue());

				NetworkCollect.LevelMatchInfoNum.Builder levelMatchInfoNumBuilder = NetworkCollect.LevelMatchInfoNum.newBuilder();
				levelMatchInfoNumBuilder.setDeviceType(typeLevelDeviceType); 
				 
				for (Entry<Integer, Integer> entryLevelNum : mapTypeLevelNumParas.entrySet() ) {
					NetworkCollect.LevelNum.Builder levelNumBuilder = NetworkCollect.LevelNum.newBuilder();
					
					levelNumBuilder.setLevel(entryLevelNum.getKey());
					levelNumBuilder.setInfoNum(entryLevelNum.getValue());
					
					levelMatchInfoNumBuilder.addLevelNums(levelNumBuilder);
				} 
				getRunStateOverview.addTypeLevelNum(levelMatchInfoNumBuilder);
			}
			
			Map<String, Object> mapUpTypeNum = (Map<String, Object>) mapGetChartData.get("upTypeNum");
			for (Entry<String, Object> entryUpTypeNum : mapUpTypeNum.entrySet()) {
				String keyString = entryUpTypeNum.getKey();
				Integer value = (Integer)(entryUpTypeNum.getValue());
				
				NetworkCollect.TypeMatchInfoNum.Builder typeMatchInfoNumBuilder = NetworkCollect.TypeMatchInfoNum.newBuilder();
				typeMatchInfoNumBuilder.setDeviceType(keyString);
				typeMatchInfoNumBuilder.setInfoNum(value);
				
				getRunStateOverview.addUpTypeNum(typeMatchInfoNumBuilder);
			}
			
			Map<String, Object> mapTypeNum = (Map<String, Object>) mapGetChartData.get("typeNum");
			for (Entry<String, Object> entryTypeNum : mapTypeNum.entrySet()) {
				String keyString = entryTypeNum.getKey();
				Integer value = (Integer)(entryTypeNum.getValue());
				
				NetworkCollect.TypeMatchInfoNum.Builder typeMatchInfoNumBuilder = NetworkCollect.TypeMatchInfoNum.newBuilder();
				
				typeMatchInfoNumBuilder.setDeviceType(keyString);
				typeMatchInfoNumBuilder.setInfoNum(value);
				
				getRunStateOverview.addTypeNum(typeMatchInfoNumBuilder);
			}

			System.out.println("获取采集信息和上传事件分类统计信息-完成");
			mainMessageBuilder.setGetRunStateOverviews(getRunStateOverview);
			break;
		default:
			break;
		}

		NetworkCollect.MainMessage mainMessage = mainMessageBuilder.build();   
		redisTemplate.convertAndSend("GridClient", mainMessage.toByteArray()); //发布  
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}
 
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {   
		System.out.println("channelActive");  
        ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("channelInactive");
		
		ctx.fireChannelInactive(); 
	} 
}

// @Autowired
// UserHandler userHandler;
//
// @Autowired
// RoleHandler roleHandler;
//
// @Autowired
// LogsHandler logsHandler;
//
// @Autowired
// SystemUpdateHandler systemUpdateHandler;
//
// @Autowired
// BackUpHandler backUpHandler;
//
// @Autowired
// ParameterConfigHandler parameterConfigHandler;
// @Autowired
// EventHandler eventHandler;
//
// @Autowired
// AssetsHandler assetsHandler;
//
// @Autowired
// StatusRunningPage statusRunningPage;
//
// @Autowired
// ChartHandler chartHandler;
//
// @Autowired
// BaseLineCheckHandler baseLineCheckHandler;

