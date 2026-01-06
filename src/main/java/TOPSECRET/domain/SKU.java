package TOPSECRET.domain;

import java.util.UUID;

public class SKU {

    private static final int _length = 10; //Tomei por liberdade definir o tamanho dos SKUs' de 10 caractéres alfanuméricos.
    private static final String _format = "^[A-F0-9]{" + _length + "}$"; //Vamos ter uma string de caractéres, como 01234abcde.
    private final String _value;

    private SKU(String value) {
        _value = value;
    }

    public static SKU generate() {

        String generatedSKU = generateRandomSKU();

        if (!generatedSKU.matches(_format)) {
            throw new IllegalArgumentException("Generated invalid SKU");
        }
        return new SKU(generatedSKU);
    }

    private static String generateRandomSKU() {
        String uuid = UUID.randomUUID().toString();               // o UUID gera um "código" identificador único de 16 caracteres, de 0-9a-f, de forma aleatória e converte para string.
        String compact = uuid.replace("-", ""); // remove hífens
        String shortPart = compact.substring(0, _length);          // corta para 10
        return shortPart.toUpperCase();                         // transforma todas as letras para maiúsculas
    }

    public String getValue() {
        return _value;
    }
}
