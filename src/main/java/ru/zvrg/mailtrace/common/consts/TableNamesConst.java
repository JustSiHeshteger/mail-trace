package ru.zvrg.mailtrace.common.consts;

public class TableNamesConst {

    public static class AddressTable {
        public static final String TABLE_NAME = "address";
        public static final String FIELD_ADDRESS_ID = "address_id";
        public static final String FIELD_COUNTRY = "country";
        public static final String FIELD_TOWN = "town";
        public static final String FIELD_STREET = "street";
        public static final String FIELD_POSTAL_CODE = "postal_code";
    }

    public static class PersonTable {
        public static final String TABLE_NAME = "person";
        public static final String FIELD_PERSON_ID = "person_id";
        public static final String FIELD_FIRST_NAME = "first_name";
        public static final String FIELD_SECOND_NAME = "second_name";
        public static final String FIELD_THIRD_NAME = "third_name";
        public static final String FIELD_PHONE_NUMBER = "phone_number";
        public static final String FIELD_ADDRESS = "address_id";
    }

    public static final class PostageTable {
        public static final String TABLE_NAME = "postage";
        public static final String FIELD_POSTAGE_ID = "postage_id";
        public static final String FIELD_POSTAGE_TYPE = "postage_type";
        public static final String FIELD_STATUS = "status";
        public static final String FIELD_PERSON = "person_id";
    }

    public static final class PostOfficeTable {
        public static final String TABLE_NAME = "post_office";
        public static final String FIELD_POST_OFFICE_ID = "post_office_id";
        public static final String FIELD_POST_OFFICE_NAME = "post_office_name";
        public static final String FIELD_ADDRESS = "address_id";
    }

    public static final class TrackingTable {
        public static final String TABLE_NAME = "tracking";
        public static final String FIELD_TRACKING_ID = "tracking_id";
        public static final String FIELD_TIMESTAMP = "timestamp";
    }

}
