﻿
package cn.count.easydrive366.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	//合作身份者id，以2088开头的16位纯数字
	public static final String DEFAULT_PARTNER = "2088901722164970";

	//收款支付宝账号
	public static final String DEFAULT_SELLER = "2088901722164970";

	//商户私钥，自助生成
	public static final String PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOsaeztECRWAG023bF1RUBXkEdKPg+5I7sfESTOoncb6jAmZelUrIeIDoTxRi1ekw8OVWk5E4mEtZBBJcHEm+M5dd6fTJGOpPJE26oaR2MBCJAlX44pfcTuGplRoBXbOG6dkWET/VnBv8NYTRToQGcIJuirK65roOix4XpRHtcgNAgMBAAECgYEAvGKx0CGKLeJC/pk1iej4BTKFXXWGP8/NFeGgznYURzbIM4D513kCY3qJHi4xO39ZJkVs4T6tLzUR80MFiIWz4w8NdZrfi5YB7GiHwrq5VmsAff6Ls4sN/Pax24hTby2tmslhKEDz27e0iNny5mAbp0M5xREKpRI/LSCinx3K+TkCQQD8NhtlNzd88wZZN8u7Mx+XCrGYv0mKNJPbUgXZYUEoNPGsG1CDTNbKkjnn+q8nr0J4vCyJ9B9gHK02l5T+/xrjAkEA7qKVvSNIFrufYxgGH7ik1HGBUwwaRNXQbgTJWtOOkgSAvovmma0hRv2O2Tp+LfOI2Ail9K+TzecnpXXXW05UTwJAKWSn8mCxlqw8KoBhy4OEd8GljDA43Znrel9n3ll73CF0WI7TE/mUdwKwxkX6YUT+X9piZPHQBHIwa3lNVtx/iQJASpKEOJbN8EyxgkX/o+oPmFPgFhOyIdCvUbItOjTiWL8PfHw7k8qGm6ig+0FMLQ/ts5UAR3aWucGCojIxGldgAQJAU6NR0LpEhBst6sKNB3MfIxjsZDicn6Q7fqsHAWjiZaxb/l0m61VzpcdVXRwFA+FRwPaG2m6NIQi+IQX/18XKhg==";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

}
