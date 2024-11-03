package ru.zvrg.mailtrace.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

import static ru.zvrg.mailtrace.common.consts.TableNamesConst.AddressTable.FIELD_ADDRESS_ID;
import static ru.zvrg.mailtrace.common.consts.TableNamesConst.PersonTable.*;

/**
 * Сушность, определяющая получателя
 */
@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@RequiredArgsConstructor
public class Person {

    @Id
    @Column(name = FIELD_PERSON_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Имя
     */
    @NotBlank
    @NotNull
    @Size(max = 50)
    @Column(name = FIELD_FIRST_NAME, nullable = false)
    private String firstName;

    /**
     * Фамилия
     */
    @NotBlank
    @Size(max = 50)
    @Column(name = FIELD_SECOND_NAME, nullable = false)
    private String secondName;

    /**
     * Отчество
     */
    @Size(max = 50)
    @Column(name = FIELD_THIRD_NAME, nullable = true)
    private String thirdName;

    /**
     * Номер телефона
     */
    @NotBlank
    @Size(max = 12)
    @Column(name = FIELD_PHONE_NUMBER, nullable = false)
    private String phoneNumber;

    /**
     * Адрес получателя
     */
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = FIELD_ADDRESS, referencedColumnName = FIELD_ADDRESS_ID)
    private Address address;

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
        Person person = (Person) o;
        return getId() != null && Objects.equals(getId(), person.getId());
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
                "firstName = " + firstName + ", " +
                "secondName = " + secondName + ", " +
                "thirdName = " + thirdName + ", " +
                "phoneNumber = " + phoneNumber + ")";
    }
}
