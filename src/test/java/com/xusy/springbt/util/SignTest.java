package com.xusy.springbt.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.junit.Test;

/**
 * @author xsy
 * @date 2022-12-09 16:21
 * @description 签名
 */
public class SignTest {

    @Test
    public void outCallAI() {
        String domain_key = "7w5et1g2";
        long second = DateUtil.currentSeconds();
        String md5Secure = SecureUtil.md5(SecureUtil.md5(second + domain_key));
        String sign = StrUtil.subWithLength(md5Secure, 8, 10);
        System.out.println(second);
        System.out.println(sign);
    }

    @Test
    public void encryAPIUrl() {
        String formJson = "{\"username\":\"pk902333\",\"password\":\"Pk902333902333\",\"from\":\"2\"}";
        formJson = formJson.replace("/\\", "");
        String md5Secure = SecureUtil.md5(SecureUtil.md5(formJson));
        String sign = StrUtil.subWithLength(md5Secure, 2, 8);
        System.out.println(md5Secure);
        System.out.println(sign);
        System.out.println(StrUtil.subWithLength("9bf77354bd1d4ec1918bfe691162d1f7", 0, 16));
    }

    /**
     * aes加密
     */
    @Test
    public void encryptionAes() {
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES);
        String txt = "Pk902333902333";
        System.out.println(aes.encryptBase64(txt));
    }

}
