package tg.codigo.utils;

public class CpfValidator {

    public static boolean isCpfValid(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", ""); // Remove caracteres não numéricos

        // Verifica tamanho e sequência de números iguais
        if (cpf.length() != 11 || cpf.matches("(\\d)\\1{10}")) {
            return false;
        }

        int[] peso1 = {10, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] peso2 = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

        int soma1 = 0, soma2 = 0;
        for (int i = 0; i < 9; i++) {
            int num = Character.getNumericValue(cpf.charAt(i));
            soma1 += num * peso1[i];
            soma2 += num * peso2[i];
        }

        int digito1 = (soma1 % 11 < 2) ? 0 : 11 - (soma1 % 11);
        soma2 += digito1 * peso2[9];
        int digito2 = (soma2 % 11 < 2) ? 0 : 11 - (soma2 % 11);

        return digito1 == Character.getNumericValue(cpf.charAt(9)) &&
               digito2 == Character.getNumericValue(cpf.charAt(10));
    }
}
