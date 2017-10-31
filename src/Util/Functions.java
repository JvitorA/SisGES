/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author Alessandro
 */
public class Functions implements Serializable {

    /**
     * retorna senha no formato md5
     *
     * @param senha
     * @return
     * @throws java.lang.Exception
     */
    public static String senhaMD5(String senha) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(senha.getBytes());
        BigInteger hash = new BigInteger(1, md.digest());
        String retornaSenha = hash.toString(16);
        return retornaSenha;
    }

    /**
     * retorna o diretorio da aplicação
     *
     * @return
     * @throws java.lang.Exception
     */
    public static String getCurrentPath() throws Exception {
        //System.out.println(currentDirectory.getCanonicalPath());
        File currentDirectory = new File(new File(".").getAbsolutePath());
        String path = currentDirectory.getCanonicalPath() + "\\";
        return path;

    }

    /**
     * retorna uma senha temporaria de 6 digitos
     *
     * @return
     * @throws java.lang.Exception
     */
    public static String gerarSenhaTemporaria() throws Exception {
        String senha = "";
        long senhaLong = 100000 + (long) (100000 * Math.random());
        senha = String.valueOf(senhaLong);
        return senha;

    }

    /**
     * formatar uma string formatString("1234567890123", "(##) ####-####");
     *
     * @param value
     * @param pattern
     * @return
     */
    public static String formatString(String value, String pattern) {
        MaskFormatter mf;
        try {
            mf = new MaskFormatter(pattern);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(value);
        } catch (ParseException ex) {
            return value;
        }
    }
}
