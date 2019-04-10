package up.common.tool.request;

public interface MessageHead {

	public static final String userLogin = "userLogin";
	public static final String getUserByUserName = "getUserByUserName";
	public static final String getUserRolePriv = "getUserRolePriv";
	public static final String getRoleById = "getRoleById";
	public static final String getAllUserGroup = "getAllUserGroup";
	public static final String getAllUser = "getAllUser";
	public static final String deleteUser = "deleteUser";
	public static final String createUser = "createUser";
	public static final String updateUser = "updateUser";

	public static final String queryUser = "queryUser";
	public static final String deleteUserGroup = "deleteUserGroup";
	public static final String createUserGroup = "createUserGroup";
	public static final String updateUserGroup = "updateUserGroup";
	public static final String getUserGroupByName = "getUserGroupByName";
	public static final String confirmAuthen = "confirmAuthen";
	public static final String unlockUser = "unlockUser";
	public static final String initCreateUser = "initCreateUser";
	public static final String initCreateRole = "initCreateRole";
	public static final String initAssociateUsers = "initAssociateUsers";
	public static final String updateUsersRoleId = "updateUsersRoleId";
	public static final String modifyPasswordByAdmin = "modifyPasswordByAdmin";
	public static final String modifyPassword = "modifyPassword";

	public static final String getLogs = "getLogs";
	public static final String exportLogs = "exportLogs";
	public static final String cleanOrRecoverLogs = "cleanOrRecoverLogs";
	public static final String insertLog = "insertLog";
	public static final String getIsCover = "getIsCover";
	public static final String setIsCoverLogs = "setIsCoverLogs";
	public static final String checkLogsAccount = "checkLogsAccount";
	public static final String backupLogsTable = "backupLogsTable";
	public static final String exportLogsChart = "exportLogsChart";
	public static final String getCollectNumber = "getCollectNumber";
	public static final String getCollectList = "getCollectList";

	public static final String createRole = "createRole";
	public static final String updateRole = "updateRole";
	public static final String deleteRole = "deleteRole";
	public static final String getAllRole = "getAllRole";
	public static final String getRoleByName = "getRoleByName";
	
	public static final String systemUpdate = "systemUpdate";
	public static final String restart = "restart";
	
	public static final String backupDatabase = "backupDatabase";
	public static final String recoverDatabase = "recoverDatabase";
	public static final String backupConfigure = "backupConfigure";
	public static final String recoverConfigure = "recoverConfigure";
	public static final String deleteDataBackup = "deleteDataBackup";
	public static final String deleteConfBackup = "deleteConfBackup";
	public static final String getBackupDataDate = "getBackupDataDate";
	public static final String getBackupConfDate = "getBackupConfDate";
	public static final String initBackupPanel = "initBackupPanel";

	public static final String getSystemConfigs = "getSystemConfigs";
	public static final String modifySystemConfigs = "modifySystemConfigs";
	public static final String getNetworkInterfaces = "getNetworkInterfaces";
	public static final String modifyNetworkInterfaces = "modifyNetworkInterfaces";
	public static final String getGateways = "getGateways";
	public static final String modifyGateways = "modifyGateways";
	public static final String getCommuConfigs = "getCommuConfigs";
	public static final String modifyCommuConfigs = "modifyCommuConfigs";
	public static final String getEventConfigs = "getEventConfigs";
	public static final String modifyEventConfigs = "modifyEventConfigs";
	public static final String getNtpConfigs = "getNtpConfigs";
	public static final String modifyNtpConfigs = "modifyNtpConfigs";
	public static final String getBackupConfigs = "getBackupConfigs";
	public static final String modifyBackupConfigs = "modifyBackupConfigs";
	public static final String modifyWhiteList = "modifyWhiteList";
	public static final String addGateways = "addGateways";
	public static final String delGateways = "delGateways";
	public static final String importCertificate = "importCertificate";
	public static final String exportCertificate = "exportCertificate";
	public static final String updateCertificate = "updateCertificate";
	public static final String deleteCertificate = "deleteCertificate";
	public static final String initCertificatePanel  = "initCertificatePanel";
	public static final String initClientIPPanel = "initClientIPPanel";
	public static final String addClientIP  = "addClientIP";
	public static final String delClientIP = "delClientIP";
	public static final String getAllEventWarning = "getAllEventWarning";
	public static final String deleteEventWarning = "deleteEventWarning";
	public static final String insertEventWarning = "insertEventWarning";
	public static final String updateEventWarning = "updateEventWarning";
	public static final String queryEventWarning = "queryEventWarning";
	public static final String getDeviceInfoConfigs = "getDeviceInfoConfigs";
	public static final String modifyDeviceInfoConfigs = "modifyDeviceInfoConfigs";

	public static final String getDevices = "getDevices";
	public static final String insertDevice = "insertDevice";
	public static final String deleteDevice = "deleteDevice";
	public static final String modifyDevice = "modifyDevice";
	public static final String selectDeviceById = "selectDeviceById";
    public static final String setIsWhite = "setIsWhite";
    public static final String getIsWhite = "getIsWhite";
    public static final String queryDeviceByIp = "queryDeviceByIp";
    public static final String getDevicesChartData = "getDevicesChartData";
    public static final String getOnlineDevicesChartData = "getOnlineDevicesChartData";
    public static final String getcategoryChartData = "getcategoryChartData";
    public static final String 	setIsOnline = "setIsOnline";

    public static final String getCertificateById = "getCertificateById";
    public static final String delCertificateById = "delCertificateById";
    public static final String importCertificateWithIp = "importCertificateWithIp";
    public static final String getCertificates = "getCertificates";
	public static final String getUploadList = "getUploadList";

	public static final String getSVRDevice = "getSVRDevice";
	public static final String startBaseLineCheck = "startBaseLineCheck";
	public static final String stopBaseLineCheck = "stopBaseLineCheck";
	public static final String getBaseLineCheckResult = "getBaseLineCheckResult";
	public static final String getBaseLineCheckContent = "getBaseLineCheckContent";
	public static final String getBaseLineCheckHisContent = "getBaseLineCheckHisContent";
	public static final String deleteBaseLinkCheckResult = "deleteBaseLinkCheckResult";

	public static final String generateLoginInfo = "generateLoginInfo";
	public static final String generateConfigurationChangeInfo = "generateConfigurationChangeInfo";
	public static final String getVersionInfo = "getVersionInfo";

	public static final String initDangerCommand = "initDangerCommand";
	public static final String addDangerCommand  = "addDangerCommand";
	public static final String updateDangerCommand  = "updateDangerCommand";
	public static final String delDangerCommand = "delDangerCommand";
	public static final String deleteCommunication = "deleteCommunication";

	interface Chart {
		String getChartData = "getChartData";
		String getSysData = "getSysData";
	}

}
