package ar.utn.frbb.tup.spboot_demo.persistence.entity;

public class BaseEntity {
    private final Long Id;

    public BaseEntity(long id) {
        Id = id;
    }

    public Long getId() {
        return Id;
    }

}
