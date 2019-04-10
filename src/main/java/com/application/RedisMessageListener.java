package com.application;
 
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 

import org.apache.commons.codec.binary.Base64; 
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.expression.spel.ast.Literal;

import com.google.protobuf.InvalidProtocolBufferException;
import com.networkcollect.NetworkCollect; 
import com.networkcollect.NetworkCollect.MsgHeadType; 

import collect.core.security.RSACoder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;
import up.client.netty.NettyClient;
import up.common.entity.AssetsDeviceEntity;
import up.common.entity.CertificateEntity;
import up.common.entity.ClientIP;
import up.common.entity.DangerList;
import up.common.entity.LogsEntity;
import up.common.entity.RoleEntity;
import up.common.entity.UserEntity;
import up.common.entity.UserGroup;
import up.common.searchParams.CollectSearchParam;
import up.common.searchParams.LogsSearchParam;
import up.common.tool.request.MessageHead;
import up.common.xmlentity.Platform;

public class RedisMessageListener implements MessageListener {

	private NettyClient nettyClient;

	public RedisMessageListener(NettyClient nettyClient, String ServerAddress, int ServerPort) {
		this.nettyClient = nettyClient;
		this.nettyClient.start(ServerAddress, ServerPort);
		
		System.out.println("ServerAddress:" + ServerAddress + " ServerPort:" + ServerPort);
	} 
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
//		up.common.nettypojo.Message pojoMessage = getSysDataMessage();  
//		this.nettyClient.getChannelFuture().channel().writeAndFlush(pojoMessage);   
		 
