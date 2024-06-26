package top.itoolbox.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Description: 秘钥对
 * @Author: wuchu
 * @CreateTime: 2022-07-13 17:22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RSASecretKeyPair {

    /**
     * 公钥
     */
    private String publicPem;

    /**
     * 私钥
     */
    private String privatePem;
    /**
     * 公钥
     */
    private String publicKeyStr;

    /**
     * 私钥
     */
    private String privateKeyStr;

    public RSASecretKeyPair(String publicPem, String privatePem) {
        this.publicPem = publicPem;
        this.privatePem = privatePem;
    }
}
