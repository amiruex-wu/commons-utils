package org.wch.commons.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 秘钥对
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
