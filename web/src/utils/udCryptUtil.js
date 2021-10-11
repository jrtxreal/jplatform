import CryptoJS from "crypto-js";
import JSencrypt from "jsencrypt"
export default {
    //AES加密；
    aesEncrypt(msg, key, iv) {
        return CryptoJS.AES.encrypt(msg, key, {
            iv: iv,
            padding: CryptoJS.pad.Pkcs7,
            mode: CryptoJS.mode.CBC
        });
    },
    //AES解密；
    aesDecrypt(cipherText, key, iv) {
        return CryptoJS.AES.decrypt(cipherText, key, {
            iv: iv,
            padding: CryptoJS.pad.Pkcs7,
            mode: CryptoJS.mode.CBC

        });
    },
    // RSA加密
    rsaEncrypt(msg, publicKey) {
        let jsEncrypt = new JSencrypt();
        jsEncrypt.setPublicKey(publicKey);
       return  jsEncrypt.encrypt(msg);
    }
}
