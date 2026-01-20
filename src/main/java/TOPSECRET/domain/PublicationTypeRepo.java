package TOPSECRET.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PublicationTypeRepo {

    // Lista interna de tipos de publicação
    private final List<PublicationType> publicationTypes = new ArrayList<>();

    // Verifica se um tipo já existe
    public boolean exists(String typeName) {
        if (typeName == null) return false;

        for (int i = 0; i < publicationTypes.size(); i++) {
            PublicationType type = publicationTypes.get(i);

            if (type.getPublicationType().equalsIgnoreCase(typeName)) {
                return true;
            }
        }
        return false;
    }

    // Cria e guarda um novo PublicationType
    public PublicationType create(String typeName) {
        if (exists(typeName)) {
            throw new IllegalStateException("This publication type already exists!");
        }

        PublicationType type = new PublicationType(typeName);
        publicationTypes.add(type);
        return type;
    }

    // Vai buscar um tipo existente pelo nome
//    public PublicationType findByTypeName(String typeName) {
//        if (typeName == null) return null;
//
//        for (int i = 0; i < types.size(); i++) {
//            PublicationType type = types.get(i);
//
//            if (type.getPublicationType().equalsIgnoreCase(typeName)) {
//                return type;
//            }
//        }
//        return null;
//    }

    // Devolve uma cópia da lista (não quebra encapsulamento)
    public Collection<PublicationType> getAll() {
        return new ArrayList<>(publicationTypes);
    }
}