		try {
			NetworkCollect.MainMessage mainMessage = NetworkCollect.MainMessage.parseFrom(message.getBody()); 
			if(mainMessage.getMsgType().compareTo(MsgHeadType.HT_Invalid) == 0)
				return; 
			if(mainMessage.getMsgType().compareTo(MsgHeadType.HT_InsertLog) == 0)
				return; 
			 
			up.common.nettypojo.Message pojoMessage = null;
			switch (mainMessage.getMsgType()) {
			case HT_GetCollectList:
				NetworkCollect.GetCollectList getCollectList = mainMessage.getGetCollectList();
				pojoMessage = getCollectListMessage(getCollectList);
				System.out.println("获取采集信息列表");
				break;
			case HT_GetUploadList:
				NetworkCollect.GetUploadList getUploadList = mainMessage.getGetUploadList();
				pojoMessage = getUploadListMessage(getUploadList);
				System.out.println("获取上传事件列表");
				break;
			case HT_GetLogs:
				NetworkCollect.GetLogs getLogs = mainMessage.getGetLogs();
				pojoMessage = getLogsMessage(getLogs);
				System.out.println("获取审计日志列表");
				break;
			case HT_ExportLogs:
				NetworkCollect.ExportLogs exportLogs = mainMessage.getExportLogs();
				pojoMessage = getExportLogsMessage(exportLogs);
				System.out.println("导出日志");
				break;
			case HT_GetDevices:
				NetworkCollect.GetDevices getDevices = mainMessage.getGetDevices();
				pojoMessage = getDevicesMessage(getDevices);
				System.out.println("获取资产设备列表");
				break;
			case HT_InsertDevice:
				NetworkCollect.InsertDevice insertDevice = mainMessage.getInsertDevice();
				pojoMessage = getInsertDeviceMessage(insertDevice);
				System.out.println("添加资产设备:" + insertDevice.getAssetInfo().getName());
				break;
			case HT_DeleteDevice:
				NetworkCollect.DeleteDevice deleteDevice = mainMessage.getDeleteDevice();
				pojoMessage = getDeleteDeviceMessage(deleteDevice);
				System.out.println("删除资产设备:" + deleteDevice.getId());
				break;
			case HT_ModifyDevice:
				NetworkCollect.ModifyDevice modifyDevice = mainMessage.getModifyDevice();
				pojoMessage = getModifyDeviceMessage(modifyDevice);
				System.out.println("修改资产设备:" + modifyDevice.getAssetInfo().getName());
				break;
			case HT_GetDevicesChartData:
				pojoMessage = getDevicesChartDataMessage();
				System.out.println("按设备类型统计所有资产设备");
				break;
			case HT_GetOnlineDevicesChartData:
				pojoMessage = getOnlineDevicesChartDataMessage();
				System.out.println("按设备类型统计在线资产设备"); 
				break; 
			case HT_GetSysData:
				pojoMessage = getSysDataMessage();
				System.out.println("获取当前装置系统资源利用情况"); 
				break;
			case HT_GetChartData:
				pojoMessage = getChartDataMessage();
				System.out.println("获取采集信息和上传事件分类统计信息"); 
				break;
			case HT_UserLogin: 
				NetworkCollect.UserLoginInfo userLoginInfo = mainMessage.getUserLogin();
				pojoMessage = getUserLogMessage(userLoginInfo.getUsername(), userLoginInfo.getPassword());
				System.out.println("用户登录:" + userLoginInfo.getUsername()); 
				break;
			case HT_InitCreateUser:
				NetworkCollect.InitCreateUser initCreateUser = mainMessage.getInitCreateUser();
				pojoMessage = getInitCreateUserMessage(initCreateUser);
				System.out.println("初始化用户新增/编辑界面"); 
				break;
			case HT_GetAllUser:
				NetworkCollect.GetAllUser getAllUser = mainMessage.getGetAllUser();
				pojoMessage = getAllUserMessage(getAllUser); 
				System.out.println("获取所有用户"); 
				break;
			case HT_CreateUser:
				NetworkCollect.CreateUser createUser = mainMessage.getCreateUser();
				NetworkCollect.UserEntity createUserEntity = createUser.getUserinfo(); 
				pojoMessage = getCreateUserMessage(createUserEntity);
				System.out.println("新增用户:" + createUserEntity.getUsername()); 
				break;
			case HT_DeleteUser:
				NetworkCollect.DeleteUser deleteUser =  mainMessage.getDeleteUser();
				List<String> listUserNameList = deleteUser.getUserNameListList();
				pojoMessage = getDeleteUserMessage(listUserNameList);
				System.out.println("删除用户:" + listUserNameList); 
				break;
			case HT_UpdateUser:
				NetworkCollect.UpdateUser updateUser = mainMessage.getUpdateUser();
				NetworkCollect.UserEntity updateUserEntity =  updateUser.getUserinfo();
				pojoMessage = getUpdateUserMessage(updateUserEntity);
				System.out.println("更新用户:" + updateUserEntity.getUsername());
				break;
			case HT_UnlockUser:
				NetworkCollect.UnlockUser unlockUser = mainMessage.getUnlockUser();
				pojoMessage = getUnlockUserMessage(unlockUser);
				System.out.println("解锁用户:" + unlockUser.getUsername());
				break;
			case HT_ModifyPasswordByAdmin:
				NetworkCollect.ModifyPasswordByAdmin modifyPasswordByAdmin = mainMessage.getModifyPasswordByAdmin();
				pojoMessage = getModifyPasswordByAdminMessage(modifyPasswordByAdmin);
				System.out.println("管理员修改用户密码");
				break;
			case HT_ModifyPassword:
				NetworkCollect.ModifyPassword modifyPassword = mainMessage.getModifyPassword();
				pojoMessage = getModifyPasswordMessage(modifyPassword);
				System.out.println("用户修改用户密码");
				break;
			case HT_InitCreateRole:
				NetworkCollect.InitCreateRole initCreateRole = mainMessage.getInitCreateRole();
				pojoMessage = getInitCreateRoleMessage(initCreateRole);
				System.out.println("初始化角色创建/编辑界面");
				break;
			case HT_GetAllUserGroup:  
				pojoMessage = getAllUserGroupMessage(); 
				System.out.println("获取所有用户组"); 
				break; 
			case HT_CreateUserGroup:
				NetworkCollect.CreateUserGroup createUserGroup = mainMessage.getCreateUserGroup();
				NetworkCollect.UserGroup createGroupEntity = createUserGroup.getGroupInfo();
				pojoMessage = getCreateUserGroupMessage(createGroupEntity);
				System.out.println("新增用户组:" + createGroupEntity.getGroupname());
				break;
			case HT_UpdateUserGroup:
				NetworkCollect.UpdateUserGroup updateUserGroup = mainMessage.getUpdateUserGroup();
				NetworkCollect.UserGroup updateGroupEntity = updateUserGroup.getGroupInfo();
				pojoMessage = getUpdateUserGroupMessage(updateGroupEntity);
				System.out.println("更新用户组:" + updateGroupEntity.getGroupname());
				break;
			case HT_DeleteUserGroup:
				NetworkCollect.DeleteUserGroup deleteUserGroup = mainMessage.getDeleteUserGroup();
				String groupName = deleteUserGroup.getGroupName();
				pojoMessage = getDeleteUserGroupMessage(groupName);
				System.out.println("删除用户组:" + groupName);
				break;
			case HT_GetAllRole:
				NetworkCollect.GetAllRole getAllRole = mainMessage.getGetAllRole();
				pojoMessage = getAllRoleMessage(getAllRole.getRolename(), getAllRole.getPageNumber(), getAllRole.getPageSize());
				System.out.println("获取所有角色信息");
				break;
			case HT_CreateRole:
				NetworkCollect.CreateRole createRole = mainMessage.getCreateRole();
				NetworkCollect.RoleEntity roleEntity =  createRole.getRoleInfo();
				List<Integer> listAddUserId = createRole.getAddUserIdListList();
				pojoMessage = getCreateRoleMessage(roleEntity, listAddUserId);
				System.out.println("新增角色信息:" + roleEntity.getName() + " 用户ID:" + listAddUserId);
				break;
			case HT_DeleteRole:
				NetworkCollect.DeleteRole deleteRole = mainMessage.getDeleteRole();
				List<String> listDeleteRoleName =  deleteRole.getRoleNameListList();
				pojoMessage = getDeleteRoleMessage(listDeleteRoleName);
				System.out.println("删除角色:" + listDeleteRoleName);
				break;
			case HT_UpdateRole:
				NetworkCollect.UpdateRole updateRole =  mainMessage.getUpdateRole();
				pojoMessage = getUpdateRoleMessage(updateRole);
				System.out.println("更新角色:" + updateRole.getRoleInfo().getName() + " 关联新增的用户ID:" + updateRole.getAddUserIdListList() + " 关联删除的用户ID:" + updateRole.getDelUserIdListList());
				break;
			case HT_UpdateUsersRoleId:
				NetworkCollect.UpdateUsersRoleId updateUsersRoleId = mainMessage.getUpdateUsersRoleId();
				pojoMessage = getUpdateUsersRoleIdMessage(updateUsersRoleId);
				System.out.println("更新用户与角色的关系, 角色ID:" + updateUsersRoleId.getRoleId() + " "+ updateUsersRoleId.getAddUserIdListList() + " " + updateUsersRoleId.getDelUserIdListList());
				break;
			case HT_SystemUpdate:
				NetworkCollect.SystemUpdate systemUpdate = mainMessage.getSystemUpdate();
				pojoMessage = getSystemUpdateMessage(systemUpdate);
				System.out.println("系统升级");
				break;
			case HT_Restart: 
//				pojoMessage = getRestartMessage();
				System.out.println("系统服务（程序）重启");
				break; 
			case HT_GetBackupDataDate: 
				pojoMessage = getBackupDataDateMessage();
				System.out.println("获取数据库备份信息");
				break;
			case HT_BackupDatabase:
				pojoMessage = getBackupDatabaseMessage();
				System.out.println("备份数据库");
				break;
			case HT_RecoverDatabase:
				NetworkCollect.SystemDataBackup recoverDatabase = mainMessage.getSystemDataBackup();
				String recoverDataVersion = recoverDatabase.getBackupVersion();
				pojoMessage = getRecoverDatabaseMessage(recoverDataVersion);
				System.out.println("恢复数据库:" + recoverDataVersion);
				break;
			case HT_DeleteDataBackup:
				NetworkCollect.SystemDataBackup deleteDataBackup = mainMessage.getSystemDataBackup();
				String deleteDataVersion = deleteDataBackup.getBackupVersion();
				pojoMessage = getDeleteDataBackupMessage(deleteDataVersion);
				System.out.println("删除数据库备份:" + deleteDataVersion);
				break; 
			case HT_GetBackupConfDate:
				pojoMessage = getBackupConfDateMessage();
				System.out.println("获取配置文件备份信息");
				break;
			case HT_BackupConfigure:
				pojoMessage = getBackupConfigureMessage();
				System.out.println("备份配置文件");
				break;
			case HT_RecoverConfigure:
				NetworkCollect.SystemDataBackup recoverConfigure = mainMessage.getSystemDataBackup();
				String recoverConfVersion = recoverConfigure.getBackupVersion();
				pojoMessage = getRecoverConfigureMessage(recoverConfVersion);
				System.out.println("恢复配置文件:" + recoverConfVersion);
				break;
			case HT_DeleteConfBackup:
				NetworkCollect.SystemDataBackup deleteConfigure = mainMessage.getSystemDataBackup();
				String deleteConfVersion = deleteConfigure.getBackupVersion();
				pojoMessage = getDeleteConfigureMessage(deleteConfVersion);
				System.out.println("删除配置文件备份:" + deleteConfVersion);
				break;
			case HT_GenerateLoginInfo: 
				NetworkCollect.GenerateLoginInfo generateLoginInfo = mainMessage.getGenerateLoginInfo(); 
				pojoMessage = getGenerateLoginInfoMessage(generateLoginInfo.getUsername(), generateLoginInfo.getSubType());
				System.out.println("生成登录日志:" + generateLoginInfo.getUsername());
				break; 
//			case HT_InsertLog:
//				NetworkCollect.InsertLog insertLog = mainMessage.getInsertLog();
//				NetworkCollect.LogsEntity logsEntity = insertLog.getLogInfo(); 
//				pojoMessage = getInsertLogMessage(logsEntity); 
//				System.out.println("插入日志");
//				break; 
			case HT_GetSVRDevice:
				NetworkCollect.GetSvrDevices getSvrDevices = mainMessage.getGetSvrDevices();
				pojoMessage = getSVRDeviceMessage(getSvrDevices);
				System.out.println("获取服务器类的资产");
				break;
			case HT_StartBaseLineCheck:
				NetworkCollect.BaselineCheck startBaseLineCheck =  mainMessage.getBaselineCheck();
				pojoMessage = getStartBaseLineCheckMessage(startBaseLineCheck);
				System.out.println("开启基线核查:" + startBaseLineCheck.getIp());
				break;
			case HT_StopBaseLineCheck:
				NetworkCollect.BaselineCheck stopBaseLineCheck =  mainMessage.getBaselineCheck();
				pojoMessage = getStopBaseLineCheckMessage(stopBaseLineCheck);
				System.out.println("停止基线核查:" + stopBaseLineCheck.getTaskUuid());
				break;
			case HT_GetBaseLineCheckResult:
				NetworkCollect.BaselineCheck getBaseLineCheckResult =  mainMessage.getBaselineCheck();
				pojoMessage = getBaseLineCheckResultMessage(getBaseLineCheckResult);
				System.out.println("获取基线核查状态:" + getBaseLineCheckResult.getTaskUuid());
				break;
			case HT_GetBaseLineCheckContent:
				NetworkCollect.BaselineCheck getBaseLineCheckContent =  mainMessage.getBaselineCheck();
				pojoMessage = getBaseLineCheckContentMessage(getBaseLineCheckContent);
				System.out.println("获取基线核查结果内容:" + getBaseLineCheckContent.getTaskUuid());
				break; 
			case HT_GetBaseLineCheckHisContent:
				NetworkCollect.BaselineCheck getBaseLineCheckHisContent = mainMessage.getBaselineCheck();
				pojoMessage = getBaseLineCheckHisContentMessage(getBaseLineCheckHisContent.getIp());  
				System.out.println("获取基线核查历史记录:" + getBaseLineCheckHisContent.getIp());
				break; 
			case HT_DeleteBaseLinkCheckResult:
				NetworkCollect.BaselineCheck deleteBaseLinkCheckResult =  mainMessage.getBaselineCheck();
				pojoMessage = getDeleteBaseLinkCheckResultMessage(deleteBaseLinkCheckResult);
				System.out.println("删除基线核查记录:" + deleteBaseLinkCheckResult.getTaskUuid());
				break;
			case HT_GetSystemConfigs:
				pojoMessage = getSystemConfigsMessage();
				System.out.println("获取系统配置参数");
				break;
			case HT_ModifySystemConfigs:
				NetworkCollect.ModifySystemConfigs modifySystemConfigs = mainMessage.getModifySystemConfigs();
				pojoMessage = getModifySystemConfigsMessage(modifySystemConfigs);
				System.out.println("修改系统配置参数");
				break;
			case HT_GetNetworkInterfaces:
				pojoMessage = getNetworkInterfacesMessage();
				System.out.println("获取网卡配置参数");
				break;
			case HT_ModifyNetworkInterfaces:
				NetworkCollect.ModifyNetworkInterfaces modifyNetworkInterfaces = mainMessage.getModifyNetworkInterfaces();
				pojoMessage = getModifyNetworkInterfacesMessage(modifyNetworkInterfaces);
				System.out.println("修改网卡配置参数");
				break;
			case HT_GetEventConfigs:
				pojoMessage = getEventConfigsMessage();
				System.out.println("获取事件配置参数");
				break;
			case HT_ModifyEventConfigs:
				NetworkCollect.ModifyEventConfigs modifyEventConfigs = mainMessage.getModifyEventConfigs();
				pojoMessage = getModifyEventConfigsMessage(modifyEventConfigs);
				System.out.println("修改事件配置参数");
				break;
			case HT_GetNtpConfigs:
				pojoMessage = getNtpConfigsMessage();
				System.out.println("获取NTP配置参数");
				break; 
			case HT_ModifyNtpConfigs:
				NetworkCollect.ModifyNtpConfigs modifyNtpConfigs = mainMessage.getModifyNtpConfigs();
				pojoMessage = getModifyNtpConfigsMessage(modifyNtpConfigs); 
				System.out.println("修改NTP配置参数");
				break;
			case HT_GetCommuConfigs:
				pojoMessage = getCommuConfigsMessage();
				System.out.println("获取通信配置参数");
				break;
			case HT_ModifyCommuConfigs:
				NetworkCollect.ModifyCommuConfigs modifyCommuConfigs = mainMessage.getModifyCommuConfigs();
				pojoMessage = getModifyCommuConfigsMessage(modifyCommuConfigs);
				System.out.println("修改通信配置参数");
				break;
			case HT_DeleteCommunication:
				NetworkCollect.DeleteCommunication deleteCommunication = mainMessage.getDeleteCommunication();
				pojoMessage = getDeleteCommunicationMessage(deleteCommunication.getIp());
				System.out.println("删除通信配置参数:" + deleteCommunication.getIp());
				break;
			case HT_GetGateways:
				pojoMessage = getGatewaysMessage(); 
				System.out.println("获取路由配置参数");
				break;
			case HT_AddGateways:
				NetworkCollect.AddGateways addGateways = mainMessage.getAddGateways();
				pojoMessage = getAddGatewaysMessage(addGateways);
				System.out.println("新增路由配置参数");
				break;
			case HT_DelGateways:
				NetworkCollect.DelGateways delGateways = mainMessage.getDelGateways();
				pojoMessage = getDelGatewaysMessage(delGateways);
				System.out.println("删除路由配置参数");
				break;
			case HT_ModifyGateways:
				NetworkCollect.ModifyGateways modifyGateways = mainMessage.getModifyGateways();
				pojoMessage = getModifyGatewaysMessage(modifyGateways);
				System.out.println("修改路由配置参数");
				break;
			case HT_GetDeviceInfoConfigs:
				pojoMessage = getDeviceInfoConfigsMessage();
				System.out.println("获取装置信息");
				break;
			case HT_ModifyDeviceInfoConfigs: 
				NetworkCollect.ModifyDeviceInfoConfigs modifyDeviceInfoConfigs = mainMessage.getModifyDeviceInfoConfigs();
				pojoMessage = getModifyDeviceInfoConfigsMessage(modifyDeviceInfoConfigs);
				System.out.println("修改装置信息");
				break;
			case HT_InitClientIPPanel:
				NetworkCollect.InitClientIPPanel initClientIPPanel = mainMessage.getInitClientIPPanel();
				pojoMessage = getInitClientIPPanelMessage(initClientIPPanel);
				System.out.println("客户端IP管理界面");
				break;
			case HT_AddClientIP:
				NetworkCollect.AddClientIP addClientIP = mainMessage.getAddClientIP();
				pojoMessage = getAddClientIPMessage(addClientIP.getIp()); 
				System.out.println("新增客户端IP:" + addClientIP.getIp());
				break;
			case HT_DelClientIP:
				NetworkCollect.DelClientIP delClientIP = mainMessage.getDelClientIP();
				pojoMessage = getDelClientIPMessage(delClientIP);
				System.out.println("删除客户端IP:" + delClientIP.getIpInfo().getIp());
				break;
			case HT_InitDangerCommand:
				NetworkCollect.InitDangerCommand initDangerCommand = mainMessage.getInitDangerCommand();
				pojoMessage = getInitDangerCommandMessage(initDangerCommand); 
				System.out.println("获取危险操作命令列表,当前页数:" + initDangerCommand.getPageNumber() + ",每页记录数:" + initDangerCommand.getPageSize());
				break;
			case HT_AddDangerCommand:
				NetworkCollect.AddDangerCommand addDangerCommand = mainMessage.getAddDangerCommand();
				pojoMessage = getAddDangerCommandMessage(addDangerCommand);
				System.out.println("添加危险操作命令");
				break;
			case HT_UpdateDangerCommand:
				NetworkCollect.UpdateDangerCommand updateDangerCommand = mainMessage.getUpdateDangerCommand();
				pojoMessage = getUpdateDangerCommandMessage(updateDangerCommand); 
				System.out.println("修改危险操作命令");
				break;
			case HT_DelDangerCommand:
				NetworkCollect.DelDangerCommand delDangerCommand = mainMessage.getDelDangerCommand();
				pojoMessage = getDelDangerCommandMessage(delDangerCommand); 
				System.out.println("删除危险操作命令");
				break;
			case HT_InitCertificatePanel:
				pojoMessage = getInitCertificatePanelMessage();
				System.out.println("初始化证书页面");
				break;
			case HT_ImportCertificate:
				NetworkCollect.ImportCertificate importCertificate = mainMessage.getImportCertificate(); 
				pojoMessage = getImportCertificateMessage(importCertificate); 
				System.out.println("导入证书");
				break;
			case HT_UpdateCertificate:
				NetworkCollect.UpdateCertificate updateCertificate = mainMessage.getUpdateCertificate();
				pojoMessage = getUpdateCertificateMessage(updateCertificate); 
				System.out.println("更新证书");
				break;
			case HT_DeleteCertificate:
				NetworkCollect.DeleteCertificate deleteCertificate = mainMessage.getDeleteCertificate();
				pojoMessage = getDeleteCertificateMessage(deleteCertificate);
				System.out.println("删除证书");
				break;
			case HT_ExportCertificate:
				NetworkCollect.ExportCertificate exportCertificateMessage = mainMessage.getExportCertificate();
				pojoMessage = getExportCertificateMessage(exportCertificateMessage);
				System.out.println("导出证书"); 
				break;
			default:
				break;
			}
			
			System.out.println(pojoMessage); 
			ChannelFuture future = this.nettyClient.getChannelFuture().channel().writeAndFlush((Object)pojoMessage);   
		} catch (InvalidProtocolBufferException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
	}

	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}
 
	public UserEntity getUserEntityBody(NetworkCollect.UserEntity userEntity){
		UserEntity userEntityBody = new UserEntity();
		  
		userEntityBody.setId(userEntity.getId());
		userEntityBody.setUsername(userEntity.getUsername());
		userEntityBody.setRealname(userEntity.getRealname());
		userEntityBody.setRoleid(userEntity.getRoleid());
		userEntityBody.setGroupid(userEntity.getGroupid());
		 
		byte[] encryptPass = RSACoder.handleByPublic(userEntity.getPassword().getBytes(),
				Base64.decodeBase64(RSACoder.getPublicKeyString()), true);  
		String pass = Base64.encodeBase64String(encryptPass);
		userEntityBody.setPassword(pass); 
		
		userEntityBody.setDescription(userEntity.getDescription());
		userEntityBody.setUkey(userEntity.getUkey());
		userEntityBody.setIfinner(userEntity.getIfinner());
		
		return userEntityBody;
	}
	
	public RoleEntity  getRoleEntityBody(NetworkCollect.RoleEntity roleEntity) {
		RoleEntity roleEntityBody = new RoleEntity();
		 
		roleEntityBody.setId(roleEntity.getId());
		roleEntityBody.setName(roleEntity.getName());
		roleEntityBody.setRoletype(roleEntity.getRoletype());
		roleEntityBody.setDescription(roleEntity.getDescription());
		roleEntityBody.setIfinner(roleEntity.getIfinner());
		roleEntityBody.setRunstatus(roleEntity.getRunstatus());
		roleEntityBody.setAssetsmanage(roleEntity.getAssetsmanage());
		roleEntityBody.setColinfo(roleEntity.getColinfo());
		roleEntityBody.setEvent(roleEntity.getEvent());
		roleEntityBody.setParadigm(roleEntity.getParadigm());
		roleEntityBody.setSyslog(roleEntity.getSyslog());
		roleEntityBody.setParadigm(roleEntity.getParadigm()); 
		roleEntityBody.setComm(roleEntity.getComm());
		roleEntityBody.setTopo(roleEntity.getTopo());
		roleEntityBody.setNetflow(roleEntity.getNetflow());
		roleEntityBody.setTest(roleEntity.getTest());
		roleEntityBody.setBackup(roleEntity.getBackup());
		roleEntityBody.setBaselinkcheck(roleEntity.getBaselinkcheck());
		roleEntityBody.setSystemupdate(roleEntity.getSystemupdate());
		
		return roleEntityBody;
	}
	
	public CertificateEntity getCertificateEntityBody(NetworkCollect.CertificateEntity certificateEntity){
		CertificateEntity certificateEntityBody = new CertificateEntity();
		  
		certificateEntityBody.setId(certificateEntity.getId());
		certificateEntityBody.setIp(certificateEntity.getIp());
		certificateEntityBody.setType(certificateEntity.getType());
		certificateEntityBody.setName(certificateEntity.getName());
		certificateEntityBody.setFormat(certificateEntity.getFormat());
		certificateEntityBody.setValidityPeriod(certificateEntity.getValidityPeriod());
		certificateEntityBody.setSubject(certificateEntity.getSubject());
		certificateEntityBody.setIssue(certificateEntity.getIssue());
		certificateEntityBody.setContent(certificateEntity.getContent());
		certificateEntityBody.setOriginalContent(certificateEntity.getOriginalContent()); 
		
		return certificateEntityBody;
	}
	
	
	public AssetsDeviceEntity getAssetsDeviceEntityBody(NetworkCollect.AssetsDeviceEntity assetsDeviceEntity){
		AssetsDeviceEntity assetsDeviceEntityBody = new AssetsDeviceEntity();
		   
		assetsDeviceEntityBody.setId(assetsDeviceEntity.getId());
		assetsDeviceEntityBody.setIp(assetsDeviceEntity.getIp());
		assetsDeviceEntityBody.setName(assetsDeviceEntity.getName());
		assetsDeviceEntityBody.setMac(assetsDeviceEntity.getMac());
		assetsDeviceEntityBody.setSerialnumber(assetsDeviceEntity.getSerialnumber());
		assetsDeviceEntityBody.setFactoryname(assetsDeviceEntity.getFactoryname());
		assetsDeviceEntityBody.setFactorydec(assetsDeviceEntity.getFactorydec()); 
		assetsDeviceEntityBody.setIptwo(assetsDeviceEntity.getIptwo());
		assetsDeviceEntityBody.setMactwo(assetsDeviceEntity.getMactwo());
		assetsDeviceEntityBody.setDevicetype(assetsDeviceEntity.getDevicetype());
		assetsDeviceEntityBody.setPublicname(assetsDeviceEntity.getPublicname()); 
		assetsDeviceEntityBody.setSystemVersion(assetsDeviceEntity.getSystemVersion());
		assetsDeviceEntityBody.setHold((byte)assetsDeviceEntity.getHold());
		assetsDeviceEntityBody.setSnmpVersion((byte)assetsDeviceEntity.getSnmpVersion());
		assetsDeviceEntityBody.setSnmpv3Username(assetsDeviceEntity.getSnmpv3Username());
		assetsDeviceEntityBody.setSnmpv3AuthAlgo((byte)assetsDeviceEntity.getSnmpv3AuthAlgo());
		assetsDeviceEntityBody.setSnmpv3EncryptAlgo((byte)assetsDeviceEntity.getSnmpv3EncryptAlgo());
		assetsDeviceEntityBody.setSnmpRead(assetsDeviceEntity.getSnmpRead());
		assetsDeviceEntityBody.setSnmpWrite(assetsDeviceEntity.getSnmpWrite());
		assetsDeviceEntityBody.setIsonline(assetsDeviceEntity.getIsonline());
    
		return assetsDeviceEntityBody;
	}
	
	public DangerList getDangerListBody(NetworkCollect.DangerList dangerList) { 
		DangerList DangerListBody = new DangerList();
		
		DangerListBody.setId(dangerList.getId());
		DangerListBody.setIp(dangerList.getIp()); 
		DangerListBody.setContent(dangerList.getContent());
		
		return DangerListBody;
	}
	
	public up.common.nettypojo.Message getCollectListMessage(NetworkCollect.GetCollectList getCollectList) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getCollectList); 
		 
		Map<String, Object> mapCollectSearchParam = new HashMap<String, Object>();  
		mapCollectSearchParam.put("pageNumber", (Integer)getCollectList.getPageNumber());
		mapCollectSearchParam.put("pageSize", (Integer)getCollectList.getPageSize());
	 
		NetworkCollect.CollectSearchParam getCollectSearchParam = getCollectList.getSearchParams();
		
		CollectSearchParam collectSearchParam = new CollectSearchParam();   
		collectSearchParam.setLevel(getCollectSearchParam.getLevel()); 
		String deviceType = getCollectSearchParam.getDeviceType().isEmpty() ? null : getCollectSearchParam.getDeviceType(); 
		collectSearchParam.setDeviceType(deviceType); 
		collectSearchParam.setDeviceName(getCollectSearchParam.getDeviceName()); 
		collectSearchParam.setBeginTime(getCollectSearchParam.getBeginTime());
		collectSearchParam.setEndTime(getCollectSearchParam.getEndTime());

		mapCollectSearchParam.put("searchParams", collectSearchParam); 
		
		pojoMessage.setBody(mapCollectSearchParam); 
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getUploadListMessage(NetworkCollect.GetUploadList getUploadList) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getUploadList); 
		
		Map<String, Object> mapCollectSearchParam = new HashMap<String, Object>();  
		mapCollectSearchParam.put("pageNumber", (Integer)getUploadList.getPageNumber());
		mapCollectSearchParam.put("pageSize", (Integer)getUploadList.getPageSize());
		 
		NetworkCollect.CollectSearchParam getCollectSearchParam = getUploadList.getSearchParams();
		
		CollectSearchParam collectSearchParam = new CollectSearchParam();  
		collectSearchParam.setLevel(getCollectSearchParam.getLevel()); 
		String deviceType = getCollectSearchParam.getDeviceType().isEmpty() ? null : getCollectSearchParam.getDeviceType(); 
		collectSearchParam.setDeviceType(deviceType); 
		collectSearchParam.setDeviceName(getCollectSearchParam.getDeviceName()); 
		collectSearchParam.setBeginTime(getCollectSearchParam.getBeginTime());
		collectSearchParam.setEndTime(getCollectSearchParam.getEndTime()); 
		mapCollectSearchParam.put("searchParams", collectSearchParam); 
		
		pojoMessage.setBody(mapCollectSearchParam);  
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getLogsMessage(NetworkCollect.GetLogs getLogs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();
		pojoMessage.setHead(MessageHead.getLogs);  
		
		Map<String, Object> mapLogSearchParas = new HashMap<String, Object>();
		mapLogSearchParas.put("pageNumber", (Integer)getLogs.getPageNumber()); 
		mapLogSearchParas.put("pageSize", (Integer)getLogs.getPageSize());
		 
		NetworkCollect.LogsSearchParam getLogsSearchParam = getLogs.getSearchParams();
		
		LogsSearchParam logsSearchParam = new LogsSearchParam(); 
		
		logsSearchParam.setLevel(getLogsSearchParam.getLevel());
		String type = null;
		if (!getLogsSearchParam.getType().isEmpty())  
			type = getLogsSearchParam.getType(); 
		logsSearchParam.setType(type);
		
		logsSearchParam.setKeyWord(getLogsSearchParam.getKeyWord());
		logsSearchParam.setSort(getLogsSearchParam.getSort());
		logsSearchParam.setStartTime(getLogsSearchParam.getStartTime());
		logsSearchParam.setEndTime(getLogsSearchParam.getEndTime());
		
		mapLogSearchParas.put("searchParams", logsSearchParam); 
		
		pojoMessage.setBody(mapLogSearchParas);
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getExportLogsMessage(NetworkCollect.ExportLogs exportLogs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();
		pojoMessage.setHead(MessageHead.exportLogs);  
		
		Map<String, Object> mapLogSearchParas = new HashMap<String, Object>(); 
		 
		NetworkCollect.LogsSearchParam exportLogsSearchParam = exportLogs.getSearchParams();
		
		LogsSearchParam logsSearchParam = new LogsSearchParam(); 
		
		logsSearchParam.setLevel(exportLogsSearchParam.getLevel());
		String type = exportLogsSearchParam.getType().isEmpty() ? null : exportLogsSearchParam.getType(); 
		logsSearchParam.setType(type); 
		logsSearchParam.setKeyWord(exportLogsSearchParam.getKeyWord());
		logsSearchParam.setSort(exportLogsSearchParam.getSort());
		logsSearchParam.setStartTime(exportLogsSearchParam.getStartTime());
		logsSearchParam.setEndTime(exportLogsSearchParam.getEndTime());
		
		mapLogSearchParas.put("searchParams", logsSearchParam); 
		
		pojoMessage.setBody(mapLogSearchParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDevicesMessage(NetworkCollect.GetDevices getDevices) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getDevices); 
		
		Map<String, Object> mapGetDevicesParas = new HashMap<String, Object>();
		mapGetDevicesParas.put("pageNumber", getDevices.getPageNumber());
		mapGetDevicesParas.put("pageSize", getDevices.getPageSize());
		
		String deviceType = getDevices.getDeviceType().isEmpty() ? null : getDevices.getDeviceType();
		mapGetDevicesParas.put("deviceType", deviceType);
		
		mapGetDevicesParas.put("keyWord", getDevices.getKeyWord()); 
		pojoMessage.setBody(mapGetDevicesParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getInsertDeviceMessage(NetworkCollect.InsertDevice insertDevice) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.insertDevice); 
		
		NetworkCollect.AssetsDeviceEntity assetsDeviceEntity = insertDevice.getAssetInfo(); 
		
		AssetsDeviceEntity AssetsDeviceEntityBody = getAssetsDeviceEntityBody(assetsDeviceEntity);
		
		pojoMessage.setBody(AssetsDeviceEntityBody);
		return pojoMessage;
	}
	 
	
	public up.common.nettypojo.Message getDeleteDeviceMessage(NetworkCollect.DeleteDevice deleteDevice) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteDevice);  
		
		pojoMessage.setBody(deleteDevice.getId());
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyDeviceMessage(NetworkCollect.ModifyDevice modifyDevice) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyDevice);  
		
		NetworkCollect.AssetsDeviceEntity assetsDeviceEntity = modifyDevice.getAssetInfo();  
		AssetsDeviceEntity AssetsDeviceEntityBody = getAssetsDeviceEntityBody(assetsDeviceEntity);
		 
		pojoMessage.setBody(AssetsDeviceEntityBody);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDevicesChartDataMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getDevicesChartData); 
		
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getOnlineDevicesChartDataMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getOnlineDevicesChartData); 
		
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getSysDataMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.Chart.getSysData); 
		
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getChartDataMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.Chart.getChartData); 
		
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getCreateUserGroupMessage(NetworkCollect.UserGroup userGroup) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.createUserGroup);
		UserGroup userGroupBody = new UserGroup();
		userGroupBody.setId(userGroup.getId()); 
		userGroupBody.setGroupname(userGroup.getGroupname());
		userGroupBody.setGroupdescription(userGroup.getGroupdescription());
		userGroupBody.setIfinner((byte)userGroup.getIfinner());
		pojoMessage.setBody(userGroupBody);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getUpdateUserGroupMessage(NetworkCollect.UserGroup userGroup) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();   
		pojoMessage.setHead(MessageHead.updateUserGroup);
		UserGroup userGroupBody = new UserGroup();
		userGroupBody.setId(userGroup.getId()); 
		userGroupBody.setGroupname(userGroup.getGroupname());
		userGroupBody.setGroupdescription(userGroup.getGroupdescription());
		userGroupBody.setIfinner((byte)userGroup.getIfinner());
		pojoMessage.setBody(userGroupBody);
		return pojoMessage;
	}
	 

	 
	public up.common.nettypojo.Message getUserLogMessage(String user, String pass) {  
		up.common.nettypojo.Message message = new up.common.nettypojo.Message();
		message.setHead(MessageHead.userLogin);
		
		byte[] encryptUser = RSACoder.handleByPublic(user.getBytes(),
				Base64.decodeBase64(RSACoder.getPublicKeyString()), true);
		byte[] encryptPass = RSACoder.handleByPublic(pass.getBytes(),
				Base64.decodeBase64(RSACoder.getPublicKeyString()), true);
		String username = bytesToHexString(encryptUser);
		String password = bytesToHexString(encryptPass);
		
		Map<String, Object> map = new HashMap<>();
		map.put("username", username);
		map.put("password", password);
		message.setBody(map);
		return message;
	}
	 

	public up.common.nettypojo.Message getInitCreateUserMessage(NetworkCollect.InitCreateUser initCreateUser) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.initCreateUser);
		
		Map<String,Object> mapInitCreateUserParas = new HashMap<String, Object>();
		mapInitCreateUserParas.put("username", initCreateUser.getUsername());
		mapInitCreateUserParas.put("flag", initCreateUser.getFlag());
		
		pojoMessage.setBody(mapInitCreateUserParas);
		return pojoMessage;
	}
	  

	public up.common.nettypojo.Message getInitDangerCommandMessage(NetworkCollect.InitDangerCommand initDangerCommand) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.initDangerCommand);
		
		Map<String, Object> mapInitDangerCommandParas = new HashMap<String, Object>();
		mapInitDangerCommandParas.put("pageNum", (Integer)initDangerCommand.getPageNumber());
		mapInitDangerCommandParas.put("pageSize", (Integer)initDangerCommand.getPageSize());
		
		pojoMessage.setBody(mapInitDangerCommandParas);
		return pojoMessage;
	}
	 
	public up.common.nettypojo.Message getAddDangerCommandMessage(NetworkCollect.AddDangerCommand addDangerCommand) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.addDangerCommand);
		 
		NetworkCollect.DangerList dangerList = addDangerCommand.getDangerInfo();
		
		DangerList DangerListBody = getDangerListBody(dangerList);
		
		pojoMessage.setBody(DangerListBody);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getUpdateDangerCommandMessage(NetworkCollect.UpdateDangerCommand updateDangerCommand) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.updateDangerCommand);
		 
		NetworkCollect.DangerList dangerList = updateDangerCommand.getDangerInfo();
		DangerList DangerListBody = getDangerListBody(dangerList);
		
		pojoMessage.setBody(DangerListBody);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDelDangerCommandMessage(NetworkCollect.DelDangerCommand delDangerCommand) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.delDangerCommand);
		 
		NetworkCollect.DangerList dangerList = delDangerCommand.getDangerInfo();
		DangerList DangerListBody = getDangerListBody(dangerList);
	
		pojoMessage.setBody(DangerListBody);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getSVRDeviceMessage(NetworkCollect.GetSvrDevices getSvrDevices) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.getSVRDevice);
		 
		Map<String, Object> mapGetSVRDeviceParas = new HashMap<String, Object>();
		mapGetSVRDeviceParas.put("pageNum", getSvrDevices.getPageNum());
		mapGetSVRDeviceParas.put("pageSize", getSvrDevices.getPageSize());
		
		pojoMessage.setBody(mapGetSVRDeviceParas);
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getStartBaseLineCheckMessage(NetworkCollect.BaselineCheck BaselineCheck) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.startBaseLineCheck);
		  
		Map<String,Object> mapStartBaseLineCheckParas = new HashMap<String, Object>();
		mapStartBaseLineCheckParas.put("ip", BaselineCheck.getIp());
		
		pojoMessage.setBody(mapStartBaseLineCheckParas); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getStopBaseLineCheckMessage(NetworkCollect.BaselineCheck BaselineCheck) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.stopBaseLineCheck);
		  
		Map<String,Object> mapStopBaseLineCheckParas = new HashMap<String, Object>();
		mapStopBaseLineCheckParas.put("uuid", BaselineCheck.getTaskUuid());
		
		pojoMessage.setBody(mapStopBaseLineCheckParas); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getBaseLineCheckResultMessage(NetworkCollect.BaselineCheck BaselineCheck) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.getBaseLineCheckResult);
		  
		Map<String,Object> mapGetBaseLineCheckResultParas = new HashMap<String, Object>();
		mapGetBaseLineCheckResultParas.put("uuid", BaselineCheck.getTaskUuid());
		
		pojoMessage.setBody(mapGetBaseLineCheckResultParas); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getBaseLineCheckContentMessage(NetworkCollect.BaselineCheck BaselineCheck) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.getBaseLineCheckContent);
		  
		Map<String,Object> mapGetBaseLineCheckContentParas = new HashMap<String, Object>();
		mapGetBaseLineCheckContentParas.put("uuid", BaselineCheck.getTaskUuid());
		
		pojoMessage.setBody(mapGetBaseLineCheckContentParas); 
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getBaseLineCheckHisContentMessage(String deviceIp) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.getBaseLineCheckHisContent);
		
		Map<String, String> mapDeviceIp = new HashMap<String, String>();
		mapDeviceIp.put("ip", deviceIp);
		pojoMessage.setBody(mapDeviceIp);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeleteBaseLinkCheckResultMessage(NetworkCollect.BaselineCheck BaselineCheck) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message(); 
		pojoMessage.setHead(MessageHead.deleteBaseLinkCheckResult);
		  
		Map<String,Object> mapDeleteBaseLinkCheckResultParas = new HashMap<String, Object>();
		mapDeleteBaseLinkCheckResultParas.put("uuid", BaselineCheck.getTaskUuid());
		
		pojoMessage.setBody(mapDeleteBaseLinkCheckResultParas); 
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getBackupDataDateMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getBackupDataDate);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getBackupDatabaseMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.backupDatabase); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getRecoverDatabaseMessage(String versionInfo) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.recoverDatabase);
		
		pojoMessage.setBody(versionInfo);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeleteDataBackupMessage(String versionInfo) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteDataBackup);
		
		pojoMessage.setBody(versionInfo);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getBackupConfDateMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getBackupConfDate);
		return pojoMessage;
	}

	public up.common.nettypojo.Message getBackupConfigureMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.backupConfigure);
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getRecoverConfigureMessage(String version) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.recoverConfigure);
		
		pojoMessage.setBody(version);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeleteConfigureMessage(String version) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteConfBackup);
		
		pojoMessage.setBody(version);
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getGenerateLoginInfoMessage(String username, int subType) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.generateLoginInfo);
		
		Map<String, Object> mapGenerateLoginInfoParam = new HashMap<String, Object>();
		mapGenerateLoginInfoParam.put("username", username); 
		mapGenerateLoginInfoParam.put("subType", subType);
		pojoMessage.setBody(mapGenerateLoginInfoParam);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getInsertLogMessage(NetworkCollect.LogsEntity logsEntity) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.insertLog);
		LogsEntity logsEntityBody = new LogsEntity();
		logsEntityBody.setId(logsEntity.getId());
		logsEntityBody.setType(logsEntity.getType());
		logsEntityBody.setLevel(logsEntity.getLevel());
		logsEntityBody.setTargetObj(logsEntity.getTargetObj());
		logsEntityBody.setActionDesc(logsEntity.getActionDesc());
		logsEntityBody.setActionTime(logsEntity.getActionTime());
		logsEntityBody.setLastmodifytime(logsEntity.getLastmodifytime());
		pojoMessage.setBody(logsEntityBody);
		return pojoMessage;
	}
	 
	public up.common.nettypojo.Message getInitCreateRoleMessage(NetworkCollect.InitCreateRole initCreateRole) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.initCreateRole);
		 
		Map<String,Object> mapInitCreateRoleParas = new HashMap<String, Object>();
		mapInitCreateRoleParas.put("rolename", initCreateRole.getRolename());
		mapInitCreateRoleParas.put("flag", initCreateRole.getFlag());
		
		pojoMessage.setBody(mapInitCreateRoleParas);
		
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getAllUserGroupMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getAllUserGroup);
		 
		return pojoMessage;
	} 

	public up.common.nettypojo.Message getAllUserMessage(NetworkCollect.GetAllUser getAllUser) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getAllUser);
		
		Map<String,Object> mapGetAllUserSearchParas = new HashMap<String,Object>();
		mapGetAllUserSearchParas.put("orderType", getAllUser.getOrderType());
		mapGetAllUserSearchParas.put("groupName", getAllUser.getGroupName());
		mapGetAllUserSearchParas.put("username", getAllUser.getUsername());
		mapGetAllUserSearchParas.put("pageNumber", getAllUser.getPageNumber());
		mapGetAllUserSearchParas.put("pageSize", getAllUser.getPageSize());
		
		pojoMessage.setBody(mapGetAllUserSearchParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getCreateUserMessage(NetworkCollect.UserEntity createUserEntity) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();   
		pojoMessage.setHead(MessageHead.createUser); 
		
		UserEntity userEntityBody = getUserEntityBody(createUserEntity);
		
		pojoMessage.setBody(userEntityBody);
		return pojoMessage;
	} 
	
	public up.common.nettypojo.Message getDeleteUserMessage(List<String> listUserName) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteUser);
		
		List<String> tmpUserNameList = new ArrayList<String>();
		for (int i = 0; i < listUserName.size(); i++)  
			tmpUserNameList.add(listUserName.get(i));
 
		Map<String, Object> mapDeleteUserParas = new HashMap<String, Object>();
		mapDeleteUserParas.put("list", tmpUserNameList);
		
		pojoMessage.setBody(mapDeleteUserParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getUpdateUserMessage(NetworkCollect.UserEntity updateUserEntity) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.updateUser);
		
		UserEntity userEntityBody = getUserEntityBody(updateUserEntity);
		
		pojoMessage.setBody(userEntityBody);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getUnlockUserMessage(NetworkCollect.UnlockUser unlockUser) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.unlockUser);
		 
		Map<String,Object> mapUnlockUserParas = new HashMap<String, Object>();
		mapUnlockUserParas.put("username", unlockUser.getUsername());
		
		pojoMessage.setBody(mapUnlockUserParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyPasswordByAdminMessage(NetworkCollect.ModifyPasswordByAdmin modifyPasswordByAdmin) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyPasswordByAdmin);
		  
		byte[] encryptPass = RSACoder.handleByPublic(modifyPasswordByAdmin.getPassword().getBytes(),
				Base64.decodeBase64(RSACoder.getPublicKeyString()), true); 
		String password = bytesToHexString(encryptPass);
		 
		Map<String, Object> mapModifyPasswordParas = new HashMap<>();
		mapModifyPasswordParas.put("username", modifyPasswordByAdmin.getUsername());
		mapModifyPasswordParas.put("password", password);
		
		pojoMessage.setBody(mapModifyPasswordParas);
		 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyPasswordMessage(NetworkCollect.ModifyPassword modifyPassword) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyPassword);
		  
		byte[] encryptOriPassEncrypt = RSACoder.handleByPublic(modifyPassword.getOriPassEncrypt().getBytes(),
				Base64.decodeBase64(RSACoder.getPublicKeyString()), true);
		byte[] encryptNewPassEncrypt = RSACoder.handleByPublic(modifyPassword.getNewPassEncrypt().getBytes(),
				Base64.decodeBase64(RSACoder.getPublicKeyString()), true); 
		 
		String oriPassEncrypt = bytesToHexString(encryptOriPassEncrypt);
		String newPassEncrypt = bytesToHexString(encryptNewPassEncrypt); 
		 
		Map<String, Object> mapModifyPasswordParas = new HashMap<>();
		mapModifyPasswordParas.put("username", modifyPassword.getUsername());
		mapModifyPasswordParas.put("oriPassEncrypt", oriPassEncrypt);
		mapModifyPasswordParas.put("newPassEncrypt", newPassEncrypt);
		mapModifyPasswordParas.put("isFisrtLogin", modifyPassword.getIsFisrtLogin());
		
		pojoMessage.setBody(mapModifyPasswordParas);
		 
		return pojoMessage;
	}
 
	 
	public up.common.nettypojo.Message getDeleteUserGroupMessage(String groupName) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteUserGroup);
		pojoMessage.setBody(groupName); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getAllRoleMessage(String rolename, int pageNumber, int pageSize) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getAllRole);
		
		Map<String, Object> mapGetAllSearchParas = new HashMap<String, Object>();
		mapGetAllSearchParas.put("rolename", rolename);
		mapGetAllSearchParas.put("pageNumber", pageNumber);
		mapGetAllSearchParas.put("pageSize", pageSize);
		
		pojoMessage.setBody(mapGetAllSearchParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getCreateRoleMessage(NetworkCollect.RoleEntity roleEntity, List<Integer> listUserId) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.createRole); 
		 
		RoleEntity roleEntityBody = getRoleEntityBody(roleEntity);
		Map<String, Object> mapCreateRoleParas = new HashMap<String, Object>();
		mapCreateRoleParas.put("role", roleEntityBody);
		 
		List<Integer> tmpUserIdList = new ArrayList<Integer>();
		for (int i = 0; i < listUserId.size(); i++) {
			tmpUserIdList.add(listUserId.get(i));
		}
		mapCreateRoleParas.put("addUser", tmpUserIdList);
		
		pojoMessage.setBody(mapCreateRoleParas);
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeleteRoleMessage(List<String> listDeleteRoleName) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteRole);
		
		List<String> tmpDeleteRoleNameList = new ArrayList<String>();
		for (int i = 0; i < listDeleteRoleName.size(); i++) {
			tmpDeleteRoleNameList.add(listDeleteRoleName.get(i));
		} 
		
		pojoMessage.setBody((Object)tmpDeleteRoleNameList);
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getUpdateRoleMessage(NetworkCollect.UpdateRole updateRole) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.updateRole);
		
		Map<String, Object> mapUpdateRoleParas = new HashMap<String, Object>();
		
		RoleEntity roleEntityBody = getRoleEntityBody(updateRole.getRoleInfo());
		mapUpdateRoleParas.put("role", roleEntityBody);
		
		List<Integer> tmpAddUserIdList = new ArrayList<Integer>();
		for (int i = 0; i < updateRole.getAddUserIdListList().size(); i++) {
			tmpAddUserIdList.add(updateRole.getAddUserIdListList().get(i));
		}
		mapUpdateRoleParas.put("addUser", tmpAddUserIdList);
		
		List<Integer> tmpDelUserIdList = new ArrayList<Integer>();
		for (int i = 0; i < updateRole.getDelUserIdListList().size(); i++) {
			tmpDelUserIdList.add(updateRole.getDelUserIdListList().get(i));
		}
		mapUpdateRoleParas.put("delUser", tmpDelUserIdList);
		
		pojoMessage.setBody(mapUpdateRoleParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getUpdateUsersRoleIdMessage(NetworkCollect.UpdateUsersRoleId updateUsersRoleId) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.updateUsersRoleId);
		
		Map<String, Object> mapUpdateUsersRoleIdParas = new HashMap<String, Object>();

		mapUpdateUsersRoleIdParas.put("roleId", updateUsersRoleId.getRoleId());
		
		List<Integer> tmpAddUserIdList = new ArrayList<Integer>();
		for (int i = 0; i < updateUsersRoleId.getAddUserIdListList().size(); i++) {
			tmpAddUserIdList.add(updateUsersRoleId.getAddUserIdListList().get(i));
		}
		mapUpdateUsersRoleIdParas.put("addUser", tmpAddUserIdList);
		
		List<Integer> tmpDelUserIdList = new ArrayList<Integer>();
		for (int i = 0; i < updateUsersRoleId.getDelUserIdListList().size(); i++) {
			tmpDelUserIdList.add(updateUsersRoleId.getDelUserIdListList().get(i));
		}
		mapUpdateUsersRoleIdParas.put("delUser", tmpDelUserIdList);
		 
		
		pojoMessage.setBody(mapUpdateUsersRoleIdParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getSystemUpdateMessage(NetworkCollect.SystemUpdate systemUpdate) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.systemUpdate);
		
		Map<String, Object> mapSystemUpdateParas = new HashMap<String, Object>();
		  
		mapSystemUpdateParas.put("content", systemUpdate.getFileContent().toByteArray());
		mapSystemUpdateParas.put("md5", systemUpdate.getFileMd5().toByteArray());
		
		pojoMessage.setBody(mapSystemUpdateParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getRestartMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.restart);
		 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getVersionInfoMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getVersionInfo); 
		
		return pojoMessage;
	}
	  
	
	public up.common.nettypojo.Message getSystemConfigsMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getSystemConfigs); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifySystemConfigsMessage(NetworkCollect.ModifySystemConfigs modifySystemConfigs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifySystemConfigs); 
		
		Map<String, Object> mapModifySystemConfigsParas = new HashMap<String, Object>();
		mapModifySystemConfigsParas.put("idx", modifySystemConfigs.getIdx());
		
		NetworkCollect.SystemConfigInfo systemConfigInfo = modifySystemConfigs.getConfigInfo();
		
		Map<String, String> mapSystemConfigInfoParas = new HashMap<String, String>();
		mapSystemConfigInfoParas.put("loginfailedwithin", systemConfigInfo.getLoginfailedwithin());
		mapSystemConfigInfoParas.put("loginfailedtime", systemConfigInfo.getLoginfailedtime());
		mapSystemConfigInfoParas.put("passwordvalidwarntime", systemConfigInfo.getPasswordvalidwarntime());
		mapSystemConfigInfoParas.put("accountlockedtime", systemConfigInfo.getAccountlockedtime());
		mapSystemConfigInfoParas.put("passwordvalidtime", systemConfigInfo.getPasswordvalidtime());
		mapSystemConfigInfoParas.put("historicalrepetition", systemConfigInfo.getHistoricalrepetition());
		mapSystemConfigInfoParas.put("operationloghold", systemConfigInfo.getOperationloghold());
		mapSystemConfigInfoParas.put("monitorloghold", systemConfigInfo.getMonitorloghold());
		mapSystemConfigInfoParas.put("uploadloghold", systemConfigInfo.getUploadloghold());
		mapSystemConfigInfoParas.put("snmptrapip", systemConfigInfo.getSnmptrapip());
		
		mapModifySystemConfigsParas.put("objectMap", mapSystemConfigInfoParas);
		
		NetworkCollect.UserEntity userEntity = modifySystemConfigs.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);
		
		mapModifySystemConfigsParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapModifySystemConfigsParas);
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getEventConfigsMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getEventConfigs); 
		
		return pojoMessage;
	}
	
	
	public up.common.nettypojo.Message getModifyEventConfigsMessage(NetworkCollect.ModifyEventConfigs modifyEventConfigs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyEventConfigs); 
		
		Map<String, Object> mapModifyEventConfigsParas = new HashMap<String, Object>();
		mapModifyEventConfigsParas.put("idx", modifyEventConfigs.getIdx());
		 
		NetworkCollect.EventConfigInfo eventConfigInfo = modifyEventConfigs.getConfigInfo();
		Map<String, String> mapEventConfigInfoParas = new HashMap<String, String>();
		mapEventConfigInfoParas.put("cputhreshold", eventConfigInfo.getCputhreshold());
		mapEventConfigInfoParas.put("memorythreshold", eventConfigInfo.getMemorythreshold());
		mapEventConfigInfoParas.put("flowthreshold", eventConfigInfo.getFlowthreshold());
		mapEventConfigInfoParas.put("mergeperiod", eventConfigInfo.getMergeperiod());
		mapEventConfigInfoParas.put("loginfailedtime", eventConfigInfo.getLoginfailedtime());
		mapEventConfigInfoParas.put("diskthreshold", eventConfigInfo.getDiskthreshold());
		mapEventConfigInfoParas.put("uploaddividing", eventConfigInfo.getUploaddividing());
