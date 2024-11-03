package ru.zvrg.mailtrace.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;
import ru.zvrg.mailtrace.common.PostageStatus;
import ru.zvrg.mailtrace.common.PostageType;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static ru.zvrg.mailtrace.common.consts.TableNamesConst.PersonTable.FIELD_PERSON_ID;
import static ru.zvrg.mailtrace.common.consts.TableNamesConst.PostageTable.*;

/**
 * Сущность, определяющая посылку
 */
@Entity
@Table(name = TABLE_NAME)
@Getter
@Setter
@RequiredArgsConstructor
@Accessors(chain = true)
public class Postage {

    /**
     * Идентификатор посылки
     */
    @Id
    @Column(name = FIELD_POSTAGE_ID)
    private UUID identifier;

    /**
     * Тип посылки
     */
    @NotNull
    @Column(name = FIELD_POSTAGE_TYPE)
    @Enumerated(EnumType.STRING)
    private PostageType postageType;

    /**
     * Текущее состояние посылки
     */
    @NotNull
    @Column(name = FIELD_STATUS)
    @Enumerated(EnumType.STRING)
    private PostageStatus status;

    /**
     * Данные получателя
     */
    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = FIELD_PERSON, referencedColumnName = FIELD_PERSON_ID)
    private Person person;

    /**
     * История отслеживания
     */
    @JsonManagedReference("postageReference")
    @OneToMany(mappedBy = "postage", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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
        Postage postage = (Postage) o;
        return getIdentifier() != null && Objects.equals(getIdentifier(), postage.getIdentifier());
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
                "identifier = " + identifier + ", " +
                "postageType = " + postageType + ")";
    }
}
