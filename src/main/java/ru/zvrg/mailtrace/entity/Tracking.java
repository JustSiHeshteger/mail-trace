package ru.zvrg.mailtrace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

import static ru.zvrg.mailtrace.common.consts.TableNamesConst.PostOfficeTable.FIELD_POST_OFFICE_ID;
import static ru.zvrg.mailtrace.common.consts.TableNamesConst.PostageTable.FIELD_POSTAGE_ID;
import static ru.zvrg.mailtrace.common.consts.TableNamesConst.TrackingTable.*;

/**
 * Сущность для отслеживания состояния посылки
 */
@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = TABLE_NAME)
@Accessors(chain = true)
public class Tracking {

    @Id
    @Column(name = FIELD_TRACKING_ID)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Дата и время изменения
     */
    @NotNull
    @Column(name = FIELD_TIMESTAMP)
    private LocalDateTime timestamp;

    /**
     * Связь с посылкой
     */
    @JsonBackReference("postageReference")
    @ManyToOne
    @JoinColumn(name = FIELD_POSTAGE_ID, nullable = false)
    private Postage postage;

    /**
     * Связь с почтовым отделением
     */
    @JsonBackReference("postOfficeReference")
    @ManyToOne
    @JoinColumn(name = FIELD_POST_OFFICE_ID, nullable = false)
    private PostOffice postOffice;

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
        Tracking tracking = (Tracking) o;
        return getId() != null && Objects.equals(getId(), tracking.getId());
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
                "timestamp = " + timestamp + ")";
    }
}