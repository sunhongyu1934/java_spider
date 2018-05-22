package dama;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

public class qqchaoren {
	static
	{
		System.loadLibrary("JDC");
	}
	//按本地图片路径识别
	public native static String JRecYZM_A(String strYZMPath,String strVcodeUser,String strVcodePass,String strsoftkey);
	//上传图片字节数组识别
	public native static String JRecByte_A(byte[] img,int len,String strVcodeUser,String  strVcodePass,String strSoftkey);
	//查询剩余点数
	public native static String JGetUserInfo(String strUser,String strPass);
	//报告错误验证码
	public native static void JReportError(String strVcodeUser,String strYzmId);
	//读本地图片至字节数组
	public static byte[] toByteArrayFromFile(String imageFile) throws Exception
	{
		InputStream is = null;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			is = new FileInputStream(imageFile);
			byte[] b = new byte[1024];
			int n;
			while ((n = is.read(b)) != -1)
			{
				out.write(b, 0, n);

			}// end while
		} catch (Exception e)
		{
			throw new Exception("System error,SendTimingMms.getBytesFromFile", e);
		} finally
		{
			if (is != null)
			{
				try
				{
					is.close();
				} catch (Exception e)
				{}// end try
			}// end if

		}// end try
		return out.toByteArray();
	}

	public static void main(String[] args) throws Exception
	{
		//打码配置信息开始
		String user = "user";
		String pass = "pass";
		String softId = "0";//软件ID,作者帐号后台添加
		//打码配置信息结束

		String info = JGetUserInfo(user, pass);
		System.out.println("剩余点数:"+info);

		//String s = JRecYZM_A(yzmPath,user,pass,softId);
		//System.out.println(s);

		String yzmPath = "img.jpg";
		byte[] img = toByteArrayFromFile(yzmPath);
		String s = JRecByte_A(img,img.length,user,pass,softId);
		String[] result = s.split("\\|!\\|");
		if (result.length==2) {
			System.out.println("识别结果:"+result[0]);
			System.out.println("验证码ID:"+result[1]);
			/*提交验证码,提交目标返回验证码对错*/
			//JReportError(user,result[1]);//如果验证码错误,则报告错误,返回积分
		}
		else {
			System.out.println("识别失败,失败原因为:"+s);
		}
	}
}
