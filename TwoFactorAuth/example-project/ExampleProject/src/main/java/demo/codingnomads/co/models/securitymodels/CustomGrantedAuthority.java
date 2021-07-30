package demo.codingnomads.co.models.securitymodels;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomGrantedAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(nullable = false)
//    private PersistableObject;

    @Column(nullable = false)
    private String objectType;

    @Column(nullable = false)
    private Long objectId;

    @Column(nullable = false)
    private String permission;


    public static class Permissions {
        //owns the object. same permissions as MANIPULATE.
        public static String OWNER = "OWNER";

        //equal to WRITE + READ + DELETE + SHARE
        public static String MANIPULATE = "MANIPULATE";

        //equal WRITE + READ
        public static String ACCESS = "ACCESS";

        public static String WRITE = "WRITE";
        public static String READ = "READ";
        public static String DELETE = "DELETE";
        public static String SHARE = "SHARE";
    }

    @Override
    public String getAuthority() {
        return objectType + "_" + objectId + "_" + permission;
    }

}
