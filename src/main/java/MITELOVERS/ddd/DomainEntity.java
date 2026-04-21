package MITELOVERS.ddd;

public interface DomainEntity <ID extends DomainId> {

    public ID identity();

    public boolean sameAs(Object object);
}
