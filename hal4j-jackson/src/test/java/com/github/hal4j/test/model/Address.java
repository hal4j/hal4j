package com.github.hal4j.test.model;

import java.io.Serializable;

public class Address implements Serializable {

    public final String postalCode;

    public final String countryCode;

    public final String region;

    public final String settlement;

    public final String streetAddress;

    public Address(String postalCode,
                   String countryCode,
                   String region,
                   String settlement,
                   String streetAddress) {
        this.postalCode = postalCode;
        this.countryCode = countryCode;
        this.region = region;
        this.settlement = settlement;
        this.streetAddress = streetAddress;
    }

}
