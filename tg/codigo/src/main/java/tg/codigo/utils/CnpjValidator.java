package tg.codigo.utils;

public class CnpjValidator {

    public static boolean isCnpjValid(String cnpj) {
        cnpj = cnpj.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

        // Verifica tamanho e sequência de números iguais
        if (cnpj.length() != 14 || cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        int[] peso1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] peso2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma1 = 0, soma2 = 0;
        for (int i = 0; i < 12; i++) {
            int num = Character.getNumericValue(cnpj.charAt(i));
            soma1 += num * peso1[i];
            soma2 += num * peso2[i];
        }

        int digito1 = (soma1 % 11 < 2) ? 0 : 11 - (soma1 % 11);
        soma2 += digito1 * peso2[12];
        int digito2 = (soma2 % 11 < 2) ? 0 : 11 - (soma2 % 11);

        return digito1 == Character.getNumericValue(cnpj.charAt(12)) &&
               digito2 == Character.getNumericValue(cnpj.charAt(13));
    }
}