//		mapEventConfigInfoParas.put("logsnumhold", eventConfigInfo.getLogsnumhold());

		mapModifyEventConfigsParas.put("objectMap", mapEventConfigInfoParas);
		
		NetworkCollect.UserEntity userEntity = modifyEventConfigs.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity); 
		
		mapModifyEventConfigsParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapModifyEventConfigsParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getNtpConfigsMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getNtpConfigs); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyNtpConfigsMessage(NetworkCollect.ModifyNtpConfigs modifyNtpConfigs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyNtpConfigs); 
		
		Map<String, Object> mapModifyNtpConfigsParas = new HashMap<String, Object>();
		mapModifyNtpConfigsParas.put("idx", modifyNtpConfigs.getIdx());
		
		NetworkCollect.NtpConfigInfo NtpConfigInfo = modifyNtpConfigs.getConfigInfo();
		Map<String, String> mapNtpConfParas = new HashMap<String, String>();
		mapNtpConfParas.put("main_neta", NtpConfigInfo.getMainNeta());
		mapNtpConfParas.put("main_netb", NtpConfigInfo.getMainNeta());
		mapNtpConfParas.put("alternate_neta", NtpConfigInfo.getMainNeta());
		mapNtpConfParas.put("alternate_netb", NtpConfigInfo.getAlternateNetb());
		mapNtpConfParas.put("port", NtpConfigInfo.getPort());
		mapNtpConfParas.put("peroid", NtpConfigInfo.getPeroid());
		mapNtpConfParas.put("broadcast", NtpConfigInfo.getBroadcast());
		
		mapModifyNtpConfigsParas.put("objectMap", mapNtpConfParas);
		
		NetworkCollect.UserEntity userEntity =  modifyNtpConfigs.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);
		mapModifyNtpConfigsParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapModifyNtpConfigsParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getCommuConfigsMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getCommuConfigs); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyCommuConfigsMessage(NetworkCollect.ModifyCommuConfigs modifyCommuConfigs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyCommuConfigs); 
		
		Map<String, Object> mapModifyCommuConfigsParas = new HashMap<String, Object>();
		mapModifyCommuConfigsParas.put("idx", modifyCommuConfigs.getIdx());
		 
		NetworkCollect.CommuConfigInfo commuConfigInfo = modifyCommuConfigs.getConfigInfo();
		Map<String, String> mapCommuConfigsParas = new HashMap<String, String>();
		mapCommuConfigsParas.put("workstation", commuConfigInfo.getWorkstation());
		mapCommuConfigsParas.put("security", commuConfigInfo.getSecurity());
		mapCommuConfigsParas.put("network", commuConfigInfo.getNetwork());
		mapCommuConfigsParas.put("agent", commuConfigInfo.getAgent());
		mapModifyCommuConfigsParas.put("objectMap", mapCommuConfigsParas);
		
		List<Object> listObjects = new ArrayList<Object>();
		List<NetworkCollect.PlatformInfo> listPlatformInfos = modifyCommuConfigs.getPlatInfoList();
		for (int i = 0; i < listPlatformInfos.size(); i++) {
			NetworkCollect.PlatformInfo platformInfo = listPlatformInfos.get(i);
			
			Platform platform = new Platform();
			platform.setId(platformInfo.getId());
			platform.setIpaddress(platformInfo.getIpaddress());
			platform.setPublicKey(platformInfo.getPublicKey());
			platform.setEventport(platformInfo.getEventport());
			platform.setPermission(platformInfo.getPermission());
			platform.setGroup(platformInfo.getGroup());
			
			listObjects.add(platform);
		}
		
		mapModifyCommuConfigsParas.put("objectList", listObjects);
		 
		NetworkCollect.UserEntity userEntity =  modifyCommuConfigs.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);
		mapModifyCommuConfigsParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapModifyCommuConfigsParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeleteCommunicationMessage(String ip) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteCommunication);  
		
		pojoMessage.setBody(ip);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getNetworkInterfacesMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getNetworkInterfaces); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyNetworkInterfacesMessage(NetworkCollect.ModifyNetworkInterfaces modifyNetworkInterfaces) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyNetworkInterfaces); 
		
		Map<String, Object> mapModifyNetworkInterfacesParas = new HashMap<String, Object>();
		mapModifyNetworkInterfacesParas.put("idx", modifyNetworkInterfaces.getIdx());
		
		List<Map<String, String>> listNetworkInterfaces = new ArrayList<Map<String, String>>();
		List<NetworkCollect.NetworkConfigInfo> listModifyNetworkInterfaces = modifyNetworkInterfaces.getConfigInfoList();
		for (int i = 0; i < listModifyNetworkInterfaces.size(); i++) {
			NetworkCollect.NetworkConfigInfo networkConfigInfo = listModifyNetworkInterfaces.get(i);
			
			Map<String, String> mapNetworkInterfacesParas = new HashMap<String, String>();
			mapNetworkInterfacesParas.put("interfacename", networkConfigInfo.getName());
			mapNetworkInterfacesParas.put("hostaddr", networkConfigInfo.getHostaddr());
			mapNetworkInterfacesParas.put("subnetmask", networkConfigInfo.getSubnetmask());
			
			listNetworkInterfaces.add(mapNetworkInterfacesParas);
		}
		
		mapModifyNetworkInterfacesParas.put("list", listNetworkInterfaces);
		
		NetworkCollect.UserEntity userEntity = modifyNetworkInterfaces.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);
		mapModifyNetworkInterfacesParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapModifyNetworkInterfacesParas);
		return pojoMessage;
	}  
	
	public up.common.nettypojo.Message getGatewaysMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getGateways); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getAddGatewaysMessage(NetworkCollect.AddGateways addGateways) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.addGateways); 
		
		Map<String, Object> mapAddGatewaysParas = new HashMap<String, Object>(); 
		
		List<NetworkCollect.GatewaysInfo> listGatewaysInfos = addGateways.getGatewaysInfoListList(); 
		NetworkCollect.GatewaysInfo gatewaysInfo = listGatewaysInfos.get(0);
		
		Map<String, String> mapGatewaysParas = new HashMap<String, String>();
		mapGatewaysParas.put("id", gatewaysInfo.getId());
		mapGatewaysParas.put("destination", gatewaysInfo.getDestination());
		mapGatewaysParas.put("mask", gatewaysInfo.getMask());
		mapGatewaysParas.put("gateway", gatewaysInfo.getGateway());
		mapGatewaysParas.put("interfacename", gatewaysInfo.getNetwork());
		mapGatewaysParas.put("flags", gatewaysInfo.getFlags()); 
	 
		mapAddGatewaysParas.put("objectMap", mapGatewaysParas);
		
		NetworkCollect.UserEntity userEntity = addGateways.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);

		mapAddGatewaysParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapAddGatewaysParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDelGatewaysMessage(NetworkCollect.DelGateways delGateways) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.delGateways); 
		
		Map<String, Object> mapDelGatewaysParas = new HashMap<String, Object>();
		
		List<Map<String, String>> listGatewaysParas = new ArrayList<Map<String, String>>();

		List<NetworkCollect.GatewaysInfo> listGatewaysInfos = delGateways.getGatewaysInfoListList();
		for (int i = 0; i < listGatewaysInfos.size(); i++) {
			NetworkCollect.GatewaysInfo gatewaysInfo = listGatewaysInfos.get(i);
			
			Map<String, String> mapGatewaysParas = new HashMap<String, String>();
			mapGatewaysParas.put("id", gatewaysInfo.getId());
			mapGatewaysParas.put("destination", gatewaysInfo.getDestination());
			mapGatewaysParas.put("mask", gatewaysInfo.getMask());
			mapGatewaysParas.put("gateway", gatewaysInfo.getGateway());
			mapGatewaysParas.put("interfacename", gatewaysInfo.getNetwork());
			mapGatewaysParas.put("flags", gatewaysInfo.getFlags());
			
			listGatewaysParas.add(mapGatewaysParas);
		}
	 
		mapDelGatewaysParas.put("objectMap", listGatewaysParas);
		
		NetworkCollect.UserEntity userEntity = delGateways.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);

		mapDelGatewaysParas.put("user", userEntityBody);
		
		pojoMessage.setBody(mapDelGatewaysParas);
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyGatewaysMessage(NetworkCollect.ModifyGateways modifyGateways) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyGateways); 
		
		Map<String, Object> mapModifyGateways = new HashMap<String, Object>();
		mapModifyGateways.put("idx", modifyGateways.getIdx());
		
		List<Map<String, String>> listModifyGateways = new ArrayList<Map<String,String>>();
		List<NetworkCollect.GatewaysInfo> listGatewaysInfo = modifyGateways.getGatewaysInfoListList();
		for (int i = 0; i < listGatewaysInfo.size(); i++) {
			NetworkCollect.GatewaysInfo gatewaysInfo = listGatewaysInfo.get(i);
			
			Map<String, String> mapGatewaysInfoParas = new HashMap<String, String>();
			mapGatewaysInfoParas.put("id", gatewaysInfo.getId());
			mapGatewaysInfoParas.put("destination", gatewaysInfo.getDestination());
			mapGatewaysInfoParas.put("mask", gatewaysInfo.getMask());
			mapGatewaysInfoParas.put("gateway", gatewaysInfo.getGateway());
			mapGatewaysInfoParas.put("interfacename", gatewaysInfo.getNetwork());
			mapGatewaysInfoParas.put("flags", gatewaysInfo.getFlags());
			
			listModifyGateways.add(mapGatewaysInfoParas);
		}
		mapModifyGateways.put("list", listModifyGateways);
		
		NetworkCollect.UserEntity userEntity = modifyGateways.getUserInfo();
		UserEntity userEntityBody = getUserEntityBody(userEntity);
		mapModifyGateways.put("user", userEntityBody);
		
		pojoMessage.setBody(mapModifyGateways);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeviceInfoConfigsMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.getDeviceInfoConfigs); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getModifyDeviceInfoConfigsMessage(NetworkCollect.ModifyDeviceInfoConfigs modifyDeviceInfoConfigs) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.modifyDeviceInfoConfigs); 
		
		Map<String, Object> mapModifyDeviceInfoConfigsParas = new HashMap<String, Object>();
		mapModifyDeviceInfoConfigsParas.put("idx", modifyDeviceInfoConfigs.getIdx());
		
		NetworkCollect.DeviceInfoConfigInfo deviceInfoConfigInfo = modifyDeviceInfoConfigs.getConfigInfo();
		
		Map<String, String> deviceInfoConfigsParas = new HashMap<String, String>();
		deviceInfoConfigsParas.put("devicename", deviceInfoConfigInfo.getNameAndVersion());
		deviceInfoConfigsParas.put("privateKey", deviceInfoConfigInfo.getKeyWord());
		
		mapModifyDeviceInfoConfigsParas.put("deviceInfo", deviceInfoConfigsParas);
		
		pojoMessage.setBody(mapModifyDeviceInfoConfigsParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getInitClientIPPanelMessage(NetworkCollect.InitClientIPPanel initClientIPPanel) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.initClientIPPanel); 
		
		Map<String,Object> mapInitClientIPPanelParas = new HashMap<String, Object>();
		mapInitClientIPPanelParas.put("pageNum", initClientIPPanel.getPageNum());
		mapInitClientIPPanelParas.put("pageSize", initClientIPPanel.getPageSize());
		
		pojoMessage.setBody(mapInitClientIPPanelParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getAddClientIPMessage(String clientIp) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.addClientIP); 
		
		ClientIP clientIPBody = new ClientIP();
		clientIPBody.setIp(clientIp);
		
		pojoMessage.setBody(clientIPBody); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDelClientIPMessage(NetworkCollect.DelClientIP delClientIP) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.delClientIP); 
		
		NetworkCollect.ClientIP clientIP = delClientIP.getIpInfo();
		ClientIP clientIPBody = new ClientIP();
		clientIPBody.setId(clientIP.getId());
		clientIPBody.setIp(clientIP.getIp());
		clientIPBody.setCreatetime(clientIP.getCreatetime());
		
		pojoMessage.setBody(clientIPBody); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getInitCertificatePanelMessage() {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.initCertificatePanel); 
		
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getImportCertificateMessage(NetworkCollect.ImportCertificate importCertificate) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.importCertificate); 
		
		Map<String,Object> mapImportCertificateParas = new HashMap<String, Object>();
		
		UserEntity userEntityBody = getUserEntityBody(importCertificate.getUserInfo());
		mapImportCertificateParas.put("user", userEntityBody);
		
		CertificateEntity certificateEntity = getCertificateEntityBody(importCertificate.getEntity()); 
		mapImportCertificateParas.put("entity", certificateEntity);
		
		mapImportCertificateParas.put("type", importCertificate.getType());
		
		mapImportCertificateParas.put("ip", importCertificate.getIp());
		
		pojoMessage.setBody(mapImportCertificateParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getUpdateCertificateMessage(NetworkCollect.UpdateCertificate updateCertificate) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.updateCertificate); 
		 
		Map<String, Object> mapUpdateCertificateParas = new HashMap<String, Object>();
		
		UserEntity userEntity = getUserEntityBody(updateCertificate.getUserInfo());  
		mapUpdateCertificateParas.put("user", userEntity);
		
		CertificateEntity certificateEntity = getCertificateEntityBody(updateCertificate.getEntity()); 
		mapUpdateCertificateParas.put("entity", certificateEntity);
		
		mapUpdateCertificateParas.put("type", updateCertificate.getType());
		mapUpdateCertificateParas.put("ip", updateCertificate.getIp());
		mapUpdateCertificateParas.put("id", updateCertificate.getId());
		
		pojoMessage.setBody(mapUpdateCertificateParas); 
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getDeleteCertificateMessage(NetworkCollect.DeleteCertificate deleteCertificate) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.deleteCertificate); 
		
		Map<String,Object> mapDeleteCertificateParas = new HashMap<String, Object>();
		
		UserEntity userEntityBody = getUserEntityBody(deleteCertificate.getUserInfo());
		mapDeleteCertificateParas.put("user", userEntityBody);
		 
		mapDeleteCertificateParas.put("id", deleteCertificate.getId());
		
		mapDeleteCertificateParas.put("type", deleteCertificate.getType()); 
		
		pojoMessage.setBody(mapDeleteCertificateParas);
		return pojoMessage;
	}
	
	public up.common.nettypojo.Message getExportCertificateMessage(NetworkCollect.ExportCertificate exportCertificate) {
		up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
		pojoMessage.setHead(MessageHead.exportCertificate); 
		
		Map<String,Object> mapExportCertificateParas = new HashMap<String, Object>(); 
		
		mapExportCertificateParas.put("id", exportCertificate.getId());
		
		mapExportCertificateParas.put("filePath", exportCertificate.getFilePath()); 
		
		mapExportCertificateParas.put("fileName", exportCertificate.getFileName()); 
		
		pojoMessage.setBody(mapExportCertificateParas);
		return pojoMessage;
	}
}



//public up.common.nettypojo.Message Message() {
//	up.common.nettypojo.Message pojoMessage = new up.common.nettypojo.Message();  
//	pojoMessage.setHead(MessageHead.userLogin);
//	return pojoMessage;
//}

 
