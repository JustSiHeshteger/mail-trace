package ru.zvrg.mailtrace.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.util.List;
import java.util.Objects;

import static ru.zvrg.mailtrace.common.consts.TableNamesConst.AddressTable.FIELD_ADDRESS_ID;
import static ru.zvrg.mailtrace.common.consts.TableNamesConst.PostOfficeTable.*;

/**
 * Сущность, определяющая почтовое отделение
 */
@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = TABLE_NAME, uniqueConstraints = {
        @UniqueConstraint(columnNames = {FIELD_POST_OFFICE_NAME, FIELD_ADDRESS})
})
public class PostOffice {

    @Id
    @Column(name = FIELD_POST_OFFICE_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Название почтового отделения
     */
    @NotBlank
    @Size(max = 20)
    @Column(name = FIELD_POST_OFFICE_NAME)
    private String postOfficeName;

    /**
     * Адрес почтового отделения
     */
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = FIELD_ADDRESS, referencedColumnName = FIELD_ADDRESS_ID)
    private Address address;

    /**
     * Посылки, которые содержаться в данном почтовом отделении
     */
    @JsonManagedReference("postOfficeReference")
    @OneToMany(mappedBy = "postOffice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Tracking> trackingList;

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
        PostOffice that = (PostOffice) o;
        return getId() != null && Objects.equals(getId(), that.getId());
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
                "postOfficeName = " + postOfficeName + ", " +
                "address = " + address + ")";
    }
}
