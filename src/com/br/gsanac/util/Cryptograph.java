package com.br.gsanac.util;

/**
 * <p>
 * Classe responsável pela criptografia dos dados
 * </p>
 * 
 * @author Bruno Barros
 * @date 28/01/2010
 */
public class Cryptograph {

    /**
     * <p>
     * Realiza a rotação bitwise de um número 32-bits para a esquerda
     * </p>
     * 
     * @author Bruno Barros
     * @date 28/01/2010
     * @param num
     *            Numero a ser rotacionado
     * @param cnt
     *            Potencia de 10
     * @return Representação base64 do seu SHA-1
     */

    private static int rol(int num, int cnt) {

        return (num << cnt) | (num >>> (32 - cnt));

    }

    /**
     * <p>
     * Recebe a string e retorna a representação base64 do seu SHA-1
     * </p>
     * 
     * @author Bruno Barros
     * @date 28/01/2010
     * @param str
     *            String a ser convertida
     * @return Representação base64 do seu SHA-1
     */

    public static String encode(String str) {

        // Converte a string em uma sequencia de 16 blocos de palavras, guardadas num vetor.

        // Adiciona os bits e o tamanho, conforme descrito no SHA1 padr�o

        byte[] x = str.getBytes();

        int[] blks = new int[(((x.length + 8) >> 6) + 1) * 16];

        int i;

        for (i = 0; i < x.length; i++) {

            blks[i >> 2] |= x[i] << (24 - (i % 4) * 8);

        }

        blks[i >> 2] |= 0x80 << (24 - (i % 4) * 8);

        blks[blks.length - 1] = x.length * 8;

        // Calcula o hash 160 bit SHA1 de sequencia de blocos

        int[] w = new int[80];

        int a = 1732584193;

        int b = -271733879;

        int c = -1732584194;

        int d = 271733878;

        int e = -1009589776;

        for (i = 0; i < blks.length; i += 16) {

            int olda = a;

            int oldb = b;

            int oldc = c;

            int oldd = d;

            int olde = e;

            for (int j = 0; j < 80; j++) {

                w[j] = (j < 16) ? blks[i + j] : (rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1));

                int t = rol(a, 5)
                        + e
                        + w[j]
                        + ((j < 20) ? 1518500249 + ((b & c) | ((~b) & d)) : (j < 40) ? 1859775393 + (b ^ c ^ d) : (j < 60) ? -1894007588
                                + ((b & c) | (b & d) | (c & d)) : -899497514 + (b ^ c ^ d));

                e = d;

                d = c;

                c = rol(b, 30);

                b = a;

                a = t;

            }

            a = a + olda;

            b = b + oldb;

            c = c + oldc;

            d = d + oldd;

            e = e + olde;

        }

        // Converte 160 bit has para base64

        int[] words = {
                a,
                b,
                c,
                d,
                e,
                0
        };

        byte[] base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".getBytes();

        byte[] result = new byte[28];

        for (i = 0; i < 27; i++) {

            int start = i * 6;

            int word = start >> 5;

            int offset = start & 0x1f;

            if (offset <= 26) {

                result[i] = base64[(words[word] >> (26 - offset)) & 0x3F];

            } else if (offset == 28) {

                result[i] = base64[(((words[word] & 0x0F) << 2) |

                ((words[word + 1] >> 30) & 0x03)) & 0x3F];

            } else {

                result[i] = base64[(((words[word] & 0x03) << 4) |

                ((words[word + 1] >> 28) & 0x0F)) & 0x3F];

            }

        }

        result[27] = '=';

        return new String(result);
    }
}