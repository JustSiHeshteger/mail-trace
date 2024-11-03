package ru.zvrg.mailtrace.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

import static ru.zvrg.mailtrace.common.consts.TableNamesConst.AddressTable.*;

/**
 * Сущность, определяющая адрес
 */
@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@RequiredArgsConstructor
public class Address {

    @Id
    @Column(name = FIELD_ADDRESS_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Страна
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = FIELD_COUNTRY, nullable = false)
    private String country;

    /**
     * Город
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = FIELD_TOWN, nullable = false)
    private String town;

    /**
     * Улица
     */
    @NotBlank
    @Size(max = 100)
    @Column(name = FIELD_STREET, nullable = false)
    private String street;

    /**
     * Почтовый индекс
     */
    @NotBlank
    @Size(max = 20)
    @Column(name = FIELD_POSTAL_CODE, nullable = false)
    private String postalCode;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;

        Class<?> oEffectiveClass = o instanceof HibernateProxy
                ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
                : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
                : this.getClass();

        if (thisEffectiveClass != oEffectiveClass) return false;
        Address address = (Address) o;
        return getId() != null && Objects.equals(getId(), address.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
                ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
                : getClass().hashCode();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "country = " + country + ", " +
                "town = " + town + ", " +
                "street = " + street + ", " +
                "postalCode = " + postalCode + ")";
    }
}
